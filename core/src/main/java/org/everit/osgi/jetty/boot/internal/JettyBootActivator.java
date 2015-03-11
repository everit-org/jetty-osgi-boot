/*
 * Copyright (C) 2015 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.osgi.jetty.boot.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.http.proxy.ProxyListener;
import org.apache.felix.http.proxy.ProxyServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.everit.osgi.jetty.boot.JettyBootConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * Activator of Jetty Boot Bundle that registers a Jetty Server based on configuration.
 *
 */
public class JettyBootActivator implements BundleActivator {

  private static final String CONNECTOR_NAME_HTTP = "http";

  private static final String CONNECTOR_NAME_HTTPS = "https";

  private Server server;

  private ServiceRegistration<Server> serverServiceRegistration;

  private void addPasswordToServiceProps(final Dictionary<String, Object> serviceProps,
      final String syspropName, final String password) {

    if (password != null) {
      serviceProps.put(syspropName, "*******");
    }
  }

  private void createHttpConnector(final JettyConfiguration configuration) {
    ServerConnector serverConnector = createServerConnector(configuration.host,
        configuration.httpPort,
        configuration.idleTimeout);
    serverConnector.setName(CONNECTOR_NAME_HTTP);
  }

  private void createHttpsConnector(final JettyConfiguration configuration) {
    ServerConnector serverConnector = createServerConnector(configuration.host,
        configuration.httpsPort,
        configuration.idleTimeout);
    serverConnector.setName(CONNECTOR_NAME_HTTPS);

    SslConnectionFactory sslConnectionFactory = new SslConnectionFactory();

    SslContextFactory sslContextFactory = sslConnectionFactory.getSslContextFactory();
    sslContextFactory.setKeyStorePath(configuration.keystore);

    sslContextFactory.setKeyStoreType(configuration.keyStoreType);

    sslContextFactory.setKeyStorePassword(configuration.keystorePassword);

    if (configuration.keyPassword != null) {
      sslContextFactory.setKeyManagerPassword(configuration.keyPassword);
    }

    if (configuration.certAlias != null) {
      sslContextFactory.setCertAlias(configuration.certAlias);
    }

    if (configuration.trustStore != null) {
      sslContextFactory.setTrustStorePath(configuration.trustStore);
      sslContextFactory.setTrustStoreType(configuration.trustStoreType);
      sslContextFactory.setTrustStorePassword(configuration.trustStorePassword);
    }

    setClientCertOnSSLContextFactory(configuration, sslContextFactory);

    serverConnector.addConnectionFactory(sslConnectionFactory);
    serverConnector.setDefaultProtocol(sslConnectionFactory.getProtocol());
  }

  private ServerConnector createServerConnector(final String host, final int port,
      final int idleTimeout) {

    ServerConnector serverConnector = new ServerConnector(server);
    if (host != null) {
      serverConnector.setHost(host);
    }
    serverConnector.setPort(port);
    serverConnector.setReuseAddress(true);
    serverConnector.setIdleTimeout(idleTimeout);

    server.addConnector(serverConnector);

    return serverConnector;
  }

  private Dictionary<String, Object> createServiceProps(final JettyConfiguration configuration) {
    Dictionary<String, Object> serviceProps = new Hashtable<String, Object>();

    serviceProps.put(JettyBootConstants.PROP_HTTPS_KEYSTORE, configuration.keystore);

    addPasswordToServiceProps(serviceProps, JettyBootConstants.PROP_HTTPS_KEYSTORE_PASSWORD,
        configuration.keystorePassword);

    putIfNotNull(serviceProps, JettyBootConstants.PROP_HTTPS_KEYSTORE_KEY_ALIAS,
        configuration.certAlias);

    addPasswordToServiceProps(serviceProps, JettyBootConstants.PROP_HTTPS_KEYSTORE_KEY_PASSWORD,
        configuration.keyPassword);

    serviceProps.put(JettyBootConstants.SERVICE_PROPERTY_JETTY_SERVER_NAME,
        JettyBootConstants.SERVICE_PROPERTY_VALUE_JETTY_SERVER_NAME);

    serviceProps.put(Constants.SERVICE_DESCRIPTION,
        JettyBootConstants.SERVICE_DESCRIPTION_JETTY_SERVER);

    if (configuration.host != null) {
      serviceProps.put(JettyBootConstants.PROP_HTTP_HOST, configuration.host);
    } else {
      serviceProps.put(JettyBootConstants.PROP_HTTP_HOST, "*");
    }

    serviceProps.put(JettyBootConstants.PROP_IDLE_TIMEOUT, configuration.idleTimeout);
    serviceProps.put(JettyBootConstants.PROP_CONTEXT_PATH, configuration.contextPath);
    serviceProps.put(JettyBootConstants.PROP_SESSION_TIMEOUT, configuration.sessionTimeout);
    serviceProps.put(JettyBootConstants.PROP_HTTP_PORT, configuration.httpPort);

    if (configuration.httpsPort > 0) {
      serviceProps.put(JettyBootConstants.PROP_HTTP_PORT_SECURE, configuration.httpsPort);
    }

    if (configuration.trustStore != null) {
      serviceProps.put(JettyBootConstants.PROP_HTTPS_TRUSTSTORE, configuration.trustStore);
      serviceProps.put(JettyBootConstants.PROP_HTTPS_TRUSTSTORE_TYPE,
          configuration.trustStoreType);

      addPasswordToServiceProps(serviceProps, JettyBootConstants.PROP_HTTPS_TRUSTSTORE_PASSWORD,
          configuration.trustStorePassword);
    }

    putIfNotNull(serviceProps, JettyBootConstants.PROP_HTTPS_CLIENTCERT,
        configuration.clientCert);

    return serviceProps;
  }

  private ContextHandlerCollection createServletContextCollection(final BundleContext context,
      final JettyConfiguration configuration) {
    ContextHandlerCollection handlerCollection = new ContextHandlerCollection();
    ServletContextHandler servletContextHandler = new ServletContextHandler(handlerCollection,
        configuration.contextPath);

    servletContextHandler.addEventListener(new ProxyListener());
    servletContextHandler.addServlet(new ServletHolder(new ProxyServlet()), "/*");
    servletContextHandler.setAttribute(BundleContext.class.getName(), context);

    SessionHandler sessionHandler = new SessionHandler();
    sessionHandler.getSessionManager().setMaxInactiveInterval(configuration.sessionTimeout);

    servletContextHandler.setSessionHandler(sessionHandler);
    return handlerCollection;
  }

  private void putIfNotNull(final Dictionary<String, Object> serviceProps, final String key,
      final Object value) {
    if (value != null) {
      serviceProps.put(key, value);
    }
  }

  private JettyConfiguration readConfiguration(final BundleContext context) {
    JettyConfiguration configuration = new JettyConfiguration();

    configuration.httpPort = resolveIntProperty(JettyBootConstants.PROP_JETTY_BOOT_HTTP_PORT,
        JettyBootConstants.PROP_HTTP_PORT, JettyBootConstants.DEFAULT_HTTP_PORT);

    configuration.httpsPort = resolveIntProperty(
        JettyBootConstants.PROP_JETTY_BOOT_HTTP_PORT_SECURE,
        JettyBootConstants.PROP_HTTP_PORT_SECURE, JettyBootConstants.DEFAULT_HTTP_PORT_SECURE);

    if (configuration.httpPort < 0 && configuration.httpsPort < 0) {
      return configuration;
    }

    configuration.host = System.getProperty(JettyBootConstants.PROP_HTTP_HOST);

    configuration.contextPath = System.getProperty(JettyBootConstants.PROP_CONTEXT_PATH,
        JettyBootConstants.DEFAULT_CONTEXT_PATH);

    configuration.idleTimeout = resolveIntProperty(JettyBootConstants.PROP_IDLE_TIMEOUT,
        JettyBootConstants.DEFAULT_IDLE_TIMEOUT);

    configuration.sessionTimeout = resolveIntProperty(JettyBootConstants.PROP_SESSION_TIMEOUT,
        JettyBootConstants.DEFAULT_SESSION_TIMEOUT);

    configuration.keystore = resolveKeyStore(context);

    configuration.keyStoreType = System.getProperty(JettyBootConstants.PROP_HTTPS_KEYSTORE_TYPE,
        JettyBootConstants.DEFAULT_HTTPS_KEYSTORE_TYPE);

    configuration.keystorePassword = System.getProperty(
        JettyBootConstants.PROP_HTTPS_KEYSTORE_PASSWORD,
        JettyBootConstants.DEFAULT_HTTPS_KEYSTORE_PASSWORD);

    configuration.certAlias = System
        .getProperty(JettyBootConstants.PROP_HTTPS_KEYSTORE_KEY_ALIAS);

    configuration.keyPassword = System
        .getProperty(JettyBootConstants.PROP_HTTPS_KEYSTORE_KEY_PASSWORD);

    configuration.trustStore = System.getProperty(JettyBootConstants.PROP_HTTPS_TRUSTSTORE);

    if (configuration.trustStore != null) {
      configuration.trustStoreType = System.getProperty(
          JettyBootConstants.PROP_HTTPS_TRUSTSTORE_TYPE,
          JettyBootConstants.DEFAULT_HTTPS_KEYSTORE_TYPE);

      configuration.trustStorePassword = System
          .getProperty(JettyBootConstants.PROP_HTTPS_TRUSTSTORE_PASSWORD);
    }

    configuration.clientCert = System.getProperty(JettyBootConstants.PROP_HTTPS_CLIENTCERT);

    return configuration;
  }

  private int resolveIntProperty(final String systemPropertyName, final int defaultValue) {
    return resolveIntProperty(systemPropertyName, null, defaultValue);
  }

  private int resolveIntProperty(final String systemPropertyName, final String fallbackProperty,
      final int defaultValue) {

    String property = System.getProperty(systemPropertyName);

    if (property == null && fallbackProperty != null) {
      property = System.getProperty(fallbackProperty);
    }
    if (property == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(property);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Could not parse value of '" + systemPropertyName
          + "' property value: "
          + property, e);
    }
  }

  private String resolveKeyStore(final BundleContext context) {
    String result = System.getProperty(JettyBootConstants.PROP_HTTPS_KEYSTORE);
    if (result == null) {
      result = context.getBundle().getResource("META-INF/development-keystore.jks")
          .toExternalForm();
    }
    return result;
  }

  private void setClientCertOnSSLContextFactory(final JettyConfiguration configuration,
      final SslContextFactory sslContextFactory) {
    if (configuration.clientCert != null) {
      if (JettyBootConstants.OPTION_CLIENTCERT_NEEDS.equalsIgnoreCase(configuration.clientCert)) {
        sslContextFactory.setNeedClientAuth(true);
      } else if (JettyBootConstants.OPTION_CLIENTCERT_WANTS
          .equalsIgnoreCase(configuration.clientCert)) {
        sslContextFactory.setWantClientAuth(true);
      } else if (!JettyBootConstants.OPTION_CLIENTCERT_NONE
          .equalsIgnoreCase(configuration.clientCert)) {
        throw new IllegalArgumentException("Invalid value for property '"
            + JettyBootConstants.PROP_HTTPS_CLIENTCERT + "': " + configuration.clientCert);
      }
    }
  }

  @Override
  public void start(final BundleContext context) throws Exception {
    JettyConfiguration configuration = readConfiguration(context);

    if (configuration.httpPort < 0 && configuration.httpsPort < 0) {
      return;
    }

    server = new Server();

    if (configuration.httpPort >= 0) {
      createHttpConnector(configuration);
    }

    if (configuration.httpsPort >= 0) {
      createHttpsConnector(configuration);
    }

    ContextHandlerCollection handlerCollection = createServletContextCollection(context,
        configuration);

    server.setHandler(handlerCollection);

    try {
      server.start();

      updatePortsWithLive(configuration);

      Dictionary<String, Object> serviceProps = createServiceProps(configuration);

      serverServiceRegistration = context.registerService(Server.class, server, serviceProps);
    } catch (Exception e) {
      try {
        server.stop();
        server.destroy();
      } catch (Exception stopE) {
        e.addSuppressed(stopE);
      }
      throw e;
    }
  }

  @Override
  public void stop(final BundleContext context) throws Exception {
    if (serverServiceRegistration != null) {
      serverServiceRegistration.unregister();
      server.stop();
      server.destroy();
    }
  }

  private void updatePortsWithLive(final JettyConfiguration configuration) {
    Connector[] connectors = server.getConnectors();
    for (Connector connector : connectors) {
      if (connector instanceof NetworkConnector) {
        String connectorName = connector.getName();
        if (CONNECTOR_NAME_HTTP.equals(connectorName)
            || CONNECTOR_NAME_HTTPS.equals(connectorName)) {
          @SuppressWarnings("resource")
          NetworkConnector networkConnector = ((NetworkConnector) connector);
          if (CONNECTOR_NAME_HTTP.equals(connectorName)) {
            configuration.httpPort = networkConnector.getLocalPort();
          } else {
            configuration.httpsPort = networkConnector.getLocalPort();
          }
        }
      }
    }
  }

}

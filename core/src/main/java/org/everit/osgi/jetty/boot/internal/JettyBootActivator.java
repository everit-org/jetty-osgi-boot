/**
 * This file is part of Everit - Jetty OSGi Boot.
 *
 * Everit - Jetty OSGi Boot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Jetty OSGi Boot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Jetty OSGi Boot.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.osgi.jetty.boot.internal;

import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.http.proxy.ProxyListener;
import org.apache.felix.http.proxy.ProxyServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.everit.osgi.jetty.boot.JettyBootConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

public class JettyBootActivator implements BundleActivator {

    private Server server;
    private ServiceRegistration<Server> serverServiceRegistration;

    private ServerConnector createServerConnector(final int port) {

        String host = System.getProperty(JettyBootConstants.SYSPROP_HTTP_HOST);

        Integer idleTimeout = resolveProperty(JettyBootConstants.SYSPROP_IDLE_TIMEOUT,
                JettyBootConstants.DEFAULT_IDLE_TIMEOUT);

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

    private int resolveProperty(final String systemPropertyName, final int defaultValue) {
        String property = System.getProperty(systemPropertyName);
        if (property == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Could not parse value of '" + systemPropertyName + "' property value: "
                    + property, e);
        }
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        int httpPort = resolveProperty(JettyBootConstants.SYSPROP_HTTP_PORT, JettyBootConstants.DEFAULT_HTTP_PORT);

        int httpsPort = resolveProperty(JettyBootConstants.SYSPROP_HTTP_PORT_SECURE,
                JettyBootConstants.DEFAULT_HTTP_PORT_SECURE);

        String contextPath = System.getProperty(JettyBootConstants.SYSPROP_CONTEXT_PATH,
                JettyBootConstants.DEFAULT_CONTEXT_PATH);

        server = new Server();

        if (httpPort >= 0) {
            createServerConnector(httpPort);
        }

        if (httpsPort >= 0) {
            ServerConnector serverConnector = createServerConnector(httpsPort);

            SslConnectionFactory sslConnectionFactory = new SslConnectionFactory();

            SslContextFactory sslContextFactory = sslConnectionFactory.getSslContextFactory();
            URL keystore = context.getBundle().getResource("META-INF/keystore.jks");
            sslContextFactory.setKeyStorePath(keystore.toExternalForm());
            sslContextFactory.setKeyStorePassword("changeit");

            serverConnector.addConnectionFactory(sslConnectionFactory);
            serverConnector.setDefaultProtocol(sslConnectionFactory.getProtocol());
        }

        ContextHandlerCollection handlerCollection = new ContextHandlerCollection();
        server.setHandler(handlerCollection);

        ServletContextHandler servletContextHandler = new ServletContextHandler(handlerCollection, contextPath);

        servletContextHandler.addEventListener(new ProxyListener());
        servletContextHandler.addServlet(new ServletHolder(new ProxyServlet()), "/*");
        servletContextHandler.setAttribute(BundleContext.class.getName(), context);
        try {
            server.start();

            Dictionary<String, Object> serviceProps = new Hashtable<String, Object>();

            serviceProps.put(JettyBootConstants.SERVICE_PROPERTY_JETTY_SERVER_NAME,
                    JettyBootConstants.SERVICE_PROPERTY_VALUE_JETTY_SERVER_NAME);

            serviceProps.put(Constants.SERVICE_DESCRIPTION, JettyBootConstants.SERVICE_DESCRIPTION_JETTY_SERVER);
            serverServiceRegistration = context.registerService(Server.class, server, serviceProps);
        } catch (Exception e) {
            try {
                server.stop();
            } catch (Exception stopE) {
                e.addSuppressed(stopE);
            }
            throw e;
        }
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        serverServiceRegistration.unregister();
        server.stop();
        server.destroy();
    }

}

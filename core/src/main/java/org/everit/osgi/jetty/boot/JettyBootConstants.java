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
package org.everit.osgi.jetty.boot;

/**
 * Constant values of Everit - Jetty Boot.
 */
public final class JettyBootConstants {

  /**
   * Default value of {@link #SYSPROP_CONTEXT_PATH}.
   */
  public static final String DEFAULT_CONTEXT_PATH = "/";

  /**
   * Default value of {@value #SYSPROP_HTTP_PORT}.
   */
  public static final int DEFAULT_HTTP_PORT = 0;

  /**
   * Default value of {@value #SYSPROP_HTTP_PORT_SECURE}.
   */
  public static final int DEFAULT_HTTP_PORT_SECURE = 443;

  /**
   * Default value of {@link #SYSPROP_HTTPS_KEYSTORE_PASSWORD}.
   */
  public static final String DEFAULT_HTTPS_KEYSTORE_PASSWORD = "changeit";

  /**
   * Default value of {@value #SYSPROP_IDLE_TIMEOUT}.
   */
  public static final int DEFAULT_IDLE_TIMEOUT = 60000;

  /**
   * Default value of {@value #SYSPROP_SESSION_TIMEOUT}.
   */
  public static final int DEFAULT_SESSION_TIMEOUT = 30 * 60;

  /**
   * Jetty {@link org.eclipse.jetty.server.Server} is registered as an OSGi service with
   * {@link org.osgi.framework.Constants#SERVICE_DESCRIPTION} OSGi service property.
   */
  public static final String SERVICE_DESCRIPTION_JETTY_SERVER = "Everit Jetty Boot Server";

  /**
   * Jetty {@link org.eclipse.jetty.server.Server} is registered as an OSGi service with this
   * service property with this name and {@value #SERVICE_PROPERTY_VALUE_JETTY_SERVER_NAME} value.
   */
  public static final String SERVICE_PROPERTY_JETTY_SERVER_NAME = "jetty.server.name";

  /**
   * Jetty {@link org.eclipse.jetty.server.Server} is registered as an OSGi service with this
   * service property with {@link #SERVICE_PROPERTY_JETTY_SERVER_NAME} name and this value.
   */
  public static final String SERVICE_PROPERTY_VALUE_JETTY_SERVER_NAME = "everit.boot";

  /**
   * The servlet Context Path to use for the Http Service. If this property is not configured it
   * defaults to {@value #DEFAULT_CONTEXT_PATH}. This must be a valid path starting with a slash and
   * not ending with a slash (unless it is the root context).
   */
  public static final String SYSPROP_CONTEXT_PATH = "org.everit.osgi.jetty.boot.context_path";

  /**
   * Host name or IP Address of the interface to listen on. The default is null causing Jetty to
   * listen on all interfaces.
   */
  public static final String SYSPROP_HTTP_HOST = "org.everit.osgi.jetty.boot.host";

  /**
   * The port used for servlets and resources available via HTTP. A negative port number disables
   * HTTP port.
   */
  public static final String SYSPROP_HTTP_PORT = "org.osgi.service.http.port";

  /**
   * The port used for servlets and resources available via HTTPS. A negative port number disables
   * HTTPS port.
   */
  public static final String SYSPROP_HTTP_PORT_SECURE = "org.osgi.service.http.port.secure";

  /**
   * The file or URL of the SSL Key store. Default value is the keystore that is embedded into the
   * boot bundle.
   */
  public static final String SYSPROP_HTTPS_KEYSTORE = "org.everit.osgi.jetty.boot.https.keystore";

  /**
   * Alias of SSL certificate for the connector.
   */
  public static final String SYSPROP_HTTPS_KEYSTORE_KEY_ALIAS =
      "org.everit.osgi.jetty.boot.https.keystore.key.alias";

  /**
   * The password for the key in the keystore.
   */
  public static final String SYSPROP_HTTPS_KEYSTORE_KEY_PASSWORD =
      "org.everit.osgi.jetty.boot.https.keystore.key.password";

  /**
   * Password of the HTTPS keystore. Default value is {@value #DEFAULT_HTTPS_KEYSTORE_PASSWORD}.
   */
  public static final String SYSPROP_HTTPS_KEYSTORE_PASSWORD =
      "org.everit.osgi.jetty.boot.https.keystore.password";

  /**
   * Sets the maximum Idle time for a connection, which roughly translates to the
   * {@link java.net.Socket#setSoTimeout(int)} call, although with NIO implementations other
   * mechanisms may be used to implement the timeout.
   *
   * <p>
   * For more information, see
   * {@link org.eclipse.jetty.server.AbstractConnector#setIdleTimeout(long)}.
   */
  public static final String SYSPROP_IDLE_TIMEOUT = "org.everit.osgi.jetty.boot.idle_timeout";

  /**
   * In case this system property is specified, it overrides the value of
   * {@value #SYSPROP_HTTP_PORT} system property.
   */
  public static final String SYSPROP_JETTY_BOOT_HTTP_PORT = "org.everit.osgi.jetty.boot.http.port";

  /**
   * In case this system property is specified, it overrides the value of
   * {@value #SYSPROP_HTTP_PORT_SECURE} system property.
   */
  public static final String SYSPROP_JETTY_BOOT_HTTP_PORT_SECURE =
      "org.everit.osgi.jetty.boot.http.port.secure";

  /**
   * Allows for the specification of the Session life time as a number of seconds.
   */
  public static final String SYSPROP_SESSION_TIMEOUT = "org.everit.osgi.jetty.boot.session.timeout";

  private JettyBootConstants() {
  }
}

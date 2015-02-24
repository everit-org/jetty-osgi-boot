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
package org.everit.osgi.jetty.boot;

public final class JettyBootConstants {

    public static final String DEFAULT_CONTEXT_PATH = "/";

    public static final int DEFAULT_HTTP_PORT = 80;

    public static final int DEFAULT_HTTP_PORT_SECURE = 443;

    public static final int DEFAULT_IDLE_TIMEOUT = 60000;

    public static final String SERVICE_DESCRIPTION_JETTY_SERVER = "Everit Jetty Boot Server";

    public static final String SERVICE_PROPERTY_JETTY_SERVER_NAME = "jetty.server.name";

    public static final String SERVICE_PROPERTY_VALUE_JETTY_SERVER_NAME = "everit.boot";

    public static final String SYSPROP_CONTEXT_PATH = "org.everit.osgi.jetty.boot.context_path";

    public static final String SYSPROP_HTTP_HOST = "org.everit.osgi.jetty.boot.host";

    public static final String SYSPROP_HTTP_PORT = "org.osgi.service.http.port";

    public static final String SYSPROP_HTTP_PORT_SECURE = "org.osgi.service.http.port.secure";

    public static final String SYSPROP_IDLE_TIMEOUT = "org.everit.osgi.jetty.boot.idle_timeout";

    private JettyBootConstants() {
    }
}

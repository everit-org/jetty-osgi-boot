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

/**
 * Configuration settings of Jetty.
 */
public class JettyConfiguration {

  public String certAlias;

  public String clientCert;

  public String contextPath;

  public String host;

  public int httpPort;

  public int httpsPort;

  public int idleTimeout;

  public String keyPassword;

  public String keystore;

  public String keystorePassword;

  public String keyStoreType;

  public int sessionTimeout;

  public String trustStore;

  public String trustStorePassword;

  public String trustStoreType;

}

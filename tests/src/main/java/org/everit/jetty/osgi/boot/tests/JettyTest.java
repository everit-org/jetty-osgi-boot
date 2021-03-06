/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
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
package org.everit.jetty.osgi.boot.tests;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.everit.jetty.osgi.boot.JettyBootConstants;
import org.everit.osgi.dev.testrunner.TestDuringDevelopment;
import org.junit.Test;
import org.osgi.framework.ServiceReference;

/**
 * Tests if Jetty is really booted, listens on port and webconsole is running.
 *
 */
public class JettyTest {

  /**
   * Simple {@link Authenticator} that authenticates with default user name and password on
   * webconsole.
   */
  private static final class WebconsoleAuthenticator extends Authenticator {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication("admin", "admin".toCharArray());
    }
  }

  private final ServiceReference<Server> reference;

  public JettyTest(final ServiceReference<Server> reference) {
    this.reference = reference;
  }

  private void readContent(final URL url) throws IOException {
    try (InputStream inputStream = url.openConnection().getInputStream()) {
      inputStream.read();
    }
  }

  @Test
  @TestDuringDevelopment
  public void testServerAvailability() {
    Object httpPortProp = reference.getProperty(JettyBootConstants.PROP_HTTP_PORT);
    int httpPort = (Integer) httpPortProp;

    try {
      Authenticator.setDefault(new WebconsoleAuthenticator());

      URL url = new URL("http://localhost:" + httpPort + "/system/console");
      readContent(url);
    } catch (IOException e) {
      throw new AssertionError("Cannot download content", e);
    }

  }
}

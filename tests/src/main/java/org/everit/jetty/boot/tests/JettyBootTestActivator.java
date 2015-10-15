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
package org.everit.jetty.boot.tests;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jetty.server.Server;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Bundle activator that registers the test service.
 */
public class JettyBootTestActivator implements BundleActivator {

  private ServiceRegistration<JettyTest> testSR;

  private ServiceTracker<Server, Server> tracker;

  @Override
  public void start(final BundleContext context) throws Exception {
    tracker = new ServiceTracker<Server, Server>(context, Server.class,
        new ServiceTrackerCustomizer<Server, Server>() {

          @Override
          public Server addingService(final ServiceReference<Server> reference) {
            Server server = context.getService(reference);
            JettyTest jettyTest = new JettyTest(reference);

            Dictionary<String, Object> properties = new Hashtable<String, Object>();
            properties.put(TestRunnerConstants.SERVICE_PROPERTY_TEST_ID, "JettyTest");
            properties.put(TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE, "junit4");
            testSR = context.registerService(JettyTest.class, jettyTest, properties);

            return server;
          }

          @Override
          public void modifiedService(final ServiceReference<Server> reference,
              final Server service) {
            // Nothing here
          }

          @Override
          public void removedService(final ServiceReference<Server> reference,
              final Server service) {
            context.ungetService(reference);
          }
        });
    tracker.open();
  }

  @Override
  public void stop(final BundleContext context) throws Exception {
    testSR.unregister();
    tracker.close();
  }

}

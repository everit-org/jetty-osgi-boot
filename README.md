# osgi-jetty-boot
A Bundle that starts a Jetty server with HTTPService support.
The goal of this bundle the have the least dependency that is necessary
to start a Web Server that can run OSGi Webconsole.

After installing this bundle with its dependencies, a Jetty will
be started on port 80 (HTTP) and 443 (HTTPS) by default. The default
settings can be overridden by the following system properties:

 - __org.osgi.service.http.port__: The port used for servlets and resources
   available via HTTP. A negative port number disables HTTP port.
 - __org.osgi.service.http.port.secure__: The port used for servlets and
   resources available via HTTPS. A negative port number disables HTTPS port.
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
 - __:
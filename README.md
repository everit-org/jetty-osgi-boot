# jetty-osgi-boot
A Bundle that starts a Jetty server with HTTPService support.
The goal of this bundle the have the least dependency that is necessary
to start a Web Server that can run OSGi Webconsole.

The HTTPService support is based on the Apache Felix implementation.

After installing this bundle with its dependencies, a Jetty will
be started on port 80 (HTTP) and 443 (HTTPS) by default. The default
settings can be overridden by the following system properties:

 * __org.osgi.service.http.port__: The port used for servlets and resources
   available via HTTP. A negative port number disables HTTP port.

 * __org.everit.jetty.osgi.boot.http.port__: In case this system property is
   specified, it overrides the value of "org.osgi.service.http.port" system
   property.

 * __org.osgi.service.http.port.secure__: The port used for servlets and
   resources available via HTTPS. A negative port number disables HTTPS port.

 * __org.everit.jetty.osgi.boot.http.port.secure__: In case this system
   property is specified, it overrides the value of
   "org.osgi.service.http.port.secure" system property.

 * __org.everit.jetty.osgi.boot.host__: Host name or IP Address of the
   interface to listen on. The default is null causing Jetty to listen
   on all interfaces.

 * __org.everit.jetty.osgi.boot.context_path__: The servlet Context Path to
   use for the Http Service. If this property is not configured it defaults
   to "/". This must be a valid path starting with a slash and not ending with
   a slash (unless it is the root context).

 * __org.everit.jetty.osgi.boot.idle_timeout__: Connection timeout in
   milliseconds. The default is 60000 (60 seconds).

 * __org.everit.jetty.osgi.boot.session.timeout__: Allows for the
   specification of the Session life time as a number of seconds.

 * __org.everit.jetty.osgi.boot.https.keystore__: The file or URL of the SSL
   Key store. Default value is the keystore that is embedded into the boot
   bundle.

 * __org.everit.jetty.osgi.boot.https.keystore.type__: Type of keystore for
   HTTPS connections. Default value is "JKS".

 * __org.everit.jetty.osgi.boot.https.keystore.password__: Password of the
   HTTPS keystore. Default value is "changeit".

 * __org.everit.jetty.osgi.boot.https.keystore.key.alias__: Alias of SSL
   certificate for the connector.

 * __org.everit.jetty.osgi.boot.https.keystore.key.password__: The password
   for the key in the keystore.

 * __org.everit.jetty.osgi.boot.https.truststore__: Path or URL of the file
   containing the truststore.

 * __org.everit.jetty.osgi.boot.https.truststore.type__: Type of truststore.
   Default value is "JKS".
 

 * __org.everit.jetty.osgi.boot.https.truststore.password__: The password for
   the trust store.

 * __org.everit.jetty.osgi.boot.https.clientcertificate__: Flag to determine
   if the HTTPS protocol requires, wants or does not use client certificates.
   Legal values are needs, wants and none. The default is none.

## Download

The bundle and its dependencies are available on [maven-central][1]

[1]: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22org.everit.jetty.osgi.boot%22

# https-certificate-check
A simple tool to check connectivity via HTTPS and display certificate information.
## Purpose
This is a command line utility written in Java to check the connectivity and certificates on a machine. The application will
throw an exception to indicate if it can not make a connection to the target server. If the application is successful, it
indicates a 200 OK and displays the negotiated cipher.
##Build
To build the project as a simple super-jar, run the following command:
```
mvn clean compile assembly:single
```
This produces a jar that can be run from any location. This compiles the code and adds all of the required libraries into the jar. This avoids messy separate library dependencies when you need to test it on a remote machine.

## Usage
Run the application using the **$JAVA_HOME** configured for the application being tested.
### help
To display usage information
```
java -jar https-certificate-check-1.0.0.jar
```
> usage: HttpsConnectionCertificateCheck
>
> -p,--printcerts <boolean>   (true/false) Prints out certificates found
>
> -u,--URL <url>              The URL to be checked
### -u --URL
To test the connectivity you pass in a URL to be tested.
```
java -jar https-certificate-check-1.0.0.jar --URL https://github.com
```
### -p --printcerts
To print out the certificate information pass the value **true**
```
java -jar https-certificate-check-1.0.0.jar --URL https://github.com -p true
```
## Advanced Usage
You may test the connectivity using specific protocols by using the **-Dhttp.protocols=** option.

### TLS 1.0 (TLSv1)
```
java -Dhttps.protocols=TLSv1 -jar https-certificate-check-1.0.0-jar-with-dependencies.jar -URL https://github.com
```
### TLS 1.1 (TLSv1.1)
```
java -Dhttps.protocols=TLSv1.1 -jar https-certificate-check-1.0.0-jar-with-dependencies.jar -URL https://github.com
```
### TLS 1.2 (TLSv1.2)
```
java -Dhttps.protocols=TLSv1.2 -jar https-certificate-check-1.0.0-jar-with-dependencies.jar -URL https://github.com
```
### SSL 3 AKA TLS 1.3 (SSLv3)
```
java -Dhttps.protocols=SSLv3 -jar https-certificate-check-1.0.0-jar-with-dependencies.jar -URL https://github.com
```
## Troubleshooting
If you execute a command and it does not work, e.g. SSLv3 is disabled on most servers today. You will get an error message like the one below. This indicates that the protocol is not supported.
```
java -Dhttps.protocols=SSLv3 -jar https-certificate-check-1.0.0-jar-with-dependencies.jar -URL https://github.com
Please check the ciphers on the certificate to see if they are supported in your JDK version.
javax.net.ssl.SSLHandshakeException: No appropriate protocol (protocol is disabled or cipher suites are inappropriate)
	at sun.security.ssl.Handshaker.activate(Handshaker.java:529)
	at sun.security.ssl.SSLSocketImpl.kickstartHandshake(SSLSocketImpl.java:1492)
	at sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1361)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1413)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1397)
	at sun.net.www.protocol.https.HttpsClient.afterConnect(HttpsClient.java:559)
	at sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection.connect(AbstractDelegateHttpsURLConnection.java:185)
	at sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1564)
	at sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1492)
	at java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:480)
	at sun.net.www.protocol.https.HttpsURLConnectionImpl.getResponseCode(HttpsURLConnectionImpl.java:347)
	at com.bluelotussoftware.utils.HttpsConnectionCertificateCheck.printConnectionDetails(HttpsConnectionCertificateCheck.java:93)
	at com.bluelotussoftware.utils.HttpsConnectionCertificateCheck.main(HttpsConnectionCertificateCheck.java:75)
  ```
In most cases, the exception stack trace should provide sufficient details on where to begin checking.

If you are in doubt about what is enabled/disabled for the Java version you are running, check the **java.security** settings located in the **$JAVA_HOME\jre\lib\security** directory.

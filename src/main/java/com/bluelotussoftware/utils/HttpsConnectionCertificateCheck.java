/*
 * Copyright 2018 John Yeary <jyeary@bluelotussoftware.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluelotussoftware.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @version 1.0.0
 */
public class HttpsConnectionCertificateCheck {

    /**
     * The value of the parsed URL from the command line.
     */
    private static String PARSED_CMDLINE_URL;

    /**
     * Main application entry point.
     *
     * @param args Command line arguments to be evaluated.
     */
    public static void main(String[] args) {

        CommandLineParser clp = new DefaultParser();
        try {
            CommandLine cl = clp.parse(HttpsConnectionCertificateCheck.createOptions(), args);
            if (cl.hasOption("u")) {
                PARSED_CMDLINE_URL = cl.getOptionValue("u");
            } else if (cl.hasOption("URL")) {
                PARSED_CMDLINE_URL = cl.getOptionValue("URL");
            } else {
                HttpsConnectionCertificateCheck.help();
                System.exit(1);
            }
        } catch (ParseException ex) {
            System.err.println("An error occurred processing your request.");
            HttpsConnectionCertificateCheck.help();
        }

        if (PARSED_CMDLINE_URL != null && !PARSED_CMDLINE_URL.isEmpty()) {
            try {
                HttpsURLConnection connection = (HttpsURLConnection) new URL(PARSED_CMDLINE_URL).openConnection();
                HttpsConnectionCertificateCheck.printConnectionDetails(connection);
            } catch (MalformedURLException ex) {
                ex.printStackTrace(System.err);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    /**
     * Print out details of the certificate as provided by the connection.
     *
     * @param connection The connection to evaluate for connection and
     * certificate details.
     */
    private static void printConnectionDetails(HttpsURLConnection connection) {
        if (connection != null) {
            try {
                System.out.println("Response Code : " + connection.getResponseCode());
                System.out.println("Cipher Suite : " + connection.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = connection.getServerCertificates();
                for (Certificate cert : certs) {
                    System.out.println("Certificate Type : " + cert.getType());
                   
                    System.out.println("Certificate Hash Code : " + cert.hashCode());
                    System.out.println("Certificate Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Certificate Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n~~~~~~~~~~~~~~~~~~~ BEGIN: Certificate ~~~~~~~~~~~~~~~~~~~~~~~~");
                    System.out.println(cert.toString());
                    System.out.println("\n~~~~~~~~~~~~~~~~~~~ END: Certificate ~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                }
            } catch (SSLHandshakeException e) {
                System.err.println("Please check the ciphers on the certificate to see if they are supported in your JDK version.");
                e.printStackTrace(System.err);
            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace(System.err);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Print out help.
     */
    private static void help() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("HttpsConnectionCertificateCheck", HttpsConnectionCertificateCheck.createOptions());
    }

    /**
     * Set the options for the application.
     *
     * @return A set of options to parse on the command line.
     */
    private static Options createOptions() {
        Options options = new Options();
        options.addOption("u", "URL", true, "The URL to checked");
        return options;
    }

}

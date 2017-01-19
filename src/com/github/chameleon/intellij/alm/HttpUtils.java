package com.github.chameleon.intellij.alm;

import java.net.Socket;

/**
 * Created by Guy Guetta on 21/04/2016.
 * Sets up HTTP connection and determines whether to use proxy etc
 */
public class HttpUtils {
    private HttpUtils() {
    }

    // Setup System "keep-alive" HTTP header value
    public static void SetSystemKeepAlive(Boolean isKeepAlive) {
        System.setProperty("http.keepAlive", isKeepAlive ? "true" : "false");
    }

    // Setup System proxy for logs in "Fiddler"
    public static void SetSystemProxy() {
        if (System.getProperty("should.set.proxy") != null
                && System.getProperty("should.set.proxy").equals("true")) {
//            String proxyHost = "127.0.0.1";
//            int proxyPort = 8888;
//            if (isProxyServerRunning(proxyHost, proxyPort)) {
            // HTTP
            System.setProperty("http.proxyHost", "web-proxy.ftc.hpecorp.net");
            System.setProperty("http.proxyPort", String.valueOf(8080));
            // HTTPS
            System.setProperty("https.proxyHost", "web-proxy.ftc.hpecorp.net");
            System.setProperty("https.proxyPort", String.valueOf(8080));
//            }
        }
    }

    //TODO: may be add this???
    //System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    //System.setProperty("http.agent", "");

    private static boolean isProxyServerRunning(String host, int port) {
        Socket s = null;
        try {
            s = new Socket(host, port);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (s != null)
                try {
                    s.close();
                } catch (Exception e) {
                }
        }
    }
}

package com.toastedbits.kadenze.mlartmusic;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class URLUtils {
    public static URLConnection spoofBrowserConnection(final String urlStr) throws IOException {
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        //Light spoofing as if we were a web browser so some sites accept the request
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
        conn.setRequestProperty("Accept", "image/*");

        return conn;
    }
}

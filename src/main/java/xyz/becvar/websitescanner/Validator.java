package xyz.becvar.websitescanner;

import java.net.HttpURLConnection;
import java.net.URL;

public class Validator {

    // function for check if url is valid adress
    public boolean isURL(String url) {
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) { }
        return false;
    }

    // function for strip url
    public String urlStrip(String url) {
        // remove protocol form url
        url = url.replace("https://", "").replace("http://", "");

        // remove last char /
        url = removeLastSlash(url);

        return url;
    }


    // function for remove last /
    public String removeLastSlash(String url) {
        // remove last char /
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        } else {
            return url;
        }
    }

    // check if site is https
    public boolean isHttpOrHttpsUrl(String url) {

        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            con.connect();

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

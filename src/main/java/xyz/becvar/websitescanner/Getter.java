package xyz.becvar.websitescanner;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class Getter {

    //Function for get real website ip
    public String getIP(String url) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(url).getHost());

            //Remove protocol form url
            url = url.replace("https://", "").replace("http://", "");

            //Remove last char /
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }

            return ip.toString().replace(url + "/", "");
        } catch (UnknownHostException | MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

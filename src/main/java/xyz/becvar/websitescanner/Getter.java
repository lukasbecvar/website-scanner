package xyz.becvar.websitescanner;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class Getter {

    public String getIP(String url) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(url).getHost());

            url = url.replace("https://", "").replace("http://", "");

            return ip.toString().replace(url + "/", "");
        } catch (UnknownHostException | MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

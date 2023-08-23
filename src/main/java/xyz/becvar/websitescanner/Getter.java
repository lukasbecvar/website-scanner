package xyz.becvar.websitescanner;

import xyz.becvar.websitescanner.utils.console.Logger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class Getter {

    // get real website ip or proxy server adress
    public String getIP(String url) {
        try {
            InetAddress ip = InetAddress.getByName(new URL(url).getHost());

            url = Main.validator.urlStrip(url);

            return ip.toString().replace(url + "/", "");

        } catch (UnknownHostException | MalformedURLException e) {
            Logger.log("website: " + url + " is offline");
        }
        return null;
    }
}

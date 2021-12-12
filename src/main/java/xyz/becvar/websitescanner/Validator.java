package xyz.becvar.websitescanner;

public class Validator {

    //Function for check if url is valid adress
    public boolean isURL(String url) {
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) { }
        return false;
    }
}

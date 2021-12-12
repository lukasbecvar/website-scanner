import org.junit.Assert;
import org.junit.Test;
import xyz.becvar.websitescanner.Getter;

public class GetterTest {

    public static Getter getter = new Getter();

    @Test
    public void testIPGet() {
        String val = getter.getIP("https://www.google.com");
        Assert.assertEquals("172.217.23.228", val);
    }
}

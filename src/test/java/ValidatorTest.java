import org.junit.Assert;
import org.junit.Test;
import xyz.becvar.websitescanner.Validator;

public class ValidatorTest {

    public static Validator validator = new Validator();

    @Test
    public void testValidURLFlase() {
        boolean val = validator.isURL("idk");
        Assert.assertEquals(false, val);
    }

    @Test
    public void testValidURLTrue() {
        boolean val = validator.isURL("https://www.google.com");
        Assert.assertEquals(true, val);
    }
}

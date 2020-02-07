package connector;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RequestTest {

    private static final String validRequest = "GET /index1.html HTTP/1.1";

    @Test
    public void testParse(){
        InputStream input = new ByteArrayInputStream(validRequest.getBytes());
        Request request = new Request(input);
        request.parse();
        Assert.assertEquals("/index1.html" , request.getUri());
    }
}
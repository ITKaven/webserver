package connector;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResponseTest {

    private static  String validRequest = "GET /index.html HTTP/1.1";
    private static  String invalidRequest = "GET /notfound.html HTTP/1.1";

    private static  String status200 = "HTTP/1.1 200 OK\r\n\r\n";
    private static  String status404 = "HTTP/1.1 404 File Not Found\r\n\r\n";

    @Test
    public void testSendStaticResource() throws IOException {
        InputStream input = new ByteArrayInputStream(validRequest.getBytes());
        Request request = new Request(input);
        request.parse();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Response response = new Response(output);
        response.setRequest(request);
        response.sendStaticResource();

        String path = getClass().getClassLoader().getResource("index.html").getPath();
        String resource = new String(Files.readAllBytes(Paths.get(path.substring(1 , path.length()))));

        Assert.assertEquals(status200 + resource , output.toString());
    }

    @Test
    public void testValidSendStaticResource() throws IOException {
        InputStream input = new ByteArrayInputStream(invalidRequest.getBytes());
        Request request = new Request(input);
        request.parse();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Response response = new Response(output);
        response.setRequest(request);
        response.sendStaticResource();

        String path = getClass().getClassLoader().getResource("404.html").getPath();
        String resource = new String(Files.readAllBytes(Paths.get(path.substring(1 , path.length()))));

        Assert.assertEquals(status404 + resource , output.toString());
    }
}
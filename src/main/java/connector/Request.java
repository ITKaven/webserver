package connector;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private static final int BUFFER_SIZE = 1024;

    private InputStream input;

    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public String getUri() {
        return uri;
    }

    public void parse(){
        int length = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            length = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder request = new StringBuilder();
        for (int i = 0; i < length; i++) {
            request.append((char) buffer[i]);
        }

        uri = parseUri(request.toString());
    }

    public String parseUri(String request){
        int index1 , index2;
        index1 = request.indexOf(' ');
        if(index1 != -1){
            index2 = request.indexOf(' ', index1+1);
            if(index2 != -1){
                return request.substring(index1 + 2 , index2);
            }
        }
        // 没有办法解析出uri
        return "";
    }
}

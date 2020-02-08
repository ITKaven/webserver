package connector;

import java.io.IOException;
import java.io.InputStream;

public class Request{

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
            // 读取流里面的数据，并且记录长度
            length = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将数据转化成StringBuilder
        StringBuilder request = new StringBuilder();
        for (int i = 0; i < length; i++) {
            request.append((char) buffer[i]);
        }

        // 分析出客户端想要的资源
        uri = parseUri(request.toString());
    }

    /**
     *从 “GET /index.html HTTP/1.1
     *      ......
     *       ”中获取index.html
     * 通过空格来分离出来
     * */
    public String parseUri(String request){
        int index1 , index2;
        // 第一个空格的位置
        index1 = request.indexOf(' ');
        if(index1 != -1){
            // 第二个空格的位置
            index2 = request.indexOf(' ', index1+1);
            if(index2 != -1){
                // 分离出资源名称
                return request.substring(index1 + 2 , index2);
            }
        }
        // 没有办法解析出uri
        return "";
    }
}

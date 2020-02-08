package connector;

import java.io.*;

public class Response {

    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        try{
            // 通过request的uri，获取资源的路径
            String filePath = getClass()
                    .getClassLoader()
                    .getResource(request.getUri()).getFile();
            // 创建资源文件
            File file = new File(filePath.substring(1 , filePath.length()));
            // 将资源写入流里面，HttpStatus.SC_OK是状态码
            write(file , HttpStatus.SC_OK);
        } catch (Exception e) {
            // 当出现错误时，简单处理 ，发送404.html给客户端
            String errorFilePath = getClass().getClassLoader().getResource("404.html").getFile();
            // 将资源写入流里面，HttpStatus.SC_NOT_FOUND是状态码
            write(new File(errorFilePath.substring(1 , errorFilePath.length())) ,
                    HttpStatus.SC_NOT_FOUND);

        }
    }

    private void write(File resource , HttpStatus status) throws IOException {

        try(FileInputStream fis = new FileInputStream(resource)){
            // 先将协议、状态码等必要信息写入流中，ConnectorUtils是工具类
            output.write(ConnectorUtils.renderStatus(status).getBytes());
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            // 把资源文件写入流中
            while((length = fis.read(buffer , 0 , BUFFER_SIZE)) != -1){
                output.write(buffer , 0 ,length);
            }
        }
    }
}

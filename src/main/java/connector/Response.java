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
            String filePath = getClass()
                    .getClassLoader()
                    .getResource(request.getUri()).getFile();
            File file = new File(filePath.substring(1 , filePath.length()));
            write(file , HttpStatus.SC_OK);
        } catch (Exception e) {
            // 简单处理
            String errorFilePath = getClass().getClassLoader().getResource("404.html").getFile();
            write(new File(errorFilePath.substring(1 , errorFilePath.length())) ,
                    HttpStatus.SC_NOT_FOUND);

        }
    }

    private void write(File resource , HttpStatus status) throws IOException {
        try(FileInputStream fis = new FileInputStream(resource)){
            output.write(ConnectorUtils.renderStatus(status).getBytes());
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while((length = fis.read(buffer , 0 , BUFFER_SIZE)) != -1){
                output.write(buffer , 0 ,length);
            }
        }
    }
}

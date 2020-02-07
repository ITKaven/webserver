package connector;

import processor.StaticProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector implements Runnable {

    private static final int DEFAULT_PORT = 8888;

    private ServerSocket server;
    private int port;

    public Connector(){
        this(DEFAULT_PORT);
    }

    public Connector(int port) {
        this.port = port;
    }

    public void start(){
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            System.out.println("启动服务器，监听端口：" + port);
            while(true){
                Socket socket = server.accept();
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                Request request = new Request(input);
                request.parse();

                Response response = new Response(output);
                response.setRequest(request);

                StaticProcessor processor = new StaticProcessor();
                processor.process(response);

                close(socket);
            }
        } catch (IOException e) {
            // 浏览器可以识别状态码，当状态码表示请求不成功时（如404），似乎会断开socket，所以这里不进行处理
        } finally{
            close(server);
        }
    }

    private void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

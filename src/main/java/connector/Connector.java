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
            // 创建ServerSocket，绑定、监听端口
            server = new ServerSocket(port);
            System.out.println("启动服务器，监听端口：" + port);
            while(true){
                // 等待客户端连接
                Socket socket = server.accept();
                // 获取输入流
                InputStream input = socket.getInputStream();
                // 获取输出流
                OutputStream output = socket.getOutputStream();

                // 创建请求request，并且传入输入流（有客户端请求的信息）
                Request request = new Request(input);
                // request通过输入流的信息，分析出客户端想要的资源
                request.parse();

                // 创建响应response，并且传入输出流（方便将获取的资源发送给客户端）
                Response response = new Response(output);
                // response需要request的uri（客户端请求的资源）
                response.setRequest(request);

                // 创建处理者processor
                StaticProcessor processor = new StaticProcessor();
                // processor通过response把数据发送给客户端
                processor.process(response);

                //关闭socket
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

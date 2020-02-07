package processor;

import connector.Response;

import java.io.IOException;

public class StaticProcessor {

    public void process(Response response){
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import connector.Connector;

public final class Bootstrap {
    public static void main(String[] args) {
        Connector connector = new Connector();
        connector.start();
    }
}

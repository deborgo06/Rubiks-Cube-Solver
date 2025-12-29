import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;

public class FrontendServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(5600), 0);

        server.createContext("/", (HttpExchange exchange) -> {

            File file = new File("index.html"); 

            if (!file.exists()) {
                String msg = "index.html not found";
                exchange.sendResponseHeaders(404, msg.length());
                exchange.getResponseBody().write(msg.getBytes());
                exchange.close();
                return;
            }

            byte[] data = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, data.length);
            exchange.getResponseBody().write(data);
            exchange.close();
        });

        server.start();
        System.out.println("Frontend running at http://localhost:5600");
    }
}


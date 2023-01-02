import MainPackage.Executor;
import ReservationStationPackage.ReservationStation;
import ReservationStationPackage.ReservationStationItem;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;



public class Server {
    public static void main(String[] args) {
        Executor executor = new Executor();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
//                    System.out.println(exchange.getRequestMethod());
                    try {
                        if (exchange.getRequestMethod().equals("GET")) {

                            executor.run();
                            System.out.println(executor.getJsonData());
                            InputStream inputStream = exchange.getRequestBody();
                            byte[] bytes = inputStream.readAllBytes();
                            String requestBody = new String(bytes);
                            OutputStream outputStream = exchange.getResponseBody();
                            exchange.sendResponseHeaders(200, executor.getJsonData().length());
                            outputStream.write(executor.getJsonData().getBytes());
                            outputStream.close();

                        } else if (exchange.getRequestMethod().equals("POST")) {

                            InputStream inputStream = exchange.getRequestBody();
                            byte[] bytes = inputStream.readAllBytes();
                            String requestBody = new String(bytes);
                            System.out.println("requestBody: " + requestBody);
                            executor.enqueueInstructions(requestBody.substring(1, requestBody.length() - 1));
                            exchange.sendResponseHeaders(200, "POST request received".getBytes().length);
                            OutputStream outputStream = exchange.getResponseBody();
                            outputStream.write("POST request received".getBytes());
                            outputStream.close();

                        } else {
                            System.out.println("Invalid request");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            server.start();
            System.out.println("Server started on port 8080");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

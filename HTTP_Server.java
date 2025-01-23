/* 
Darian Dean

My first HTTP server in Java. 
Following Javarevisited "How to create HTTP Server in Java - ServerSocket Example 
https://javarevisited.blogspot.com/2015/06/how-to-create-http-server-in-java-serversocket-example.html#
*/

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP_Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket server = new ServerSocket(8080, 50, null)) {
            System.out.println("Listening for connection on port 8080 (accessible to the network) ....");

            while(true) {
                try {
                    Socket socket = server.accept();
                    System.out.println("Accepted connection from " + socket.getInetAddress());
                    new Thread(new ClientHandler(socket)).start();
                } catch (IOException e) {
                    System.out.println("Error accepting client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not start server: " + e.getMessage());
        }

    }

}
/* 
Darian Dean

My first HTTP server in Java. 
Following Javarevisited "How to create HTTP Server in Java - ServerSocket Example 
https://javarevisited.blogspot.com/2015/06/how-to-create-http-server-in-java-serversocket-example.html#
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HTTP_Server {
    public static void main(String[] args) throws Exception {
        final ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");
        
        while(true) {
            // Socket clientSocket = server.accept();
            // InputStreamReader isr = new InputStreamReader((clientSocket.getInputStream()));
            // BufferedReader reader = new BufferedReader(isr);
            
            // String line = reader.readLine();
            // while (!line.isEmpty()) {
            //     System.out.println(line);
            //     line = reader.readLine();
            // }

            try (Socket socket = server.accept()) {
                Date today = new Date();
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            }

        }

    }

}
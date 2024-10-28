/*
 * 
 * This class implements Runnable and is responsible for handling the clients
 * requests. Each instance of ClientHandler is created with a Socket and will 
 * run in its own thread.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

// handel client connections
public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String requestLine = reader.readLine();
            System.out.println("Recieved request: " + requestLine);

            Date today = new Date();
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;

            socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}

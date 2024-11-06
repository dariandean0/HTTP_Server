/*
 * 
 * This class implements Runnable and is responsible for handling the clients
 * requests. Each instance of ClientHandler is created with a Socket and will 
 * run in its own thread.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

// handle client connections
public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream outputStream = socket.getOutputStream()) {

            // Read the request line (e.g., GET / HTTP/1.1)
            String requestLine = reader.readLine();
            System.out.println("Received request: " + requestLine);

            // Parse the request to get the requested resource (file)
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            // Default content type is HTML
            String contentType = "text/html";
            String content = "";

            // Determine the file path based on the request
            String filePath = "C:\\Personal Projects\\HTTP_Server\\html" + (path.equals("/") ? "/index.html" : path);

            // Check if the request is for a specific static file (CSS, JS)
            if (path.endsWith(".css")) {
                filePath = "C:\\Personal Projects\\HTTP_Server\\html" + path; // Serve CSS file
                contentType = "text/css";
            } else if (path.endsWith(".js")) {
                filePath = "C:\\Personal Projects\\HTTP_Server\\html" + path; // Serve JS file
                contentType = "application/javascript";
            }

            // Try reading the file content and serve the response
            try {
                byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
                content = new String(fileContent);

                // Respond with a 200 OK status and the file content
                String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                      "Content-Type: " + contentType + "; charset=utf-8\r\n" +
                                      "Content-Length: " + fileContent.length + "\r\n" +
                                      "\r\n" +
                                      content;

                outputStream.write(httpResponse.getBytes());
            } catch (IOException e) {
                // Handle case where file is not found (404)
                String errorResponse = "HTTP/1.1 404 Not Found\r\n\r\n<h1>404 Not Found</h1>";
                outputStream.write(errorResponse.getBytes());
            }

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

/*
 * 
 * This class implements Runnable and is responsible for handling the clients
 * requests. Each instance of ClientHandler is created with a Socket and will 
 * run in its own thread.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

// handle client connections
public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String requestLine = reader.readLine();
            System.out.println("Recieved request: " + requestLine);

            //Date today = new Date();
            //String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
            
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];

            if (method.equals("GET")) {
                handleGetRequest(path);
            }
            
            //socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
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

    private void handleGetRequest(String path) throws IOException {
        // Default to index.html if no specific file requested
        if (path.equals("/")) {
            path = "/index.html";
        }

        File file = new File("web" + path);
        if (file.exists()) {
            String contentType = "text/html";
            if (path.endsWith(".css")) {
                contentType = "text/css";
            } else if (path.endsWith(".js")) {
                contentType = "application/javascript";
            }

            byte[] content = readFile(file);
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                  "Content-Type: " + contentType + "\r\n" +
                                  "Content-Length: " + content.length + "\r\n" +
                                  "\r\n";

            socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            socket.getOutputStream().write(content);
        } else {
            String httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
            socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        }
    }

    private byte[] readFile(File file) throws IOException {
        try (InputStream is = new FileInputStream(file);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[1024];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }
}

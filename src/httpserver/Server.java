package httpserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Server started.");

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String request = reader.readLine();
                System.out.println("Received request: " + request);

                // Process the request and generate the response
                String response = processRequest(request);

                // Send the response
                writer.write(response);
                writer.flush();

                // Close the connection
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String processRequest(String request) {
            // Process the request and generate the response
            // You can implement your own logic here
            // This is a simple example that returns a basic HTTP response
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<h1>I'm responding</h1>";

            return response; 
        }
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
    }
}

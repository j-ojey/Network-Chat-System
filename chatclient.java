import java.io.*;
import java.net.*;
import java.util.Scanner;

public class chatclient {
    private static final String SERVER_ADDRESS = "192.168.149.25"; // Replace with server IP address if not running locally
    private static final int SERVER_PORT = 22223;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Connected to the chat server");
            System.out.println(in.readLine());  // Read the prompt for nickname
            String nickname = scanner.nextLine();
            out.println(nickname);  // Send nickname to the server

            // Thread to read messages from the server
            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading from server: " + e.getMessage());
                }
            }).start();

            // Main thread to send user messages to the server
            while (true) {
                String userMessage = scanner.nextLine();
                out.println(userMessage);
                if (userMessage.equalsIgnoreCase("/exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}

import java.io.*;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ChatClient {
    public static boolean disconnected;
    private static ChatClientThread outputThread;
    private static ChatClientKeepAliveThread keepAliveThread;

    public static void main (String [] args) throws IOException {
        Socket socket = null;
        PrintWriter out = null;
        Scanner in = null;
        disconnected = false;

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String nickname = args[2];

        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: " + host);
            e.printStackTrace();
            System.exit(1);
        }

        String serverMsg = null;
        out.println("/name " + nickname); // Greet server and change name
        
        //Thread to handle client -> server messages
        outputThread = new ChatClientThread(out);
        outputThread.start();
        //Thread to handle periodic pings to server to stop connection dying
        keepAliveThread = new ChatClientKeepAliveThread(out);
        keepAliveThread.start();

        // Loop to handle server -> client messages
        while (!disconnected) {
            try {
                serverMsg = in.nextLine(); // Get message from server
                System.out.println(serverMsg); // And echo it to terminal
            } 
            catch (NoSuchElementException e) {
                disconnected = true;
            }
        }

        System.out.println("Disconnected.");
        keepAliveThread.interrupt();
        outputThread.interrupt();
        
        out.close();
        in.close();
        socket.close();
    }
}

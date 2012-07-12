import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
	private static boolean disconnect;
	private static ChatClientThread outputThread;
	
	public static void main (String [] args) throws IOException {
		Socket socket = null;
        PrintWriter out = null;
        Scanner in = null;
        disconnect = false;
        
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String nickname = args[2];
 
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: " + host);
            System.exit(1);
        }

        String serverMsg = null;
        
        out.println("/name " + nickname); // Greet server and change name
        outputThread = new ChatClientThread(out); //Thread to handle client -> server messages
        outputThread.start();
        
        // Loop to handle server -> client messages
    	while (!disconnected()) {
    		serverMsg = in.nextLine();
     		System.out.println(serverMsg); // And echo it to terminal
    	}
    	
   	
        System.out.println("Disconnected.");
        out.close();
        in.close();
        socket.close();
	}
	
	public static boolean disconnected() {
		return disconnect;
	}
}

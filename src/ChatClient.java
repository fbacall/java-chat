import java.io.*;
import java.net.*;

public class ChatClient {
	public static void main (String [] args) throws IOException {
		Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String nickname = args[2];
 
        try {
            socket = new Socket(host, 4444);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: " + host);
            System.exit(1);
        }

        String userInput = null;
        String serverMsg = null;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        out.println("Hi Server!"); // Greeting to server
        
    	while ((serverMsg = in.readLine()) != null) {
    		System.out.println("server: " + serverMsg); // And echo it to terminal
    	    if(serverMsg.equalsIgnoreCase("end"))
    	    	break;    	    
    		userInput = stdIn.readLine(); // Get user input
    	    out.println(userInput); // Send user input to server
    	    System.out.println("client: " + userInput); // And echo it to terminal
    	}
    	
        System.out.println("Done");
        out.close();
        in.close();
        socket.close();
	}
}

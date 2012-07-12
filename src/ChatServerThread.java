import java.io.*;
import java.net.*;

public class ChatServerThread extends Thread {
	
	private Socket socket;
	
	public ChatServerThread(Socket socket) {
		super("LogServerThread");
		this.socket = socket;
	}
	
	public void run() {

		try {
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(
		    										socket.getInputStream()));
		    
		    //do something with the request
		    String userInput;
		    while ((userInput = in.readLine()) != null) {
		    	System.out.println("client: " + userInput);
		    	out.println("You said: " + userInput);
    	    	if(userInput.equalsIgnoreCase("bye")){
    		    	System.out.println("Closing connection.");
    	    		out.println("end");
    	    		break;
    	    	}    	    		
		    }
		    
		    out.close();
		    in.close();
		    socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

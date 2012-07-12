import java.io.*;
import java.net.*;

public class ChatServerThread extends Thread {
	
	public Socket socket;
	private ChatServer server;
	public User user;
	
	public ChatServerThread(User user, Socket socket, ChatServer server) {
		super("ChatUserThread");
		this.socket = socket;
		this.user = user;
		this.server = server;
	}
	
	public void run() {

		try {
			//New client msg
			server.sendToAll("***" + user.name + " connected ("+ user.address +") ***");
		    //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(new InputStreamReader(
		    										socket.getInputStream()));
		    
		    //do something with the request
		    String msg;
		    while ((msg = in.readLine()) != null) {
    	    	if(msg.equalsIgnoreCase("/dc")) {
    		    	String fullMsg = "***" + user.name + " disconnected ("+ user.address +") ***";
    		    	System.out.println(fullMsg);
    		    	server.sendToAll(fullMsg);
    	    		break;
    	    	}
    	    	else if(msg.startsWith("/name")) {
    	    		String oldName = user.name;
    	    		user.name = msg.split(" ")[1];
    		    	String fullMsg = "***" + oldName + " changed name to: " + user.name + " ***";
    		    	System.out.println(fullMsg);
    		    	server.sendToAll(fullMsg);
    	    	}
    	    	else {    	    		
    		    	String fullMsg = user.name + ": " + msg;
    		    	System.out.println(fullMsg);
    		    	server.sendToAll(fullMsg);    		    	
    	    	}	    		
		    }
		    
		    //out.close();
		    in.close();
		    socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

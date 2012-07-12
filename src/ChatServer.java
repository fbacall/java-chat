import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {
	
	private ServerSocket socket;
	private ArrayList<ChatServerThread> userThreads;
	
	
	public static void main (String [] args) throws IOException {
		ServerSocket socket = null;
		int port = 4444;
		
		try {
			socket = new ServerSocket(port);
		}
		catch(IOException e) {
			System.err.println("Couldn't create server socket on port: " + port);
			System.exit(-1);
		}
		
		ChatServer server = new ChatServer(socket);	
		System.out.println("Server running on port: " + port);
		
		server.serve();
	}
	
	public ChatServer(ServerSocket socket) {
		this.socket = socket;
		this.userThreads = new ArrayList<ChatServerThread>();		 
	}
	
	public void serve() throws IOException {
		while(true) {
			Socket newUserSocket = socket.accept();
			User newUser = new User("User" + userThreads.size(), newUserSocket.getLocalAddress().getHostAddress());
			ChatServerThread newUserThread = new ChatServerThread(newUser, newUserSocket, this); // Thread to communicate to client with
			this.userThreads.add(newUserThread);
			newUserThread.start(); // Go!
		}
	}
	
	public void sendToAll(String msg) {
		Iterator<ChatServerThread> iter = this.userThreads.iterator();
		while(iter.hasNext()) {
			ChatServerThread thread = iter.next();
			try {
				PrintWriter out = new PrintWriter(thread.socket.getOutputStream(), true);
				out.println(msg);
				System.out.println("Sent to " + thread.user.address);
			}
			catch (IOException e) {
				System.err.println("Failed to send msg to client: " + thread.user.name + 
						" (" + thread.user.address + ")");
			}

		}		
	}
}

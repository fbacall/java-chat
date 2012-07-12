import java.io.*;
import java.net.*;

public class ChatServer {
	
	private int max_clients;
	private ServerSocket socket;
	private ChatServerThread [] clientThreads;
	
	
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
		
		ChatServer server = new ChatServer(socket, 20);	
		System.out.println("Server running on port: " + port);
		
		server.serve();
	}
	
	public ChatServer(ServerSocket socket, int max_clients) {
		this.socket = socket;
		this.max_clients = max_clients;
		this.clientThreads = new ChatServerThread[max_clients];		 
	}
	
	public void serve() throws IOException {
		while(true) {
			ChatServerThread newClient = new ChatServerThread(socket.accept()); // Thread to communicate to client with
			this.clientThreads[clientThreads.length] = newClient;
			newClient.start(); // Go!
		}
	}
	
	public void sendToAll(String msg) {
		
	}
}

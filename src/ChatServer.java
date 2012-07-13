import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ChatServer {

    private ServerSocket socket;
    private ArrayList<ChatServerThread> userThreads;
    private static final int CLIENT_TIMEOUT = 3 * 60 * 1000;

    public static void main (String [] args) throws IOException {
        ServerSocket socket = null;
        int port = Integer.parseInt(args[0]);        
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
            newUserSocket.setSoTimeout(CLIENT_TIMEOUT);
            User newUser = new User("User" + userThreads.size(), newUserSocket.getLocalAddress().getHostAddress());
            ChatServerThread newUserThread = new ChatServerThread(newUser, newUserSocket, this); // Thread to communicate to client with
            this.userThreads.add(newUserThread);
            newUserThread.start(); // Go!
        }
    }

    public void sendToAll(String msg) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        StringBuilder dateString = new StringBuilder( dateFormat.format( new Date() ) );
        msg = "[" + dateString + "] " + msg;
                
        System.out.println(msg);
        Iterator<ChatServerThread> iter = this.userThreads.iterator();        
        while(iter.hasNext()) {
            ChatServerThread thread = iter.next();
            try {
                PrintWriter out = new PrintWriter(thread.socket.getOutputStream(), true);
                out.println(msg);
                System.out.print("(sent to " + thread.user.address + ") ");
            }
            catch (IOException e) {
                System.err.println("Failed to send msg to client: " + thread.user.name + 
                        " (" + thread.user.address + ")");
            }
        }
        System.out.println();
    }

    // Get list of threads/sessions/connections/sockets whatever    
    public ArrayList<ChatServerThread> getUserThreads() {
        return userThreads;    	
    }
    
    // Get list of users
    public User[] getUsers() {
        User [] users = new User[userThreads.size()];
        int index = 0;
        Iterator<ChatServerThread> iter = this.userThreads.iterator();        
        while(iter.hasNext()) {            
            users[index++] = iter.next().user;
        }
        return users;        
    }

    // Check if name contains valid characters
    public boolean validName(String name) {
        return name.matches("[a-zA-Z0-9_-]+");
    }
    
    // Check if name in use by another user.
    public boolean nameInUse(String name) {
        boolean inUse = false;
        User [] users = getUsers();
        for(int i = 0; i < users.length; i++) {
            if(users[i].name.equals(name)) {
                inUse = true;
                break;
            }                
        }        
        return inUse;
    }
}

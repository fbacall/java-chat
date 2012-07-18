package chatServer;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ChatServer {

    private ServerSocket socket;
    private ArrayList<User> users;
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
        this.users = new ArrayList<User>();		 
    }

    public void serve() throws IOException {
        while(true) {
            Socket newUserSocket = socket.accept();
            newUserSocket.setSoTimeout(CLIENT_TIMEOUT);
            this.users.add(new User("User" + users.size(), newUserSocket, this));
        }
    }

    public void sendToAll(String msg) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        StringBuilder dateString = new StringBuilder( dateFormat.format( new Date() ) );
        msg = "[" + dateString + "] " + msg;
                
        System.out.println(msg);
        Iterator<User> iter = this.users.iterator();        
        while(iter.hasNext()) {
            User user = iter.next();
            user.sendMessage(msg);
        }
    }

    // Get list of connected users    
    public ArrayList<User> getUserThreads() {
        return users;    	
    }

    // Check if name contains valid characters
    public boolean validName(String name) {
        return name.matches("[a-zA-Z0-9_-]+");
    }
    
    // Check if name in use by another user.
    public boolean nameInUse(String name) {
        boolean inUse = false;
        Iterator<User> iter = users.iterator();
        while(iter.hasNext()) {
            if(iter.next().getName().equals(name)) {
                inUse = true;
                break;
            }                
        }        
        return inUse;
    }
    
    public void disconnectClient(User user) {
        users.remove(user);
    }
    
    public String welcomeMessage(String newUser) {
        int otherUserCount = users.size() - 1;
        String msg = "-------------------------\n" +
                     otherUserCount + " other users online" + (otherUserCount > 0 ? ": " : ".");
        for(int i = 0; i < users.size(); i++) {
            String name = users.get(i).getName();
            if(!name.equals(newUser)) {
                msg += name + ((i == (otherUserCount-1)) ? "." : ", ");
            }
        }
        msg += "\n-------------------------";
        return msg;
    }
}

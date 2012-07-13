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
            server.sendToAll("*** " + user.name + " connected ("+ user.address +") ***");
            //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            //do something with the request
            String msg;
            while ((msg = in.readLine()) != null) {
                if(msg.equalsIgnoreCase("/ping")) {
                	System.out.println("Ping from " + user.name + " ("+ user.address +")");
                }
                else if (msg.equalsIgnoreCase("/dc")) {
                    server.sendToAll("*** " + user.name + " disconnected ("+ user.address +") ***");
                    break;
                }
                else if(msg.startsWith("/name")) {
                    String oldName = user.name;
                    user.name = msg.split(" ",2)[1];
                    server.sendToAll("*** " + oldName + " changed name to: " + user.name + " ***");
                }
                else if(msg.startsWith("/me")) {
                    user.name = msg.split(" ",2)[1];
                    server.sendToAll(user.name + " " + msg);
                }
                else {    	    		
                    server.sendToAll(user.name + ": " + msg);    		    	
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

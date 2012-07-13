package chatServer;
import java.io.*;
import java.net.*;

public class ChatServerThread extends Thread {

    public Socket socket;
    private ChatServer server;
    public User user;
    public boolean disconnected;

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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Loop that waits for client input
            try {
                out.println(server.welcomeMessage(user.name));
                String msg;
                while ((msg = in.readLine()) != null) {
                    // Ping to keep connection alive. Don't output anything to rest of server
                    if(msg.equalsIgnoreCase("/ping")) {
                        System.out.println("Ping from " + user.name + " ("+ user.address +")");
                    }
                    // Disconnect
                    else if (msg.equalsIgnoreCase("/dc")) {
                        server.sendToAll("*** " + user.name + " disconnected ("+ user.address +") ***");
                        break;
                    }
                    // Change user name if a valid name given
                    else if(msg.startsWith("/name")) {
                        String oldName = user.name;
                        String newName = msg.split(" ",2)[1];
                        if(newName == null || !server.validName(newName)){
                            out.println("Invalid name. Please use only alphanumeric characters, hyphens and underscores.");    
                        }
                        else if(server.nameInUse(newName)) {
                            out.println("Name '" + newName + "' already in use.");
                        }
                        else {
                            user.name = newName;
                            server.sendToAll("*** " + oldName + " changed name to: " + user.name + " ***");
                        }
                    }
                    // Emote
                    else if(msg.startsWith("/me")) {
                        user.name = msg.split(" ",2)[1];
                        server.sendToAll(user.name + " " + msg);
                    }
                    // Normal chat message
                    else {    	    		
                        server.sendToAll(user.name + ": " + msg);    		    	
                    }
                }
            }
            catch (SocketTimeoutException e) {
                server.sendToAll("*** " + user.name + " timed out ("+ user.address +") ***");           
            }
            catch (SocketException e) {
                server.sendToAll("*** " + user.name + " disconnected ("+ user.address +") ***");
            }
            finally {               
                out.close();
                in.close();
                socket.close();
                server.disconnectClient(this);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package chatServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class User {

    private String name;
    private Socket socket;
    private ChatServerThread thread;
    private ChatServer server;
    
    public User(String name, Socket socket, ChatServer server) {
        this.name = name;
        this.socket = socket;
        this.server = server;
        this.thread = new ChatServerThread(this, socket, server);
        this.thread.start();
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public ChatServerThread getThread() {
        return thread;
    }
    
    public void sendMessage(String message) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
        }
        catch (IOException e) {
            System.err.println("Failed to send message to " + getName() + " (" + getAddress() + ")" );            
        }
    }
    
    public void disconnect() {
        server.disconnectClient(this);
    }
}

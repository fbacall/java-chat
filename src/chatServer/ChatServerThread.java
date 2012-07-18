package chatServer;

import java.io.*;
import java.net.*;

public class ChatServerThread extends Thread {

    private Socket socket;
    private ChatServer server;
    private User user;
    public boolean disconnected;

    public ChatServerThread(User user, Socket socket, ChatServer server) {
        super("ChatServerThread");
        this.user = user;
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            // New client msg
            server.sendToAll("*** " + user.getName() + " connected (" + user.getAddress() + ") ***");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Loop that waits for client input
            try {
                out.println(server.welcomeMessage(user.getName()));
                String msg;
                while ((msg = in.readLine()) != null) {
                    // Ping to keep connection alive. Don't output anything to
                    // rest of server
                    if (msg.equalsIgnoreCase("/ping")) {
                        System.out.println("Ping from " + user.getName() + " ("
                                + user.getAddress() + ")");
                    }
                    // Disconnect
                    else if (msg.equalsIgnoreCase("/dc")) {
                        server.sendToAll("*** " + user.getName()
                                + " disconnected (" + user.getAddress() + ") ***");
                        break;
                    }
                    // Change user name if a valid name given
                    else if (msg.startsWith("/name")) {
                        String oldName = user.getName();
                        String newName = msg.split(" ", 2)[1];
                        if (newName == null || !server.validName(newName)) {
                            out.println("Invalid name. Please use only alphanumeric characters, hyphens and underscores.");
                        } else if (server.nameInUse(newName)) {
                            out.println("Name '" + newName
                                    + "' already in use.");
                        } else {
                            user.setName(newName);
                            server.sendToAll("*** " + oldName
                                    + " changed name to: " + user.getName() + " ***");
                        }
                    }
                    // Emote
                    else if (msg.startsWith("/me")) {
                        server.sendToAll(user.getName() + " " + msg.split(" ", 2)[1]);
                    }
                    else if (msg.equalsIgnoreCase("/status")) {
                        out.println(server.welcomeMessage(user.getName()));
                    }
                    // Normal chat message
                    else {
                        server.sendToAll(user.getName() + ": " + msg);
                    }
                }
            }
            catch (SocketTimeoutException e) {
                server.sendToAll("*** " + user.getName() + " timed out ("
                        + user.getAddress() + ") ***");
            }
            catch (SocketException e) {
                server.sendToAll("*** " + user.getName() + " disconnected ("
                        + user.getAddress() + ") ***");
            }
            finally {
                out.close();
                in.close();
                socket.close();
                user.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

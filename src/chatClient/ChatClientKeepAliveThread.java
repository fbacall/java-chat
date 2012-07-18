package chatClient;
import java.io.PrintWriter;
import java.util.NoSuchElementException;


public class ChatClientKeepAliveThread extends Thread {

    private PrintWriter out;

    public ChatClientKeepAliveThread(PrintWriter out) {
        super("ChatClientKeepAliveThread");
        this.out = out;
    }

    public void run() {
        try {
            while (!ChatClient.disconnected) {
                Thread.sleep(60 * 1000);
                out.println("/ping");                
            }
        } 
        catch (InterruptedException e) {
            ChatClient.disconnected = true;
        }
        catch (NoSuchElementException e) {
            ChatClient.disconnected = true;
        }
    }
}

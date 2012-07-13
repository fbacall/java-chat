package chatClient;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ChatClientInputThread extends Thread {

    private PrintWriter out;

    public ChatClientInputThread(PrintWriter out) {
        super("ChatClientInputThread");
        this.out = out;		
    }

    public void run() {
        Scanner stdIn = new Scanner(System.in);
        String userInput;
        while(!ChatClient.disconnected) {
            try {
                userInput = stdIn.nextLine();
                out.println(userInput); // Send user input to server
                if(userInput.equals("/dc"))
                    ChatClient.disconnected = true;
            }
            catch (NoSuchElementException e) {
                ChatClient.disconnected = true;
            }
        }
    }

}

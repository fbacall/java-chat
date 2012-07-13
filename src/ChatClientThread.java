import java.io.PrintWriter;
import java.util.Scanner;


public class ChatClientThread extends Thread {

    private PrintWriter out;

    public ChatClientThread(PrintWriter out) {
        super("ChatClientThread");
        this.out = out;		
    }

    public void run() {
        Scanner stdIn = new Scanner(System.in);
        String userInput;
        while(!ChatClient.disconnected) {
        	userInput = stdIn.nextLine();
            out.println(userInput); // Send user input to server
            if(userInput.equals("/dc"))
            	ChatClient.disconnected = true;
        }
    }

}

import java.io.PrintWriter;
import java.util.Scanner;


public class ChatClientThread extends Thread {

	private PrintWriter out;

	public ChatClientThread(PrintWriter out) {
		this.out = out;		
	}

	public void run() {
		Scanner stdIn = new Scanner(System.in);
		String userInput;
		while((userInput = stdIn.nextLine()) != null) {
			out.println(userInput); // Send user input to server
		}
	}

}

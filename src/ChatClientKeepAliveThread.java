import java.io.PrintWriter;


public class ChatClientKeepAliveThread extends Thread {
	
    private PrintWriter out;
    
    public ChatClientKeepAliveThread(PrintWriter out) {
    	this.out = out;
    }
	
    public void run() {
		try {
	        while (!ChatClient.disconnected) {
	        	out.println("/ping");
	            Thread.sleep(60 * 1000);
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
    }
}

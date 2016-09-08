package Template;

import java.net.*;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


import java.awt.EventQueue;
import java.io.*;

import java.util.Scanner;

public class Client implements Runnable
{
	private Socket socket = null;
	private Thread thread = null;
	private PrintWriter streamOut = null; //Used to be DataStreamOutputWriter
	//private ClientThread client = null;
	private String username;
	//private ChatGUI frame;
	Scanner sc;

	public Client(String ipAddr, String username, int serverPort)
	{
		this.username = username;
		
		// set up the socket to connect to the gui
		try
		{
			socket = new Socket(ipAddr, serverPort);
			streamOut = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
			sc = new Scanner(new BufferedInputStream(socket.getInputStream())); 
			start();
		} catch (UnknownHostException h)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Unknown Host " + h.getMessage());
			System.exit(1);
		} catch (IOException e)
		{
			JOptionPane.showMessageDialog(new JFrame(), "IO exception: " + e.getMessage());
			System.exit(1);
		}
		
		// 2. SPAWN A LISTENER FOR THE SERVER. THIS WILL KEEP RUNNING
				// when a message is received, an appropriate method is called
				ServerListener sl = new ServerListener(this, socket);
				new Thread(sl).start();
	}

	public void run()
	{
		//TODO check for a new message, once we receive it, steamOut will send it to the server
		
	}

	public synchronized void handleChat(String msg)
	{
		//TODO
		try {
			streamOut.println(msg);
			streamOut.flush();
		}
		catch (Exception e)
		{
		}
	}

	public void start() throws IOException
	{
		//frame = new ChatGUI(username);
		//frame.setVisible(true);
		//TODO 
		
	}

	public void stop()
	{
		//TODO
	
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name: ");
		String name = sc.nextLine();
		Client client = new Client("localhost", name, 1222);
	}
}
class ServerListener implements Runnable {
	Client c;
	Scanner in; // this is used to read which is a blocking call

	ServerListener(Client c, Socket s) {
		try {
			this.c = c;
			in = new Scanner(new BufferedInputStream(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) { // run forever
			//System.out.println("Client - waiting to read");
			String msg = in.nextLine();
			c.handleChat(msg);
		}

	}
}

package Template;

import java.net.*;
import java.io.*;
import java.util.*;

/*
 * The Client that can be run both as a console or a GUI
 */
public class Client  {

	// for I/O
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;

	// the server, the port and the username
	private String server, username;
	private int port;


	/*
	 * Constructor call when used from a GUI
	 * in console mode the ClienGUI parameter is null
	 */
	Client(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}

	/*
	 * To start the dialog
	 */
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
		} 
		// if it failed not much I can so
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}

		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
		display(msg);

		/* Creating both Data Stream */
		try
		{
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server 
		new ServerListener().start();
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		// success we inform the caller that it worked
		return true;
	}
	
	/*
	 * To send a message to the console or the GUI
	 */
	private void display(String msg) {
		System.out.println(msg);
	}

	/*
	 * To send a message to the server
	 */
	void sendMessage(ChatMessage msg) {
		try {
			sOutput.writeObject(msg);
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	/*
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	private void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {} // not much else I can do
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {} // not much else I can do
		try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} // not much else I can do

	}
	
	public static void main(String[] args) {

		// wait for messages from user
		Scanner scan = new Scanner(System.in);

		// default values
		int portNumber = 1500;
		String serverAddress = "localhost";
		String userName;
		String msg = "";
		
		// Get username
		System.out.println("Enter username:");
		userName = scan.nextLine();
		boolean isAdmin = userName.equalsIgnoreCase("admin");
		// create the Client object
		Client client = new Client(serverAddress, portNumber, userName);
		if(!client.start())
			return;

		try {
			if (isAdmin){
				while (true) {
					// 3. KEEP LISTENING AND RESPONDING TO CLIENT REQUESTS
					System.out.println("1. Broadcast message to all clients.");
					System.out.println("2. List messages so far.");
					System.out.println("3. Deleted a selected message");
					System.out.print("> ");
					msg = scan.nextLine();
					if(msg.contains("3")) {
						System.out.println("Enter ID of message to delete:");
						int idToDelete = scan.nextInt();
						client.sendMessage(new ChatMessage(ChatMessage.DELETE, "",idToDelete));
					}
					else if(msg.contains("2")) {
						client.sendMessage(new ChatMessage(ChatMessage.LIST, "",ChatMessage.count));				
					}
					else if(msg.contains("1")) {
						System.out.print("> ");
						msg = scan.nextLine();
						// increment message counter
						client.sendMessage(new ChatMessage(ChatMessage.BROADCAST, msg,ChatMessage.incrementCount()));
					}
				}
			}
			else{
				while (true) {
					// 3. KEEP LISTENING AND RESPONDING TO CLIENT REQUESTS
					System.out.println("1. Send a txt message to the server.");
					System.out.println("2. Send a image file to the server.");
					System.out.print("> ");
					msg = scan.nextLine();
					if(msg.contains("2")) {
						System.out.print("Input Image data:\n> ");
						msg = scan.nextLine();
						// increment msg counter
						client.sendMessage(new ChatMessage(ChatMessage.IMAGE, msg,ChatMessage.incrementCount()));
					}
					else if(msg.contains("1")) {
						System.out.print("Input text:\n> ");
						msg = scan.nextLine();
						// Increment msg counter
						client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, msg,ChatMessage.incrementCount()));
					}
				}
			}
		}
		catch (Exception ex) {
			scan.close();
			client.disconnect();
		}
	}


	class ServerListener extends Thread {

		public void run() {
			while(true) {
				try {
					ChatMessage cm = (ChatMessage) sInput.readObject();
					String msg = "";
					if ( cm.getType() == ChatMessage.MESSAGE || cm.getType() == ChatMessage.BROADCAST || cm.getType() == ChatMessage.LIST){
						msg = Encryption.decryptMessage(cm.getMessage());
					}
					else if (cm.getType() == cm.IMAGE){
						//TODO: decrypt image
						msg = Encryption.decryptImage(cm.getMessage());
					}
					// if console mode print the message and add back the prompt
					System.out.println(msg);
					System.out.print("> ");
				}
				catch(IOException e) {
					display("Server has close the connection: " + e);
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
}

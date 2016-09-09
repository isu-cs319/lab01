package Template;
//package lab3;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	// a unique ID for each connection
	private static int uniqueId;
	// an ArrayList to keep the list of the Client
	private ArrayList<ClientHandler> clients;
	private ArrayList<ChatMessage> msgs;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
	private File file = new File("chat.txt");


	public Server(int port) {
		// the port
		this.port = port;
		// ArrayList for the Client list
		clients = new ArrayList<ClientHandler>();
		msgs = new ArrayList<ChatMessage>();
	}

	public void start() {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try 
		{
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while(keepGoing) 
			{
				// format message saying we are waiting
				display("Server waiting for Clients on port " + port + ".");

				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!keepGoing)
					break;
				ClientHandler t = new ClientHandler(socket);  // make a thread of it
				clients.add(t);									// save it in the ArrayList
				t.start();
			}

			try {
				serverSocket.close();
				for(int i = 0; i < clients.size(); ++i) {
					ClientHandler tc = clients.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					}
					catch(IOException ioE) {
						// not much I can do
					}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		catch (IOException e) {
			String msg = " Exception on new ServerSocket: " + e + "\n";
			display(msg);
		}
	}
	protected void stop() {
		keepGoing = false;
		// connect to myself as Client to exit statement 
		// Socket socket = serverSocket.accept();
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			// nothing I can really do
		}
	}


	// For debugging mainly
	private void display(String msg) {
		System.out.println(msg);
	}

	/*
	 *  to broadcast a message to all Clients
	 */
	private synchronized void broadcast(ChatMessage cm) {
		String message = cm.getMessage();
		cm.setID(ChatMessage.incrementCount());
		msgs.add(cm);  // Add to msgs
		String messageLf = "Admin:" + message + "\n";
		// display message on console or GUI
		System.out.print(messageLf);

		// we loop in reverse order in case we would have to remove a Client
		// because it has disconnected
		for(int i = clients.size(); --i >= 0;) {
			ClientHandler ct = clients.get(i);
			// try to write to the Client if it fails remove it from the list
			if(!ct.writeMsg(messageLf)) {
				clients.remove(i);
			}
		}
	}


	synchronized void sendMessage(ChatMessage cm, int id) {
		String msg = cm.getMessage();
		cm.setID(ChatMessage.incrementCount());
		msgs.add(cm); // Add to msgs
		// scan the array list until we found the Id
		for(int i = 0; i < clients.size(); ++i) {
			ClientHandler ct = clients.get(i);
			if(ct.clientID == id) {
				//TODO: encrypt, store in chat.txt
				msg = (ct.username+": "+msg+"\n");
				if(!ct.writeMsg(msg)) {
					clients.remove(id);
				}
				return;
			}
		}
	}

	void listMessages(int clientID){
		//TODO decrypt
		for(int i = 0; i < clients.size(); ++i) {
			ClientHandler ct = clients.get(i);
			if(ct.clientID == clientID) {
				for(int j = 0; j < msgs.size(); ++j) {
					ChatMessage cm = msgs.get(j);
					ct.writeMsg("Message with ID " + Integer.toString(cm.getID()) + ": >"+cm.getMessage()+"\n");
				}
			}
		}

	}

	synchronized void deleteMessage(int id){
		//TODO
		// scan the array list until we found the Id
		for(int i = 0; i < msgs.size(); ++i) {
			ChatMessage cm = msgs.get(i);
			// found it
			if(cm.getID() == id) {
				msgs.remove(i);
				return;
			}
		}
	}

	String encryptMessage(String txt){
		return txt;
	}

	String decryptMessage(String txt){
		return txt;
	}

	synchronized void sendImage(ChatMessage cm, int id) {
		String msg = cm.getMessage();
		cm.setID(ChatMessage.incrementCount());
		msgs.add(cm); // Add to msgs
		// scan the array list until we found the Id
		for(int i = 0; i < clients.size(); ++i) {
			ClientHandler ct = clients.get(i);
			if(ct.clientID == id) {
				// TODO: Send as bytes, encrypt.
				if(!ct.writeMsg(msg+"\n")) {
					clients.remove(id);
				}
				return;
			}
		}
	}

	public static void main(String[] args) {
		// start server on port 1500 unless a PortNumber is specified 
		int portNumber = 1500;
		// create a server object and start it
		Server server = new Server(portNumber);
		server.start();
	}


	class ClientHandler extends Thread {
		// the socket where to listen/talk
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		int clientID;
		String username;
		ChatMessage cm;

		ClientHandler(Socket socket) {
			// a unique id
			clientID = ++uniqueId;
			this.socket = socket;
			/* Creating both Data Stream */
			System.out.println("Thread trying to create Object Input/Output Streams");
			try
			{
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username
				username = (String) sInput.readObject();
				display(username + " just connected.");
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
			}
		}

		// what will run forever
		public void run() {
			while(true) {
				// read a String (which is an object)
				try {
					cm = (ChatMessage) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}

				// Type of message
				switch(cm.getType()) {
				case ChatMessage.BROADCAST:
					broadcast(cm);
					break;
				case ChatMessage.MESSAGE:
					sendMessage(cm,clientID);
					break;
				case ChatMessage.IMAGE:
					sendImage(cm,clientID);
					break;
				case ChatMessage.LIST:
					listMessages(clientID);
					break;
				case ChatMessage.DELETE:
					deleteMessage(cm.getID());
					break;
				}
			}
			//remove(id);
			close();
		}

		// try to close everything
		private void close() {
			// try to close the connection
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}

		/*
		 * Write a String to the Client output stream
		 */
		private boolean writeMsg(String msg) {
			// if Client is still connected send the message to it
			if(!socket.isConnected()) {
				close();
				return false;
			}
			// write the message to the stream
			try {
				sOutput.writeObject(msg);
			}
			// if an error occurs, do not abort just inform the user
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}
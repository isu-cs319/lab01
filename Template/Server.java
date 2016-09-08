package Template;
//package lab3;

import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import java.io.*;

public class Server implements Runnable
{
	
	private ServerSocket serverSocket = null;
	private Thread mainThread = null;
	private File file = new File("chat.txt");
	private PrintWriter writer;
	//private ServerGUI frame;
	private Thread guiMessageThread;

	public Server(int port)
	{
		//TODO Binding and starting server
		try
		{
			System.out.println("Binding to port " + port + ", please wait  ...");
			serverSocket = new ServerSocket(port);
			System.out.println("Server started: " + serverSocket);
			start();
		} catch (IOException ioe)
		{
			System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
		}
	}

	public void run()
	{
		//TODO wait for a client or show error
		
	}

	public void start()
	{
		 //frame = new ServerGUI();
		 //frame.setVisible(true);
		int clientNum = 0;
		//TODO launch a thread to read for new messages by the server
		while (true) {
			Socket clientSocket = null;
			try {

				// 2.1 WAIT FOR CLIENT TO TRY TO CONNECT TO SERVER
				System.out.println("Waiting for client " + (clientNum + 1)
						+ " to connect!");
				clientSocket = serverSocket.accept(); // 4.

				// 2.2 SPAWN A THREAD TO HANDLE CLIENT REQUEST
				System.out.println("Server got connected to client "
						+ ++clientNum);
				Thread t = new Thread(new ClientHandler(clientSocket, clientNum));
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}

			// 2.3 GO BACK TO WAITING FOR OTHER CLIENTS
			// (While the thread that was created handles the connected client's
			// request)

		} // end while loop
	}
	
	public void stop()
	{
		//TODO
		
	}

	private int findClient(int ID)
	{
		//TODO Find Client
		

		return -1;
	}

	public synchronized void handle(String input)
	{
		// TODO new message, send to clients and then write it to history
	
		//TODO update own gui
		
	}

	public synchronized void remove(int ID)
	{
		//TODO get the serverthread, remove it from the array and then terminate it
		
	}

	private void addThread(Socket socket)
	{
		//TODO add new client
		
	}

	public static void main(String args[])
	{
		Server server = new Server(1222);
	}
}
class ClientHandler implements Runnable {
	Socket s; // this is socket on the server side that connects to the CLIENT
	int num; // keeps track of its number just for identifying purposes
	String name; // Name of Client

	ClientHandler(Socket s, int n) {
		this.s = s;
		num = n;
	}

	// This is the client handling code
	// This keeps running handling client requests 
	// after initially sending some stuff to the client
	public void run() { 
		Scanner in;
		PrintWriter out;
		
		try {
			// 1. GET SOCKET IN/OUT STREAMS
			in = new Scanner(new BufferedInputStream(s.getInputStream())); 
			out = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
			int response = 0;
			
			// 2. PRINT SOME STUFF TO THE CLIENT
			if (name.contains("admin")) {
				while (response != 1 && response != 2 && response != 3) {
					// 3. KEEP LISTENING AND RESPONDING TO CLIENT REQUESTS
					out.println("1. Broadcast message to all clients.");
					out.flush();
					out.println("2. List messages so far.");
					out.flush();
					out.println("3. Deleted a selected message");
					out.flush();
					response = Integer.parseInt(in.nextLine());
					}
				handleAdminRequest(response);
			}
			else {
			while (response != '1' && response != '2') {
			// 3. KEEP LISTENING AND RESPONDING TO CLIENT REQUESTS
			out.println("1. Send a txt message to the server.");
			out.flush();
			out.println("2. Send a image file to the server.");
			out.flush();
			response = Integer.parseInt(in.nextLine());// (int) in.nextInt();
			}
			handleRequest(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// This handling code dies after doing all the printing
	} // end of method run()
	
	void handleRequest(int c) {
		if (c == 1){
			chat();
		}
		else{
			uploadImage();
		}
	}
	
	void chat(){
		// TODO: Encrypt, save to chat.txt
		System.out.println("Called chat() from server-side");
	}
	
	void uploadImage(){
		
	}
	
	void handleAdminRequest(int c){
		if (c == 1){
			broadcast();
		}
		else if (c == 2){
				
		}
		else{
			deleteMessage();
		}
	}
	
void broadcast() {
		
}

void listHistory() {
	// TODO: Decrypt, load from chat.txt
}

void deleteMessage() {
	
}

} // end of class ClientHandler
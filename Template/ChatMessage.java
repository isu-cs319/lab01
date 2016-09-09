package Template;

import java.io.*;
/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

	protected static final long serialVersionUID = 1112122200L;

	// The different types of message sent by the Client
	// WHOISIN to receive the list of the users connected
	// MESSAGE an ordinary message
	// LOGOUT to disconnect from the Server
	static final int MESSAGE = 1, IMAGE = 2, BROADCAST = 3, LIST = 4, DELETE = 5;
	public static int count = 0;
	private int type;
	private String message;
	private int id;
	// constructor
	ChatMessage(int type, String message, int ID) {
		this.type = type;
		this.message = message;
		this.id = ID;
	}
	
	// getters
	int getType() {
		return type;
	}
	String getMessage() {
		return message;
	}
	int getID() {
		return id;
	}
	void setID(int id) {
		this.id = id;
	}
	public static synchronized int incrementCount() {
        return count++;
    }
}



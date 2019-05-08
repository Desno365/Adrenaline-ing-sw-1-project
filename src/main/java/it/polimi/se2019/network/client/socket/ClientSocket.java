package it.polimi.se2019.network.client.socket;

import it.polimi.se2019.network.client.ConnectionToServerInterface;
import it.polimi.se2019.network.client.MessageReceiverInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.utils.ServerConfigParser;
import it.polimi.se2019.utils.Utils;

import java.io.*;
import java.net.Socket;

/**
 * Socket that receives messages from the server and forwards them to the message handler.
 * Also sends client's messages to the server.
 * @author MarcerAndrea
 */
public class ClientSocket extends Thread implements ConnectionToServerInterface {

	private Socket socketClient;
	private MessageReceiverInterface messageReceiver;
	private ObjectInputStream objInStream;
	private ObjectOutputStream objOutStream;
	private boolean active;


	/**
	 * Creates a socket between the client and the server.
	 * @param messageReceiver the associated interface that will receive the messages.
	 */
	public ClientSocket(MessageReceiverInterface messageReceiver) throws IOException{
		super("CUSTOM: Socket Connection to Server"); // Give a name to the thread for debugging purposes.
		this.messageReceiver = messageReceiver;
		socketClient = new Socket(ServerConfigParser.getHost(), ServerConfigParser.getSocketPort());
		objOutStream = new ObjectOutputStream(socketClient.getOutputStream());
		objInStream = new ObjectInputStream(socketClient.getInputStream());
		active = true;
		this.start();
	}


	/**
	 * Send a message to the server.
	 * @param message the message to send.
	 */
	@Override // Of ConnectionToServerInterface.
	public void sendMessage(Message message) {
		try {
			objOutStream.writeObject(message);
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: sendMessage()", e);
		}
	}

	/**
	 * Returns true if and only if the connection is active.
	 * @return true if and only if the connection is active.
	 */
	@Override // Of ConnectionToServerInterface.
	public boolean isConnectionActive() {
		return active;
	}

	/**
	 * Closes the connection with the server.
	 */
	@Override // Of ConnectionToServerInterface.
	public void closeConnection() {
		active = false;
		try {
			socketClient.close();
		} catch (IOException e) {
			Utils.logError("Error in ClientSocket: closeConnection()", e);
		}
	}

	/**
	 * Listens for messages from the server.
	 */
	@Override // Of Thread.
	public void run() {
		try{
			while(isConnectionActive()){
				Message message = (Message) objInStream.readObject();
				messageReceiver.processMessage(message);
			}
		} catch (IOException | ClassNotFoundException e) {
			Utils.logWarning("Connection closed by the server.");
			closeConnection();
			messageReceiver.lostConnection();
		}
	}
}

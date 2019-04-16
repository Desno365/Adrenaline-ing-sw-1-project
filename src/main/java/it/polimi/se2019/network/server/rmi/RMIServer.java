package it.polimi.se2019.network.server.rmi;

import it.polimi.se2019.network.ConnectionInterface;
import it.polimi.se2019.network.message.Message;
import it.polimi.se2019.network.server.MessageHandler;
import it.polimi.se2019.utils.Utils;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerSkeletonInterface {

	private transient MessageHandler messageHandler;


	/**
	 * Create a new instance of a RMIServer and start it.
	 * @param serverMessageHandler TODO
	 * @throws RemoteException
	 */
	public RMIServer(MessageHandler serverMessageHandler) throws RemoteException {
		super();
		this.messageHandler = serverMessageHandler;
		startRMIServer();
	}

	/**
	 * Register a new client that is connected to the server. This method is called remotely by the client.
	 * @param client the RMI implementation of the client.
	 * @throws RemoteException
	 */
	@Override
	public void registerClient(ConnectionInterface client) throws RemoteException {
		messageHandler.onClientRegistration(client);
	}

	/**
	 * Receives a message from the client and handles it. This method is called remotely by the client.
	 * @param message the message received from the client.
	 * @throws RemoteException
	 */
	@Override
	public void receiveMessage(ConnectionInterface client, Message message) throws RemoteException {
		messageHandler.onMessageReceived(client, message);
	}

	/**
	 * Start the RMI server and register it on the RMI registry,
	 * @throws RemoteException
	 */
	private void startRMIServer() throws RemoteException {
		// Register server.
		System.setProperty("java.rmi.server.hostname", "localhost");
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind("Server", this);

		Utils.logInfo("RMI server is ready.");
	}
}

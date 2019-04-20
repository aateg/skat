package br.com.sbk.sbking.networking.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class LobbyServer {

	private static final int NUMBER_OF_PLAYERS_TO_FORM_A_GAME = 4;

	final static Logger logger = Logger.getLogger(LobbyServer.class);

	private static final String NETWORKING_CONFIGURATION_FILENAME = "networkConfiguration.cfg";
	private static final int COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR = 1;

	private static final int MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS = 2;

	private ExecutorService pool;
	private Collection<GameServer> runningGameServers = new ArrayList<GameServer>();

	public LobbyServer() {
		this.pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS);
	}

	public void run() {
		int port = this.getPortFromNetworkingProperties();

		try (ServerSocket listener = new ServerSocket(port)) {
			logger.info("Game Server is Running...");
			logger.info("My InetAddress is: " + listener.getInetAddress());
			logger.info("Listening for connections on port: " + port);

			GameServer gameServer = new GameServer();
			logger.info("Created new gameServer");

			for (int i = 0; i < NUMBER_OF_PLAYERS_TO_FORM_A_GAME; i++) {
				runningGameServers.add(gameServer);
				Socket connectingPlayerSocket = listener.accept();
				Serializator connectingPlayerSerializator = initializeSerializator(connectingPlayerSocket);
				if (connectingPlayerSocket == null || connectingPlayerSerializator == null) {
					logger.error("Could not communicate with client. Will not add it and listen for next one.");
					continue;
				}
				PlayerNetworkInformation connectingPlayerNetworkInformation = new PlayerNetworkInformation(
						connectingPlayerSocket, connectingPlayerSerializator);
				gameServer.addPlayer(connectingPlayerNetworkInformation);
			}
			pool.execute(gameServer);
		} catch (IOException e) {
			logger.fatal("Fatal error listening to new connections. Exiting lobby server.");
			logger.fatal(e);
		}
		logger.info("Lobby has ended. Exiting main thread.");
	}

	private Serializator initializeSerializator(Socket socket) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			return new Serializator(objectInputStream, objectOutputStream);
		} catch (Exception e) {
			logger.debug(e);
		}
		return null;
	}

	private int getPortFromNetworkingProperties() {
		FileProperties fileProperties = new FileProperties(NETWORKING_CONFIGURATION_FILENAME);
		int port = 0;
		try {
			NetworkingProperties networkingProperties = new NetworkingProperties(fileProperties,
					new SystemProperties());
			port = networkingProperties.getPort();
		} catch (Exception e) {
			logger.fatal("Could not get port from properties.");
			logger.debug(e);
			System.exit(COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR);
		}

		return port;
	}

}

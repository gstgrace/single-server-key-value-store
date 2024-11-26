package server;

import common.LoggerInterface;
import common.Logger;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP server that handles client connections and performs key-value operations.
 */
public class TCPServer implements ServerInterface {
  private final int port;
  private final KeyValueStore store;
  private final LoggerInterface logger;
  private ServerSocket serverSocket;

  /**
   * Constructs a TCP server on the specified port.
   *
   * @param port The port on which the server will run.
   */
  public TCPServer(int port) {
    this.port = port;
    this.store = new KeyValueStore();
    this.logger = new Logger("TCPServerLog.log");
  }

  /**
   * Starts the TCP server and listens for client connections.
   */
  @Override
  public void execute() {
    try {
      serverSocket = new ServerSocket(port);
      logger.log("INFO", "TCPServer running on port " + port);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        logger.log("INFO", "Connection established with " + clientSocket.getInetAddress());

        // Handle client in a separate method
        handleClient(clientSocket);
      }
    } catch (IOException e) {
      logger.log("ERROR", "Server error: " + e.getMessage());
    } finally {
      shutdown();
    }
  }

  /**
   * Handles communication with the connected client.
   *
   * @param clientSocket The socket for the connected client.
   */
  private void handleClient(Socket clientSocket) {
    try (BufferedReader in = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

      String input;
      while ((input = in.readLine()) != null) {
        String response = handleRequest(input, clientSocket.getInetAddress().toString());
        out.println(response);
        logger.log("INFO", "Processed request: " + input);
      }
    } catch (IOException e) {
      logger.log("ERROR", "Client handling error: " + e.getMessage());
    }
  }

  /**
   * Processes the client's request and returns a response.
   *
   * @param request The client's request.
   * @param clientAddress The address of the connected client.
   * @return The server's response to the request.
   */
  private String handleRequest(String request, String clientAddress) {
    if (request == null || request.trim().isEmpty()) {
      logger.log("WARNING", "Received empty request from " + clientAddress);
      return "ERROR: Empty request";
    }

    String[] tokens = request.trim().split("\\s+", 3);
    String command = tokens[0].toUpperCase();

    try {
      switch (command) {
        case "PUT":
          if (tokens.length != 3) return "ERROR: Invalid PUT command";
          String putResult = store.put(tokens[1], tokens[2]);
          return "PUT_SUCCESS";
        case "GET":
          if (tokens.length != 2) return "ERROR: Invalid GET command";
          String value = store.get(tokens[1]);
          return value != null ? "GET_SUCCESS: " + value : "GET_FAILURE: Key not found";
        case "DELETE":
          if (tokens.length != 2) return "ERROR: Invalid DELETE command";
          String removed = store.delete(tokens[1]);
          return removed != null ? "DELETE_SUCCESS" : "DELETE_FAILURE: Key not found";
        default:
          return "ERROR: Unknown command";
      }
    } catch (Exception e) {
      logger.log("ERROR", "Error handling request from " + clientAddress + ": " + e.getMessage());
      return "ERROR: Exception occurred while processing request";
    }
  }

  /**
   * Shuts down the TCP server and closes the server socket.
   */
  @Override
  public void shutdown() {
    try {
      if (serverSocket != null && !serverSocket.isClosed()) {
        serverSocket.close();
      }
      logger.close();
    } catch (IOException e) {
      logger.log("ERROR", "Error closing server socket: " + e.getMessage());
    }
  }
}

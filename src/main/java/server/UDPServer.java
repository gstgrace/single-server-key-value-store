package server;

import common.LoggerInterface;
import common.Logger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP server that handles client requests and performs key-value operations.
 */
public class UDPServer implements ServerInterface {
  private final int port;
  private final KeyValueStore store;
  private final LoggerInterface logger;
  private DatagramSocket socket;

  /**
   * Constructs a UDP server on the specified port.
   *
   * @param port The port on which the server will run.
   */
  public UDPServer(int port) {
    this.port = port;
    this.store = new KeyValueStore();
    this.logger = new Logger("UDPServerLog.log");
  }

  /**
   * Starts the UDP server and listens for client requests.
   */
  @Override
  public void execute() {
    try {
      socket = new DatagramSocket(port);
      logger.log("INFO", "UDPServer running on port " + port);
      byte[] buffer = new byte[1024];

      while (true) {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String request = new String(packet.getData(), 0, packet.getLength());

        logger.log("INFO", "Received request from " + packet.getAddress() + ": " + request);

        String response = handleRequest(request, packet.getAddress().toString());
        byte[] responseData = response.getBytes();

        // Handle response size exceeding buffer
        if (responseData.length > buffer.length) {
          response = "ERROR: Response too large";
          responseData = response.getBytes();
        }

        DatagramPacket responsePacket = new DatagramPacket(
            responseData, responseData.length, packet.getAddress(), packet.getPort());
        socket.send(responsePacket);

        logger.log("INFO", "Sent response to " + packet.getAddress() + ": " + response);
      }
    } catch (Exception e) {
      logger.log("ERROR", "Server error: " + e.getMessage());
    } finally {
      shutdown();
    }
  }

  /**
   * Handles the client's request and returns the appropriate response.
   *
   * @param request The client's request.
   * @param clientAddress The address of the connected client.
   * @return The server's response to the request.
   */
  private String handleRequest(String request, String clientAddress) {
    if (request == null || request.trim().isEmpty()) {
      logger.log("WARNING", "Received empty or malformed request from " + clientAddress);
      return "ERROR: Empty or malformed request";
    }

    String[] tokens = request.trim().split("\\s+", 3);
    String command = tokens[0].toUpperCase();

    try {
      switch (command) {
        case "PUT":
          if (tokens.length != 3) return "ERROR: Invalid PUT command";
          store.put(tokens[1], tokens[2]);
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
   * Shuts down the UDP server and closes the socket.
   */
  @Override
  public void shutdown() {
    if (socket != null && !socket.isClosed()) {
      socket.close();
    }
    logger.close();
  }
}

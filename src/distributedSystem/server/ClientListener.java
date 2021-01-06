package distributedSystem.server;

import distributedSystem.IntegerWrapper;
import distributedSystem.Job;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

class ClientListener extends Thread {
    ServerSocket serverSocket = null;
    Queue<Job> readyJobs;
    static ArrayList<ClientHandler> clients;
    final int MIN_CLIENT_ID = 0;
    static int clientID;

    IntegerWrapper maxJobID;

    public ClientListener(ServerSocket serverSocket, ArrayList<ClientHandler> clients, Queue<Job> readyJobs, IntegerWrapper maxJobID) {
        this.readyJobs = readyJobs;
        this.serverSocket = serverSocket;
        clientID = MIN_CLIENT_ID;
        this.clients = clients;
        this.maxJobID = maxJobID;
    }

    public void run() {
        System.out.println("Started ClientListener thread.");

        while (true) {

            try {
                Socket socket;
                synchronized (serverSocket) {
                    socket = serverSocket.accept();
                }
                System.out.println("Connection Established with client " + (clientID + 1));

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println(++clientID);
                System.out.println("assigned clientID to client " + (clientID));

                ClientHandler client = new ClientHandler(in, out, clientID, readyJobs, maxJobID);
                clients.add(client);

                System.out.println("Added client " + clientID + " to client list");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Connection Error");

            }
        }

    }

    public static void removeClient(ClientHandler client) {
        try {
            client.clientCommunicationThread.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.clientWriter.close();
        clients.remove(client);
        System.out.println("Client number " + client.clientID + " has disconnected.");
    }
}
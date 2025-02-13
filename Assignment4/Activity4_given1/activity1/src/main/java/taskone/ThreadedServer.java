/**
 File: Server.java
 Author: Student in Fall 2020B
 Description: Server class in package taskone.
 */

package taskone;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;
import java.util.*;

/**
 * Class: Server
 * Description: Server tasks.
 */
class ThreadedServer extends Thread{
    static Socket conn;
    static Performer performer;
    private int id;

    public ThreadedServer(Socket sock, int id) {
        this.conn = sock;
        this.id = id;
    }

    public static void main(String[] args) throws Exception {
        int port;
        Socket sock;
        int id = 0;
        StringList strings = new StringList();
        performer = new Performer(strings);

        if (args.length != 1) {
            // gradle runServer -Pport=9099 -q --console=plain
            System.out.println("Usage: gradle runServer -Pport=9099 -q --console=plain");
            System.exit(1);
        }
        port = -1;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port] must be an integer");
            System.exit(2);
        }
        ServerSocket server = new ServerSocket(port);
        System.out.println("Threaded Server Started...");
        while (true) {
            System.out.println("Accepting a Request...");
            sock = server.accept();
            ThreadedServer threadedServer = new ThreadedServer(sock, id++);
            threadedServer.start();
        }
    }

    public void run() {
        boolean quit = false;
        OutputStream out = null;
        InputStream in = null;
        try {
            out = conn.getOutputStream();
            in = conn.getInputStream();
            System.out.println("Threaded Server connected to client:");
            while (!quit) {
                byte[] messageBytes = NetworkUtils.receive(in);
                JSONObject message = JsonUtils.fromByteArray(messageBytes);
                JSONObject returnMessage = new JSONObject();

                int choice = message.getInt("selected");
                switch (choice) {
                    case (1):
                        String inStr = (String) message.get("data");
                        returnMessage = performer.add(inStr);
                        break;
                    case (2):
                        returnMessage = performer.display();
                        break;
                    case (3):
                        returnMessage = performer.count();
                        break;
                    case (0):
                        returnMessage = performer.quit();
                        break;
                    default:
                        returnMessage = performer.error("Invalid selection: " + choice
                                + " is not an option");
                        break;
                }
                // we are converting the JSON object we have to a byte[]
                byte[] output = JsonUtils.toByteArray(returnMessage);
                NetworkUtils.send(out, output);
            }
            // close the resource
            System.out.println("close the resources of client ");
            out.close();
            in.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

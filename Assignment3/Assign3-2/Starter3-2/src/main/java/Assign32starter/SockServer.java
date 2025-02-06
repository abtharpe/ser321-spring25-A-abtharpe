package Assign32starter;
import java.net.*;
import java.util.Base64;
import java.util.Set;
import java.util.Stack;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;
import java.io.*;
import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * A class to demonstrate a simple client-server connection using sockets.
 * Ser321 Foundations of Distributed Software Systems
 */
public class SockServer {
	static Stack<String> imageSource = new Stack<String>();

	public static void main (String args[]) {
		Socket sock;
		try {

			ServerSocket serv;
			String portString = System.getProperty("port");
			int port = 8888;

			if (portString != null) {
				try {
					port = Integer.parseInt(portString);
					serv = new ServerSocket(port);
				} catch (NumberFormatException e) {
					System.out.println("Invalid port number, using default port: " + port);
					serv = new ServerSocket(port);
				}
			} else {
				serv = new ServerSocket(port);
			}

			System.out.println("Server ready for connetion");

			// placeholder for the person who wants to play a game
			String name = "";
			int points = 0;

			// read in one object, the message. we know a string was written only by knowing what the client sent. 
			// must cast the object from Object to desired type to be useful
			while(true) {
				sock = serv.accept(); // blocking wait

				// could totally use other input outpur streams here
				ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
				OutputStream out = sock.getOutputStream();

				String s = (String) in.readObject();
				JSONObject json = new JSONObject(s); // the requests that is received

				JSONObject response = new JSONObject();

				if (json.getString("type").equals("start")){
					
					System.out.println("- Got a start");
				
					response.put("type","hello" );
					response.put("value","Hello, Please start by telling me your name in the text box above." );
					response = sendImg("img/hi.png", response); // calling a method that will manipulate the image and will make it send ready
					
				} else if (json.getString("type").equals("hiBack")) {

					name = json.getString("name");
					response.put("type", "hiBack");
					response.put("value", "Howzit, " + name + "!!\n" + "Let's go ahead and start the game!!\n" +
							"You will have to try and guess what the location is for the photo bieng displayed.\n" +
							"Please enter the number for the location you believe to be correct: \n" +
							"1 - England\n 2 - Italy\n 3 - United States");

				} else if (json.getString("type").equals("playing")) {
					//Logic for evaluating a user guess
					String guess = json.getString("guess");
				} else {

					System.out.println("not sure what you meant");
					response.put("type","error" );
					response.put("message","unknown response" );

				}
				PrintWriter outWrite = new PrintWriter(sock.getOutputStream(), true); // using a PrintWriter here, you could also use and ObjectOutputStream or anything you fancy
				outWrite.println(response.toString());
			}
			
		} catch(Exception e) {e.printStackTrace();}
	}

	/* TODO this is for you to implement, I just put a place holder here */
	public static JSONObject sendImg(String filename, JSONObject obj) throws Exception {
		File file = new File(filename);

		if (file.exists()) {
			// import image
			// I did not use the Advanced Custom protocol
			// I read in the image and translated it into basically into a string and send it back to the client where I then decoded again
			byte[] imageBytes = Files.readAllBytes(Paths.get(filename));
			String image = Base64.getEncoder().encodeToString(imageBytes);
			obj.put("image", "temp message");
		} 
		return obj;
	}
}

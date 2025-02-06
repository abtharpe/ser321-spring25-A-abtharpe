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
import java.util.Random;


/**
 * A class to demonstrate a simple client-server connection using sockets.
 * Ser321 Foundations of Distributed Software Systems
 */
public class SockServer {
	static Stack<String> imageSource = new Stack<String>();

	static private String[] images = {
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\Colosseum1.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\Colosseum2.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\Colosseum3.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\Colosseum4.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\GrandCanyon1.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\GrandCanyon2.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\GrandCanyon3.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\GrandCanyon4.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\Stonehenge1.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\Stonehenge3.png",
			"C:\\Users\\Brock Tharpe\\IdeaProjects\\ser321-spring25-A-abtharpe\\Assignment3\\Assign3-2\\Starter3-2\\img\\Stonehenge4.png",};


	public static void main (String args[]) {
		Random random = new Random();
		int randomNum = 11;
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
					
				} else if (json.getString("type").equals("hiBack")) {

					System.out.println("- Got a User Name");

					name = json.getString("name");
					response.put("type", "hiBack");
					response.put("value", "Howzit, " + name + "!!\n" +
							"Please enter a number for one of the menu options below: \n" +
							"1 - See leaderboard\n" +
							"2 - Play the game\n" +
							"3 - Exit game");

				} else if (json.getString("type").equals("menu")) {

					String menuSelect = json.getString("menuSelect");

					if (menuSelect.equals("1")) {

						System.out.println("- Got a User menu selection of 1 - See Leaderboard");

						response.put("type", "leaderboard");

					} else if (menuSelect.equals("2")) {

						System.out.println("- Got a User menu selection of 2 - Play the game");

						response.put("type", "playing");
						response.put("value",
								"Let's go ahead and start the game!!\n" +
										"You will have to try and guess what the location is for the photo bieng displayed.\n" +
										"Please enter the number for the location you believe to be correct: \n" +
										"1 - England\n 2 - Italy\n 3 - United States");

						randomNum = random.nextInt(11);
						response.put("imageName", images[randomNum]);

					} else if (menuSelect.equals("3")) {

						System.out.println("- Got a User menu selection of 3 - Exit game");

						response.put("type", "exit");

					} else {

						response.put("type", "Hi");

					}

				} else if (json.getString("type").equals("playing")) {

					System.out.println("- User is playing game");

					//Logic for evaluating a user guess
					String guess = json.getString("guess");

					if (guess.equals("1") && 8 <= randomNum && randomNum <= 10) {
						//correct guess
						points++;
						response.put("type", "playing");
						response.put("value", "That's Correct!!\n\nPlease enter the number for the location you believe to be correct: \n" +
								"1 - England\n 2 - Italy\n 3 - United States");
						randomNum = random.nextInt(11);
						response.put("imageName", images[randomNum]);
						response.put("score", points);
					} else if (guess.equals("2") && 0 <= randomNum && randomNum <= 3) {
						//correct guess
						points++;
						response.put("type", "playing");
						response.put("value", "That's Correct!!\n\nPlease enter the number for the location you believe to be correct: \n" +
								"1 - England\n 2 - Italy\n 3 - United States");
						randomNum = random.nextInt(11);
						response.put("imageName", images[randomNum]);
						response.put("score", points);
					} else if (guess.equals("3") && 4 <= randomNum && randomNum <= 7) {
						//correct guess
						points++;
						response.put("type", "playing");
						response.put("value", "That's Correct!!\n\nPlease enter the number for the location you believe to be correct: \n" +
								"1 - England\n 2 - Italy\n 3 - United States");
						randomNum = random.nextInt(11);
						response.put("imageName", images[randomNum]);
						response.put("score", points);
					} else {
						//incorrect guess
						response.put("type", "playing");
						response.put("value", "That is not correct):\n\nPlease enter the number for the location you believe to be correct: \n" +
								"1 - England\n 2 - Italy\n 3 - United States");
						randomNum = random.nextInt(11);
						response.put("imageName", images[randomNum]);
					}

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
			obj.put("image", image);
		} 
		return obj;
	}
}

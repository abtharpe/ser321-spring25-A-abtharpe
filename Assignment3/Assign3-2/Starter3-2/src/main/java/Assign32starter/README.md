# Game Protocol and Network Communication

## Project Description
This project implements a client-server-based game where players guess Wonders of the World. The game is played over a network, with the server handling the logic of the game and managing the state, while the client interacts with the user. The client is responsible for collecting user inputs and sending them to the server, while the server processes these inputs and responds accordingly.

## Requirements Checklist

| Requirement                                                                 | Fulfilled (Yes/No) |
|-----------------------------------------------------------------------------|--------------------|
| 1. Keep the Client simple, with most logic handled by the Server            | Yes                |
| 2. Client sends a "hello", and server responds asking for name and age      | Yes                |
| 3. Client sends name and server greets the client by name                    | Yes                |
| 4. Client presents choices for leaderboard, playing the game, or quitting   | Yes                |
| 5. Evaluations of inputs are handled on the server                          | Yes                |
| 6. **Playing the Game** (Start, Guess, Skip, Next, Remaining, Rounds)       | Partial                |
| 7. **Leaderboard functionality** (storing and displaying points)            | No                |
| 8. Display player score after game ends                                      | Yes                |
| 9. Game returns to main menu after completing rounds                        | No                |
| 10. Robust protocol with headers and payloads                               | Partial                |
| 11. Good error handling and recovery for network issues or invalid input   | Partial                |

## Protocol Description

### 1. Client Request: **Start the Game**
- **Description**: The client sends a `start` request to initiate the game.
- **Request Format**:
    ```json
    {
      "type": "start"
    }
    ```
- **Server Response**: The server responds asking the client to provide their name.
    ```json
    {
      "type": "hello",
      "value": "Hello, Please start by telling me your name in the text box above."
    }
    ```

### 2. Client Request: **Provide User Name**
- **Description**: After receiving the "hello" message, the client sends their name.
- **Request Format**:
    ```json
    {
      "type": "hiBack",
      "name": "John Doe"
    }
    ```
- **Server Response**: The server greets the user and prompts them with a menu.
    ```json
    {
      "type": "hiBack",
      "value": "Howzit, John Doe!!\nPlease enter a number for one of the menu options below:\n1 - See leaderboard\n2 - Play the game\n3 - Exit game"
    }
    ```

### 3. Client Request: **Select Menu Option**
- **Description**: The client sends the menu selection (1 for leaderboard, 2 for game, 3 to quit).
- **Request Format**:
    ```json
    {
      "type": "menu",
      "menuSelect": "1"
    }
    ```
- **Server Response**: The server responds based on the selected menu option:
    - **Option 1**: Display leaderboard.
    ```json
    {
      "type": "leaderboard"
    }
    ```
    - **Option 2**: Start the game.
    ```json
    {
      "type": "playing",
      "value": "Let's go ahead and start the game!!\nYou will have to try and guess what the location is for the photo being displayed.\nPlease enter the number for the location you believe to be correct:\n1 - England\n2 - Italy\n3 - United States",
      "imageName": "image1.jpg"
    }
    ```
    - **Option 3**: Exit the game.
    ```json
    {
      "type": "exit"
    }
    ```

### 4. Client Request: **Guess the Location**
- **Description**: The client sends a guess for the location based on the hint provided.
- **Request Format**:
    ```json
    {
      "type": "playing",
      "guess": "1"
    }
    ```
- **Server Response**: The server checks if the guess is correct or not, and responds accordingly.
    - **Correct Guess**: If the guess is correct, the server responds with a new round.
    ```json
    {
      "type": "playing",
      "value": "That's Correct!!\nPlease enter the number for the location you believe to be correct:\n1 - England\n2 - Italy\n3 - United States",
      "imageName": "image2.jpg",
      "score": 1
    }
    ```
    - **Incorrect Guess**: If the guess is incorrect, the server responds with an error message.
    ```json
    {
      "type": "playing",
      "value": "That is not correct):\nPlease enter the number for the location you believe to be correct:\n1 - England\n2 - Italy\n3 - United States",
      "imageName": "image3.jpg"
    }
    ```

### 5. Client Request: **Skip or Next**
- **Description**: The client can send a `skip` or `next` command to move to the next round or hint.
- **Request Format**:
    ```json
    {
      "type": "playing",
      "action": "skip"  // or "next"
    }
    ```
- **Server Response**: If `skip` or `next` is chosen, the server sends the next round of hints or information.
    ```json
    {
      "type": "playing",
      "value": "Here is the next hint!\nPlease enter the number for the location you believe to be correct:\n1 - England\n2 - Italy\n3 - United States",
      "imageName": "image4.jpg"
    }
    ```

### 6. Client Request: **Exit the Game**
- **Description**: If the client decides to exit the game, they send the `exit` request.
- **Request Format**:
    ```json
    {
      "type": "exit"
    }
    ```
- **Server Response**: The server acknowledges the exit request.
    ```json
    {
      "type": "exit",
      "message": "Thanks for playing! Goodbye!"
    }
    ```

### 7. Client Request: **Error Handling**
- **Description**: If the client sends an invalid or unrecognized command, the server will handle it gracefully and inform the client.
- **Request Format**:
    ```json
    {
      "type": "unknown",
      "message": "Some unknown request"
    }
    ```
- **Server Response**: The server responds with an error message.
    ```json
    {
      "type": "error",
      "message": "unknown response"
    }
## Screen Capture
https://youtu.be/8_PyeXnHQ1I 

## UDP utilization
For us to use UDP, we would need to make the following changes.

### 1. Connectionless
- We would need to remove the connection componenets of the program. UDP does not utilize a connection but instead has the client and server send messages independent of each other.

### 2. Message Ordering
- We would need to add in something for ensuring message order

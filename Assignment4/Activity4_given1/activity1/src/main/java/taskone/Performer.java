/**
  File: Performer.java
  Author: Student in Fall 2020B
  Description: Performer class in package taskone.
*/

package taskone;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import static java.lang.Thread.sleep;

/**
 * Class: Performer 
 * Description: Threaded Performer for server tasks.
 */
class Performer {

    private StringList state;


    public Performer(StringList strings) {
        this.state = strings;
    }

    public JSONObject add(String str) throws InterruptedException {
        System.out.println("Start add"); 
        JSONObject json = new JSONObject();
        json.put("datatype", 1);
        json.put("type", "add");
        sleep(6000); // to make this take a bit longer
        state.add(str);
        json.put("data", state.toString());
        System.out.println("end add");
        return json;
    }

    public JSONObject display() throws InterruptedException {
        System.out.println("Start display");
        JSONObject json = new JSONObject();
        json.put("datatype", 2);
        json.put("type", "display");
        sleep(6000); // IDK, seems like the cool kids take an extra 6 seconds
        json.put("data", state.toString());
        System.out.println("end display");
        return json;
    }

    public JSONObject count() throws InterruptedException {
        System.out.println("Start count");
        JSONObject json = new JSONObject();
        json.put("datatype", 3);
        json.put("type", "count");
        sleep(4000); // ): my mom said I can only wait 4 seconds
        json.put("data", String.valueOf(state.size()));
        System.out.println("end count");
        return json;
    }

    public JSONObject quit() {
        System.out.println("Client has quit the connection");
        JSONObject json = new JSONObject();
        json.put("datatype", 0);
        json.put("type", "quit");
        json.put("data", "Client Quit");
        return json;
    }

    public static JSONObject error(String err) {
        JSONObject json = new JSONObject();
        json.put("error", err);
        return json;
    }


}

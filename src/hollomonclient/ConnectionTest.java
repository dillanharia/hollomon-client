package hollomonclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionTest {

    public static void main(String[] args) {
        connectAndLogin();
    }
    	
    	
    private static void connectAndLogin() {
    	String serverHost = "netsrv.cim.rhul.ac.uk";
        int serverPort = 1812;

        
        String username = "evidence".toLowerCase();
        String password = "internationaltodiscover";

        try (
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Connected to server.");

            outputWriter.println(username);
            outputWriter.println(password);

            String serverResponse;
            while ((serverResponse = inputReader.readLine()) != null) {
                System.out.println(serverResponse);

                if (serverResponse.equals("OK")) {
                    break;
                }
            }

            System.out.println("Connection closed.");
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
            e.printStackTrace();
        }
    }
}
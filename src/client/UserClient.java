package client;

import server.ChatInterface;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class UserClient {

    private User user;

    public static void main(String [] args) {

        try {

            if (args.length < 1) {
                System.out.println("Usage: java UserClient <rmiregistry host>");
                return;
            }

            String host = args[0];

            Registry registry = LocateRegistry.getRegistry(host);
            ChatInterface h = (ChatInterface) registry.lookup("ChatService");

            Scanner scan = new Scanner(System.in);
            String first_name = "";
            String last_name = "";

            System.out.println("TESTTT");

            while (first_name.isEmpty()) {
                System.out.println("Enter your first name: ");
                first_name = scan.nextLine();
            }

            while (last_name.isEmpty()) {
                System.out.println("Enter your first name: ");
                last_name = scan.nextLine();
            }

            User u = new User();
            u.setFirstName(first_name);
            u.setLastName(last_name);

            String message = "";
            while ((message = scan.nextLine()) == null) {
                h.say(u, message);
            }

        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }

    }

}

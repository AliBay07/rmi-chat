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
            String userName = "";
            int response = -1;
            User u = null;

            while (userName.isEmpty() || response == -1) {
                System.out.println("Enter your user name: ");
                userName = scan.nextLine();

                if (!(userName.isEmpty())) {
                    u = new User();
                    u.setUserName(userName);

                    response = h.enter(u);

                    if (response == -1) {
                        System.out.println("UserName already taken, choose another one!");
                    }
                }
            }

            String message = "";
            while (!(message.equals(":qw!"))) {
                System.out.print("Enter message (:qw! to leave): ");
                message = scan.nextLine();
                if (!(message.equals(":qw!"))) {
                    h.say(u, message);
                }
            }

            h.exit(u);

        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }
    }
}

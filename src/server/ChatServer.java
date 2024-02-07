package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ChatServer {

    public static void main(String [] args) {

        try {

            Chat c = new Chat();
            ChatInterface chat_ref = (ChatInterface) UnicastRemoteObject.exportObject(c, 0);

            Registry registry = null;
            if (args.length > 0)
                registry= LocateRegistry.getRegistry(Integer.parseInt(args[0]));
            else
                registry = LocateRegistry.getRegistry();
            registry.rebind("ChatService", chat_ref);

            System.out.println ("Server ready");


        } catch (Exception e) {
            System.err.println("Error on server :" + e);
            e.printStackTrace();
        }


    }
}

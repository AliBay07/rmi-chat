package server;

import client.User;
import client.UserClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote {

    public int enter(User u) throws RemoteException;
    public int exit(User u) throws RemoteException;
    public void say(User u, String message) throws RemoteException;

}

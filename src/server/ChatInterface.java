package server;

import client.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote {
    int enter(User u, ChatObserver observer) throws RemoteException;
    int exit(User u, ChatObserver observer) throws RemoteException;
    void say(User u, String message) throws RemoteException;
}

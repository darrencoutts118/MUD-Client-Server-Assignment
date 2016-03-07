/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3524.solutions.mud;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MUDService extends Remote
{
    
    public String introduction() throws RemoteException;
  
    
    public String getStartLocation() throws RemoteException;
    public String location(String location) throws RemoteException;
    public String moveDirection(String current, String direction) throws RemoteException;
    public boolean addUser(String username) throws RemoteException;
    public void updateUserLocation(String username, String location) throws RemoteException ;
    public String getPlayersAtLocation(String location) throws RemoteException;
    public boolean takeItem(String item, String location) throws RemoteException;
    public void changeMUD(String name) throws RemoteException;
    public String getServers() throws RemoteException;


}

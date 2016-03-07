/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3524.solutions.mud;

import java.rmi.*; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author darren
 */
public class MUDServiceImpl implements MUDService {
    
    private MUD m; 
    public Map<String, MUD> Servers = new HashMap<String, MUD>(); 
    
    
    public MUDServiceImpl()    throws RemoteException
    { 
        Servers.put("demo", new MUD("mymud.edg","mymud.msg","mymud.thg"));
        Servers.put("demo2", new MUD("mymud.edg","mymud.msg","mymud.thg"));
    }
    
    public String introduction() throws RemoteException
    {
        if(m==null){
            m = Servers.get("demo");
        }
        String output = ( "=============================== \n \n Welcome to the MUD Server! \n \n=============================== \n" );
        output += "Please enter a username: ";

        return output;
        
    }
    
    public String getStartLocation() throws RemoteException {
        return m.startLocation();
    }
    
    
    public String location(String location) throws RemoteException{
        
        return m.getVertex(location).toString();
        
    }
    
    public String moveDirection(String current, String direction) throws RemoteException{
        Vertex currentVertex = m.getVertex(current);
        if(currentVertex._routes.containsKey(direction)){
            Edge newLocation = currentVertex._routes.get(direction);
            Vertex newVert = (newLocation._dest);
            //System.out.print(newVert._name);
            
        return newVert._name;
        } else {
            return current;
        }
    }


    public boolean addUser(String username) throws RemoteException {
        
        if(m.users.size() < 10){
            m.users.put(username, m.startLocation());
            return true;
        } else {
            return false;
        }
    }
    
    public void updateUserLocation(String username, String location) throws RemoteException {
        m.users.remove(username);
        m.users.put(username, location);
    
        //System.out.println(m.users);
    
    }
    
    public String getPlayersAtLocation(String location) throws RemoteException{
        
        ArrayList<String> Players = new ArrayList<String>();
        String username;
        
        StringBuilder sb = new StringBuilder(); 
        
        Iterator itter = m.users.keySet().iterator();
        
        while (itter.hasNext()) {
	    username = itter.next().toString();
            if(m.users.get(username).equalsIgnoreCase(location)){
                Players.add(username);
                sb.append(username);
                sb.append(", ");
            }
                        
	}
 
        sb.setLength(sb.length() - 2);
        
        return "You can see: " + sb.toString();
        
    }
    
    public boolean takeItem(String item, String location) throws RemoteException {
        Vertex currentVertex = m.getVertex(location);
        List<String> things = currentVertex._things;        
        if(things.contains(item)){
            m.delThing(location, item);
            
            return true;
        }
        
        return false;
    }
    
    public void changeMUD(String name) throws RemoteException {
        //System.out.println("Server is changing to " + name);
        m = Servers.get(name);
        
    }
    
    public String getServers() throws RemoteException{
    
        StringBuilder sb = new StringBuilder();
        Iterator it = Servers.keySet().iterator();
        
        while(it.hasNext()){
            sb.append(it.next().toString());
            sb.append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        
        return "Currently running servers: " + sb.toString();
    }

    
    
}

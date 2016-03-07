/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3524.solutions.mud;

import java.rmi.Naming; 
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;

import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.InetAddress;
import java.util.Iterator;

/**
 *
 * @author darren
 */
public class MUDClient {

    static MUDService service;
    static BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
    private static String username;
    private static String location; 
    private static String servername = "demo";
    private List<String> inv;
    
    public static void main(String args[]) throws Exception{
        
        if(args.length < 2){
            System.err.println("Missing arguments <host> <port>");
            return;
        } 
        
        // Parse arguments 
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        
        // Setup Security Manager
        System.setProperty("java.security.policy", "mud.policy");
        System.setSecurityManager( new RMISecurityManager() );
        
        try {
            
            String regURL = "rmi://" + hostname + ":" + port + "/MudService";
            service = (MUDService)Naming.lookup(regURL);
            
            setup();            
            
        } 
        catch (java.io.IOException e) {
            System.err.println( "I/O error." );
            System.err.println( e.getMessage() );
        }
        catch (java.rmi.NotBoundException e) {
            System.err.println( "Server not bound." );
            System.err.println( e.getMessage() );
        }
        
    }
    
    static void setup() throws Exception {
        System.out.println(service.getServers());
            
        System.out.println("Please enter servername:");
        servername = in.readLine();

        service.changeMUD(servername);

        System.out.println(service.introduction());

        username = in.readLine();
        location = service.getStartLocation();

        if(service.addUser(username)){
                        systemStarted();

        } else {
            System.out.println("Sory - this server is currently busy. Please try again later");
        }
    }
    
    
    static void systemStarted() throws Exception{
        
        boolean accepting = true;
        
        location(location);
        
        try {
        
            while(accepting){
                String input = in.readLine();
    
                service.changeMUD(servername);

                if(input.equals("quit") || input.equals("exit")){
                    accepting = false;
                } else if(input.equals("whoami?")){
                    System.out.println(username);
                } else if(input.toLowerCase().contains("move")){
                    String moving[] = input.split(" ");
                    String direction = moving[1];
                    
                    if(direction.equalsIgnoreCase("north") || direction.equalsIgnoreCase("east") || direction.equalsIgnoreCase("south") || direction.equalsIgnoreCase("west")){
                       String newlocation = service.moveDirection(location, direction);
                       if(newlocation.equals(location)){
                           System.out.println("Can not move " + direction);
                       } else {
                           location = newlocation;
                           location(location);
                           service.updateUserLocation(username, location);
                       }
                    } else {
                        System.out.println("Unknown Direction " + direction);
                    }
                    
                } else if(input.equals("who")){
                    System.out.println(service.getPlayersAtLocation(location) + "\n");
                } else if(input.contains("take")){
                    String splt[] = input.split(" ");
                    String item = splt[1];
                    
                    if(service.takeItem(item, location)){
                        System.out.println("You now own the " + item+"\n");
                    } else {
                        System.out.println("Could not take the " + item+"\n");
                    }
                }
            }

        } catch(Exception e){
            return;
        }
        
    }
    
    static void location(String locationname) throws Exception{
        
        System.out.println(service.location(locationname));
        
    }
    
}

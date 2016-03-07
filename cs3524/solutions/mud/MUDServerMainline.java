/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs3524.solutions.mud;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author darren
 */
public class MUDServerMainline {
    
    static BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));

    
    public static void main(String args[]){
        
        if(args.length < 2){
            System.err.println("You must provide two arguments: <regport> <serverport>");
            return;
        } 
        
        int registryPort = Integer.parseInt(args[0]);
        int serverPort = Integer.parseInt(args[1]);
   
        System.out.println("Attempting to start server running on port " + Integer.toString(registryPort));
        
        try {
        
            String hostname = (InetAddress.getLocalHost()).getCanonicalHostName();
            
            // Setup Security Manager
            System.setProperty("java.security.policy", "mud.policy");
            System.setSecurityManager( new RMISecurityManager() );

            // Generate the remote objects
            MUDServiceImpl mudservice = new MUDServiceImpl();
            MUDService mudstub = (MUDService)UnicastRemoteObject.exportObject(mudservice, serverPort);
            
            String regURL = "rmi://" + hostname + ":" + registryPort + "/MudService";
            
            try {
            Naming.rebind(regURL, mudstub);
            
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("Server is running at "+regURL);
            System.out.println("Launching Admin Mode");
            
            
            while(true){
                
                String input = in.readLine();
                
                if(input.contains("create")){
                    String[] arguments = input.split(" ");
                    
                    if(mudservice.Servers.size() < 5){
                        System.out.println("Create a mud with the name " + arguments[1]);

                        MUD newmud = new MUD(arguments[2],arguments[3],arguments[4]);
                        mudservice.Servers.put(arguments[1], newmud);
                    } else {
                        System.out.println("Sorry - you can only have 5 MUD's running at a time");
                    }
                }
                
                
            }
            
            
        } 
        catch (java.net.UnknownHostException e) {
            System.err.println("Gannot get local host name.");
        }
        catch (java.io.IOException e){
            System.err.println("Failed to regitser.");
        }
        
    }
    
}

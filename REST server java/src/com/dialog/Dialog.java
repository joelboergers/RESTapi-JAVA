package com.dialog;

import com.server.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Dialog {
	private static final int SERVERSTART    = 1;
    private static final int SERVERENDE     = 2;
    private static final int INFO			= 3;
    private static final int SERVERSTATUS	= 4;
    private static final int ENDE           = 99;
    
    private int status = 0;
    Scanner input                           = new Scanner(System.in);
    
    private static int port = 4711;
    private Server server;
    
    
    public static void main(String[] args) throws IOException{
        new Dialog().start();
        
        
    }
  
    public void start() throws IOException{
    	server = new Server(port);
        int funktion = -1;
        while (funktion != ENDE){
            try{
                funktion = einlesenFunktion();
                ausfuehrenFunktion(funktion);
            } catch (IllegalArgumentException e){
                System.out.println(e);
            } catch (InputMismatchException e){
                System.out.println(e);
                input.nextLine();
            } catch (Exception e){
                System.out.println("Ausnahme gefangen: " + e);
                e.printStackTrace(System.out);   
            }
            
        }
        
    
    }
    

   private int einlesenFunktion(){
        System.out.println("Wählen Sie ein Ziffer, um die entsprechende Aktion durchzuführen!"              	+ "\r\n"
        				+ "Wenn der Server gestartet ist können Sie einfache Aktionen über POSTMAN duchführen."	+ "\r\n"
                        + SERVERSTART               +   ": Startet den Server"                              	+ "\r\n"
                        + SERVERENDE                +   ": Stop den server."                                	+ "\r\n"
                        + INFO						+	": Info uber die Postman Methoden"						+ "\r\n"
                        + SERVERSTATUS				+	": Server status"										+ "\r\n"
                        + ENDE                      +   ": Programm beenden."                               	+ "\r\n"
                        + "-----------------------------------------------------\r\n");
                       
        return input.nextInt();
   }
   

    private void ausfuehrenFunktion(int funktion){
        switch (funktion){
            case SERVERSTART:
            	try {
            		server.start();
            		status = 1;
        		}
        		catch(Exception exception) {
        			System.out.println("Server already running");
        		}
            	break;
            	
            case SERVERENDE:
            	try {
            		server.stop();
            		status = 0;
            	}catch(Exception exception) {
        			System.out.println("Error during server stop");
        		}
            	break;
            	
            case INFO:
                try {
        			Scanner scanner = new Scanner(new File("readme.txt"));
        			while (scanner.hasNextLine()) {
        				System.out.println(scanner.nextLine());
        			}
        			scanner.close();
        		} catch (FileNotFoundException e) {
        			e.printStackTrace();
        		}
                break;
                
            case SERVERSTATUS:
            	if(status != 0) {
            		System.out.println("Server is running");
            		break;
            	}
            	System.out.println("Server is not running");
                break;
                
            case ENDE:
                System.out.println("Programmende");
                System.exit(0);
                break;
                
            default:
                throw new IllegalArgumentException("Die Angabe ist falsch gegeben!");
        }
    }        
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

/**
 *
 * @author kaspe
 */
public class ClientHandler extends Thread
{

    private final Socket s;
    private final Scanner input; 
    private final PrintWriter writer; 

    public ClientHandler(Socket s) throws IOException
    {
        this.s = s;
        
        input = new Scanner(s.getInputStream());
        writer = new PrintWriter(s.getOutputStream(), true);
        System.out.println("const done");
    }
    
    public void send(String msg)
    {
        writer.println(msg.toUpperCase());
    }

    @Override
    public void run()
    {
        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.STOP))
        {
            EchoServer.send(message);
            Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
        }
        writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
        try
        {
            EchoServer.removeHandler(this);
            s.close();
            
        } catch (IOException ex)
        {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(EchoServer.class.getName()).log(Level.INFO, "Closed a Connection");
    }

}

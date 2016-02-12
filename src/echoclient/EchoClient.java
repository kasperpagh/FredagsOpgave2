package echoclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;
import utils.Utils;

public class EchoClient extends Observable implements Runnable
{

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    private Gui gui;
    private boolean running;
        private static final Properties properties = Utils.initProperties("server.properties");

    public void connect(String address, int port) throws UnknownHostException, IOException
    {

        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
    }

    public void send(String msg)
    {
        output.println(msg);
    }

    public void stop() throws IOException
    {
        output.println(ProtocolStrings.STOP);
        running = false;
    }

    public void run()
    {
        running = true;
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        
        try
        {
            connect(ip, port);
            System.out.println("Jeg er nu connected");
        } catch (IOException ex)
        {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (running)
        {
            String msg = input.nextLine();
            System.out.println("her er input: " + msg);
            setChanged();
            notifyObservers(msg);
            if (msg.equals(ProtocolStrings.STOP))
            {
                try
                {
                    socket.close();
                } catch (IOException ex)
                {
                    Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

//    public static void main(String[] args) throws IOException
//    {
//
//
//        
//        if (args.length == 2)
//        {
//            port = Integer.parseInt(args[0]);
//            ip = args[1];
//        }
//        try
//        {
//
////            EchoClient tester = new EchoClient();
////            tester.connect(ip, port);
////            System.out.println("Sending 'Hello world'");
////            tester.send("Hello World");
////            System.out.println("Waiting for a reply");
////            System.out.println("Received: "); //Important Blocking call         
////            tester.stop();
////            System.in.read();
//        } catch (UnknownHostException ex)
//        {
//            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex)
//        {
//            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
}

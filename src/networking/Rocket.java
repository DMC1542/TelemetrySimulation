package networking;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.DatagramPacket;

public class Rocket
{
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private DatagramPacket packet;

    public Rocket(String hostName, int port)
    {
        try{
            byte[] tempData = new byte[200];

            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(hostName);
            this.port = port;
            this.packet = packet = new DatagramPacket(tempData, tempData.length , address , port);
            
        } catch (SocketException se) {
            System.out.println("SocketException thrown: " + se);
        } catch (UnknownHostException uhe) {
            System.out.println("UnknownHostException thrown: " + uhe);
        }
    }
    
    public void broadcast()
    {
        try{
            socket.send(packet);
        } catch (IOException ioe) {
            System.out.println("IOException thrown from rocket while broadcasting: " + ioe);
        }
    }

    public void preparePacket()
    {

    }
}
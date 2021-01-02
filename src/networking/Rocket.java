package networking;

import sim.Simulation;
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
            byte[] tempData = new byte[100];

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

    public void preparePacket(Simulation sim)
    {
        /*
            Structure: (Starting Byte | Data)
            0 | velocityX
            5 | velocityY
            9 | accelerationX
            13 | accelerationY
            17 | altitude
            21 | temperature
         */

       byte[] data = new byte[100];

       data[0] = (byte)sim.velocityX;
       data[5] = (byte)sim.velocityY;
       data[9] = (byte)sim.accelerationX;
       data[13] = (byte)sim.accelerationY;
       data[17] = (byte)sim.altitude;
       data[21] = (byte)sim.temperature;
    }
}
package networking;

import sim.Simulation;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Rocket
{
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private DatagramPacket packet;

    public Rocket(String hostName, int port)
    {
        try{
            byte[] tempData = new byte[Server.PACKET_SIZE];

            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(hostName);
            this.port = port;
            this.packet = new DatagramPacket(tempData, Server.PACKET_SIZE , address , port);
            
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

    public void preparePacket(float[] simData)
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

        // Prepare temporary array list for appending bytes
        ArrayList<Byte> byteArrayList = new ArrayList<>();

        // Add values to the array list
        for (int i = 0; i < simData.length; i++)
        {
            byte[] temp = ByteBuffer.allocate(4).putFloat(simData[i]).array();

            for (int j = 0; j < temp.length; j++)
            {
                byteArrayList.add(temp[j]);
            }
        }

        // Get a byte[] from ArrayList<Byte>
        byte[] finalData = new byte[Server.PACKET_SIZE];
        for (int i = 0; i < byteArrayList.size(); i++)
        {
            finalData[i] = byteArrayList.get(i).byteValue();
        }

        packet.setData(finalData);
    }
}
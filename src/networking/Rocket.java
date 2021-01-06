package networking;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Emulates the rocket itself during flight. The rocket will be given telemetry from the
 * simulation, and the rocket will handle preparing the information and sending it over the
 * network as UDP packets to the ground station, or server.
 *
 * @author Domenic Cacace
 *
 * Language: Java 15
 * File: Rocket.java
 *
 * Date Created: 1/2/2021
 * Last Modified: 1/5/2021
 */

public class Rocket
{
    /** The socket from which to send data */
    private DatagramSocket socket;
    /** The address to which to send the packet */
    private InetAddress address;
    /** The port to which the packet is destined */
    private int port;
    /** The packet containing the telemetry */
    private DatagramPacket packet;

    /**
     * Creates an instance of Rocket.
     * @param hostName The name of the server host
     * @param port The port to which the station is bound
     */
    public Rocket(String hostName, int port)
    {
        try{
            byte[] tempData = new byte[Server.PACKET_SIZE];

            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(hostName);
            this.port = port;
            this.packet = new DatagramPacket(tempData, Server.PACKET_SIZE , address , port);
            
        } catch (SocketException se) {
            System.out.println("SocketException thrown by the rocket: " + se);
        } catch (UnknownHostException uhe) {
            System.out.println("UnknownHostException thrown by the rocket: " + uhe);
        }
    }

    /**
     * Sends the current packet out to the server.
     */
    public void broadcast()
    {
        try{
            socket.send(packet);
        } catch (IOException ioe) {
            System.out.println("IOException thrown from rocket while broadcasting: " + ioe);
        }
    }

    /**
     * Prepares the packet from the telemetry given as a parameter.
     * @param simData The telemetry information from the simulation.
     */
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
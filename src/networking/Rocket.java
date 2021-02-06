package networking;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.DatagramPacket;

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
 * Last Modified: 1/6/2021
 */

public class Rocket
{
    /** The socket from which to send data */
    private DatagramSocket socket;
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

            InetAddress address = InetAddress.getByName(hostName);

            if (hostName.equals("localhost"))
                this.socket = new DatagramSocket();
            else
                this.socket = new DatagramSocket(port);

            this.packet = new DatagramPacket(tempData, Server.PACKET_SIZE , address, port);
            
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
    public void updatePacket(byte[] simData)
    {
        packet.setData(simData);
    }
}
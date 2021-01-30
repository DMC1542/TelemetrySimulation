package networking;

import sim.Simulation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

/**
 * This class represents the Ground Station itself.
 * Takes UDP packets, decodes them, then prints that information to the console.
 *
 * @author Domenic Cacace
 *
 * Language: Java 15
 * File: Server.java
 *
 * Date Created: 1/2/2021
 * Last Modified: 1/6/2021
 */

public class Server
{
    /** The socket which will receive traffic */
    private DatagramSocket socket;
    /** The telemetry data from the simulation */
    private short[] sTelemetry = new short[Simulation.NUM_MEASUREMENTS];
    /** The telemetry data from the simulation */
    private float[] fTelemetry = new float[3];
    /** Toggles whether to print the telemetry after receiving a packet */
    private boolean displayTelemetry = true;
    /** A clean byte[] to construct our receiving packet */
    private byte[] data = new byte[PACKET_SIZE];
    /** The packet in which incoming traffic will be stored */
    private DatagramPacket packet = new DatagramPacket(data, PACKET_SIZE);

    /** The size, in bytes, of each packet to be received */
    public static final int PACKET_SIZE = Simulation.NUM_MEASUREMENTS * 4;

    /**
     * Creates an instance of the server using the port provided.
     * @param port : The port on which to host the server.
     */
    public Server(int port)
    {
        try {
            socket = new DatagramSocket(port);
        }catch (IOException ioe) {
            System.out.println("Server-side IOException thrown during socket initialization: " + ioe);
        }
    }

    /**
     * The main loop which the server will run. Accepts a packet, parses the information,
     * then updates telemetry for printing.
     */
    public void catchAndParse()
    {
        // Wait for packet.
        try {
            socket.receive(packet);
        } catch (IOException ioe) {
            System.out.println("Server threw IOException while waiting for packet: " + ioe);
        }

        // Packet received. Let's display the information.
        data = packet.getData();

        // Parse info.
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int telemetryIndex = 0;

        for (int i = 8; i < 25; i += 2)
        {
            sTelemetry[telemetryIndex] = buffer.getShort(i);
            telemetryIndex++;
        }

        buffer.getFloat(26);
        buffer.getFloat(31);
        buffer.getFloat(36);

        // buffer.get(boolToByte(gpsFix));

        //GPS_NUM_SATS 1
        //GPS_TIME_H 1
        //GPS_TIME_M 1
        //GPS_TIME_S 1
        //GPS_TIME_MS 2 # milliseconds is 2 bytes

        sTelemetry[telemetryIndex++] = buffer.getShort(41); //TODO temp

        /*
        buffer.put(boolToByte(chargeCont1));
        buffer.put(boolToByte(chargeCont2));
        buffer.put(boolToByte(chargeCont3));
        buffer.put(boolToByte(chargeCont4));

        buffer.put(boolToByte(chargeDeploy1));
        buffer.put(boolToByte(chargeDeploy2));
        buffer.put(boolToByte(chargeDeploy3));
        buffer.put(boolToByte(chargeDeploy4));
        */

        //buffer.putShort(temperature);

        // Display telemetry if appropriate
        if (displayTelemetry)
            printTelemetry();
    }

    /**
     * Prints all the telemetry information from the simulation.
     */
    public void printTelemetry()
    {
        System.out.println("\n----------PACKET RECEIVED----------");
        System.out.println("Uptime: " + telemetry[6]);
        System.out.println("Horizontal Velocity: " + telemetry[0]);
        System.out.println("Vertical Velocity: " + telemetry[1]);
        System.out.println("Acceleration X: " + telemetry[2]);
        System.out.println("Acceleration Y: " + telemetry[3]);
        System.out.println("Acceleration Z: " + telemetry[3]);
        System.out.println("Acceleration High G: " + telemetry[3] + "\n");
        System.out.println("Roll: " + telemetry[4]);
        System.out.println("Pitch: " + telemetry[4]);
        System.out.println("Yaw: " + telemetry[4]);
        System.out.println("Altitude 1: " + telemetry[4]);
        System.out.println("Altitude 2: " + telemetry[4]);
        System.out.println("Temperature: " + telemetry[5] + " F");
        System.out.println("----------PACKET END----------\n");
    }

    /**
     * Closes the server by closing the socket, thus killing the thread.
     */
    public void close()
    {
        socket.close();
    }
}

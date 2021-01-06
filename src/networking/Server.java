package networking;

import sim.Simulation;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
 * Last Modified: 1/5/2021
 */

public class Server extends Thread
{
    /** The socket which will receive traffic */
    private DatagramSocket socket;
    /** The telemetry data from the simulation */
    private float[] telemetry = new float[Simulation.NUM_MEASUREMENTS];
    /** Toggles whether to print the telemetry after receiving a packet */
    private boolean displayTelemetry = true;

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
            System.out.println("IOException thrown: " + ioe);
        }
    }

    /**
     * The main loop which the server will run. Accepts a packet, parses the information,
     * then updates telemetry for printing.
     */
    @Override
    public void run()
    {
        while (!socket.isClosed())
        {
            byte[] data = new byte[PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(data, PACKET_SIZE);

            // Wait for packet.
            try {
                socket.receive(packet);
            } catch (IOException ioe) {
                System.out.println("Server threw IOException while waiting for packet: " + ioe);
            }

            // Packet received. Let's display the information.
            data = packet.getData();

            // Convert the byte information into int bits, then into float values.
            int telemetryIndex = 0;
            for (int i = 0; i < PACKET_SIZE; i += 4)
            {
                int temp = 0;
                temp += (((int) data[i]) & 0xFF) << 24;
                temp += (((int) data[i + 1]) & 0xFF) << 16;
                temp += (((int) data[i + 2]) & 0xFF) << 8;
                temp += (((int) data[i + 3]) & 0xFF);

                telemetry[telemetryIndex] = Float.intBitsToFloat(temp);
                telemetryIndex++;
            }

            // Display telemetry if appropriate
            if (displayTelemetry)
                printTelemetry();
        }
    }

    /**
     * Prints all the telemetry information from the simulation.
     */
    public void printTelemetry()
    {
        System.out.println("Horizontal Velocity: " + telemetry[0]);
        System.out.println("Vertical Velocity: " + telemetry[1]);
        System.out.println("Horizontal Acceleration: " + telemetry[2]);
        System.out.println("Vertical Acceleration: " + telemetry[3]);
        System.out.println("Altitude: " + telemetry[4]);
        System.out.println("Temperature: " + telemetry[5] + " F");
    }

    /**
     * Closes the server by closing the socket, thus killing the thread.
     */
    public void close()
    {
        socket.close();
    }
}

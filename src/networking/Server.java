package networking;

import sim.Simulation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread
{
    private DatagramSocket socket;
    private float[] telemetry = new float[Simulation.NUM_MEASUREMENTS];
    private boolean displayTelemetry = true;

    public static final int PACKET_SIZE = Simulation.NUM_MEASUREMENTS * 4;

    public Server(int port)
    {
        try {
            socket = new DatagramSocket(port);
        }catch (IOException ioe) {
            System.out.println("IOException thrown: " + ioe);
        }
    }

    @Override
    public void run()
    {
        while (!socket.isClosed())
        {
            byte[] data = new byte[PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(data, PACKET_SIZE);

            try {
                socket.receive(packet);
            } catch (IOException ioe) {
                System.out.println("Server threw IOException while waiting for packet: " + ioe);
            }

            data = packet.getData();

            // Packet received. Let's display the information.
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

    public void printTelemetry()
    {
        System.out.println("Horizontal Velocity: " + telemetry[0]);
        System.out.println("Vertical Velocity: " + telemetry[1]);
        System.out.println("Horizontal Acceleration: " + telemetry[2]);
        System.out.println("Vertical Acceleration: " + telemetry[3]);
        System.out.println("Altitude: " + telemetry[4]);
        System.out.println("Temperature: " + telemetry[5]);
    }
}

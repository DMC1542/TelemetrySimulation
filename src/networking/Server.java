package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread
{
    private DatagramSocket socket;
    public static final int PACKET_SIZE = 100;

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
            int temp = 0;
            temp += (((int) data[0]) & 0xFF) << 24;
            temp += (((int) data[1]) & 0xFF) << 16;
            temp += (((int) data[2]) & 0xFF) << 8;
            temp += (((int) data[3]) & 0xFF);

            System.out.println(Float.intBitsToFloat(temp));
        }
    }
}

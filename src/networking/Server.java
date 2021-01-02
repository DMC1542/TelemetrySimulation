package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Server extends Thread
{
    private DatagramSocket socket;

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
            byte[] data = new byte[100];
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                socket.receive(packet);
            } catch (IOException ioe) {
                System.out.println("Server threw IOException while waiting for packet: " + ioe);
            }

            float f = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            System.out.println(f);
        }
    }
}

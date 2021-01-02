package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server
{
    private DatagramSocket socket;

    public Server(int port)
    {
        try {
            socket = new DatagramSocket(port);
            run();
        }catch (IOException ioe) {
            System.out.println("IOException thrown: " + ioe);
        }
    }

    private void run()
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

            System.out.println(data);
        }
    }
}

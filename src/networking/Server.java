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
        }catch (IOException ioe) {
            System.out.println("IOException thrown: " + ioe);
        }
    }
}

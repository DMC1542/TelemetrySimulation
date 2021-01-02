import networking.Rocket;
import networking.Server;
import sim.Simulation;

/**
 * The entry point for the program. Runs the simulation by sending telemetry packets.
 *
 * @author Domenic Cacace
 *
 * Language: Java 15
 * File: Start.java
 *
 * Date Created: 1/2/2021
 * Last Modified: 1/2/2021
 */

public class Start
{
    public static void main(String args[])
    {
        Server server = new Server(8080);
        server.start();

        Rocket rocket = new Rocket("localhost", 8080);

        Simulation sim = new Simulation();

        rocket.preparePacket(sim);
        rocket.broadcast();
    }
}

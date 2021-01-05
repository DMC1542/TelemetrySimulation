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
        Simulation sim = new Simulation();
        Rocket rocket = new Rocket(args[0], Integer.parseInt(args[1]));

        Server server = new Server(8080);
        server.start();

        // For debugging, can be removed later.
        rocket.preparePacket(sim.getTelemetry());
        rocket.broadcast();

        /* Main loop
        while (true)
        {
            // sim.update(); or sim.nextStep();

            rocket.preparePacket(sim.getTelemetry());
            rocket.broadcast();
        }
         */
    }
}

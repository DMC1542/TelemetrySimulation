import networking.Rocket;
import networking.Server;
import sim.Simulation;

/**
 * The entry point for the program. Begins the simulation, by:
 *     1) Launching server, creating rocket model, creating simulation for telemetry modeling
 *     2) Entering the main loop:
 *         a) Updating the telemetry simulation
 *         b) Sending the telemetry to the rocket model
 *         c) Sending the packet to the server (ground station)
 *
 * @author Domenic Cacace
 *
 * Language: Java 15
 * File: Start.java
 *
 * Date Created: 1/2/2021
 * Last Modified: 1/5/2021
 */

public class Start
{
    /** Determines whether the simulation continues. */
    private boolean isActive = true;

    /**
     * The main method. Serves as the entry point.
     *
     * Usage: java Start hostName port
     *
     * @param args The user-provided arguments containing host name and port.
     */
    public static void main(String args[])
    {
        Simulation sim = new Simulation();
        Rocket rocket = new Rocket(args[0], Integer.parseInt(args[1]));

        Server server = new Server(Integer.parseInt(args[1]));
        server.start();

        // For debugging, can be removed later.
        rocket.preparePacket(sim.getTelemetry());
        rocket.broadcast();

        /* Main loop
        while (isActive)
        {
            // sim.update(); or sim.nextStep();

            rocket.preparePacket(sim.getTelemetry());
            rocket.broadcast();
        }

        server.close();
        */
    }
}

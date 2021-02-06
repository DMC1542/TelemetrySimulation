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
 * Last Modified: 1/6/2021
 */

public class Start
{
    /** Flag for running the sim in real time */
    private static boolean inRealTime = false;
    /** Flag for running in debug mode */
    private static boolean debugMode = false;
    /** The receiving server */
    private static Server server;

    /**
     * The main method. Serves as the entry point.
     *
     * Usage: java Start hostName port [-orm exportName] [-rt] [-d]
     *
     * @param args The user-provided arguments containing host name and port, optionally:
     *             -orm : Open Rocket Model
     *             -rt : Real Time sim
     *             -d : Debug mode, which runs the receiving server.
     */
    public static void main(String[] args)
    {
        // Start rocket and the simulation
        Rocket rocket = new Rocket(args[0], Integer.parseInt(args[1]));
        Simulation sim = new Simulation();

        // If flags, toggle their status
        if (args.length > 2)
        {
            for (int i = 2; i < args.length; i++)
            {
                if (args[i].equals("-orm"))
                {
                    sim.enableOrmMode(true, args[i + 1]);
                    i++;
                }
                else if (args[i].equals("-rt"))
                {
                    inRealTime = true;
                }
                else if (args[i].equals("-d"))
                {
                    debugMode = true;
                }
            }
        }

        // If in debug mode, start the server
        if (debugMode)
            server = new Server(Integer.parseInt(args[1]));

        // Main loop
        float[] lastTelemetryReadout = sim.getTelemetry();

        while (true)
        {
            // Update that simulation.
            sim.update();
            float[] currentTelemetry = sim.getTelemetry();

            /* Check to see if we reached the end of the simulation. The times should be
            exactly the same. If they are, break out of the loop.*/
            if (lastTelemetryReadout[6] == currentTelemetry[6])
            {
                if (debugMode)
                    server.close();
                break;
            }
            else
            {
                if (inRealTime)
                {
                    try {
                        Thread.sleep((long)((currentTelemetry[6] - lastTelemetryReadout[6]) * 1000.0));
                    } catch (InterruptedException ie) {
                        System.out.println("InterruptedException thrown while emulating packet delay: " + ie);
                    }
                }

                rocket.preparePacket(currentTelemetry);
                rocket.broadcast();

                if (debugMode)
                    server.catchAndParse();

                // Reset lastTelemetryReadout
                lastTelemetryReadout = currentTelemetry;
            }
        }
    }
}

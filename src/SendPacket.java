import networking.Rocket;
import sim.Simulation;

public class SendPacket
{

    /**
     * Usage: hostName port
     * @param args
     */
    static void main(String args[])
    {
        Rocket rocket = new Rocket(args[0], Integer.parseInt(args[1]));
        Simulation sim = new Simulation();

        rocket.preparePacket(sim.getTelemetry());
        rocket.broadcast();
    }
}

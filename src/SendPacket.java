import networking.Rocket;
import sim.Simulation;

public class SendPacket
{

    /**
     * Usage: hostName port
     * @param args
     */
    public static void main(String args[])
    {
        Rocket rocket = new Rocket(args[0], Integer.parseInt(args[1]));
        Simulation sim = new Simulation();

        float[] data = sim.getTelemetry();

        rocket.preparePacket(sim.getTelemetry());
        rocket.broadcast();

        System.out.println("VelocityX: " + data[0]);
        System.out.println("VelocityY: " + data[1]);
        System.out.println("AccelerationX: " + data[2]);
        System.out.println("AccelerationY: " + data[3]);
        System.out.println("Altitude: " + data[4]);
        System.out.println("Temperature: " + data[5]);
        System.out.println("Time: " + data[6]);
    }
}

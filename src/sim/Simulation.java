package sim;

public class Simulation
{
    public static final int NUM_MEASUREMENTS = 6;
    private float accelerationX, accelerationY, velocityX, velocityY, altitude, temperature;

    public Simulation()
    {
        // Initial temp values - for testing.
        this.accelerationX = 10;
        this.accelerationY = 11;
        this.velocityX = 15;
        this.velocityY = 20;
        this.altitude = 1000;
        this.temperature = 60; // in Fahrenheit
    }

    public float[] getTelemetry()
    {
        float[] data = new float[NUM_MEASUREMENTS];

        // Predetermined order for packet building.
        // Please see documentation for specific ordering.
        data[0] = velocityX;
        data[1] = velocityY;
        data[2] = accelerationX;
        data[3] = accelerationY;
        data[4] = altitude;
        data[5] = temperature;

        return data;
    }
}

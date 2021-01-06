package sim;

/**
 * The simulation which will generate telemetry for the rocket.
 *
 * @author Domenic Cacace
 *
 * Language: Java 15
 * File: Simulation.java
 *
 * Date Created: 1/2/2021
 * Last Modified: 1/5/2021
 */

public class Simulation
{
    /** The number of measurements or telemetry values to be sent */
    public static final int NUM_MEASUREMENTS = 6;
    /** Telemetry values. */
    private float accelerationX, accelerationY, velocityX, velocityY, altitude, temperature;

    /**
     * Generates an instance of the Simulation class.
     * Gives initial values for all measurements
     */
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

    /**
     * Returns the current telemetry values as an array of floats
     * @return A float array of all current numeric measurements
     */
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

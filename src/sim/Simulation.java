package sim;

public class Simulation
{
    public float accelerationX, accelerationY, velocityX, velocityY, altitude;
    public int temperature;

    public Simulation()
    {
        // Initial temp values.
        this.accelerationX = 0;
        this.accelerationY = 0;
        this.velocityX = 0;
        this.velocityY = 0;
        this.altitude = 0;
        this.temperature = 60; // in Fahrenheit
    }
}

package sim;

import java.io.*;

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
    public static final int NUM_MEASUREMENTS = 7;
    /** Telemetry values */
    private float accelerationX, accelerationY, velocityX, velocityY, altitude, temperature, time;
    /** Telemetry values */
    private float latitude, longitude;
    /** Records if open rocket model flag is toggled or not */
    private boolean ormIsEnabled = false;
    /** The file name for the ORM export if applicable. Otherwise, empty string. */
    private String fileName = "";
    /** The reader to read in the Open rocket model export */
    private BufferedReader in;

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
        this.time = -1;
    }

    public void update()
    {
        // Follow the ORM if enabled
        if (ormIsEnabled)
        {
            /*
                Please note that only 5 of the 6 currently implemented measurements are
                in the ORM model. Temperature (in my quick glance of the export list) is not
                a variable that is exported by OpenRocket.
                Please see the open rocket export file for more info on variable ordering.
             */

            String line = null;
            line = readNextLine();

            // Check for EOF
            if (line != null)
            {
                // Make sure the line isn't a comment.
                while (line != null && line.substring(0, 1).equals("#"))
                {
                    line = readNextLine();
                }

                // The previous code does NOT account for comments in the csv at EOF
                // Check for this quickly.
                if (line != null)
                {
                    String[] data = line.split(",");

                    time = Float.parseFloat(data[0]);
                    altitude = Float.parseFloat(data[1]);
                    velocityY = Float.parseFloat(data[2]);
                    accelerationY = Float.parseFloat(data[3]);
                    velocityX = Float.parseFloat(data[4]);
                    accelerationX = Float.parseFloat(data[5]);
                }
            }
        }
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
        data[6] = time;

        return data;
    }

    public void enableOrmMode(boolean val, String fileName)
    {
        this.ormIsEnabled = val;
        this.fileName = fileName;

        try{
            in = new BufferedReader( new FileReader(fileName) );
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException thrown in Simulation while loading the " +
                    "open rocket export: " + fnfe);
        }
    }

    public String readNextLine()
    {
        String line = null;

        try {
            line = in.readLine();
        } catch (IOException ioe) {
            System.out.println("IOException thrown while reading line from reader: " + ioe);
        }

        return line;
    }
}

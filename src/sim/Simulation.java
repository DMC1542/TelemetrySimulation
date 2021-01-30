package sim;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * The simulation which will generate telemetry for the rocket.
 *
 * @author Domenic Cacace
 *
 * Language: Java 15
 * File: Simulation.java
 *
 * Date Created: 1/2/2021
 * Last Modified: 1/6/2021
 */

public class Simulation
{
    /** The number of measurements or telemetry values to be sent */
    public static final int NUM_MEASUREMENTS = 7;
    /** Telemetry values */
    private short accelerationX, accelerationY, accelerationZ, accelHighG,
            roll, pitch, yaw,
            velocityX, velocityY, altitude1, altitude2, temperature, time,
            latitude, longitude,
            uptime;
    /** Telemetry values */
    private float gpsLat, gpsLong, gpsAlt;
    /** Telemetry values */
    private boolean gpsFix, chargeCont1, chargeCont2, chargeCont3, chargeCont4,
            chargeDeploy1, chargeDeploy2, chargeDeploy3, chargeDeploy4;
    /** Records if open rocket model flag is toggled or not */
    private boolean ormIsEnabled = false;
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
        this.altitude1 = 1000;
        this.altitude2 = 1000;
        this.temperature = 60; // in Fahrenheit
        this.time = -1;
    }

    /**
     * Advances the simulation by one 'tick'. Updates the telemetry values accordingly.
     */
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

            String line;
            line = readNextLine();

            // Check for EOF
            if (line != null)
            {
                // Make sure the line isn't a comment.
                while (line != null && line.charAt(0) == '#')
                {
                    line = readNextLine();
                }

                // The previous code does NOT account for comments in the csv at EOF
                // Check for this quickly.
                if (line != null)
                {
                    String[] data = line.split(",");

                    time = Short.parseShort(data[0]);
                    altitude1 = Short.parseShort(data[1]);
                    velocityY = Short.parseShort(data[2]);
                    accelerationY = Short.parseShort(data[3]);
                    velocityX = Short.parseShort(data[4]);
                    accelerationX = Short.parseShort(data[5]);
                }
            }
        }
    }

    /**
     * Returns the current telemetry values, in order, as a byte array
     * @return A byte array of all current numeric measurements
     */
    public byte[] getTelemetryAsBArray()
    {
        // Predetermined order for packet building.
        // Please see documentation for specific ordering.

        ByteBuffer buffer = ByteBuffer.allocate(59);
        buffer.putShort(accelerationX);
        buffer.putShort(accelerationY);
        buffer.putShort(accelerationZ);
        buffer.putShort(accelHighG);
        buffer.putShort(pitch);
        buffer.putShort(roll);
        buffer.putShort(yaw);
        buffer.putShort(altitude1);
        buffer.putShort(altitude2);

        buffer.putFloat(gpsLat);
        buffer.putFloat(gpsLong);
        buffer.putFloat(gpsAlt);

        buffer.put(boolToByte(gpsFix));

        //GPS_NUM_SATS 1
        //GPS_TIME_H 1
        //GPS_TIME_M 1
        //GPS_TIME_S 1
        //GPS_TIME_MS 2 # milliseconds is 2 bytes

        buffer.putShort(uptime);

        buffer.put(boolToByte(chargeCont1));
        buffer.put(boolToByte(chargeCont2));
        buffer.put(boolToByte(chargeCont3));
        buffer.put(boolToByte(chargeCont4));

        buffer.put(boolToByte(chargeDeploy1));
        buffer.put(boolToByte(chargeDeploy2));
        buffer.put(boolToByte(chargeDeploy3));
        buffer.put(boolToByte(chargeDeploy4));

        buffer.putShort(temperature);

        return buffer.array();
    }

    /**
     * Sets the ORM flag to be true and sets up the necessary reader to read Open rocket export.
     * @param val A boolean which determines whether ORM mode is enabled or disabled.
     * @param fileName The name of the Open Rocket csv to load.
     */
    public void enableOrmMode(boolean val, String fileName)
    {
        this.ormIsEnabled = val;

        if (ormIsEnabled)
        {
            try{
                in = new BufferedReader( new FileReader(fileName) );
            } catch (FileNotFoundException fnfe) {
                System.out.println("FileNotFoundException thrown in Simulation while loading the " +
                        "open rocket export: " + fnfe);
            }
        }
    }

    /**
     * Simply reads in the next line from the reader and returns it.
     * @return The next line of the file which is opened by the reader.
     */
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

    private byte boolToByte(boolean bool)
    {
        if (bool)
            return (byte)1;
        else
            return (byte)0;
    }
}

package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Parameter;

/**
 * This will handle reading in and parsing the config files
 * for various rockets. It will pass the order for measurements
 * in packets to the rocket
 *
 * @author Domenic Cacace
 *
 * Language: Java 15
 * File: ConfigParser.java
 *
 * Date Created: 1/31/2021
 * Last Modified: 1/31/2021
 */

public class ConfigParser
{
    private BufferedReader in;

    public ConfigParser(String configFile)
    {
        String line = null;

        try{
            in = new BufferedReader( new FileReader(configFile));
            line = in.readLine();
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException thrown trying to load config file: " + fnfe);
        } catch (IOException ioe) {
            System.out.println("IOException thrown while reading in line from config file: " + ioe);
        }

        try {
            line = in.readLine();
        } catch (IOException ioe) {
            System.out.println("IOException thrown while reading line from reader: " + ioe);
        }

        // Check for EOF
        // TODO make this a while loop
        while(line != null)
        {
            // If it is NOT a comment or blank lines
            if (!line.substring(0, 1).equals("#") || !line.equals(""))
            {

            }
        }
    }
}

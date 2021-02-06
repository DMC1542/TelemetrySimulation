package sim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class ConfigLoader
{
    private ArrayList<String> measurementNames = new ArrayList<>();
    private ArrayList<Object> measurementValues = new ArrayList<>();
    private int timeIndex;

    // Networking specific variables
    private String protocol;
    private InetAddress addr;
    private int port;

    // Rocket vars
    private String deviceName;

    public void loadConfigFile(String fileName)
    {
        BufferedReader in = null;
        String line = null;

        try{
            in = new BufferedReader(new FileReader(fileName));
            line = in.readLine();

            while (line != null)
            {
                // if the line is not empty and isn't a comment,
                if (line.length() != 0 && !line.substring(0, 1).equals("#"))
                {
                    // Determine if the line contains measurement or networking data
                    // networking data has = in the assignment, measurements do not

                    // For networking data
                    if (line.indexOf(" = ") != -1)
                    {
                        String var = line.substring(0, line.indexOf(" = "));
                        String value = line.substring(line.indexOf(" = ") + 3);

                        if (var.equals("protocol"))
                            protocol = value;
                        else if (var.equals("addr"))
                            addr = InetAddress.getByName(value);
                        else if (var.equals("port"))
                            port = Integer.parseInt(value);
                    }

                    // Parse the line
                    // TODO
                }

                // Read the next line
                line = in.readLine();
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException thrown while creating readers for config loading: " + fnfe);
        } catch (IOException ioe) {
            System.out.println("IOException thrown while trying to read lines from config file: " + ioe);
        }
    }


}

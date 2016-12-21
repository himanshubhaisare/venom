package service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by hbhaisare on 17/11/2016.
 */
public class Utils {

    /**
     * Concatenate string array into a string
     *
     * @param args
     * @return
     */
    public static String concatenate(String[] args) {
        String[] tokens = Arrays.copyOfRange(args, 0, args.length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            sb.append(tokens[i]);
            if (i != tokens.length - 1) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }

    /**
     * Handle creation of output file
     *
     * @param args
     * @return
     * @throws IOException
     */
    public static File getOutputFile(String[] args) throws IOException {
        File output;
        if (!args[1].isEmpty()) {
            output = new File(args[1]);
        } else {
            output = new File("output.txt");
        }

        if (output.exists()) {
            if (!output.delete()) {
                System.out.println(output.getName()+" was not deleted");
            }
        }
        if (!output.createNewFile()) {
            System.out.println(output.getName()+" was not created");
        }

        return output;
    }
}

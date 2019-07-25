package similarwordsgenerator;

import java.io.File;

public class Directory {
    private File userHomeProgram;
    private static Directory ourInstance = new Directory();

    public static Directory getInstance() {
        return ourInstance;
    }

    private Directory() {

        String path = System.getProperty("user.home") + File.separator + ".similarwordsgenerator";

        new File(path).mkdir();

        userHomeProgram = new File(path);
    }

    public File getUserHomeProgram() {
        return userHomeProgram;
    }
}

package similarwordsgenerator;

import java.io.File;
import java.net.URISyntaxException;

public class Directory {
    private File dirFile;
    private static Directory ourInstance = new Directory();

    public static Directory getInstance() {
        return ourInstance;
    }

    private Directory() {
        try {
            dirFile = new File(AppMain.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public File getDirFile() {
        return dirFile;
    }
}

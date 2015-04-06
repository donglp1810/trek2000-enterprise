package utils;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by trek2000 on 27/10/2014.
 */
public class ImageFileFilter implements FileFilter {
    private final String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};
    private File file;

    /**
     *
     */
    public ImageFileFilter(File mFileNew) {
        this.file = mFileNew;
    }

    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}

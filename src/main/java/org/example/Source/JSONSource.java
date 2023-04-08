package org.example.Source;

import java.io.File;

public class JSONSource {
    private String url;
    boolean isLocal;
    File file;
    public JSONSource(String url, boolean isLocal) {
        this.url = url;
        this.isLocal = isLocal;
        fetchFile();
    }

    private void fetchFile() {
        if (isLocal) {
            File file = new File(url);
            if (file == null)   throw new RuntimeException("File not found at " + url);
            this.file = file;
        }
        else {
            /*
                TODO: Fetch file from url given, download it and return when asked for.
             */
            throw new RuntimeException("Download of file currently not supported. Please download the file locally and provide path to the file");
        }
    }

    public File getFile() {
        if (file == null)   fetchFile();
        return file;
    }
}

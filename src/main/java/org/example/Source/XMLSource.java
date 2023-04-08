package org.example.Source;

import java.io.File;

public class XMLSource {

    private final String url;
    boolean isLocal;
    File file;
    public XMLSource(String url, boolean isLocal) {
        this.url = url;
        this.isLocal = isLocal;
        fetchFile();
    }

    private void fetchFile() {
        if (isLocal) {
            File file = new File(url);
            if (file.exists() && !file.isDirectory())   this.file = file;
            else throw new RuntimeException("File not found at " + url);

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

    public static void main(String[] args) {
        XMLSource o = new XMLSource("/home/albus/temp.xml", true);
    }
}

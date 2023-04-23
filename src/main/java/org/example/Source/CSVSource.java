package org.example.Source;

import java.io.File;

public class CSVSource {
    private String url;
    private boolean isLocal;
    private File file;
    public CSVSource(String url, boolean isLocal) {
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
            throw new RuntimeException("Online files currently not supported. Please download the file locally and provide path to the file");
        }
    }

    public File getFile() {
        if (file == null)   fetchFile();
        return file;
    }
}
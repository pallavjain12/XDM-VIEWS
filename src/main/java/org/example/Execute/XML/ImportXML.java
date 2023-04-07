package org.example.Execute.XML;

import java.io.InputStream;

public class ImportXML {
    String pathToXML;
    boolean isLocal;
    ImportXML() {
        pathToXML = null;
        isLocal = true;
    }

    public void loadData() throws NoPathDefinedForXML {
        if (pathToXML == null) throw new NoPathDefinedForXML();

    }

}

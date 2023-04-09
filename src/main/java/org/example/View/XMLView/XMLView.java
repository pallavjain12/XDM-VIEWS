package org.example.View.XMLView;

import org.example.Common.Condition;
import org.example.Source.XMLSource;
import org.example.View.View;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLView extends View {
    private XMLSource source;
    private ArrayList<String> select;
    private ArrayList<Condition> conditions;
    public XMLView() {
        this.select = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.source = null;
    }

    public void addSource(XMLSource source) { this.source = source; }

    public void loadData() throws ParserConfigurationException, IOException, SAXException {
        if (source == null) throw new RuntimeException("No source defined for XMLView");
        File file = source.getFile();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        NodeList nodeList = doc.getElementsByTagName("student");
    }
}

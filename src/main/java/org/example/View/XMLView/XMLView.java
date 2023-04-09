package org.example.View.XMLView;

import com.mysql.cj.jdbc.SuspendableXAConnection;
import org.example.Common.Condition;
import org.example.Source.XMLSource;
import org.example.View.View;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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

    public void addSelect(String selectString) { this.select.add(selectString); }

    public void addCondition(Condition condition) { this.conditions.add(condition); }

    public void addSource(XMLSource source) { this.source = source; }

    public void loadData() throws ParserConfigurationException, IOException, SAXException {
        if (source == null) throw new RuntimeException("No source defined for XMLView");
        File file = source.getFile();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        iterateRecursively(doc.getDocumentElement().getFirstChild());
    }

    public static void iterateRecursively(Node node) {
        int i  = 0;
        while(i <= 5) {
            NodeList list = node.getChildNodes();
            for (int j = 0; j < list.getLength(); j++) {
                Node currentNode = list.item(j);
                System.out.println(currentNode.getNodeName() + " " + currentNode.getChildNodes().getLength() + " " + currentNode.getTextContent());
            }
            i += 1;
            node = node.getNextSibling();
        }
        /*
            1: Element Node
            2: Attribute node
            3: Text Node
            4: CDATA_SECTION_NODE
         */

//        NodeList nodeList = node.getChildNodes();
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            Node currentNode = nodeList.item(i);
//            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
//                childRecursion(currentNode);
//            }
//        }
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        XMLView o = new XMLView();
        XMLSource source = new XMLSource("/home/albus/DoNotTouchThis/XDM-VIEWS/src/main/resources/students.xml", true);
        o.addSource(source);
        o.loadData();
    }
}

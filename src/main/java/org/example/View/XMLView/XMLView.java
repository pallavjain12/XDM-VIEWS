package org.example.View.XMLView;

import com.mysql.cj.jdbc.SuspendableXAConnection;
import org.example.Common.Condition;
import org.example.Source.XMLSource;
import org.example.View.View;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;

import static org.example.Common.Condition.filterBasedOnCondition;
import static org.example.Common.Condition.selectSelectedAttributes;

public class XMLView {
    private XMLSource source;
    private ArrayList<String> select;
    private String condition;

    private JSONArray dataRows;
    public XMLView() {
        dataRows = new JSONArray();
        this.select = new ArrayList<>();
        this.condition = null;
        this.source = null;
    }

    public void addSelect(String selectString) { this.select.add(selectString); }

    public void addCondition(String condition) { this.condition = condition; }

    public void addSource(XMLSource source) { this.source = source; }

    public void loadData() {
        try {
            if (source == null) throw new RuntimeException("No source defined for XMLView");
            File file = source.getFile();
            String line = "", str = "";
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                str+=line;
            }
            JSONObject xmlObject = XML.toJSONObject(str);
            JSONObject obj = xmlObject.getJSONObject(new ArrayList<>(xmlObject.keySet()).get(0));
            this.dataRows = selectSelectedAttributes(filterBasedOnCondition(obj.getJSONArray(new ArrayList<>(obj.keySet()).get(0)), this.condition), this.select);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred while loading xml data : " + e.getMessage());
        }

    }
    public JSONArray getJSONArray() {
        return dataRows;
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        XMLView o = new XMLView();
        XMLSource source = new XMLSource("/home/albus/DoNotTouchThis/XDM-VIEWS/src/main/resources/students.xml", true);
        o.addSource(source);
        o.loadData();
    }
}

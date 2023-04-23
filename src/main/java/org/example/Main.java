package org.example;

/*
    TODO:
        Current Implementation: For every view new connection is made.
        Appropriate Implementation: Create a table data structure for every source which stores connection request.
                                    Every View data structure reside inside that table
                                    For every view reuse the connection and close when finished.


 */

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class Main {
    public static void main(String[] args) {
        display();
    }
    static void display() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse("/home/albus/DoNotTouchThis/XDM-VIEWS/src/main/resources/students.xml");
            XPath xp = XPathFactory.newInstance().newXPath();
            NodeList n1 = (NodeList) xp.compile("/students/student/address").evaluate(d, XPathConstants.NODESET);

            System.out.println("number of nodes = " + n1.getLength());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
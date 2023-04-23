package org.example.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Xml2json {

    static String line="",str="";
    public static void main(String[] args) throws JSONException, IOException {
//        String link = "/home/albus/DoNotTouchThis/XDM-VIEWS/src/main/resources/students.xml";
//        BufferedReader br = new BufferedReader(new FileReader(link));
//        while ((line = br.readLine()) != null) {
//            str+=line;
//        }
//        JSONObject jsondata = XML.toJSONObject(str);
//        System.out.println(jsondata);
        JSONObject obj = new JSONObject();
        obj.put("xml", "asdf");
        System.out.println(obj.isNull("csv"));

    }
}
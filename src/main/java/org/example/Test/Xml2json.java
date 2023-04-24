package org.example.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Xml2json {

    static String line="",str="";
    public static void main(String[] args) throws JSONException, IOException {
        String link = "/home/albus/DoNotTouchThis/XDM-VIEWS/src/main/resources/student_courses.xml";
        BufferedReader br = new BufferedReader(new FileReader(link));
        while ((line = br.readLine()) != null) {
            str+=line;
        }
        JSONObject jsondata = XML.toJSONObject(str);
        JSONObject obj = jsondata.getJSONObject(new ArrayList<>(jsondata.keySet()).get(0));
        JSONArray array = obj.getJSONArray(new ArrayList<>(obj.keySet()).get(0));
        System.out.println(array);
    }
}
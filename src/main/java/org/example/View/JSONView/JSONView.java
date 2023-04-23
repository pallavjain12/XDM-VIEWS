package org.example.View.JSONView;

import org.example.Source.JSONSource;
import org.example.View.View;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSONView {
    JSONSource source;
    ArrayList<String> select;

    String conditions;
    JSONArray dataRows;

    public JSONView() {
        source = null;
        select = null;
        conditions = null;
        JSONObject dataRows;
    }

    public void addSource(JSONSource source) {
        this.source = source;
    }

    public void addConditions (String conditions) {
        this.conditions = conditions;
    }

    public void addSelect(String select) {
        this.select.add(select);
    }

    public void loadData() throws IOException  {
        if (source == null) throw new RuntimeException("No source defined for JSON source");
        StringBuilder sbr = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(source.getFile()));
        String temp = br.readLine();
        while(temp != null) {
            sbr.append(temp);
            temp = br.readLine();
        }
        dataRows = new JSONArray(sbr.toString());
    }
    public JSONArray getJSONArray() {
        return dataRows;
    }
}

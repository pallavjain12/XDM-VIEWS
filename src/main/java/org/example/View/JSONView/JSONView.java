package org.example.View.JSONView;

import org.example.Source.JSONSource;
import org.example.View.View;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.Common.Condition.filterBasedOnCondition;
import static org.example.Common.Condition.selectSelectedAttributes;

public class JSONView {
    JSONSource source;
    ArrayList<String> select;

    String conditions;
    JSONArray dataRows;

    public JSONView() {
        source = null;
        select = new ArrayList<>();
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

    public void loadData() {
        try {
            if (source == null) throw new RuntimeException("No source defined for JSON view");
            StringBuilder sbr = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(source.getFile()));
            String temp = br.readLine();
            while(temp != null) {
                sbr.append(temp);
                temp = br.readLine();
            }
            dataRows = selectSelectedAttributes(filterBasedOnCondition(new JSONArray(sbr.toString()), this.conditions), this.select);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occureed while reading json data" + e);
        }

    }

    public JSONArray getJSONArray() {
        return dataRows;
    }
}

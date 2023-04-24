package org.example.View.CSVSource;

import org.example.Source.CSVSource;
import org.example.Source.JSONSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CSVView {
    String conditions;
    JSONArray dataRows;
    CSVSource source;
    Set<String> select = new HashSet<>();

    public CSVView() {
        conditions = "";
        dataRows = new JSONArray();
        source = null;
    }

    public void addSource(CSVSource source) {
        this.source = source;
    }

    public void loadData() {
        try {
            if (source == null) throw new RuntimeException("No source defined for CSV database");
            BufferedReader br = new BufferedReader(new FileReader(source.getFile()));
            dataRows = new JSONArray();
            String[] columns = br.readLine().split(",");
            String temp = br.readLine();
            while (temp != null) {
                JSONObject obj = new JSONObject();
                String[] values = temp.split(",");
                for (int i = 0; i < columns.length; i++) {
                    if (select.contains(columns[i]))    continue;
                    obj.put(columns[i], values[i]);
                }
                temp = br.readLine();
                dataRows.put(obj);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Exception occurred while reading csv file " + e);
        }
    }

    public void addSelect(String select) {
        this.select.add(select);
    }

    public void display() {
        System.out.println(dataRows.toString());
    }

    public void addCondition(String conditions) {
        this.conditions = conditions;
    }

    public JSONArray getJSONArray() {
        return dataRows;
    }

    public static void main(String[] args) throws IOException {
        CSVView view = new CSVView();
        CSVSource source = new CSVSource("/home/albus/DoNotTouchThis/XDM-VIEWS/src/main/resources/database/student_courses.csv", true);
        view.addSource(source);
        view.loadData();
        view.display();
    }
}

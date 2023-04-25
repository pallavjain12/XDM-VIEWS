package org.example.View.SQLView;

import org.example.Common.Condition;
import org.example.Source.SQLSource;
import org.example.View.View;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLView {
    private ArrayList<HashMap<String, String>> rows;
    private ArrayList<String> select;
    private String conditions;
    private String table;
    private String query;
    private SQLSource source;

    private JSONArray dataRows;

    public SQLView() {
        this.table = null;
        this.query = null;
        this.select = new ArrayList<>();
        this.conditions = "";
        this.rows = new ArrayList<>();
        this.dataRows = new JSONArray();
    }
    public void addSource(SQLSource source) {
        this.source = source;
    }

    public void addTable(String table) {
        this.table = table;
    }

    /*
        TODO:
         - check if table exits before executing query
     */

    public void loadData() {

        if (source == null) throw new RuntimeException("No source defined for SQLView");
        if (table == null)  throw new RuntimeException("No table defined for SQLView");
        if (!source.hasTable(table))   throw new RuntimeException("Table with name " + table + " not found at " + source.getURL());

        prepareStatement();
        try {
            Connection connection = source.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnNumber = rsmd.getColumnCount();
            while(rs.next()) {
                HashMap<String, String> tempMap = new HashMap<>();
                JSONObject object = new JSONObject();
                for (int i = 1; i <= columnNumber; i++) {
                    object.put(rsmd.getColumnName(i), rs.getString(i));
                    tempMap.put(rsmd.getColumnName(i), rs.getString(i));
                }
                dataRows.put(object);
                rows.add(tempMap);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(query);
            throw new RuntimeException("Error occurred while connecting to SQL server " + e);
        }
    }

    public JSONArray getJSONArray() {
        return dataRows;
    }

    /*
        TODO:
            - Check if column exists. Store column once to
            - How to add different conditions for same/ different fields.
     */
    private void prepareStatement() {
        StringBuilder query = new StringBuilder("select");
        if (this.select.size() != 0) {
            for (int i = 0; i < this.select.size(); i++) {
                if (i != select.size() - 1) {
                    query.append(" ").append(select.get(i)).append(",");
                }
                else {
                    query.append(" ").append(select.get(i));
                }
            }
        }
        else {
            query.append(" *");
        }
        query.append(" from ").append(table);
        if (this.conditions.length() != 0) {
            query.append(" where");
                query.append(" ").append(conditions);
        }
        query.append(";");
        System.out.println(query);
        this.query = query.toString();
    }

    public void addCondition(String condition) { this.conditions = condition; }

    public void addSelectColumn(String column) { this.select.add(column); }

    public static void main(String[] args) throws SQLException {
        SQLView obj = new SQLView();
        SQLSource source = new SQLSource("localhost:3306/marketdb", "root", "password");
        obj.addSource(source);
        obj.addTable("dim_prod");
//        obj.addSelectColumn("product_category");
        obj.loadData();
    }
}


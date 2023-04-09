package org.example.View.SQLView;

import org.example.Common.Condition;
import org.example.Source.SQLSource;
import org.example.View.View;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLView extends View {
    private final ArrayList<String> select;
    private final ArrayList<Condition> conditions;
    private String table;
    private String query;
    private SQLSource source;

    SQLView() {
        this.table = null;
        this.query = null;
        this.select = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.rows = new ArrayList<>();
    }
    public void addSource(SQLSource source) { this.source = source; }

    public void addTable(String table) { this.table = table; }

    /*
        TODO:
         - check if table exits before executing query
     */

    public void loadData() throws SQLException {

        if (source == null) throw new RuntimeException("No source defined for SQLView");
        if (table == null)  throw new RuntimeException("No table defined for SQLView");
        if (!source.hasTable(table))   throw new RuntimeException("Table with name " + table + " not found at " + source.getURL());
        prepareStatement();

        Connection connection = source.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnNumber = rsmd.getColumnCount();
        while(rs.next()) {
            HashMap<String, String> tempMap = new HashMap<>();
            for (int i = 1; i <= columnNumber; i++) {
                tempMap.put(rsmd.getColumnName(i), rs.getString(i));
            }
            rows.add(tempMap);
        }
    }

    /*
        TODO:
            - Check if column exists. Store column once to
            - How to add different conditions for same/ different fields.
     */
    private void prepareStatement() {
        StringBuilder query = new StringBuilder("select");
        if (this.select.size() != 0) {
            for (String s : this.select) {
                query.append(" ").append(s);
            }
        }
        else {
            query.append(" *");
        }
        query.append(" from ").append(table);
        if (this.conditions.size() != 0) {
            query.append(" where");
            for (Condition s : conditions) {
                query.append(" ").append(s.getColumn()).append(" ").append(s.getOperator()).append(" ").append(s.getValue());
            }
        }
        query.append(";");
        this.query = query.toString();
    }

    public void addCondition(Condition condition) { this.conditions.add(condition); }

    public void addSelectColumn(String column) { this.select.add(column); }

    public static void main(String[] args) throws SQLException {
        SQLView obj = new SQLView();
        SQLSource source = new SQLSource("localhost:3306/marketdb", "root", "password");
        obj.addSource(source);
        obj.addTable("dim_prod");
//        obj.addSelectColumn("product_category");
        obj.loadData();
        obj.display();
    }
}


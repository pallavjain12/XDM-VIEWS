package org.example.View.SQLView;

import org.example.Common.Condition;
import org.example.Source.SQLSource;
import org.example.View.View;

import java.sql.*;
import java.util.ArrayList;

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
        if (!source.hasTable(this.table))   throw new RuntimeException("Table with name " + this.table + " not found at " + source.getURL());
        prepareStatement();

        Connection connection = source.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnNumber = rsmd.getColumnCount();
        while(rs.next()) {
            for (int i = 1; i <= columnNumber; i++) {
                System.out.print(rs.getString(i) + " ");
            }
            System.out.println("");
        }
    }

    /*
        TODO:
            - Check if column exists. Store column once to
            - How to add different conditions for same/ different fields.
     */
    private void prepareStatement() {
        String query = "select";
        if (this.select.size() != 0) {
            for (String s : this.select) {
                query += (" " + s);
            }
        }
        else {
            query += " *";
        }
        query += " from " + table;
        if (this.conditions.size() != 0) {
            query += " where";
            for (Condition s : conditions) {
                query += " " + s.getColumn() + " " + s.getOperator() + " " + s.getValue();
            }
        }
        query += ";";
        this.query = query;
    }

    public void addCondition(Condition condition) { this.conditions.add(condition); }

    public void addSelectColumn(String column) { this.select.add(column); }

    public static void main(String[] args) throws SQLException {
        SQLView obj = new SQLView();
        SQLSource source = new SQLSource("localhost:3306/marketdb", "root", "password");
        obj.addSource(source);
        obj.addTable("dim_prod");
        obj.addSelectColumn("prod_id");
        obj.loadData();
    }
}


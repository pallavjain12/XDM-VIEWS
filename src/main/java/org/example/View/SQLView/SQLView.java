package org.example.View.SQLView;

import org.example.Common.Condition;
import org.example.Source.SQLSource;
import org.example.View.View;

import java.sql.*;
import java.util.ArrayList;

public class SQLView extends View {
    private String URL;
    private String USERID;
    private String PASSWORD;

    private ArrayList<String> select;
    private ArrayList<Condition> conditions;
    private String table;
    private String query;

    private SQLSource source;

    SQLView() {
        this.table = null;
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

    public void loadData() throws SQLException, ClassNotFoundException {

        // TODO: create exception for source null
        if (source == null) throw new SQLException("No source defined for View");

        prepareStatement();

        Connection connection = source.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnNumber = rsmd.getColumnCount();
        System.out.println(columnNumber);
        System.out.println("");
        System.out.flush();
        while(rs.next()) {
            for (int i = 1; i <= columnNumber; i++) {
                System.out.print(rsmd.getColumnName(i) + ": " + rs.getString(i) + " ");
            }
            System.out.println("");
        }

        /*
            Accessing meta data of database
         */

        DatabaseMetaData meta = connection.getMetaData();
        try (ResultSet tables = meta.getTables(null, null, "%", new String[] { "TABLE" })) {
            while (tables.next()) {
                String catalog = tables.getString("TABLE_CAT");
                String schema = tables.getString("TABLE_SCHEM");
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table: " + tableName);
                System.out.println("SCHEMA: " + schema);
                System.out.println("catlog: " + catalog);
                try (ResultSet primaryKeys = meta.getPrimaryKeys(catalog, schema, tableName)) {
                    while (primaryKeys.next()) {
                        System.out.println("Primary key: " + primaryKeys.getString("COLUMN_NAME"));
                    }
                }
            }
        }
    }

    /*
        TODO:
            - Check if column exists. Store column once to
     */
    private void prepareStatement() {
        String query = "select";
        if (this.select != null && this.select.size() != 0) {
            for (String s : this.select) {
                query += (" " + s);
            }
        }
        else {
            query += " *";
        }
        query += " from " + table;
        if (this.conditions != null && this.conditions.size() != 0) {
            query += " where";
            for (Condition s : conditions) {
                query += " " + s.getColumn() + " " + s.getOperator() + " " + s.getValue();
            }
        }
        query += ";";
        this.query = query;
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public void addSelectColumn(String column) {
        this.select.add(column);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SQLView obj = new SQLView();
        SQLSource source = new SQLSource("localhost:3306/marketdb", "root", "password");
        obj.addSource(source);
        obj.addTable("dim_prod");
        obj.loadData();
    }
}


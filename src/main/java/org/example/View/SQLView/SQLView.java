package org.example.View.SQLView;

import org.example.Common.Condition;
import org.example.View.View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLView extends View {
    private String URL;
    private String USERID;
    private String PASS;

    private ArrayList<String> select;
    private ArrayList<Condition> conditions;
    private String table;
    private String query;

    SQLView() {
        this.URL = null;
        this.PASS = null;
        this.table = null;
        this.USERID = null;
    }
    public void addURL(String URL) {
        this.URL = URL;
    }
    public void addCredentials(String USERID, String PASS) {
        this.USERID = USERID;
        this.PASS = PASS;
    }

    public void addTable(String table) {
        this.table = table;
    }

    /*
        TODO:
         - check if table exits before executing query
     */
    public void loadData() throws NoURLFoundException, NoCredentialsFoundForURL, ClassNotFoundException, SQLException {
        if (this.URL == null)   throw new NoURLFoundException();
        if (this.USERID == null || this.PASS == null) throw new NoCredentialsFoundForURL(URL);

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USERID, PASS);
        prepareStatement();
    }

    /*
        TODO:
            - Check if column exists. Store column once to
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

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public void addSelectColumn(String column) {
        this.select.add(column);
    }
}


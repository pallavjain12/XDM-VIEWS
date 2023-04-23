package org.example.Source;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SQLSource {
    private final String USERID;
    private final String PASSWORD;
    private final String URL;

    private Set<String> tables;
    private Connection connection;
    public SQLSource(String URL, String userid, String password) {
        this.URL = URL;
        this.USERID = userid;
        this.PASSWORD = password;

        tables = new HashSet<>();
        try {
            storeDatabaseInfo();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("SQL exception occured while connecting sql database");
        }
    }

    public Connection getConnection() {
        if (connection == null) {

            try {
                if (this.URL == null) throw new RuntimeException("No URL found for connection to database");
                if (this.USERID == null || this.PASSWORD == null) throw new RuntimeException("Credential not found");
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://" + URL, USERID, PASSWORD);

            }
            catch(ClassNotFoundException e) {
                throw new RuntimeException("Class \"com.mysql.cj.jdbc.Driver\" not dound" + e);
            }
            catch (SQLException e) {
                throw new RuntimeException("Exception while creating connection " + e);
            }
        }
        return connection;
    }

    private void storeDatabaseInfo() throws SQLException {

        DatabaseMetaData meta = getConnection().getMetaData();

        try (ResultSet tables = meta.getTables(null, null, "%", new String[] { "TABLE" })) {
            /*
                TODO:   This code fetches all the tables in all the databases user is able to access.
                        Filter out the tables according to selected database or create a map<database_name, set<table_name>>
             */
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
//                String catalog = tables.getString("TABLE_CAT");
//                String schema = tables.getString("TABLE_SCHEMA");
//                try (ResultSet primaryKeys = meta.getPrimaryKeys(catalog, schema, tableName)) {
//                    while (primaryKeys.next()) {
//                        System.out.println("Primary key: " + primaryKeys.getString("COLUMN_NAME"));
//                    }
//                }
                this.tables.add(tableName);
            }
        }
    }

    public boolean hasTable(String tableName) {
        return this.tables.contains(tableName);
    }

    public String getURL() {    return this.URL;    }
}

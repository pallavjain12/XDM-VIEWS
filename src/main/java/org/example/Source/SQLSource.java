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
    public SQLSource(String URL, String userid, String password) throws SQLException {
        this.URL = "jdbc:mysql://" + URL;
        this.USERID = userid;
        this.PASSWORD = password;

        tables = new HashSet<>();
        storeDatabaseInfo();
    }

    public Connection getConnection() {
        if (connection == null) {

            try {
                if (this.URL == null) throw new RuntimeException("No URL found for connection to database");
                if (this.USERID == null || this.PASSWORD == null) throw new RuntimeException("Credential not found");
                System.out.println("checkpoint1");
                System.out.flush();
                Class.forName("com.mysql.cj.jdbc.Driver");
                this.connection = DriverManager.getConnection(URL, USERID, PASSWORD);

            }
            catch(ClassNotFoundException e) {
                System.out.println("Driver class not found for sql jdbc driver");
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

            while (tables.next()) {
//                String catalog = tables.getString("TABLE_CAT");
//                String schema = tables.getString("TABLE_SCHEM");
                String tableName = tables.getString("TABLE_NAME");
                this.tables.add(tableName);
//                try (ResultSet primaryKeys = meta.getPrimaryKeys(catalog, schema, tableName)) {
//                    while (primaryKeys.next()) {
//                        System.out.println("Primary key: " + primaryKeys.getString("COLUMN_NAME"));
//                    }
//                }
            }
        }
    }

    public boolean hasTable(String tableName) {
        return this.tables.contains(tableName);
    }
}

package org.example.View.SQLView;

import org.example.View.View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLView extends View {
    public String URL;
    public String USERID;
    public String PASS;

    private boolean haveCredentials;
    private boolean haveURL;
    SQLView() {
        haveCredentials = false; haveURL = false;
    }
    public void addURL(String URL) {
        this.URL = URL;
        haveURL = true;
    }
    public void addCredentials(String USERID, String PASS) {
        this.USERID = USERID;
        this.PASS = PASS;
        haveCredentials = true;
    }

    public void getData() throws NoURLFoundException, NoCredentialsFoundForURL, ClassNotFoundException, SQLException {
        if (!haveURL)   throw new NoURLFoundException();
        if (!haveCredentials) throw new NoCredentialsFoundForURL(URL);
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(URL, USERID, PASS);
    }
}

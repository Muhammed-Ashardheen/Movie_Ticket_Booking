package com.movie.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // >>> Change these to match your PostgreSQL setup <<<
    private static final String URL  = "jdbc:postgresql://localhost:5432/moviedb";
    private static final String USER = "postgres";
    private static final String PASS = "password";

    static {
        try { Class.forName("org.postgresql.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

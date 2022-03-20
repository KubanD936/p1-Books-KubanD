package com.revature;

import com.revature.server.Server;

import java.sql.*;

public class App {
    public static void main(String[] args) throws SQLException {
        new Server().run();
    }
}

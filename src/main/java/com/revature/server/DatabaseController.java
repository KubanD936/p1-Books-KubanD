package com.revature.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.domain.Books;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    // Connect to DB
    Connection connection;

    public void BuildDatabase(){
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/test;INIT=runscript from 'classpath:schema.sql'", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    HttpServlet booksServlet = new HttpServlet() {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            List<Books> books = new ArrayList<>();
            try {
                ResultSet rs = connection.prepareStatement("select * from com.revature.domain.Books").executeQuery();
                while (rs.next()) {
                    books.add(new Books(rs.getInt("BookId"), rs.getString("Name")));
                }
            } catch (SQLException e) {
                System.err.println("Failed to retrieve from db: " + e.getSQLState());
            }

            // Get a JSON Mapper
            ObjectMapper mapper = new ObjectMapper();
            String results = mapper.writeValueAsString(books);
            resp.setContentType("application/json");
            resp.getWriter().println(results);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            ObjectMapper mapper = new ObjectMapper();
            Books newBooks = mapper.readValue(req.getInputStream(), Books.class);
            System.out.println(newBooks);
            try {
                PreparedStatement stmt = connection.prepareStatement("insert into 'book' values (?, ?)");
                stmt.setInt(1, newBooks.getBookId());
                stmt.setString(2, newBooks.getName());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Failed to insert: " + e.getMessage());
            }
        }
    };
}

package com.revature.Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.domain.Books;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BooksServlet extends HttpServlet{
    private Connection connection;

    //final HttpServlet booksServlet = new HttpServlet() {
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
}

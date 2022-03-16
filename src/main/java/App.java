import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws SQLException {
        // Connect to DB
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;INIT=runscript from 'classpath:schema.sql'", "sa", "");


        HttpServlet booksServlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                List<Books> books = new ArrayList<>();
                try {
                    ResultSet rs = connection.prepareStatement("select * from Books").executeQuery();
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

        // Run server
        Tomcat server = new Tomcat();
        server.getConnector();
        server.addContext("", null);
        server.addServlet("", "defaultServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                String filename = req.getPathInfo();
                String resourceDir = "static";
                InputStream file = getClass().getClassLoader().getResourceAsStream(resourceDir + filename);
                String mimeType = getServletContext().getMimeType(filename);
                resp.setContentType(mimeType);
                IOUtils.copy(file, resp.getOutputStream());
            }
        }).addMapping("/*");
        server.addServlet("", "booksServlet", booksServlet).addMapping("/books");
        try {
            server.start();
        } catch (LifecycleException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}

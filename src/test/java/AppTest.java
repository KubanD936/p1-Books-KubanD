import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.domain.Books;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppTest {
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:~/test;INIT=runscript from 'classpath:schema.sql'", "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void doGet(HttpServletRequest req, HttpServletResponse resp)
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

        Assertions.assertEquals("bookId, name");
    }
}

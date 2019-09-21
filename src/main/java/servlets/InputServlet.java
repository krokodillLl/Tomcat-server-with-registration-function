package servlets;

import accounts.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "servlets.InputServlet")
public class InputServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if(login.equals("") || password.equals("")) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Incorrect data");
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/authorisation\"> </body> </html>");
            return;
        }



        if(!userAuthentication(login, password)) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Incorrect username or password");
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/authorisation\"> </body> </html>");
            return;
        }


        boolean isExists = false;
        Cookie[] cookies = request.getCookies();


            for (Cookie cookie: cookies) {
                if(cookie.getName().equals("sessionId"))
                    isExists = true;
            }


        if(!isExists) {
            Cookie cookie = new Cookie("sessionId", AccountService.getInstance().getSessionId());
            cookie.setMaxAge(24*60*60);
            response.addCookie(cookie);
        }


        response.sendRedirect("http://localhost:8080/afterLogging");

    }

    public boolean userAuthentication(String login, String password) {
        String url = "jdbc:postgresql://localhost:5432/accounts";
        String sql = "SELECT * FROM tomcat WHERE login = '"+ login +"'";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(url,
                    "UserName", "Password");

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            String log = "";
            String pass = "";

            if(rs.next()) {
                log = rs.getString("login").trim();
                pass = rs.getString("password").trim();
            }

            if(!pass.equals(password)) {
                return false;
            }
            else {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

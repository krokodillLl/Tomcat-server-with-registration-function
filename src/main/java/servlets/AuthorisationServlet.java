package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import mail.EmailSender;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


@javax.servlet.annotation.WebServlet(name = "servlets.AuthorisationServlet")
public class AuthorisationServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request,
                          javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request,
                         javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String email = request.getParameter("email");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String echo = request.getParameter("key");



        if(echo != null && login != null) {
            AccountService.getInstance().getUserByLogin(login).setEcho(echo);

            if(AccountService.getInstance().getUserByLogin(login).isCertified()) {
                addToSQL(login);
            }
        }


        if(password != null &&login != null && AccountService.getInstance().getUserByLogin(login) != null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/registration\"> </body> </html>");
            response.getWriter().println("This login already exists");
            return;
        }

        getServletContext().getRequestDispatcher("/authorisation.jsp").forward(request, response);

        if(email.equals("") || login.equals("") || password.equals("")) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/registration\"> </body> </html>");
            response.getWriter().println("Input your data first");
            return;
        }

        AccountService.getInstance().addNewUser(new UserProfile(login, password, email));

        String key = AccountService.getInstance().getUserByLogin(login).getKey();
        EmailSender sender = new EmailSender("SomeEmail@gmail.com", "SomePassword");
        sender.send("localhost",  "Please follow the link \r\n http://localhost:8080/authorisation?key=" + key + "&login=" + login, email);

    }

    public void addToSQL(String login) {
        String url = "jdbc:postgresql://localhost:5432/accounts";
        String password = AccountService.getInstance().getUserByLogin(login).getPassword();
        String email = AccountService.getInstance().getUserByLogin(login).getEmail();
        String sql = "INSERT INTO tomcat (login, password, email) VALUES (?,?,?)";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(url,
                    "UserName", "Password");



            PreparedStatement stIn = conn.prepareStatement(sql);
            stIn.setString(1, login);
            stIn.setString(2, password);
            stIn.setString(3, email);

            stIn.executeUpdate();

            stIn.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

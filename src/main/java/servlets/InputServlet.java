package servlets;

import accounts.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "servlets.InputServlet")
public class InputServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if(login == null || password == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Incorrect data");
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/authorisation\"> </body> </html>");
            return;
        }

        if(AccountService.getInstance().getUserByLogin(login) == null ||
                !AccountService.getInstance().getUserByLogin(login).getPassword().equals(password)) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Incorrect username or password");
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/authorisation\"> </body> </html>");
            return;
        }

        if(AccountService.getInstance().getUserByLogin(login).getKey().equals(AccountService.getInstance().getUserByLogin(login).getEcho())) {
            AccountService.getInstance().getUserByLogin(login).setCertified(true);
        }

        if(!AccountService.getInstance().getUserByLogin(login).isCertified()) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Register or confirm your email");
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/registration\"> </body> </html>");
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
}

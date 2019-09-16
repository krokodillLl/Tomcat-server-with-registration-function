package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "servlets.AfterLoggingServlet")
public class AfterLoggingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String arg = request.getParameter("out");
        Cookie[] cookies = request.getCookies();
        boolean isExists = false;
        if (arg == null) {
            return;
        }
        if (arg.equals("DELETE")) {
            for (Cookie cookie: cookies) {
                if(cookie.getName().equals("sessionId")) {
                    response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                            "URL=http://localhost:8080/authorisation\"> </body> </html>");
                    response.getWriter().println("Goodbye");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    isExists = true;
                }
            }

            if(!isExists) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Sign in to your account first");
                response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                        "URL=http://localhost:8080/authorisation\"> </body> </html>");
                return;
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        boolean isExists = false;


        for (Cookie cookie: cookies) {
            if(cookie.getName().equals("sessionId"))
                isExists = true;
        }

        if(!isExists) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("<html> <body bgcolor=\"#DCD36A\" text=\"black\"> <meta http-equiv=\"Refresh\" content=\"3; " +
                    "URL=http://localhost:8080/authorisation\"> </body> </html>");
            response.getWriter().println("Sign in to your account first");
            return;
        }

        getServletContext().getRequestDispatcher("/afterLogging.jsp").forward(request, response);
    }
}

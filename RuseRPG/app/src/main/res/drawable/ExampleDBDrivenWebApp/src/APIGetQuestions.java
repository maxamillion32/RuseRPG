import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/APIGetQuestions")
public class APIGetQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public APIGetQuestions() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     response.setContentType("application/json");
	        // Get the printwriter object from response to write the required json object to the output stream 
	        PrintWriter out = response.getWriter();

	        out.print("Replace this string with a JSON array of questions");
	        out.flush();
	}
}

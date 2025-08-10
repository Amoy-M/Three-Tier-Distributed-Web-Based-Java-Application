/* Name: Amoy Marshalleck
Course: CNT 4714 – Spring 2025 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 23, 2025
*/
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;

import java.sql.*;
import java.util.Properties;

import javax.naming.AuthenticationException;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.util.*;

public class partsInsertServlet extends HttpServlet{

	//make connection to project4 db using the root client fromt his class instead.
	private Connection project4DB = null;
	
	public void connectDB() {
		
		//String fullPath = null;
		Properties properties = new Properties();
		String fullPath = getServletContext().getRealPath("/WEB-INF/lib/dataentry.properties");//gets the full path to properties files; 
		FileInputStream filein = null;
		MysqlDataSource dataSource = new MysqlDataSource();
		
		try {
			
			filein = new FileInputStream(fullPath);
			properties.load(filein);

			String url = properties.getProperty("MYSQL_DB_URL");
			String propertiesUser = properties.getProperty("MYSQL_DB_USERNAME");
			String propertiesPassword = properties.getProperty("MYSQL_DB_PASSWORD");
			
			System.out.println("url: " + url);
			System.out.println("propertiesUser: " + propertiesUser);
			System.out.println("propertiesPassword: " + propertiesPassword);
			
			dataSource.setUrl(url);
			dataSource.setUser(propertiesUser);
			dataSource.setPassword(propertiesPassword);
			
			project4DB = dataSource.getConnection();
			System.out.println("Connected to project 4 database");
		} catch (IOException | SQLException e1) {
			
			e1.printStackTrace();
		}

	}
	
	public int executeParts(String input1, String input2, String input3, int input4, String input5) {
		
		//Initialize the objects for getting the query
		String query = null;
		PreparedStatement p = null;
		int res = 0;
		
		try {
			
			query = "insert into parts values (?,?,?,?,?)";
			
			p = project4DB.prepareStatement(query);
			
			//Set parameters
			p.setString(1, input1);
			p.setString(2, input2);
			p.setString(3, input3);
			p.setInt(4, input4);
			p.setString(5, input5);
			
			res = p.executeUpdate();
			
			System.out.println("table successfully updated");
		}catch(SQLException e1) {
			
			e1.printStackTrace();
		}
		
		return res;
	}
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//connect to project4 database
		connectDB();
		
		//read from command field
		String input1 = request.getParameter("pnum");
		String input2 = request.getParameter("pname");
		String input3 = request.getParameter("color");
		int input4 = Integer.parseInt(request.getParameter("weight"));
		String input5 = request.getParameter("city");
		System.out.println("inputs: " + input1 + " " + input2 + " " + input3 + " " + input4 + " " + input5);
		
		//insertion
		int rows = executeParts( input1,  input2,  input3,  input4,  input5);
		
		//for re-routing to dataentry page.
		request.setAttribute("commandType", "parts");
		request.setAttribute("rowsCount", rows);
		
		request.setAttribute("pnum", input1);
		request.setAttribute("pname", input2);
		request.setAttribute("color", input3);
		request.setAttribute("weight", input4);
		request.setAttribute("city", input5);
		RequestDispatcher dispatcher = request.getRequestDispatcher("dataEntryHome.jsp");
        dispatcher.forward(request, response);
        
		//closes the connection to project4 database
		try {
			
			project4DB.close();
			System.out.println("Connection to the project 4 databse has ended");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

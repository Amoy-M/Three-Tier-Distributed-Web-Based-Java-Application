/* Name: Amoy Marshalleck
Course: CNT 4714 – Spring 2025 – Project Four
Assignment title: A Three-Tier Distributed Web-Based Application
Date: April 23, 2025
*/

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;

import java.sql.*;

import javax.naming.AuthenticationException;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.util.*;

public class clientUserServlet extends HttpServlet{

	//make connection to project4 db using the root client fromt his class instead.
	private Connection project4DB = null;
	
	//used for storing the column names and records of the db/query
	List<String[]> rows = new ArrayList<>();
    String[] columnNames = null;
	
	public void connectDB() {
		
		//String fullPath = null;
		Properties properties = new Properties();
		String fullPath = getServletContext().getRealPath("/WEB-INF/lib/client.properties");//gets the full path to properties files; 
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
	
	//takes user command and gives values to the rows and columns Name arrays
	public void executeQuery(String command) {
		
		//initialize the lists
		rows.clear();
		columnNames = null;
		
		//Initialize the objects for getting the query
		PreparedStatement p = null;
		ResultSet rs = null;
		
		ResultSetMetaData meta = null;
        int columnCount = 0;
		
		try {
			
			p = project4DB.prepareStatement(command);
			rs = p.executeQuery();
			
			//Gets the number and names of columns in the query
			meta = rs.getMetaData();
	        columnCount = meta.getColumnCount();
	        columnNames = new String[columnCount];

	        //Initializes name of each field
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = meta.getColumnName(i);
            }
            
			//Iterates through each row of the queried table
			while (rs.next()) {

				String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                rows.add(row);
			}
			
			System.out.println("query of table successfully updated");
		}catch(SQLException e1) {
			
			e1.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//connect to project4 database
		connectDB();
		
		//read from command field
		String userCommand = request.getParameter("commandName");
		System.out.println("sqlCommand: " + userCommand);
		
		//Find the sql command type (select, update, or insert)
		String queryParser[] = userCommand.split("[ *]+");
		String commandType = queryParser[0];
		commandType =  commandType.replaceFirst("^\\s*", ""); //used for removing the white spaces
		//System.out.println("commandType: " + commandType);
		
		if(commandType.equalsIgnoreCase("select")) {
			
			//Make a query to project4 db (for Tuesday 4/15)
			executeQuery(userCommand);
			
			//set the attribute of the request
			request.setAttribute("columnNames", columnNames);
			request.setAttribute("rows", rows);
		}else {
			
			System.out.println("Invalid command");
			System.out.println(commandType);
		}
		
		//forwards results of request to the rootHome webpage
		request.setAttribute("commandType", commandType); //needed to tell if sql is (select, update, insert)
		request.setAttribute("commandName", userCommand); //needed to make user command stay on screen.
		RequestDispatcher dispatcher = request.getRequestDispatcher("clientHome.jsp");
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

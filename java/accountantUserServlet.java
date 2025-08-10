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

public class accountantUserServlet extends HttpServlet {
	
	//make connection to project4 db using the root client fromt his class instead.
	private Connection project4DB = null;
	
	//used for storing the column names and records of the db/query
	List<String[]> rows = new ArrayList<>();
    String[] columnNames = null;
	
	public void connectDB() {
		
		//String fullPath = null;
		Properties properties = new Properties();
		String fullPath = getServletContext().getRealPath("/WEB-INF/lib/theaccountant.properties");//gets the full path to properties files; 
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

	//executes the 
	public void getMaxStat(String procedure) {
		
		//initialize the lists
		rows.clear();
		columnNames = null;
		
		//Initialize the objects for getting the query
		CallableStatement p = null;
		ResultSet rs = null;
		
		ResultSetMetaData meta = null;
        int columnCount = 0;
		
		try {
			
			p = project4DB.prepareCall(procedure);
				
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
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		
		//connect to project4 database
		connectDB();
		
		String userQuery = request.getParameter("selectedExe");
		System.out.println(userQuery);
		
		if(userQuery.equals("{call Get_The_Maximum_Status_Of_All_Suppliers()}") 
				|| userQuery.equals("{call Get_The_Sum_Of_All_Parts_Weights()}")
				|| userQuery.equals("{call Get_The_Total_Number_Of_Shipments()}")
				|| userQuery.equals("{call Get_The_Name_Of_The_Job_With_The_Most_Workers()}")
				|| userQuery.equals("{call List_The_Name_And_Status_Of_All_Suppliers()}")) {
			
			getMaxStat(userQuery);
//			//to check if the query is executed properly (works)
//			for(int i = 0; i < columnNames.length; i++) {
//				
//				System.out.print(columnNames[i] + "\t");
//			}
//			System.out.println();
//			
//			for(String[] tempRows : rows) {
//				for(String cell : tempRows) {
//					
//					System.out.print(cell + "\t");
//				}
//				System.out.println();
//			}
			

		}else {
			
			System.out.println("Invalid procedure called");
		}
		
		//set the attribute of the request
		request.setAttribute("columnNames", columnNames);
		request.setAttribute("rows", rows);		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("accountantHome.jsp");
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

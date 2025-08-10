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

public class rootUserServlet extends HttpServlet{
	
	//make connection to project4 db using the root client fromt his class instead.
	private Connection project4DB = null;
	
	//used for storing the column names and records of the db/query
	List<String[]> rows = new ArrayList<>();
    String[] columnNames = null;
    
    //error reason
    String errorReason = null;
    
    //stores the result set of the shipment with quantities below 100 before update to shipments.
    ResultSet shipmentBeforeRS = null;
    ResultSet shipmentAfterRS = null;
	
	public void connectDB() {
		
		//String fullPath = null;
		Properties properties = new Properties();
		String fullPath = getServletContext().getRealPath("/WEB-INF/lib/root.properties");//gets the full path to properties files; 
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
			errorReason = e1.getMessage();
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
			errorReason = e1.getMessage();
		}
	}
	
	
	public int executeUpdate(String command) {
		
		//Initialize the objects for getting the query
		PreparedStatement p = null;
		int rowsCount = 0;
		
		try {
			
			p = project4DB.prepareStatement(command);
			rowsCount = p.executeUpdate();
			System.out.println("table successfully updated");
		}catch(SQLException e1) {
			
			errorReason = e1.getMessage();
			e1.printStackTrace();
		}
		
		return rowsCount;
	}
	
	public void getShipmentsBefore() {
		
		//Initialize the objects for getting the query
		PreparedStatement p = null;
		shipmentBeforeRS = null;
		String query = "SELECT * FROM shipments";
		
		try {
			p = project4DB.prepareStatement(query);
			shipmentBeforeRS = p.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getShipmentsAfter() {
		
		//Initialize the objects for getting the query
		PreparedStatement p = null;
		shipmentAfterRS = null;
		String query = "SELECT * FROM shipments";
		
		try {
			p = project4DB.prepareStatement(query);
			shipmentAfterRS = p.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int updateAllSuppliers() {
		
		int res = 0;
		
		try {
			while(shipmentBeforeRS.next() && shipmentAfterRS.next()) {
				
				String snum1 = shipmentBeforeRS.getString("snum");
				String snum2 = shipmentAfterRS.getString("snum");
				String pnum1 = shipmentBeforeRS.getString("pnum");
				String pnum2 = shipmentAfterRS.getString("pnum");
				String jnum1 = shipmentBeforeRS.getString("jnum");
				String jnum2 = shipmentAfterRS.getString("jnum");
				int quantity1 = Integer.parseInt( shipmentBeforeRS.getString("quantity") );
				int quantity2 = Integer.parseInt( shipmentAfterRS.getString("quantity") );
				
				if(snum1.equals(snum2) && pnum1.equals(pnum2) && jnum1.equals(jnum2) 
						&& quantity1 < 100 && quantity2 >= 100) {
					
					String newSnum1 = "'" + snum1 +"'";
					res = res + executeBusinessLogic(newSnum1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			errorReason = e.getMessage();
			e.printStackTrace();
		}
		return res;
	}
	
	public int executeBusinessLogic(String supplierID) {
		
		//Initialize the objects for getting the query
		PreparedStatement p = null;
		String query = "update suppliers set status = status + 5 where snum = "+ supplierID +"";
		System.out.println(query);
		int rowsCount = 0;
		
		try {
			
			p = project4DB.prepareStatement(query);
			rowsCount = p.executeUpdate();
			//System.out.println("table successfully updated");
		}catch(SQLException e1) {
			
			errorReason = e1.getMessage();
			e1.printStackTrace();
		}
		
		return rowsCount;
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		//connect to project4 database
		connectDB();
		
		//read from command field
		//Note: Command needs to be one line
		String userCommand = request.getParameter("commandName");
		System.out.println("sqlCommand: " + userCommand);
		
		//Find the sql command type (select, update, or insert)
		String queryParser[] = userCommand.split("[ *,)(\\r\\n]+");
		String commandType = queryParser[0];
		for(int i = 0; i < queryParser.length; i++) {
			
			System.out.println(queryParser[i]);
		}
		commandType =  commandType.replaceFirst("^\\s*", ""); //used for removing the white spaces
		//System.out.println("commandType: " + commandType);
		
		boolean hasBusinessLogic = false;
		
		if(commandType.equalsIgnoreCase("select")) {
			
			//Make a query to project4 db (for Tuesday 4/15)
			executeQuery(userCommand);
			
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
			
			//set the attribute of the request
			request.setAttribute("columnNames", columnNames);
			request.setAttribute("rows", rows);
		}else if(commandType.equalsIgnoreCase("update")) {
			
			//get a copy of shipments table before update
			getShipmentsBefore();
			
			//Make an execute update function (for Wednesday 4/16)
			int rows = executeUpdate(userCommand);
			request.setAttribute("rowsUpdated", rows);
			
			//get a copy of shipments table after update
			getShipmentsAfter();
			
			int increasedStats = updateAllSuppliers();
			
			if(increasedStats > 0) {
				
				hasBusinessLogic = true;
			}
			
			request.setAttribute("increasedStats", increasedStats);
			request.setAttribute("hasBusinessLogic", hasBusinessLogic);
		}else if(commandType.equalsIgnoreCase("insert")) {
			
			//Make an execute insert function (for Thursday 4/17)
			int rows = executeUpdate(userCommand);
			request.setAttribute("rowsUpdated", rows);
			
			if(rows > 0) {
				
				String table = queryParser[2];
				System.out.println(table);
//				String snum = queryParser[6];
//				System.out.println(snum);
//				int quantity = Integer.parseInt(queryParser[7]);
//				System.out.println(quantity);
				if(table.equalsIgnoreCase("shipments")) {
					
					System.out.println("uses the shipments table");
					String snum = queryParser[4];
					System.out.println(snum);
					int quantity = Integer.parseInt(queryParser[7]);
					System.out.println(quantity);
					if(quantity >= 100) {
						
						System.out.println("quantity is greater than or equal to 100");
						
						int rows2 = executeBusinessLogic(snum);
						if(rows2 > 0) {
							hasBusinessLogic = true;
						}
					}
				}else {
					
					System.out.println("Not shipments table");
				}
			}
			
			request.setAttribute("hasBusinessLogic", hasBusinessLogic);
		}else {
			
			System.out.println("Invalid command");
			System.out.println(commandType);
		}

		//forwards results of request to the rootHome webpage
		request.setAttribute("commandType", commandType); //needed to tell if sql is (select, update, insert)
		request.setAttribute("commandName", userCommand); //needed to make user command stay on screen.
		request.setAttribute("errorReason", errorReason);
		RequestDispatcher dispatcher = request.getRequestDispatcher("rootHome.jsp");
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

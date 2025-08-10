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

//class loginInfo{
//	
//}

public class authenticationServlet extends HttpServlet{
	
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//read from username and password fields
		String username = request.getParameter("uname");
		String password = request.getParameter("psw");
		
//		This works
		System.out.println("username: " + username);
		System.out.println("password: " + password);
		
		//checks if the username and password are correct
		//don't need to check if username or password are null. HTML does it for you.
		Properties properties = new Properties();
		String fullPath = null; 
		FileInputStream filein = null;
		MysqlDataSource dataSource = new MysqlDataSource();
		
		Connection connectToCredentialsDB = null;
		String sqlQuery = "select * from usercredentials";
		PreparedStatement p = null;
		ResultSet rs = null;
		
		boolean validUserFlag = false;
		String dbUser = null;
		String dbPassword = null;
		
		try {
			
			fullPath = getServletContext().getRealPath("/WEB-INF/lib/system-app.properties"); //gets the full path to properties files
//			System.out.println(fullPath);
			filein = new FileInputStream(fullPath);
			properties.load(filein);
			
			String url = properties.getProperty("MYSQL_DB_URL");
			String propertiesUser = properties.getProperty("MYSQL_DB_USERNAME");
			String propertiesPassword = properties.getProperty("MYSQL_DB_PASSWORD");
			
//			System.out.println("url: " + url);
//			System.out.println("propertiesUser: " + propertiesUser);
//			System.out.println("propertiesPassword: " + propertiesPassword);
			
			dataSource.setUrl(url);
			dataSource.setUser(propertiesUser);
			dataSource.setPassword(propertiesPassword);
			
			
			connectToCredentialsDB = dataSource.getConnection(); //connect to credentialsDB
			
			//gets all records in the credentialsDB.
			p = connectToCredentialsDB.prepareStatement(sqlQuery);
			rs = p.executeQuery();
			
			//searches records of credentialsDB
			//compares login_username and login_password entries to user inputted username and password.
			//if user input found in db, valid and continues
			while (rs.next()) {

				dbUser =  rs.getString("login_username");
				dbPassword = rs.getString("login_password");
				if(dbUser.equals(username) && dbPassword.equals(password)) {
					
					validUserFlag = true;
					break;
				}
                //System.out.println(rs.getString("login_username") + "\t " + rs.getString("login_password"));
                
			}
			
			if(validUserFlag) {
				System.out.println("Username in DB: " + dbUser + "\tPassword in DB: " + dbPassword);
				connectToCredentialsDB.close();
			}else {
				connectToCredentialsDB.close();
				throw new AuthenticationException();
			}
			
		}catch(IOException | SQLException | AuthenticationException e1) {
			
			response.sendRedirect( "/Project4_v1/errorpage.html" );
			e1.printStackTrace();
			
		}
		//works to this point

		if(validUserFlag) {
			
			try {
				
				if(username.equals("client")) {
					
					response.sendRedirect( "/Project4_v1/clientHome.jsp" );
				}else if(username.equals("root")) {
					
					response.sendRedirect( "/Project4_v1/rootHome.jsp" );
				}else if(username.equals("theaccountant")){
					
					response.sendRedirect( "/Project4_v1/accountantHome.jsp" );
				}else {
					
					response.sendRedirect( "/Project4_v1/dataEntryHome.jsp" );
				}
				
			} catch (IOException e1) {
				
				response.sendRedirect( "/Project4_v1/errorpage.html" );
				e1.printStackTrace();
			}
		}
    }
}

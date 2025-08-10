<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1"%> 
<html>
<head>
<style>
	h1 {text-align: center;}
	h2 {text-align: center;}
	h3 {text-align: center;}
	p {text-align: center;}
	div {text-align: center;}
	
	table { border-collapse: collapse; width: 80%; margin: 20px auto; }
    th, td { border: 1px solid #333; padding: 8px; text-align: left; }
</style>
<meta charset="UTF-8">
<title>Client Home</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<h1>Welcome to the Spring 2025 Project 4 Enterprise System</h1>
	<h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
	<p>You are connected to the Project 4 Enterprise System database as a client-level user.</p>
	<p>Please enter any SQL query or update command in the box below</p>
	
	<!-- action="null" method="post" -->
	<form action="clientUser" method="post">
		<div class="container">
			<textarea id="commandName" name="commandName" rows="10" cols="70"><%= request.getAttribute("commandName") != null ? request.getAttribute("commandName") : "" %>
			</textarea><br>
			
			<button type="submit">Execute Command</button>
			<button id="clearCom" type="reset">Reset Form</button>
			<button id="clearRes" type="button" value="clear">Clear Result</button><br>
  			 <!--  <input type="submit" value="Submit"> -->
  			
		    <!--  
		    <input type="password" placeholder="Enter Password" name="psw" required><br>
		    <button type="submit">Click To Authenticate</button><br>
		    -->
		</div>
	</form>
	
	<p>Execution Results: </p>
	
		<div class = "container" id = "containerRes">
		<!-- <p>For temporary purposes</p><br> -->
    	<%--
        	String input = (String) request.getAttribute("commandName");
        	if (input != null) {
    	%>
        	<p>You submitted: <%= input %></p>
    	<%
        	}
    	--%>
    	<!-- prints the table of a query made to a database -->
   		<%
        	String[] columns = (String[]) request.getAttribute("columnNames");
        	java.util.List<String[]> data = (java.util.List<String[]>) request.getAttribute("rows");
			String commandType = (String) request.getAttribute("commandType");

        	if (columns != null && data != null && commandType != null && commandType.equalsIgnoreCase("select")) {
    	%>
	        <table>
	            <tr>
	                <% for (String col : columns) { %>
	                    <th><%= col %></th>
	                <% } %>
	            </tr>
	            <% for (String[] row : data) { %>
	                <tr>
	                    <% for (String cell : row) { %>
	                        <td><%= cell %></td>
	                    <% } %>
	                </tr>
	            <% } %>
	        </table>
    	<%
        	} else if (commandType != null && !commandType.equalsIgnoreCase("select")) {
    	%>
        		<p>Error executing the SQL statement:</p>
        		<p>client is only allowed to execute queries</p>
    	<%
        	} else {
    	%>
        		<p>No data available.</p>
    	<%
        	}
    	%>
	</div>
	
	<script>
        $(document).ready(function() {
            $('#clearRes').click(function() {
                $('#containerRes').empty();
            });
        });
        
        $(document).ready(function() {
            $('#clearCom').click(function() {
                $('#commandName').empty();
            });
        });
    </script>
</body>
</html>
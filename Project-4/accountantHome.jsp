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
	<title>Accountant Home</title>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<h1>Welcome to the Spring 2025 Project 4 Enterprise System</h1>
	<h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h2>
	<p>You are connected to the Project 4 Enterprise System database as a accountant-level user.</p>
	<p>Please enter any SQL query or update command in the box below</p>
	
	<form action="accountantUser" method="post">
			
		<div class="container">
		
			<input type="radio" id="query1" name="selectedExe" value="{call Get_The_Maximum_Status_Of_All_Suppliers()}">
			<label for="query1">Get The Maximum Status Value Of All Suppliers (Returns a Maximum Value)</label><br>
			
			<input type="radio" id="query2" name="selectedExe" value="{call Get_The_Sum_Of_All_Parts_Weights()}">
			<label for="query2">Get the Total Weight of all Parts (Returns a sum)</label><br>
			
			<input type="radio" id="query3" name="selectedExe" value="{call Get_The_Total_Number_Of_Shipments()}">
			<label for="query3">Get the Total Number of Shipments (Returns the current number of shipments in total)</label><br>
			
			<input type="radio" id="query4" name="selectedExe" value="{call Get_The_Name_Of_The_Job_With_The_Most_Workers()}">
			<label for="query4">Get the Name and Number of Workers of the Job with the Most Workers (Returns two values)</label><br>
			
			<input type="radio" id="query5" name="selectedExe" value="{call List_The_Name_And_Status_Of_All_Suppliers()}">
			<label for="query5">List The Name And Status of Every Supplier (Returns a list of supplier names with their current status)</label><br>
			
		    <!-- buttons -->
			<button type="submit">Execute Command</button>
			<button id="clearRes" type="button" value="clear">Clear Result</button><br>
	 		<!--  <input type="submit" value="Submit"> -->
	 	</div>
	</form>

	<p>Execution Results: </p>
	<div class = "container" id = "containerRes">
		<%
        	String[] columns = (String[]) request.getAttribute("columnNames");
        	java.util.List<String[]> data = (java.util.List<String[]>) request.getAttribute("rows");

        	if (columns != null && data != null) {
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
    </script>
</body>
</html>
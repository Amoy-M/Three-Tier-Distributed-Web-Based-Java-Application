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
		textarea{text-align: left;}
		
		table { border-collapse: collapse; width: 80%; margin: 20px auto; }
	    th, td { border: 1px solid #333; padding: 8px; text-align: left; }
	</style>
	<meta charset="UTF-8">
	<title>Data Entry Home</title>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	
	<h1>Welcome to the Spring 2025 Project 4 Enterprise System</h1>
	<h2>Data Entry Application</h2>
	<p>Connected to the Project 4 Enterprise SYstem database as a data-entry-level user</p>
	<p>Enter the data values in a form below to add a new record to the corresponding database table</p>
	
	<div class="container">
	
		<form action="suppliersInsert" method="post" id ="supplierForm">
			<table>
		        <tr>
		            <th>snum</th>
		            <th>sname</th>
		            <th>status</th>
		            <th>city</th>
		        </tr>
		        <tr>
		            <td>
	                	<input type="text" placeholder="Enter Value" name="snum" value="${snum != null ? snum : ''}" required /><br>
	            	</td>
		            <td>
	           			<input type="text" placeholder="Enter Value" name="sname" value="${sname != null ? sname : ''}" required><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="status" value="${status != null ? status : ''}" required><br>
					</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="city" value="${city != null ? city : ''}" required><br>	           			
	           		</td>
		        </tr>
	    	</table>
	    	
	    	<button type="submit">Enter Supplier Record Into Database</button>
			<button type="button" onClick = "clearForm()">Clear Data and Results</button>
		</form>
	</div>
	
	<div class="container">
	
		<form action="partsInsert" method="post" id ="partsForm">
			<table>
		        <tr>
		            <th>pnum</th>
		            <th>pname</th>
		            <th>color</th>
		            <th>weight</th>
		            <th>city</th>
		        </tr>
		        <tr>
		            <td>
	                	<input type="text" placeholder="Enter Value" name="pnum" value="${pnum != null ? pnum : ''}" required /><br>
	            	</td>
		            <td>
	           			<input type="text" placeholder="Enter Value" name="pname" value="${pname != null ? pname : ''}" required /><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="color" value="${color != null ? color : ''}" required /><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="weight" value="${weight != null ? weight : ''}" required /><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="city" value="${city != null ? city : ''}" required /><br>
	           		</td>	           		
		        </tr>
	    	</table>
	    	
	    	<button type="submit">Enter Part Record Into Database</button>
			<button type="reset" onClick = "clearForm2()">Clear Data and Results</button>
		</form>
	</div>

	<div class="container">
	
		<form action="jobsInsert" method="post" id ="jobsForm">
			<table>
		        <tr>
		            <th>jnum</th>
		            <th>jname</th>
		            <th>numworkers</th>
		            <th>city</th>
		        </tr>
		        <tr>
		            <td>
	                	<input type="text" placeholder="Enter Value" name="jnum" value="${jnum != null ? jnum : ''}" required><br>
	            	</td>
		            <td>
	           			<input type="text" placeholder="Enter Value" name="jname" value="${jname != null ? jname : ''}" required><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="numworkers" value="${numworkers != null ? numworkers : ''}" required><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="city" value="${city != null ? city : ''}" required><br>
	           		</td>
		        </tr>
	    	</table>
	    	
	    	<button type="submit">Enter Job Record Into Database</button>
			<button type="reset" onClick = "clearForm3()">Clear Data and Results</button>
		</form>
	</div>
		
	<div class="container" >
	
		<form action="shipmentsInsert" method="post" id ="shipmentsForm">
			<table>
		        <tr>
		            <th>snum</th>
		            <th>pnum</th>
		            <th>jnum</th>
		            <th>quantity</th>
		        </tr>
		        <tr>
		            <td>
	                	<input type="text" placeholder="Enter Value" name="snum" value="${snum != null ? snum : ''}" required><br>
	            	</td>
		            <td>
	           			<input type="text" placeholder="Enter Value" name="pnum" value="${pnum != null ? pnum : ''}" required><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="jnum" value="${jnum != null ? jnum : ''}" required><br>
	           		</td>
	           		<td>
	           			<input type="text" placeholder="Enter Value" name="quantity" value="${quantity != null ? quantity : ''}" required><br>
	           		</td>
		        </tr>
	    	</table>
	    	
	    	<button type="submit">Enter Shipment Record Into Database</button>
			<button type="reset" onClick = "clearForm4()">Clear Data and Results</button>
		</form>
	</div>
	
	
	<p>Execution Results</p>
	<div class="container" id = "containerRes">
	
		<!-- get command type (i.e supply) -->
		<%
        	String commandType = (String) request.getAttribute("commandType");
			Integer updatedRows = (Integer) request.getAttribute("rowsCount");
			String errorReason = (String) request.getAttribute("errorReason");

        	if (commandType != null && commandType.equals("supplier") && updatedRows > 0) {
				
				String snum = (String) request.getAttribute("snum");
				String sname = (String) request.getAttribute("sname");
				Integer status = (Integer) request.getAttribute("status");
				String city = (String) request.getAttribute("city");
    	%>
	        	<p>New suppliers record: (<%= snum %>, <%= sname %>, <%= status %>, <%= city %>)-successfully entered into database</p>
    	<%
        	} else if (commandType != null && commandType.equals("parts") && updatedRows > 0) {
				
				String pnum = (String) request.getAttribute("pnum");
				String pname = (String) request.getAttribute("pname");
				String color = (String) request.getAttribute("color");
				Integer weight = (Integer) request.getAttribute("weight");
				String city = (String) request.getAttribute("city");
    	%>
        		<p>New parts record: (<%= pnum %>, <%= pname %>, <%= color %>, <%= weight %>, <%= city %>)-successfully entered into database</p>
    	<%
        	} else if(commandType != null && commandType.equals("jobs") && updatedRows > 0) {
				
				String jnum = (String) request.getAttribute("jnum");
				String jname = (String) request.getAttribute("jname");
				Integer numworkers = (Integer) request.getAttribute("numworkers");
				String city = (String) request.getAttribute("city");
    	%>
        		<p>New jobs record: (<%= jnum %>, <%= jname %>, <%= numworkers %>, <%= city %>)-successfully entered into database</p>
    	<%
        	} else if (commandType != null && commandType.equals("shipments") && updatedRows > 0){
    			
				String snum = (String) request.getAttribute("snum");
				String pnum = (String) request.getAttribute("pnum");
				String jnum = (String) request.getAttribute("jnum");
				Integer quantity = (Integer) request.getAttribute("quantity");
		%>
        		<p>New shipments record: (<%= snum %>, <%= pnum %>, <%= jnum %>, <%= quantity %>)-successfully entered into database</p>
    	<%
				if(quantity >= 100){
					%>
						<p>Business Logic Triggered</p>
					<%
				}
        	}  else if (commandType != null && errorReason != null) {
    	%>
        		<p>SQL Error:</p>
        		<p><%= errorReason %></p>
    	<%
        	}  else {
    	%>
        		<p>Cannot make changes to database.</p>
    	<%
        	}
    	%>
	</div>
	
	<script>
        
        function clearForm(){
        	
        	//document.getElementById('supplierForm').reset();
        	document.querySelectorAll('#supplierForm input[type="text"]').forEach(input => input.value = "");
        	document.getElementById('containerRes').innerHTML = '';
        }
        
        function clearForm2(){
        	
        	//document.getElementById('supplierForm').reset();
        	document.querySelectorAll('#partsForm input[type="text"]').forEach(input => input.value = "");
        	document.getElementById('containerRes').innerHTML = '';
        }
        
        function clearForm3(){
        	
	       	//document.getElementById('supplierForm').reset();
	       	document.querySelectorAll('#jobsForm input[type="text"]').forEach(input => input.value = "");
	       	document.getElementById('containerRes').innerHTML = '';
        }
        
        function clearForm4(){
        	
	       	//document.getElementById('supplierForm').reset();
	       	document.querySelectorAll('#shipmentsForm input[type="text"]').forEach(input => input.value = "");
	       	document.getElementById('containerRes').innerHTML = '';
        }
        
    </script>
</body>
</html>
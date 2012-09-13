<jsp:useBean id="birtwebapp" class="birtwebapp.IsEmpty" scope="session"/>
<%@ page contentType="text/html" language="java" import="java.util.*,java.io.*;" %>

<%!
ArrayList<String> al=new ArrayList<String>();
public ArrayList<String> getFileList(String path){
		File dir = new File(path);
		for(File file : dir.listFiles()){
			if(file.getName().endsWith("logs")){
				al.add(file.getName());
			}
		}
				
		if(al.size() == 0){
			return al;
		}
		else{
			al.clear();
			dir = new File(path+"/logs");
			for(File file : dir.listFiles()){
				if(file.getName().endsWith(".log")){
					al.add(file.getName());
				}
			}
			return al;
		}
	}
%>

<html> 
<head> 
<title>index</title> 
</head> 
<body>
<h1>
Create BIRT report for project
</h1>
<form action="tobirt.jsp" method="post">
<br>
.log files:
<select name="files" size="1">
<option selected value="">Please select a .log file</option>
<%String path = new File(application.getRealPath(request.getRequestURI())).getParent();
File file=new File(path);
path = file.getParent();
String[] realPath = path.split("target");
ArrayList<String> al = getFileList(realPath[0]);
int n = al.size();
	for (int i = n-1; i >= 0; i--){
	    if(birtwebapp.findContent(realPath[0]+"/logs/"+(String) al.get(i))){
			al.remove(i);
		}
	}
 for ( int i = 0; i < al.size(); i++ ) {
        %>
        <option value=<%= al.get(i) %>> <%= al.get(i) %> </option>
        <%
    } 
%>
</select>
<br>

<br>
<input type="submit" name="submit" value="OK">
</form>

</body>
</html>
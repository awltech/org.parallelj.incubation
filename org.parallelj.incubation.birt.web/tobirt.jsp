<jsp:useBean id="birtwebapp" class="birtwebapp.BirtAction" scope="request"/>
<%@ page contentType="text/html" language="java" import="java.util.*,java.io.*;" %>
<html> 
<head> 
<title>To BIRT</title> 
</head> 
<body>
<%
String filename = request.getParameter("files");
if(filename == ""){
%>
<jsp:forward page="index.jsp"/>
<%
}
else{
String path = new File(application.getRealPath(request.getRequestURI())).getParent();
File file=new File(path);
path = file.getParent();
String[] realPath = path.split("target");
birtwebapp.birtAction(filename,realPath[0],path);
response.sendRedirect(filename+"/report.html");
}
%>
</body>
</html>
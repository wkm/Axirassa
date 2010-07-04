<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<title>Welcome</title>
		<link href="<s:url value="/css/tutorial.css"/>" rel="stylesheet" type="text/css"/>
	</head>
	<body>
		<h3>Commands</h3>
		<ul>
			<li><a href="<s:url action="Register"/>">Register</a></li>
			<li><a href="<s:url action="Logon_input"/>">Sign On</a></li>
		</ul>
	</body>
</html>
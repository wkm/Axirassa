<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<title>Login</title>
	</head>
	<body>
		<s:form action="Logon">
			<s:textfield label="%{getText('username')}" name="username"/>
			<s:textfield label="%{getText('password')}" name="password"/>
		</s:form>
	</body>
</html>
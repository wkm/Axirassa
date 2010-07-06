<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ax" uri="AxirTaglib" %>
<html>
	<ax:head>
		<s:head/>
	</ax:head>
	<body>
		<ax:header/>
		<ax:content>
			<s:form method="post" action="Register">
				<s:textfield label="%{getText('username')}" name="username"/>
				
				<s:password label="%{getText('password')}" name="password"/>
				<s:password	 label="%{getText('confirmpassword')}" name="confirmpassword"/>
				
				<s:textfield label="%{getText('email')}" name="email"/>
				<s:textfield label="%{getText('confirmemail')}" name="confirmemail"/>
			</s:form>
		</ax:content>
	</body>
</html>
<%@ include file="/taglibs.jsp" %>
<html>
	<ax:head>
		<s:head/>
	</ax:head>
	<body>
		<ax:header/>
		<ax:content>
			<s:form action="Logon">
				<s:textfield label="%{getText('username')}" name="username"/>
				<s:password label="%{getText('password')}" name="password"/>
				
				<s:submit label="Login" />
			</s:form>
		</ax:content>
	</body>
</html>
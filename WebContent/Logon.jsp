<%@ include file="/taglibs.jsp" %>
<html>
	<ax:head>
		<s:head/>
	</ax:head>
	<body>
		<ax:header/>
		<ax:content title="Logon">
			<s:form action="Logon">
				<s:textfield label="%{getText('email')}" name="email"/>
				<s:password label="%{getText('password')}" name="password"/>
				
				<s:submit/>
			</s:form>
		</ax:content>
	</body>
</html>
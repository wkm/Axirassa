<%@ include file="/taglibs.jsp" %>
<html>
	<ax:head> 
		<s:head/>
	</ax:head>
	<body>
		<ax:header/>
		<ax:content title="Create Your Account">
			<s:form action="Register" method="post">
				<s:textfield label="%{getText('email')}" name="email"/>
				<s:textfield label="%{getText('confirmemail')}" name="confirmemail"/>
			
				<ax:formseparator/>
			
				<s:password label="%{getText('password')}" name="password"/>
				<s:password	label="%{getText('confirmpassword')}" name="confirmpassword"/>
				
				<s:submit />
			</s:form>
		</ax:content>
	</body>
</html>
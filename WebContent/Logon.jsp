<%@ include file="taglibs.jsp" %>
<%@ include file="inc/header.jsp" %>

	<s:form action="Logon">
		<s:textfield label="%{getText('username')}" name="username"/>
		<s:password label="%{getText('password')}" name="password"/>
		
		<s:submit label="Login" />
	</s:form>

<%@ include file="inc/footer.jsp" %>
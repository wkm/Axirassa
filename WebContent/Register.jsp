<%@ include file="taglibs.jsp" %>
<%@ include file="inc/header.jsp" %>

<s:form action="Register">
	<s:textfield label="%{getText('username')}" name="username"/>
	
	<s:password label="%{getText('password')}" name="password"/>
	<s:password	 label="%{getText('confirmpassword')}" name="confirmpassword"/>
	
	<s:textfield label="%{getText('email')}" name="email"/>
	<s:textfield label="%{getText('confirmemail')}" name="confirmemail"/>
	
	<s:submit label="Register"/>
</s:form>

<%@ include file="inc/footer.jsp" %>
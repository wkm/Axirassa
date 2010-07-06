<%@ include file="/taglibs.jsp" %>
	
<html>
	<ax:head>
		<s:head/>
	</ax:head>
	<body>
		<ax:header/>
		<ax:content>
			<h3>Commands</h3>
			<ul>
				<li><a href="<s:url action="User/Register_input"/>">Register</a></li>
				<li><a href="<s:url action="User/Logon_input"/>">Sign On</a></li>
			</ul>
		</ax:content>
	</body>
</html>
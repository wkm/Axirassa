<#-- 
	generalized attribute inserter
	
	form:
		<@attr "id" value />
		
-->
<#macro attr name param="null">
	<#if param != "null">${name}="${param}"</#if>
</#macro>

<#macro attr_default name default param="null">
	${name} = <#if param != "null">"${param?html}"<#else>"#{default}"</#if>
</#macro>
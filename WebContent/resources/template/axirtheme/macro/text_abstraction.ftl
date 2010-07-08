<#include "attr.ftl">
<#macro textfieldabstraction type>
	<#compress>
		<input type="${type}" class="txtinput"
			name="${parameters.name?default("")?html}"
			<@attr "size" parameters.get("size") />
			<@attr "maxlength" parameters.get("maxlength") />
			<@attr "value" parameters.get("nameValue") />			
			<@attr "tabindex" parameters.tabindex />
			<@attr "title" parameters.title />
			<@attr "id" parameters.id />
			
			<#if parameters.get("disabled")?default(false) >
				disabled="disabled"
			</#if>
			
			<#if parameters.get("readonly")?default(false) >
				readonly="readonly"
			</#if>
		/>
	</#compress>
</#macro>
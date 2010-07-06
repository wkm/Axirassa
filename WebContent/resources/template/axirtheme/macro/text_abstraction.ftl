<#macro textfieldabstraction type>
	<#compress>
		<input type="${type}"
			name="${parameters.name?default("")?html}"
			<#if parameters.get("size")??>
				size="${parameters.get("size")?html}
			</#if>
			<#if parameters.title??>
				title="${parameters.title?html}"
			</#if>
		/>
	</#compress>
</#macro>
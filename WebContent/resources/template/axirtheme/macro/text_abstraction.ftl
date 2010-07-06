<#include "attr.ftl">
<#macro textfieldabstraction type>
	<#compress>
		<input type="${type}"
			name="${parameters.name?default("")?html}"
			<@attr "size" parameters.get("size") />
			<@attr "title" parameters.title />
		/>
	</#compress>
</#macro>
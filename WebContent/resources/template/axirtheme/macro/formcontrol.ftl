<#include "attr.ftl">
<#macro formcontrol>
	<tr>
		<td>
			<#if parameters.label?? >
				<label
					<@attr "for" parameters.id />
					>
					${parameters.label?html}
				</label>
			</#if>
		</td>
		<td>
			<#nested>
		</td>
</#macro>
<#include "attr.ftl">
<#macro formcontrol otherclasses="">
	<#assign hasFieldErrors = parameters.name?? && fieldErrors?? && fieldErrors[parameters.name]?? />
	<#if hasFieldErrors>
		<#list fieldErrors[parameters.name] as error>
			<tr>
				<td></td>
				<td><div class='errorMsg'>${error?html}</div></td>
			</tr>
		</#list>
	</#if>
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
		<td class='forminput${otherclasses}'>
			<#nested>
		</td>
	</tr>
</#macro>
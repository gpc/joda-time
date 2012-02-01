<div class="fieldcontain ${hasErrors(bean: employeeInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="employee.name.label" default="Name"/>
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${employeeInstance?.name}"/>
</div>
<fieldset class="embedded"><legend><g:message code="employee.job.label" default="Job"/></legend>

	<div class="fieldcontain ${hasErrors(bean: employeeInstance, field: 'job.title', 'error')} required">
		<label for="job.title">
			<g:message code="employee.job.title.label" default="Title"/>
			<span class="required-indicator">*</span>
		</label>
		<g:textField name="job.title" required="" value="${jobInstance?.title}"/>
	</div>

	<div class="fieldcontain ${hasErrors(bean: employeeInstance, field: 'job.startDate', 'error')} required">
		<label for="job.startDate">
			<g:message code="employee.job.startDate.label" default="Start Date"/>
			<span class="required-indicator">*</span>
		</label>
		<joda:datePicker name="job.startDate" value="${jobInstance?.startDate}"/>
		%{--<joda:dateField name="job.startDate" value="${jobInstance?.startDate}"/>--}%
	</div>

	<div class="fieldcontain ${hasErrors(bean: employeeInstance, field: 'job.endDate', 'error')} ">
		<label for="job.endDate">
			<g:message code="employee.job.endDate.label" default="End Date"/>
		</label>
		<joda:datePicker name="job.endDate" value="${jobInstance?.endDate}"/>
		%{--<joda:dateField name="job.endDate" value="${jobInstance?.endDate}"/>--}%
	</div>

</fieldset>

<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder; jodatest.Event" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code:'event.label', default:'Event')}"/>
		<title>Create ${entityName}</title>
		<gui:resources components="datePicker"/>
	</head>
	<body>
		<div class="nav">
			<span class="menuButton"><a class="home" href="${resource(dir: '')}">Home</a></span>
			<span class="menuButton"><g:link class="list" action="list">${entityName} List</g:link></span>
		</div>
		<div class="body">
			<h1>Create ${entityName}</h1>
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${eventInstance}">
				<div class="errors">
					<g:renderErrors bean="${eventInstance}" as="list"/>
				</div>
			</g:hasErrors>
			<g:form action="save" method="post">
				<div class="dialog">
					<table>
						<tbody>

							<tr class="prop">
								<td valign="top" class="name">
									<label for="description">
										<g:message code="event.description.label" default="Description"/>
									</label>
								</td>
								<td valign="top" class="value ${hasErrors(bean: eventInstance, field: 'description', 'errors')}">
									<input type="text" id="description" name="description" value="${fieldValue(bean: eventInstance, field: 'description')}"/>
								</td>
							</tr>

							<tr class="prop">
								<td valign="top" class="name">
									<label for="time">
										<g:message code="event.time.label" default="Time"/>
									</label>
								</td>
								<td valign="top" class="value ${hasErrors(bean: eventInstance, field: 'time', 'errors')}">
									<gui:datePicker id="time" value="${eventInstance?.time?.toDateTime()?.toDate()}" includeTime="true" formatString="${ConfigurationHolder.config.jodatime.format.org.joda.time.LocalDateTime}"/>
								</td>
							</tr>

						</tbody>
					</table>
				</div>
				<div class="buttons">
					<span class="button"><input class="save" type="submit" value="Create"/></span>
				</div>
			</g:form>
		</div>
	</body>
</html>

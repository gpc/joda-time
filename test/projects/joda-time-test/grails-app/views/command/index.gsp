<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="main"/>
		<title>Command Binding Tests</title>
	</head>
	<body>
		<div class="nav">
			<span class="menuButton"><a class="home" href="${resource(dir:'')}">Home</a></span>
		</div>
		<div class="body">
			<h1>Command Binding Tests</h1>
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>

			<g:form action="submitLocalDate" name="localDate">
				<fieldset>
					<legend>LocalDate</legend>
					<g:hasErrors bean="${localDateCommand}">
						<div class="errors">
							<g:renderErrors bean="${localDateCommand}" as="list"/>
						</div>
					</g:hasErrors>
					<g:textField name="localDate" value="${localDateCommand?.localDate}"/>
					<g:submitButton name="submit" value="Submit"/>
				</fieldset>
			</g:form>

		</div>
	</body>
</html>
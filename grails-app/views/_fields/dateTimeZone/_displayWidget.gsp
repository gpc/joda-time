<%
    def attrs = [name: property, value: value, pattern: 'ZZZ']
	if (required) attrs.required = ''
	out << joda.format(attrs)
%>

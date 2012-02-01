<%@ page defaultCodec="html" %>
<g:if test="${required}"><joda:dateField name="${property}" value="${value}" required=""/></g:if>
<g:else><joda:dateField name="${property}" value="${value}"/></g:else>
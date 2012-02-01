<%@ page defaultCodec="html" %>
<g:if test="${required}"><joda:timeField name="${property}" value="${value}" required=""/></g:if>
<g:else><joda:timeField name="${property}" value="${value}"/></g:else>
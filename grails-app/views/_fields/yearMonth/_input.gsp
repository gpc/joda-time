<%@ page defaultCodec="html" %>
<g:if test="${required}"><joda:monthField name="${property}" value="${value}" required=""/></g:if>
<g:else><joda:monthField name="${property}" value="${value}"/></g:else>
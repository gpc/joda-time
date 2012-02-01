<%@ page defaultCodec="html" %>
<g:if test="${required}"><joda:datetimeField name="${property}" value="${value}" required=""/></g:if>
<g:else><joda:datetimeField name="${property}" value="${value}"/></g:else>
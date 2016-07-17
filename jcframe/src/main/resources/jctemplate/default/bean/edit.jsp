<tr>
<td class="fieldrow" id="fieldrow_${fieldName}" name="fieldrow_${fieldName}">
  <label for="${fieldName}">${fieldHeader}</label>
</td>
<td>
  <!-- Cloudflare setting -->
  <!--email_off-->
  <select name="${fieldName}">
    <option value="0">Select ${fieldHeader} Id...</option>
    <c:forEach items='${lookupMap.get("${fieldName}")}' var="lookupBean">
      <option value='<c:out value="${lookupBean.id}"/>'
      <c:if test="${bean.${fieldName}.id == lookupBean.id}">selected="true"</c:if>
      ><c:out value="${lookupBean.displayValue}"/> [<c:out value="${lookupBean.id}"/>]</option>
    </c:forEach>
  </select>
  <!--/email_off-->
</td>
</tr>
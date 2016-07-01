<tr>
<td class="fieldrow" id="fieldrow_${fieldName}" name="fieldrow_${fieldName}">
  <label for="${fieldName}">${fieldHeader}</label>
</td>
<td
  <div class="field" id="${fieldName}" name="${fieldName}">
    <c:forEach items='${bean.${fieldName}}' var="fieldBean">
        ${linkPrefix}<c:out value="${fieldBean.displayValue}"/>${linkSuffix}
    </c:forEach>
  </div>
</td>
</tr>

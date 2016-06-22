<tr>
<td class="fieldrow" id="fieldrow_${fieldName}" name="fieldrow_${fieldName}">
  <label for="${fieldName}">${fieldHeader}</label>
</td>
<td>
  <div class="field" id="${fieldName}" name="${fieldName}">${linkPrefix}<c:out value="${bean.${fieldName}}" ${isHtml}/>${linkSuffix}</div>
</td>
</tr>
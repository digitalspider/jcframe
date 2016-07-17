              <td>
                <c:forEach items='${bean.${fieldName}}' var="fieldBean">
                  ${linkPrefix}<c:out value="${fieldBean.displayValue}"/>${linkSuffix}
                </c:forEach>
              </td>

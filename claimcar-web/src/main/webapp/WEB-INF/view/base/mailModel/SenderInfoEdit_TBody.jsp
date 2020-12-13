<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpdEmailVo" items="${prpdEmailVoList}" varStatus="status">
	<tr class="text-c">
		<%-- <td>${status.index + size + 1}
			
		</td> --%>
		<td>
			<input type="hidden"  name="prpdEmailVo[${status.index + size  }].id" 
			      value="${prpdEmailVo.id }" />
			<input type="text" class="input-text" name="prpdEmailVo[${status.index + size  }].name" 
			      value="${prpdEmailVo.name }" datatype="*" clazz="must"  maxlength="10" />
		</td>
		<td>
			<input type="text" class="input-text" name="prpdEmailVo[${status.index + size  }].email" 
			       value="${prpdEmailVo.email }" datatype="e" clazz="must" />
		</td>
		<td>
			<span class="select-box"> 
				<app:codeSelect codeType="Mold" name="prpdEmailVo[${status.index + size  }].caseType" 
					value="${prpdEmailVo.caseType }" clazz="must" id="caseType" type="select" />
			</span>
		</td>
		<td width ="27%">
			<span class="select-box">
				<c:choose>
					  <c:when test="${fn:startsWith(userComCode, '0000')}">
							<app:codeSelect
							codeType="ComCodeLv2" type="select" 
							lableType="code-name" clazz="must" 
							name="prpdEmailVo[${status.index + size  }].comCode"  value="${prpdEmailVo.comCode }" />
							<font class="must">*</font>
						</c:when>
						 <c:when test="${fn:startsWith(userComCode, '0001')}">
							<app:codeSelect
							codeType="ComCodeLv2" type="select" 
							 clazz="must" 
							name="prpdEmailVo[${status.index + size  }].comCode"  value="${prpdEmailVo.comCode }" />
							<font class="must">*</font>
						</c:when>
						<c:otherwise>
							<input type="hidden" name="prpdEmailVo[${status.index + size  }].comCode"  value="${userComCode}" /> 
							<app:codeSelect
							codeType="ComCodeLv2" type="select" 
							 clazz="must" 
							name="prpdEmailVo[${status.index + size  }].comCode" 
							value="${userComCode}"   disabled="true"  />
							<font class="must">*</font>
						</c:otherwise>
			  </c:choose><%-- 
				<app:codeSelect codeType="ComCodeLv2" name="prpdEmailVo[${status.index + size  }].comCode" 
					value="${prpdEmailVo.comCode }" clazz="must"  type="select" />  --%>
			<%-- 	<c:choose>
	                <c:when test="${fn:startsWith(userComcode, '0001')}">
	                	<app:codeSelect codeType="ComCodeLv2" name="prpdEmailVo[${status.index + size  }].comCode" 
					value="${prpdEmailVo.comCode }" clazz="must"  type="select" />
	                </c:when>
	                <c:when test="${fn:startsWith(userComcode, '0000')}">
	                	<app:codeSelect codeType="ComCodeLv2" name="prpdEmailVo[${status.index + size  }].comCode" 
					value="${prpdEmailVo.comCode }" clazz="must" type="select" />
	                </c:when>
	                <c:otherwise>
	                	 <app:codetrans codeType="ComCodeLv2" value="prpdEmailVo[${status.index + size  }].comCode" />
	               </c:otherwise>
	            </c:choose> --%>
			</span>
		</td>
		<td>
			<button type="button" class="btn  btn-primary mt-5" onclick="deleteInfo(this)"
				name="deleteBtn_${status.index + size  }" >删除</button>
		</td>
	</tr>
</c:forEach>
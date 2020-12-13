<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<c:forEach var="prpdAddresseeVo" items="${prpdAddresseeVoList}" varStatus="status">
	<tr class="text-c">
		<%-- <td>${status.index + size + 1}
			
		</td> --%>
		<td>
			<input type="hidden"  name="prpdAddresseeVo[${status.index + size  }].id" 
			      value="${prpdAddresseeVo.id }" />
			<input type="text" class="input-text" name="prpdAddresseeVo[${status.index + size  }].name" 
			      value="${prpdAddresseeVo.name }" datatype="*1-10" />
		</td>
		<td>
			<input type="text" class="input-text" name="prpdAddresseeVo[${status.index + size  }].mobileNo" 
			       value="${prpdAddresseeVo.mobileNo }" datatype="/^1[0-9]{10}$/" />
		</td>
		<td>
			<span class="select-box"> 
				<app:codeSelect codeType="ComCodeLv2" name="prpdAddresseeVo[${status.index + size  }].comCode" 
					value="${prpdAddresseeVo.comCode }" clazz="must" id="comCode" type="select" />
			</span>
		</td>
		
		<c:if test="${prpdAddresseeVo.flag eq 1}">
		   <td>
			   <select name="prpdAddresseeVo[${status.index + size  }].flag">
				   <option selected="selected" value="1">正常案件</option>
				   <option value="2">自助理赔</option>
				   <option value="3">自动上传平台失败报警邮箱</option>
			   </select> 
			</td>
		</c:if>
		<c:if test="${prpdAddresseeVo.flag eq 2}">
		   <td>
			   <select name="prpdAddresseeVo[${status.index + size  }].flag">
				   <option value="1">正常案件</option>
				   <option selected="selected" value="2">自助理赔</option>
				   <option value="3">自动上传平台失败报警邮箱</option>
			   </select> 
			</td>
		</c:if>
		<c:if test="${prpdAddresseeVo.flag eq 3}">
		   <td>
			   <select name="prpdAddresseeVo[${status.index + size  }].flag">
				   <option value="1">正常案件</option>
				   <option value="2">自助理赔</option>
				   <option selected="selected" value="3">自动上传平台失败报警邮箱</option>
			   </select> 
			</td>
		</c:if>
		<c:if test="${prpdAddresseeVo.flag eq null}">
		   <td>
			   <select name="prpdAddresseeVo[${status.index + size  }].flag">
			       <option selected="selected">请选择标志</option>
			       <option value="1">正常案件</option>
			       <option value="2">自助理赔</option>
			       <option value="3">自动上传平台失败报警邮箱</option>
			   </select> 
			</td>
		</c:if>		
		
		<td>
			<button type="button" class="btn  btn-primary mt-5" onclick="deleteInfo(this,${prpdAddresseeVo.id})"
				name="deleteBtn_${status.index + size  }" >删除</button>
		</td>
	</tr>
</c:forEach>
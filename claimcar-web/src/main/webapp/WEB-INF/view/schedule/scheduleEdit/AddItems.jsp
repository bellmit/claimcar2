<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<c:forEach var="prpLScheduleItem" items="${prpLScheduleItemses}" varStatus="status">
	<tr class="text-c">
		<td>
			<c:choose>
		    	<c:when test="${empty prpLScheduleItem.id}">
		    		<input id="${status.index}" name="checkCode" itemId="" disabled="disabled" checked="checked" group="ids" type="checkbox" value="${status.index}" />
		    	</c:when>
		    	<c:when test="${prpLScheduleItem.itemType eq 1}">
		    		<input id="${status.index}" name="checkCode" disabled="disabled" itemId="${prpLScheduleItem.id}" disabled="disabled" checked="checked" group="ids" type="checkbox" value="${status.index}" />
		    	</c:when>
		    	<c:otherwise>
		    		<input id="${status.index}" name="checkCode" disabled="disabled" itemId="${prpLScheduleItem.id}" checked="checked" group="ids" type="checkbox" value="${status.index}" onclick="checkedOrNot(this, ${status.index})" />
		    	</c:otherwise>
			</c:choose>
		</td>
		<td>
			<c:choose>
		    	<c:when test="${prpLScheduleItem.itemType eq '3'}">
		    		<app:codeSelect codeType="ItemType" dataSource="${carNoMap}" disabled="true" type="select" clazz="must" value="${prpLScheduleItem.licenseNo}" />
					<input type="hidden" name="prpLScheduleItemses[${status.index}].licenseNo" value="${prpLScheduleItem.licenseNo}" />
		    	</c:when>
		    	<c:otherwise>
		    		<app:codeSelect codeType="ItemType" disabled="true" type="select" clazz="must" value="${prpLScheduleItem.itemType}" />
		    	</c:otherwise>
			</c:choose>
			<input type="hidden" name="prpLScheduleItemses[${status.index}].itemType" value="${prpLScheduleItem.itemType}" />
		</td>
		<td>
			<c:choose>
		    	<c:when test="${prpLScheduleItem.itemType eq '4'}">
		    		
		    		<c:choose>
		    			<c:when test="${prpLScheduleItem.itemRemark eq '0' && prpLScheduleItem.itemsContent eq '0'}">
				    		&nbsp;标的车:
				       		<input type="text"  class="input-text" id="injCountTag" style="width: 15%" onblur="countSum()" name="prpLRegistPersonLosses[0].injuredcount" value="${prpLScheduleItem.itemRemark}" datatype="n" />
				       		<strong>&nbsp;伤&nbsp;</strong>
				       		<input style="width: 15%"  class="input-text" id="deaCountTag" onblur="countSum()" name="prpLRegistPersonLosses[0].deathcount" value="${prpLScheduleItem.itemsContent}" type="text" datatype="n" />
				       		<strong>&nbsp;亡</strong>
				       		<br>
							三者车:
				       		<input type="text"  class="input-text" id="injCountThird"  onblur="countSum()" style="width: 15%" name="prpLRegistPersonLosses[1].injuredcount" value="${prpLScheduleItem.itemRemark}" datatype="n" />
				       		<strong>&nbsp;伤&nbsp;</strong>
				       		<input style="width: 15%"  class="input-text" id="deaCountThird" onblur="countSum()" name="prpLRegistPersonLosses[1].deathcount" value="${prpLScheduleItem.itemsContent}" type="text" datatype="n" />
				       		<strong>&nbsp;亡</strong>
				       		<input type="hidden" name="prpLRegistPersonLosses[0].lossparty" value="1" />
					 		<input type="hidden" name="prpLRegistPersonLosses[1].lossparty" value="3" />
				       		<input   class="input-text" id="deathSum" name="prpLScheduleItemses[${status.index}].itemRemark" value="${prpLScheduleItem.itemRemark}" type="hidden" />
				       		<input type="hidden" id="injuredSum"  class="input-text" name="prpLScheduleItemses[${status.index}].itemsContent" value="${prpLScheduleItem.itemsContent}"/>
				       		
		    			</c:when>
		    			<c:otherwise>
							<input type="text"  class="input-text" style="width: 40%" name="prpLScheduleItemses[${status.index}].itemsContent" value="${prpLScheduleItem.itemsContent}" datatype="n" />
				       		<strong>&nbsp;伤&nbsp;</strong>
				       		<input style="width: 40%"  class="input-text" name="prpLScheduleItemses[${status.index}].itemRemark" value="${prpLScheduleItem.itemRemark}" type="text" datatype="n" />
				       		<strong>&nbsp;亡</strong>		    			
						</c:otherwise>
		    		</c:choose>
		    		
		       		
		       		
					
		    	</c:when>
		    	<c:otherwise>
		    		<input type="text" class="input-text" readOnly name="prpLScheduleItemses[${status.index}].itemsContent" maxlength="20" value="${prpLScheduleItem.itemsContent}" datatype="*" />
		    	</c:otherwise>
			</c:choose>
		</td>
		<td>
			<c:choose>
		    	<c:when test="${empty prpLScheduleItem.id}">
		    		<button class="btn btn-primary btn-delBtn" value="${status.index}" onClick="delItem(this);" type="button">删&nbsp;&nbsp;除</button>
		    	</c:when>
		    	<%-- <c:when test="${prpLScheduleItem.itemType eq 1}">
		    		<button class="btn btn-disabled" disabled="disabled" id="without${status.index}" value="${status.index}" onClick="scheduleOrNot(0, ${status.index});" type="button">无需调度</button>
		    	</c:when> --%>
		    	<c:otherwise>
		    	<button class="btn btn-disabled"  type="button">删除</button>
				 <%-- 	<button class="btn btn-primary btn-withoutBtn" id="without${status.index}" value="${status.index}" onClick="scheduleOrNot(0, ${status.index});" type="button">无需调度</button>
				 	<button class="hide btn-primary btn-recoverBtn" id="recover${status.index}" value="${status.index}" onClick="scheduleOrNot(1, ${status.index});" type="button">恢复调度</button> --%>
		    	</c:otherwise>
			</c:choose>
		 	<!--scheduleIitems隐藏域 开始 -->
		 	<input name="prpLScheduleItemses[${status.index}].id" value="${prpLScheduleItem.id}" type="hidden" />
		 	<input name="prpLScheduleItemses[${status.index}].registNo" value="${prpLScheduleItem.registNo}" type="hidden" />
		 	<input name="prpLScheduleItemses[${status.index}].serialNo" value="${prpLScheduleItem.serialNo}" type="hidden" />
		    <input name="prpLScheduleItemses[${status.index}].itemsName" value="${prpLScheduleItem.itemsName}" type="hidden" />
		 	<input name="prpLScheduleItemses[${status.index}].itemsSourceId" value="${prpLScheduleItem.itemsSourceId}" type="hidden" />
		 	<input name="prpLScheduleItemses[${status.index}].scheduleStatus" value="${prpLScheduleItem.scheduleStatus}" type="hidden" />
		 	<%-- <input name="prpLScheduleItemses[${status.index}].scheduledComcode" value="${prpLScheduleItem.scheduledComcode}" type="hidden"  
		 	<c:if test="${prpLScheduleItem.itemType eq '1'}">id = "checkComCode"</c:if> 
		 	<c:if test="${prpLScheduleItem.itemType eq '4'}">id = "plossComCode"</c:if>
		 	/>
		 	<input name="prpLScheduleItemses[${status.index}].scheduledUsercode" value="${prpLScheduleItem.scheduledUsercode}" type="hidden" 
		 	<c:if test="${prpLScheduleItem.itemType eq '1'}">id = "checkUserCode"</c:if> 
		 	<c:if test="${prpLScheduleItem.itemType eq '4'}">id = "plossUserCode"</c:if>
		 	/> --%>
		 	<input name="prpLScheduleItemses[${status.index}].createUser" value="${prpLScheduleItem.createUser}" type="hidden" />
		 	<input name="prpLScheduleItemses[${status.index}].createTime" value='<fmt:formatDate value="${prpLScheduleItem.createTime}" type="date" />' type="hidden" />
		 	<!--scheduleIitems隐藏域 结束 -->
		</td>
	</tr>
</c:forEach>
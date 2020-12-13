<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<c:forEach var="prpLScheduleDefLoss" items="${prpLScheduleDefLosses}" varStatus="status">
		<tr class="text-c">
			<td>
			    <c:choose>
			    	<c:when test="${checkSubmited eq 1}">
			    		<input name="checkCode" checked="checked" group="ids" dLossId="${prpLScheduleDefLoss.id+defLossSize}" disabled type="checkbox" value="${defLossSize}" />
			    	</c:when>
			    	<c:otherwise>
			    	    <input name="checkCode" checked="checked" group="ids" dLossId="${prpLScheduleDefLoss.id}" onclick="changeScheduleFlag(this)" type="checkbox" value="${status.index}" />
			    	</c:otherwise>
				</c:choose>
			</td>
			<td>
				<c:choose>
			    	<c:when test="${checkSubmited eq 1}">
			    	    <%-- <app:codeSelect codeType="null" dataSource="${defLossTypeMap}"  type="select" clazz="must" value="" name="prpLScheduleDefLosses[${status.index+defLossSize}].deflossType"/> --%>
			    		<app:codeSelect codeType="DefLossType" onchange="defLossTypeChange(this)"  name="prpLScheduleDefLosses[${status.index+defLossSize}].deflossType" type="select" clazz="must" value="${prpLScheduleDefLoss.deflossType}" /> 
			    	</c:when>
			    	<c:otherwise>
			    		<app:codeSelect codeType="DefLossType" type="select" clazz="must" value="${prpLScheduleDefLoss.deflossType}" />
			    		<input type="hidden"  name="prpLScheduleDefLosses[${status.index}].deflossType" value="${prpLScheduleDefLoss.deflossType}" />
			    	</c:otherwise>
				</c:choose>
			</td>
			<td>
				<c:choose>
			    	<c:when test="${checkSubmited eq 1}">
			    	    <div id="itemTypeDiv${status.index+defLossSize}"><app:codeSelect codeType="null" dataSource="${itemTypeMap}" onchange='lossitemTypeChange(this)' type="select" clazz="must" value="" name="prpLScheduleDefLosses[${status.index+defLossSize}].lossitemType"/></div>
			    		<%-- <app:codeSelect codeType="DefLossItemType" name="prpLScheduleDefLosses[${status.index+defLossSize}].lossitemType" type="select" clazz="must" value="${prpLScheduleDefLoss.lossitemType}" /> --%>
			    	</c:when>
			    	<c:otherwise>
			    		<app:codeSelect codeType="DefLossItemType" type="select" clazz="must" value="${prpLScheduleDefLoss.lossitemType}" />
			    		<input type="hidden" name="prpLScheduleDefLosses[${status.index}].lossitemType" value="${prpLScheduleDefLoss.lossitemType}" />
			    	</c:otherwise>
				</c:choose>
			</td>
			<td>
				<c:choose>
			    	<c:when test="${checkSubmited eq 1}">
			    	<div id="itemsContentDiv${status.index+defLossSize}">
			    		<input type="text" errormsg="请输入正确车牌号" datatype="carLicenseNo" class="input-text" maxlength='50'   name="prpLScheduleDefLosses[${status.index+defLossSize}].itemsContent" value="${prpLScheduleDefLoss.itemsContent}" />
			    	 </div>
			    	</c:when>
			    	<c:otherwise>
			    		<input type="text" readonly class="input-text" name="prpLScheduleDefLosses[${status.index}].itemsContent" value="${prpLScheduleDefLoss.itemsContent}" />
			    	</c:otherwise>
				</c:choose>
			</td>
			<td>
			    <c:choose>
			    	<c:when test="${checkSubmited eq 1}">
                        <input type="text" class="input-text" name="prpLScheduleDefLosses[${status.index+defLossSize}].deflossRemark" maxlength='50' value="${prpLScheduleDefLoss.deflossRemark}" />
			    	</c:when>
			    	<c:otherwise>
			    		<input type="text" class="input-text" name="prpLScheduleDefLosses[${status.index}].deflossRemark" maxlength='50' value="${prpLScheduleDefLoss.deflossRemark}" />
			    	</c:otherwise>
				</c:choose>
			</td>
			<td>
				<c:choose>
			    	<c:when test="${checkSubmited eq 1}">
			 			<input class="btn btn-zd" href="javascript:;" onClick="delDefLoss(this)" type="button" value="删除" />
			    	</c:when>
			    	<c:otherwise>
			    		<input class="btn btn-zd btn-disabled" disabled="disabled" href="javascript:;" onClick="delDefLoss(this)" type="button" value="删除" />
			    	</c:otherwise>
				</c:choose>
			 	<!--scheduleIitems隐藏域 开始 -->
			 	<c:choose>
			    	<c:when test="${checkSubmited eq 1}">
			 			<input name="prpLScheduleDefLosses[${status.index+defLossSize}].scheduleFlag"  value="1" type="hidden" /><!--scheduleFlag 默认是1 被调度 0为不调度 -->
			    	</c:when>
			    	<c:otherwise>
			    		<input name="prpLScheduleDefLosses[${status.index}].id" value="${prpLScheduleDefLoss.id}" type="hidden" />
					 	<input name="prpLScheduleDefLosses[${status.index}].scheduleFlag" id="scheduleFlag${status.index}" value="1" type="hidden" /><!--scheduleFlag 默认是1 被调度 0为不调度 -->
					 	<input name="prpLScheduleDefLosses[${status.index}].registNo" value="${prpLScheduleDefLoss.registNo}" type="hidden" />
					 	<input name="prpLScheduleDefLosses[${status.index}].serialNo" value="${prpLScheduleDefLoss.serialNo}" type="hidden" />
					 	<input name="prpLScheduleDefLosses[${status.index}].itemsName" value="${prpLScheduleDefLoss.itemsName}" type="hidden" />
					 	<input name="prpLScheduleDefLosses[${status.index}].scheduleStatus" value="${prpLScheduleDefLoss.scheduleStatus}" type="hidden" />
			    	</c:otherwise>
				</c:choose>
			 	<!--scheduleIitems隐藏域 结束 -->
			</td>
			
		</tr>
</c:forEach>
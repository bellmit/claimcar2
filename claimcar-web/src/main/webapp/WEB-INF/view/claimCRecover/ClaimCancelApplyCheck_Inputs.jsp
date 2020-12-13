<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">立案注销/拒赔恢复原因</div>
	<div class="formtable">
		<div class="row cl">
			<div class="col-12">
				<textarea type="textarea" class="textarea" name="cancelCode">${prpLcancelTraceVo.cancelCode } </textarea>
				<font class="must">*</font>
			</div>
		</div>
	</div>
</div>

<%-- <c:if test="">审核退回意见输入框 -->--%>
<c:if test="${handlerStatus!=3 }">
<div class="table_wrap">
	<div class="table_title f14">审核/退回原因</div>
	<div class="formtable">
		<div class="row cl">
			<div class="col-12">
			<c:if test="${(handlerStatus eq 2)}">
				<textarea type="textarea" class="textarea" name="description" id="description">${prpLcancelTraceVo.aandelCode } </textarea>
			</c:if>
			
			<c:if test="${handlerStatus!='2' }">
				<textarea type="textarea" class="textarea" name="description" id="description"></textarea>
			</c:if>
				<font class="must">*</font>
			</div>
		</div>
	</div>
</div>
</c:if>
<div class="table_wrap">
	<div class="table_title f14">审核/退回意见列表</div>
	<div class="table_cont">
		<table class="table table-bordered table-bg">
				<thead class="text-c">
					<tr>
						<th>序号</th>
						<th>时间</th>
						<th>操作员</th>
						<th>岗位</th>
						<th>内容</th>
					</tr>
				</thead>
			<tbody class="text-c">
				<c:forEach var="prpLrejectClaimTextVo" items="${prpLrejectClaimTextVos}" varStatus="index">
					<tr class="text-c" id="claimTextTr">
						<td>${index.index+1}</td>
						<c:set var="operateDate">
								<fmt:formatDate value="${prpLrejectClaimTextVo.operateDate}"/>
							</c:set>
						<td>${operateDate }</td>
						 	<c:set var="operatorName">
					 	 	<app:codetrans codeType="UserCode" codeCode="${prpLrejectClaimTextVo.operatorName }"/> 
					 	 	</c:set>
						<td>${operatorName }</td>
						<td>${prpLrejectClaimTextVo.stationName }</td>
						<td>${prpLrejectClaimTextVo.description }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
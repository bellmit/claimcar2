<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 意见列表 开始 -->
<div class="table_wrap">
	<div class="table_title f14">意见列表</div>
	<div class="table_cont table_list ">
		<table class="table table-border table-hover">
			<thead>
				<tr class="text-c">
					<th>审核人员</th>
					<th>审核时间</th>
					<th>退票审核意见</th>
				</tr>
			</thead>
			<tbody>
					<tr class="text-c">
					   
						<td><app:codetrans codeType="UserCode" codeCode="${updateUser}"/></td>
						<td>
							<fmt:formatDate  value="${updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						 <td>${auditOpinion}</td>
					</tr>
			</tbody>
		</table>
	</div>
</div>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>垫付提交</title>
</head>
<body>
	<div class="table_wrap">
		<input type="hidden" name="padId" value="${padId}">
		<input type="hidden" name="flowTaskId" value="${flowTaskId}">
		<div class="table_cont table_list">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width: 35%">当前任务环节</th>
						<th style="width: 30%">提交路劲选择</th>
						<th style="width: 35%">指定处理人</th>
					</tr>
				</thead>
				<tbody>
					<tr class="text-c">
						<td>垫付申请</td>
						<!-- <td></td> -->
						<td><input type="hidden" id="level" value="${level}">
							<c:if test="${level eq '1'}">
								垫付核赔一级
							</c:if> <c:if test="${level ne '1'}">
								垫付核赔${level}级
							</c:if></td>
						<td class="text-c">
							<input type="text" class="input-text text-c" value="${assUserVo.userName}" 
							readonly="readonly" style="width: 50%"/>
						</td>
					</tr>
				</tbody>
			</table><br /><br /> 
			<input type="button" class="btn btn-primary text-c" style="margin-left: 40%;"
				onclick="submit_padPay('${flowTaskId}','${padId}','${level}','${assUserVo.userCode}','${assUserVo.comCode}')" value="提交">
			<input type="button" class="btn btn-disabled btn-kk" onclick="cancelPadPay()" value="取消">
			<br />
			<br />
		</div>
	</div>
	<script type="text/javascript" src="/claimcar/js/padPay/padPay.js"></script>
</body>
</html>
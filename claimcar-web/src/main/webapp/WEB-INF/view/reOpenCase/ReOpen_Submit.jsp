<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>重开赔案提交</title>
</head>
<body>
	<div class="table_wrap">
		<input type="hidden" name="padId" value="${padId}"> <input
			type="hidden" name="flowTaskId" value="${flowTaskId}">
		<div class="table_cont table_list">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width: 30%">当前环节</th>
						<th style="width: 30%">下个环节</th>
					</tr>
				</thead>
				<tbody>
					<tr class="text-c">
						<td>重开赔案</td>
						<!-- <td></td> -->
						<td>重开赔案初一级审核</td>
						<!-- <td></td> -->
					</tr>
				</tbody>
			</table>
			<br />
			<div class="layui-layer-btn">
			    <a class="layui-layer-btn0" id="submitNextNode">提交任务</a><br />
			</div>
			<br/>
			<br/>
		</div>
	</div>
	
</body>
</html>
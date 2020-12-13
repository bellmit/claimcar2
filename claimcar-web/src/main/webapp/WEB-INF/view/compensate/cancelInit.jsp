<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>注销</title>
		<style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
			</style>
	</head>
	<body>
		<div class="table_wrap">
			<div class="table_title f14">车辆定损</div>
			<div class="table_cont " style="overflow-x: scroll;">
				<table class="table table-border" border="1" id="hisTable">
					<thead class="text-c">
						<th>勾选</th>
						<th>损失方</th>
						<th>车牌号</th>
						<th>定损金额</th>
					</thead>
					<c:forEach var="carVo" items="${carVos }" varStatus="rowStatus">
						<tr >
							<td><input type='checkbox' name="carVos" value="${carVo.id}" /></td>
							<td>${carVo.remark}</td>
							<td>${carVo.licenseNo}</td>
							<td>${carVo.sumVeriLossFee}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="table_wrap">
			<div class="table_title f14">财产定损</div>
			<div class="table_cont " style="overflow-x: scroll;">
				<table class="table table-border" border="1" id="hisTable">
					<thead class="text-c">
						<th>勾选</th>
						<th>损失方</th>
						<th>核损金额</th>
					</thead>
					<c:forEach var="vo" items="${propVos}" varStatus="rowStatus">
						<tr >
							<td><input name='propVos'  type='checkbox' value="${vo.id}" /></td>
							<td>${vo.remark}</td>
							<td>${vo.sumVeriLoss}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="table_wrap">
			<div class="table_title f14">人伤跟踪</div>
			<div class="table_cont " style="overflow-x: scroll;">
				<table class="table table-border" border="1" id="hisTable">
					<thead class="text-c">
						<th>勾选</th>
						<th>损失方</th>
					</thead>
					<c:forEach  var="vo" items="${persInjuredVo}" varStatus="rowStatus">
						<tr >
							<td><input name='persInjuredVo' type='checkbox'  value="${vo.id}" /></td>
							<td>${vo.remark}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="table_wrap">
			<div class="table_title f14">意见</div>
				<div class="formtable">
					<div class="col-12">
						<textarea class="textarea" name="remarks" ignore="ignore">退回定损</textarea>
					</div>
			</div>
		</div>
		<div class="text-c">
        <br/>
        <a class="btn btn-primary " onclick="submitDloss()">确定</a>
    </div>
    <script type="text/javascript" src="/claimcar/js/compensate/compensateEdit.js"></script>
	</body>
</html>
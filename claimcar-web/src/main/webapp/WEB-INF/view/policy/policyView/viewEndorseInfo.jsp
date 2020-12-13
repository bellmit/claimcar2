<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>批改纪录</title>
</head>
<body>
	<div class="table_wrap">
		<%-- <input type="hidden" id="checkMainId" value="${checkId}"> --%>
		<input type="hidden" id="prpUrl" value="${prpUrl}"/>
		<div class="table_cont table_list">
			<table class="table table-border table-hover">
				<thead>
					<tr class="text-c">
						<th style="width:10%">序号</th>
						<th style="width:10%">保单号</th>
						<th style="width:10%">批单号</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="infoVo" items="${infoVoList}" varStatus="status">
						<tr class="text-c">
							<td>${infoVo.serialNo}</td>
							<td>${infoVo.policyNo}</td>
							<td>${infoVo.endorseNo}</td>
							<td><a href="" onclick="openEndorseInfo('${hskey}','${infoVo.endorseNo}')">查看</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<script type="text/javascript">
		function openEndorseInfo(hskey,endorseNo){
			
			var prpUrl=$("#prpUrl").val();
			var strUrl = prpUrl+"/prpp/view?hskey="+hskey+"&endorseNo="+endorseNo;
			/* prpUrl + "/prpc/copyPrintFormat?policyNo="+policyNo; */
		//	var url = "/claimcar/policyView/openEndorsePage.do?endorseNo=" + endorseNo;
			window.open(strUrl,"批单信息");
			 location.reload();
			
		};
	</script>
</body>
</html>
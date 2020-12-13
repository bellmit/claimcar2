<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>批单打印</title>
</head>
<body>
<div class="formtable">
		<div class="table_con">
			<table class="table table-bordered table-bg" border="1">
				<thead class="text-c">
					<tr>
						<th style="background-color:#D3D3D3">批单号</th>
						<input type="hidden" id="prpUrl" value="${prpUrl }" />
					</tr>
				
				<c:forEach var="vo" items="${endorseNoList}" varStatus="status">
	             
					<tr>
					<td><a class="btn btn-link" id="checkRegistMsg" onclick="certifyPrint('${vo}','${riskCode }')">${vo}</a></td>
					</tr>
					<tr>
					<br color="black">
					</tr>
					</c:forEach>
				</thead>
			</table>
		</div>
	</div>
<script type="text/javascript">
function certifyPrint(endorseNo,riskCode){
	var printType="copyPrint";
	var prpUrl =$("#prpUrl").val();
	var url= prpUrl +"/prpp/printEndorseFormat.ajax?endorseNo="+endorseNo+"&riskCode="+riskCode+"&printType="+printType;
    openTaskEditWin("批单打印", url);
}
</script>
</body>
</html>
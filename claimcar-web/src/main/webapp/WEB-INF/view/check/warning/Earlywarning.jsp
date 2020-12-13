<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>山东预警推送</title>
</head>
<body>
	<div class="tabbox">
		<div id="tab-system" class="HuiTab">
			<div class="tabCon clearfix">
				<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0">
					<thead class="text-c">
						<tr>
							<th>理赔编号</th>
							<th>理赔阶段</th>
							<th>预警信息</th>
						</tr>
					</thead>
					<tbody class="text-c">
						<c:forEach var="prpLwarnInfoVo" items="${wanDefLossVoList}"
							varStatus="wanStatus">
							<tr>
								<td>${prpLwarnInfoVo.claimsequenceNo}</td>
								<td>${prpLwarnInfoVo.warnstageName}</td>
								<td><input type="button" class="btn btn-secondary"
									onclick="showdetails('${prpLwarnInfoVo.claimsequenceNo}','${prpLwarnInfoVo.warnstageCode}')"
									value="详情"></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
				
	</div>
	<script type="text/javascript">
		function showdetails(claimsequenceNo, warnstageCode) {
			var index;
			index = layer
					.open({
						type : 2,
						title : '山东预警推送详情',
						//closeBtn : 2,
						//shadeClose : true,
						scrollbar : false,
						area : [ '90%', '80%' ],
						content : "/claimcar/warnAction/viewWarnInfo.do?claimsequenceNo="+claimsequenceNo+"&warnstageCode="+warnstageCode,
						scrollbar : true
					});

		}
		
	</script>

</body>
</html>
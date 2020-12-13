<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>单证信息列表</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>
  <div class="table_cont">
  <div class="table_wrap">
     <div class="table_title f14">单证明细列表</div>
				
			<table class="table table-border" border="1" id="">
				<thead class="text-c">
					<th>序号</th>
					<th>理算编码</th>
					<th>其他费用</th>
					<th>核赔意见</th>
					<th>赔偿金额</th>
				</thead>
				<tbody class="text-c" id="">
					<c:forEach var="subrogation" items="${resultList.subrogationUnderWriteDataVo.adjustmentDataList }" varStatus="status">
						<tr>
							<td>${status.index+1 }</td>
							<td>${subrogation.adjustmentCode }</td>
							<td>${subrogation.otherFee }</td>
							<td>${subrogation.underWriteDes }</td>
							<td>${subrogation.claimAmount }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>	
		</div>
		

	</div>
</body>
</html>

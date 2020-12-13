<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			<div class="table_wrap">
					<div class="table_title f14">配件复检结果信息</div>
					<div class="table_cont" >
						<table class="table table-border" border="1" id="componentTable">
							<thead class="text-c">
								<tr>
									<th>配件名称</th>
									<th>配件编码</th>
									<th>配件id</th>
									<th>品质相符</th>
									<th>复检人</th>
									<th>复检时间</th>
									<th>鉴定结果</th>
									<th>鉴定结果类型</th>
									<th>配件估损单价</th>
									<th>鉴定价格</th>
									<th>差价</th>
									<th>复勘说明</th>
								</tr>
							</thead>
							<tbody class="text-c" id="componentBody">
								<c:forEach var="prpLDlchkInfo" items="${prpLDlchkInfos }" varStatus="status">
									<tr>
										<td>${prpLDlchkInfo.partName }</td>
										<td>${prpLDlchkInfo.partNo }</td>
										<td>${prpLDlchkInfo.partId }</td>
										<td>${prpLDlchkInfo.original }</td>
										<td>${prpLDlchkInfo.operator }</td>
										<td>${prpLDlchkInfo.operateDate }</td>
										<td>${prpLDlchkInfo.identifyResult }</td>
										<td>${prpLDlchkInfo.identifyResultType }</td>
										<td>${prpLDlchkInfo.unitPrice }</td>
										<td>${prpLDlchkInfo.appraisalPrice }</td>
										<td>${prpLDlchkInfo.decreaseAmount }</td>
										<td>${prpLDlchkInfo.remark }</td>
									</tr>
								</c:forEach>
							 </tbody>
						</table>
				   </div>
			</div>
					
<script type="text/javascript">
function showDetailedDlhkInfo(topActualId){
	var url="/claimcar/defloss/showDetailedDlhkInfo.do?topActualId="+topActualId;
	layer.open({
		title : '阳杰复检详细信息',
		type : 2,
		area : [ '100%', '100%' ],
		fix : true, // 固定
		maxmin : true,
		content : [ url, "yes" ],
		end : function() {
			
		}
	});
}
</script>

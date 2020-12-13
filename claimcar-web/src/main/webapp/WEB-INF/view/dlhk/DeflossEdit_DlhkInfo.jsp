<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			<div class="table_wrap">
					<div class="table_title f14">基本信息</div>
					<div class="table_cont" >
						<table class="table table-border" border="1" id="componentTable">
							<thead class="text-c">
								<tr>
									<th>报案号</th>
									<th>车牌号</th>
									<th>案卷</th>
									<th>估损单编号</th>
								</tr>
							</thead>
							<tbody class="text-c" id="componentBody">
								<c:forEach var="prpLDlhkMainVo" items="${prpLDlhkMainVos }" varStatus="status">
									<tr>
										<td>${prpLDlhkMainVo.notificationNo }</td>
										<td>${prpLDlhkMainVo.licenseNo }</td>
										<td><a onclick="showDetailedDlhkInfo('${prpLDlhkMainVo.topActualId }')">${prpLDlhkMainVo.topActualId }</a></td> 
										<td>${prpLDlhkMainVo.assessNo }</td>
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

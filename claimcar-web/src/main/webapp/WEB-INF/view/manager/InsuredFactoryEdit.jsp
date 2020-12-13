<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form  id="insuredForm" name="fm"  class="form-horizontal" role="form"> 
		<div class="table_wrap">
			<div class="table_title f14">被保险人维护</div>
			<div calss="table_cont table_list">
				<table class="table table-border table-hover">
					<thead>
						<tr class="text-c">
						    <th	width="10%">
						     <button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont-add" onclick="addInsuredItem('${agentCode }','${agentId }')"></button>
						     </th>
							<th >被保险人名称</th>
							<th>被保险人代码</th>
						</tr>
					</thead>
					<input type="hidden" name="agentCode" value='${agentCode }' id='agentCode'/>
					<input type="hidden" name="agentId" value='${agentId }' id='agentId'/>
				<tbody id='InsuredTbody'>
					<input type='hidden' id='InsuredSize' value='${fn:length(InsuredFactoryList) }' />
					<c:forEach var="insuredFactoryVo" items="${InsuredFactoryList }" varStatus="insuredStatus">
						<c:set var="insuredId" value="${insuredStatus.index }" />
						<%@include file="InsuredFactoryEdit_RedictItem.jsp"%>
					</c:forEach>

				</tbody>
			</table>
			</div>
		</div>
		</form>
<!--撑开页面  开始  -->
			<div class="row cl">
				<div class="form_input col-6"></div>
			</div>
			<div class="row cl">
				<div class="form_input col-6"></div>
			</div>
			<!-- 结束 -->
			<div class="btn-footer clearfix">
				<button type="button" class="btn btn-primary fl" style="margin-left: 40%" onclick="save()">保存</button	>
				<a class="btn btn-primary fl ml-5" onclick="closeAlert()">关闭</a>
			</div><br>
			<div class="row cl">
				<div class="form_input col-6"></div>
			</div>

			<script type="text/javascript" src="/claimcar/js/manage/RepairFactoryEdit.js"></script>
			<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
			<script type="text/javascript">

$(function(){
	var ajaxEdit = new AjaxEdit($('#insuredForm'));
	ajaxEdit.targetUrl = "/claimcar/manager/insuredFactorySava.ajax";
	ajaxEdit.afterSuccess = function(result) {
		layer.alert("保存成功！", function() {

			closeAlert();
		});
	};
	
	ajaxEdit.bindForm();
});
function save(){
	//layer.alert("save");
	$("#insuredForm").submit();
}

</script>
</body>
</html>
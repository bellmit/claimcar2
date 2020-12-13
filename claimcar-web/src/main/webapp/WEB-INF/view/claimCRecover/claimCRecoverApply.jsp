<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>立案注销/拒赔发起</title>
</head>
<body>
<div class="page_wrap">
		<div class="tabbox">
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
				        <c:if test="${!empty prpLcancelTraceVos}">
						<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
						<thead>
							<tr class="text-c">
								<th>选择</th>
								<th>立案号</th>
							</tr>
						</thead>
						<tbody class="text-c">
						<c:forEach var="prpLClaimVo" items="${prpLcancelTraceVos}" varStatus="status">
						  <tr>
						      <td><input type="radio" value="${prpLClaimVo.claimNo}" name="claimNo"></td>
						      <td>${prpLClaimVo.claimNo}</td>
						  </tr>
						</c:forEach>
						</tbody>
					</table>
					<div class="row text-c">
						<br/>
					</div>
					<div class="row text-c">
						<input class="btn btn-primary btn-outline" onclick="save()" id="submit" type="button" value="注销/拒赔恢复">
						<input class="btn btn-primary btn-outline" id="close" onclick="closeL()" type="button" value="关闭">
					</div>
					</c:if>
					<c:if test="${empty prpLcancelTraceVos}">
					 <div class="text-c">该案件不能注销拒赔恢复</div>
					</c:if>
					<input hidden="hidden" name="taskId" value="${taskId }" id="taskId"/>
				</div>
			</div>
		</div>
</div>
	<script type="text/javascript">
	function save(){
		var a = $('input:radio:checked').val(); 
		if(isBlank(a)){
			layer.msg("请选择立案号！");
			return ;
		}
		var taskId = $("#taskId").val(); 
		//var goUrl ="/claimcar/claimRecover/claimCancelInit.do?claimNo="+a+"&taskId="+taskId;
		//openTaskEditWin("立案注销拒赔恢复处理",goUrl);
		$.ajax({
			 url : '/claimcar/claimRecover/claimCancelInit.do?claimNo='+a+'&taskId='+taskId+'&workStatus=',
			 type : 'post', // 数据发送方式
			success : function(res) {//json是后端传过来的值
				reMsg = res.data;
				if(reMsg=="1"){
					layer.confirm('发起成功', {
						btn : [ '确定']
						}, function() {
							var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							parent.layer.close(index); //再执行关闭 
						});
				}else{
					layer.confirm('该案件不能发起', {
						btn : [ '确定']
						}, function() {
							var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
							parent.layer.close(index); //再执行关闭 
						});
				}
				},
			error : function() {
				layer.msg("获取地址数据异常");
			}
		});
	}
	
	function closeL(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭 
	}
	
	</script>
</body>
</html>

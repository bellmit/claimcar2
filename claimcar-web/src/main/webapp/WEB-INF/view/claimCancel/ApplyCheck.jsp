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
				        <c:if test="${!empty prpLClaimVoList}">
						<table class="table table-border table-hover data-table" cellpadding="0" cellspacing="0" id="resultDataTable">
						<thead>
							<tr class="text-c">
								<th>选择</th>
								<th>立案号</th>
								<th>保单号</th>
								<th>被保险人</th>
								<th>起保日期</th>
								<th>终保日期</th>
								<th>立案时间</th>
								<th>交强/商业</th>
							</tr>
						</thead>
						<tbody class="text-c">
						<c:forEach var="prpLClaimVo" items="${prpLClaimVoList}" varStatus="status">
						  <tr>
						      <td><input type="radio" value="${prpLClaimVo.claimNo}" name="claimNo"></td>
						      <td>${prpLClaimVo.claimNo}</td>
						      <td>${prpLClaimVo.policyNo}</td>
						      <td>${prpLClaimVo.insuredName}</td>
						      <c:set var="startDate">
						      	<fmt:formatDate value="${prpLClaimVo.startDate}"/>
						      </c:set>
						      <td>${startDate}</td>
						      <c:set var="endDate">
						      	<fmt:formatDate value="${prpLClaimVo.endDate}"/>
						      </c:set>
						      <td>${endDate}</td>
						      <c:set var="claimTime">
						      	<fmt:formatDate value="${prpLClaimVo.claimTime}"/>
						      </c:set>
						      <td>${claimTime}</td>
						      <td>${prpLClaimVo.riskCode}</td>
						  </tr>
						</c:forEach>
						</tbody>
					</table>
					<div class="row text-c">
						<br/>
					</div>
					<div class="row text-c">
						<input class="btn btn-primary btn-outline" onclick="save()" id="submit" type="button" value="注销/拒赔">
						<input class="btn btn-primary btn-outline" id="close" onclick="closeL()" type="button" value="关闭">
					</div>
					</c:if>
					<c:if test="${empty prpLClaimVoList}">
					 <div class="text-c">该案件没有立案</div>
					</c:if>
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
		var goUrl ="/claimcar/claim/claimCancelInit.do?claimNo="+a;
		openTaskEditWin("立案注销拒赔处理",goUrl);
		/* $.ajax({
			url : "/claimcar/claim/claimCancelInit.do?registNo="+a,
			success : function(data) {
				
			}
		}); */
	}
	
	function closeL(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index); //再执行关闭 
	}
	
	</script>
</body>
</html>

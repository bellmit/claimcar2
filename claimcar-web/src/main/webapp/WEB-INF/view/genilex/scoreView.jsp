<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>反欺诈评分信息</title>
</head>
<body>
	<div class="table_wrap">
	<div class="table_cont table_list">
	
		<table class="table table-border table-hover">
		<div class="row  cl" align="center">
		<p>
				<label class="form_label col-4">报案号：${registNo}</label>
				
				<label class="form_label col-4">标的车牌号：${carLicenseNo}</label>
				
				<label class="form_label col-4">被保险人：${insuredName }</label>
				
				
		</div>
		<br/>
		<hr style="height:1px;color:black;"/>
			<thead>
				<tr class="text-c">
					<th class="text-c">评分环节</th>
					<th class="text-c">评分日期</th>
					<th class="text-c">是否人伤</th>
					<th class="text-c">是否小额</th>
					<th class="text-c">反欺诈评分</th>
					<th class="text-c">辅助描述</th>
					<th class="text-c">规则描述</th>
					<th class="text-c">规则列表</th>
				</tr>
			</thead>
				<tbody id="scoreTbody" class="text-c">
					
					<c:forEach var="prpLFraudScoreVo" items="${scoreVos}" varStatus="status">
					<tr class="text-c">
					        <c:choose>
					        <c:when test="${prpLFraudScoreVo.scoreNode eq 'C001' }">
					        <td>报案</td>
					        </c:when>
					        <c:when test="${prpLFraudScoreVo.scoreNode eq 'C002' }">
					        <td>查勘</td>
					        </c:when>
					        <c:otherwise>
					        <td>定损</td>
					        </c:otherwise>
					        </c:choose>
					       <%--  <app:date date='${prpLFraudScoreVo.fraudScore}'/> --%>
							<td><fmt:formatDate  value="${prpLFraudScoreVo.scoreDate}" pattern="yyyy-MM-dd"/></td>
							<c:choose>
								<c:when test="${prpLFraudScoreVo.isInjured eq 'true'}">
									<td>是</td>
								</c:when>
								<c:when test="${prpLFraudScoreVo.isInjured eq 'false'}">
									<td>否</td>
								</c:when>
								<c:otherwise>
									<td></td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${prpLFraudScoreVo.isSmallAmount eq 'true'}">
									<td>是</td>
								</c:when>
								<c:when test="${prpLFraudScoreVo.isSmallAmount eq 'false'}">
									<td>否</td>
								</c:when>
								<c:otherwise>
									<td></td>
								</c:otherwise>
							</c:choose>
							<td>${prpLFraudScoreVo.fraudScore}</td>
							<td><input class="text-c" name="prpLFraudScoreVo.reasonDesc" style='width:80%;height:30px;border:none' readonly="true" onmouseover="showReasonDescText(this)" value="${prpLFraudScoreVo.reasonDesc}" id="${prpLFraudScoreVo.reasonDesc}"/></td>
							<td><input class="text-c" name="prpLFraudScoreVo.ruleDesc" style='width:80%;height:30px;border:none' readonly="true" onmouseover="showRuleDescText(this)" value="${prpLFraudScoreVo.ruleDesc}" id="${prpLFraudScoreVo.ruleDesc}"/></td>
							<td><a target='_blank' onclick="ruleShow('${prpLFraudScoreVo.fraudScoreID}')">查看</a></td>
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
	</div>
</div>

	<script type="text/javascript">
	$(function(){
		$("input[name^='prpLFraudScoreVo.']").each(function(){
			var reasonDescContext=$(this).val();
			if(reasonDescContext == null || reasonDescContext == "undefined" || reasonDescContext == "" || reasonDescContext == "null"){
				
			}else{
				var contextValue="";
				if(reasonDescContext.length>10){
					contextValue=reasonDescContext.substring(0,10)+'......';
				}else{
					contextValue=reasonDescContext;
				}
				$(this).val(contextValue);
			}
			
		});
		
	});
		
		function ruleShow(id){
			
			var params = "?fraudScoreID=" + id;
	    	layer.open({
	    		type: 2,
	    		title: '规则列表',
	    		shadeClose : true,
	    		scrollbar : false,
	    		area : [ '50%', '50%' ],
	    		content : [ "/claimcar/scoreAction/ruleShow.do" + params]
	    	});
			
		}
		
		function showReasonDescText(obj){
		      var showTextcontext=$(obj).attr("id");
		      if(showTextcontext.length<=9){
		    	  
		         return false;
		      }
		       $(obj).qtip({
		           content:showTextcontext,
		          });
		    	obj.onmouseover = null;
		    	$(obj).trigger('mouseover');//手动触发mouseover事件
		    	
		    }
		
		function showRuleDescText(obj){
		      var showTextcontext=$(obj).attr("id");
		      if(showTextcontext.length<=9){
		    	  
		         return false;
		      }
		       $(obj).qtip({
		           content:showTextcontext,
		          });
		    	obj.onmouseover = null;
		    	$(obj).trigger('mouseover');//手动触发mouseover事件
		    	
		    }
	</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!--标签页 开始-->
<div class="tabbox">
	<div id="tab-system" class="HuiTab">
		<div class="tabCon clearfix">
			<table class="table table-border table-hover data-table" style="table-layout:fixed" cellpadding="0" cellspacing="0" id="resultDataTable">
				<thead>
					<tr class="text-c">
						<th>发送节点</th>
						<th>电话号码</th>
						<th width="50%" class="text-c">短信内容</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody class="text-c">
					<c:forEach var="smsMessage" items="${smsMessageList}" varStatus="status">
						<tr>
							<td><app:codetrans codeType="sendNodecode" codeCode="${smsMessage.sendNodecode}"/></td>
							<td>${smsMessage.phoneCode }</td>
							<td style="overflow:hidden;text-overflow:ellipsis;word-break:keep-all;white-space:nowrap;" title="${smsMessage.sendText }">${smsMessage.sendText }</td>
							<td><input onclick="replacement('${smsMessage.misId }')" type="button" class="btn btn-primary fl" value="补发" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<!--table   结束-->	
		</div>
	</div>
</div>
<!--标签页 结束-->
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript">
	//短信记录
	function replacement(misId){
		var params = "?misId=" + misId;
		var url = "/claimcar/schedule/checkMsg.do";
		var index = layer.open({
			type : 2,
			title : "补发短信",
			area : [ '850px', '350px' ],
			maxmin : true, // 开启最大化最小化按钮
			content : url + params
		});
	}
</script>


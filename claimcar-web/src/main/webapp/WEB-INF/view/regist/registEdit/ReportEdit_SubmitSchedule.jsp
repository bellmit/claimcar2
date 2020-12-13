<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
	<body>
		<div>
			<div class="table_wrap">
				<div class="table_cont ">
					 <table class="table table-striped table-border table-hover">
					 		<thead>
					 			<tr class="text-c">
					 				<th>当前环节</th>
					 				<th>下一环节</th>
					 				<th>业务号</th>
					 				<th>处理人员</th>
					 				<th>操作</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 			<tr class="text-c">
					 				<td>报案</td>
					 				<td>调度</td>
					 				<td>
						 				${registNo}
						 				<input type="hidden" id="registNo" value="${registNo}" />
					 				</td>
					 				<td>
					 					<select id="assignUser">
					 						<option value="${userCode}">${userCode}</option>
					 					</select>
					 					<input type="hidden" id="assignCom" value="${comCode}" />
					 				</td>
					 				<td><input id="submit" onclick="submit()" class="btn btn-primary" type="button" value="提交调度" /></td>
					 			</tr>
					 		</tbody>
					 </table>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			function submit() {
				$("#submit").attr("disabled", true);
				$("#submit").removeClass("btn-primary");
				$("#submit").addClass("btn-disabled");
				var url = "/claimcar/regist/isSelfHelpSurVey.ajax";
				$.ajax({ 
			        type : "post", 
			        url : url, 
			        data :{
			        	"registNo" : $("#registNo").val()
			        },
			        async : false, 
			        success : function(result){ 
			        	if(result != null && result != ""){
			        		if(result.status == "200"){
			        			if(result.data != null && result.data != ""){
			        				layer.msg(result.data);
			        			}
			        		}
						}	
			        	location.href = "/claimcar/regist/submit.do?registNo="+ $("#registNo").val()+"&assignUser="+$("#assignUser").val()+"&assignCom="+$("#assignCom").val() ;
			        } 
			     });
			}
		</script>
	</body>
</html>
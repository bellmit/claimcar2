<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>解锁理算节点</title>
</head>
<body>
<div class="page_wrap" id="fu">
	<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row mb-3 cl">
							<label for=" " class="form_label col-1">报案号:</label>
							<div class="form_input col-3">
								<input id="registNo" type="text" class="input-text"/>
								<font color="red">*</font>
							</div>
							
							<label for=" " class="form_label col-1">处理人员编码:</label>
							<div class="form_input col-3">
								<input id="handler1Code" type="text" class="input-text"/>
								<font color="red">*</font>
							</div>
							
							<label for=" " class="form_label col-1">理算类型:</label>
							<div class="form_input col-3">
					 			<span class="select-box">
									<select id="nodeCode" class="select">
										<option value=""></option>
										<option value="1">商业</option>
										<option value="2">交强</option>
									</select>
									<font color="red">*</font>
								</span>
					 		</div>
						</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							   <button class="btn btn-primary btn-outline btn-search" id="search" type="button" disabled>改派</button>
					
						</div>
					</div>
				</form>
			</div>
		</div>
		</div>
	<script type="text/javascript">
	$("#search").click(function(){
		var registNo=$("#registNo").val();//报案号
		var handler1Code=$("#handler1Code").val();//处理人员编码
		var nodeCode=$("#nodeCode").val();//理算类型
		if(isBlank(registNo)){
			layer.msg("报案号不允许为空！");
			return false;
		}
		if(isBlank(handler1Code)){
			layer.msg("处理人员编码不允许为空！");
			return false;
		}
		if(isBlank(nodeCode)){
			layer.msg("理算类型不允许为空！");
			return false;
		}
		var params = {
				"registNo" : registNo,
				"handler1Code" : handler1Code,
				"nodeCode" : nodeCode
			};
		$.ajax({
			url : "/claimcar/compensate/updateHandler.ajax", // 后台处理程序
			type : 'post', // 数据发送方式
			dataType : 'json', // 接受数据格式
			data : params, // 要传递的数据
			async:false, 
			success : function(jsonData) {// 回调方法，可单独定义
				if(jsonData.status=="200"){
					layer.alert("操作成功");
					
				}else{
					var messageText = '操作失败：' + jsonData.statusText;
					layer.alert(messageText);
				}
			}
		});
	});
	
	</script>

</body>
</html>
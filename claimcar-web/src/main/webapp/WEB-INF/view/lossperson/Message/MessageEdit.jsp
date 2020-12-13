<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title></title>
</head>
<body>
	<div class="page_wrap">
		<!--查询条件 开始-->
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form">
					<div class="row cl">
							<label class="form-label col-1">发送模板:</label>
							<div class="formControls col-3">
							 	<label>
							 		<input type="radio" name="handleStatus" value="1" onclick="ModelA()">模板A
							 	</label>
							 	<label>
							 	<input type="radio" name="handleStatus" value="2" onclick="ModelB()">模板B
							 	</label>
							    <label>
							    	<input type="radio" name="handleStatus" value="3" onclick="ModelC()">模板C
							    </label>				
							</div>
						</div>
						<!--  添加-->
		<table width="100%" id="AddFile" border="0" align="center"
			cellpadding="5" cellspacing="1" class="common">
			<thead>
			</thead>
			<tbody id="CkindTbody">
			<div class="row mb-4 cl">
				<tr>
					<td class='input'>
					<div class="row  cl">
					 	 	<label class="form_label col-1">发送对象:</label>
					 	 	<div class="formControls col-3">
								<input type="text" class="input-text" id="createUser" name="createUser"/>
							</div>
					 </div> 
					 
					<div class="row cl"></div>
					 	<label class="form_label col-1">短信内容:</label>
						<div class="row cl">
						<textarea class="textarea h100" name="reasonDesc" id="reasonDesc"></textarea>
						</div>
					</td> 
				</tr>
			</div>
			</tbody>
		
		</table>
		<div class="row  cl">
					 	 	<label class="form_label col-1">你已经写了</label>
					 	 	<div class="formControls col-1">
								<input type="text" class="input-text" id="num" name=""/>
							</div>
							<label class="form_label col-1">个字符，</label>
							<label class="form_label col-1">本内容将</label>
					 	 	<div class="formControls col-1">
								<input type="text" class="input-text" id="mess" name=""/>
							</div>
							<label class="form_label col-1">条短信发送。</label>
					 	</div>
					 	
					 	<div class="row  cl">
					 	 	<label class="form_label col-2">是否延迟发送选项</label>
					 	 	<div class="formControls col-3">
								<input type="checkbox" />
							</div>
					 	</div>
					 	<div class="line"></div>
						<div class="row">
							<span class="col-offset-10 col-2">
								<input type="button" value="增加"  class="btn btn-primary btn-outline btn-search"  onclick="addCItemKind()">
							
							</span><br />
						</div>
					<div class="btn-footer clearfix text-c">
							<a class="btn btn-primary "  
								id="submit">发送</a> &nbsp;&nbsp;
						 <a class="btn btn-danger cl" id="close">关闭</a>
						</div>
					</form>
				</div>
			</div>

		</div>
	</div>
	
	<script type="text/javascript">
	$(document).ready(function(){ 
		var createUser = document.getElementsByName("createUser");
		$("#mess").val(createUser.length);
		
	});
	
	
	$("#submit").click(function() {
		var a = $("#form").serialize();
		var createUser = document.getElementsByName("createUser");
		$("#mess").val(createUser.length);
		var reasonDesc = document.getElementsByName("reasonDesc");
		var num=0;
			for ( var i = 0; i < reasonDesc.length; i++) {
				var files = reasonDesc[i];
			 num = num+files.value.length;
			}
			$("#num").val(num);
		/*  $.ajax({
             type: "POST",
             url: "/claimcar/survey/save.do",
             data:a,
             success: function(data){
            	 		alert("提交成功！");
                        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            			parent.layer.close(index); //再执行关闭 
            			
             }

         });   */
    });  
	
		$("#close").click(function() {
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index); //再执行关闭 
		});
		
		//添加导入行
		function addCItemKind() {
			var $tbody = $("#CkindTbody");
		/* 	var url = "/claimcar/message/addCItemKind.ajax";
			$.post(url, function(result) {
				$tbody.append(result);
			}); */
			
			 $.ajax({
		           type: "POST",
		           url: "/claimcar/message/addCItemKind.ajax",
		           success: function(result)
		           {
		        	   $tbody.append(result);
		        	   var createUser = document.getElementsByName("createUser");
		   			$("#mess").val(createUser.length);
		   			var reasonDesc = document.getElementsByName("reasonDesc");
		   			var num=0;
		   			for ( var i = 0; i < reasonDesc.length; i++) {
		   				var files = reasonDesc[i];
		   			 num = num+files.value.length;
		   			
		   			}
		   			$("#num").val(num);
		   		/*  var length = $('#reasonDesc').val().length; 
		   		 alert(length); */
		   		 
		   		   var val = $('input:radio[name="handleStatus"]:checked').val(); 
		           if(val != null) { 
		       
		        	   if(val=="1"){
		        		   ModelA();
		        	   }else if(val=="2"){
		        		   ModelB();
		        	   }else if(val=="3"){
		        		   ModelC();
		        	   }
		           } 
		           }
			 });
			
		}
		function ModelA(){
		 var reasonDesc = document.getElementsByName("reasonDesc");
			for ( var i = 0; i < reasonDesc.length; i++) {
   				var files = reasonDesc[i];
   			 files.value="您好："+"\n"+"312312312312123123恭喜A123123333333333333333333333333333333333333333333333333333";
   			
   			}
		}
		function ModelB(){
			 var reasonDesc = document.getElementsByName("reasonDesc");
				for ( var i = 0; i < reasonDesc.length; i++) {
	   				var files = reasonDesc[i];
	   			 files.value="您好："+"\n"+"312312312312123123恭喜B123123333333333333333333333333333333333333333333333333333";
	   			
	   			}
			}
		function ModelC(){
			 var reasonDesc = document.getElementsByName("reasonDesc");
				for ( var i = 0; i < reasonDesc.length; i++) {
	   				var files = reasonDesc[i];
	   			 files.value="您好："+"\n"+"312312312312123123恭喜C123123333333333333333333333333333333333333333333333333333";
	   			
	   			}
			}
		</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>人伤赔偿标准新增</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
			    <iframe  frameborder="0" name="iframeContent" style="display: none;"></iframe> 
				<form id="form" class="form-horizontal" role="form" method="post" enctype="multipart/form-data" action="imageInfoSave.do" target="iframeContent">
					<div class="formtable f_gray4">
					<div class="row cl accident-over-add">
						<label class="form_label col-2">地区(省市)：</label>
					    <div class="form_input col-6 clearfix">
						<div style="display:inline">
							<app:areaSelect targetElmId="damageAreaCode" areaCode="${prplFeeStandardVo.areaCode}" showLevel="2"/>
							<font color="red">*</font>
						</div>
						<input type="hidden" id="damageAreaCode" name="prplFeeStandardVo.areaCode" value="${prplFeeStandardVo.areaCode}"/>
						</div>
					</div>
					<br>
					<div class="row cl accident-over-add">
						   <label class="form-label col-2 text-c">年份：</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-4">
								<select id="myYear" name="prplFeeStandardVo.yearCode" style="width:100px;height:25px;" datatype="*">
								</select>&nbsp;月份
								<select id="mymonth" name="prplFeeStandardVo.monthCode" style="width:60px;height:25px;">
								</select>
								<font color="red">*</font>
						  </div>
					</div>
					<br>
					<div class="row cl accident-over-add">
						   <label class="form-label col-2 text-c">图片：</label>
							<!-- 时间间隔要求为一个月 -->
							<div class="formControls col-4">
								<input name="upImageFile" id="Upfile"  type="file" datatype="*" multiple nullmsg="请选择导入文件名！" style="height:25px">
								<font color="red">*</font>
						  </div>
					</div>
					<br>
					<div class="row cl accident-over-add">
						   <label class="form-label col-2 text-c">说明：</label>
							<div class="formControls col-5">
								<textarea disabled="disabled" style="width:95%;text-align:center">文件上传类型应为图片类型(JPG或PNG),且文件小于1M</textarea>
						  </div>
					</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							   <a class="btn btn-primary btn-outline btn-search" id="save">保存</a>
					           <a class="btn btn-primary btn-outline btn-noPolicy ml-20" onclick="layerCoser()">关闭</a>

						</div>
					
				</div>
				</form>
			</div>
		</div>
</div>
	<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
   
	$(function(){
		window.onload=function(){ 
			//设置年份的选择 
			var myDate= new Date(); 
			var startYear=myDate.getFullYear()-50;//起始年份 
			var endYear=myDate.getFullYear()+50;//结束年份 
			var obj=document.getElementById('myYear');
			var flag="0";
			for (var i=startYear;i<=endYear;i++) 
			{ 
			if(flag=="0"){
				obj.options.add(new Option('',''));
				flag="1";
			}else{
				obj.options.add(new Option(i,i));
			}
			
			}
		};
		
		$("#myYear").click(function(){
			var yearValue=$(this).val();
			if(yearValue != null && yearValue != "undefined" && yearValue != "" && yearValue != "null"){
				//设置月份的选择
				var startMonth=0;//起始月份
				var endMonth=12;//结束月份
				var monthObj=document.getElementById('mymonth');
				var monthFlag="0";
				$("#mymonth").empty();
				for(var i=startMonth;i<=endMonth;i++){
					if(monthFlag=="0"){
						monthObj.options.add(new Option('',''));
						monthFlag="1";
					}else{
						monthObj.options.add(new Option(i,i));
					}
					
				}
			}else{
				$("#mymonth").empty();
				
			}
		});
		$("#damageAreaCode_lv1").attr("name","feeStandrad");
		$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");

		$("#save").click(function() {
			$(".btn-search").removeClass("btn-primary");
			$(".btn-search").addClass("btn-disabled");
			var damageAreaCode = $("#damageAreaCode").val();
			var yearCode=$("#myYear").val();
			if (isBlank(damageAreaCode)) {
				layer.msg('请录入地区', {
					time : 2000 //2s后自动关闭
				});
				$(".btn-search").removeClass("btn-disabled");
				$(".btn-search").addClass("btn-primary");
				return false;
			}
			if (isBlank(yearCode)) {
				layer.msg('请录入年限', {
					time : 2000//2s后自动关闭
				});
				$(".btn-search").removeClass("btn-disabled");
				$(".btn-search").addClass("btn-primary");
				return false;
			}
			var importFile = document.getElementsByName("upImageFile");
	    	for ( var i = 0; i < importFile.length; i++) {
				var files = importFile[i];
				if (files.length != 0&&files.value == '') {
					layer.msg("请选择导入文件名!",{time : 2000});
					$(".btn-search").removeClass("btn-disabled");
					$(".btn-search").addClass("btn-primary");
					return false;
				}
			}
			$("#form").submit();
		});

		
	});
	$("iframe[name='iframeContent']").on("load", function() { 
		var responseText = $("iframe")[0].contentDocument.body.getElementsByTagName("pre")[0].innerHTML;
        var jsonObj = JSON.parse(responseText);//转换为json对象 
        if(jsonObj.status=='200'){
        	layer.msg("操作成功！",{time : 2000});
        	$(".btn-search").removeClass("btn-disabled");
			$(".btn-search").addClass("btn-primary");
        }else{
        	layer.msg(jsonObj.statusText,{time : 2000});
        	$(".btn-search").removeClass("btn-disabled");
			$(".btn-search").addClass("btn-primary");
        }
	});
	
	function layerCoser(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.location.reload();
    	parent.layer.close(index);
	}
	</script>
</body>
</html>

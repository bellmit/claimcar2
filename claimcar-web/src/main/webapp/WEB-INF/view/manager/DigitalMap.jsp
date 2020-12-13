<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>电子地图</title>
<style >
	.digtal-title{
	    font-size: 18px;
	    font-weight: 1000;
	    width: 130px;
	    display: inline-block;
	    vertical-align: middle;
	    height: 50px;
	    line-height: 50px;
	 }
	 .digtal-open{
	    vertical-align: middle;
	    display: inline-block;
	    height: 50px;
	    line-height: 50px;
	    margin: 0;
	    padding: 0;
	 }
	 .digtal-label{
     	font-size: 12px;
	    height: 50px;
	    line-height: 50px;
	 }
	 .digtal-button{
	     margin-left: 58px;
	 }
</style>
</head>

<body>
<div class="top_btn">
		<span class="digtal-title">电子地图</span>
		<c:choose>
			<c:when test="${isOpen == 0}">
				<input type="checkbox" id="open" class="digtal-open" checked="checked"  onclick ="checkboxOnClick(this)" value="${isOpen}" /> 
			</c:when>
			<c:otherwise>
				<input type="checkbox" id="open"  class="digtal-open" onclick ="checkboxOnClick(this)" value="${isOpen}" />

			</c:otherwise>
		</c:choose>
		 <label class="digtal-label">开启</label>
		<input class="btn btn-primary btn-noagree digtal-button" id="submit" onclick="openDigtalMap()"  value="确定" style="width:60px;height:32px">
</div>
		
		
		<script type="text/javascript">
		function checkboxOnClick(checkbox){
			if(checkbox.checked == true){
				$("#open").val(0);
			} else{
				$("#open").val(1);
			}
		}
		//操作电子地图
		function openDigtalMap(){
			var url="/claimcar/digtalmap/operateDigtalMap/" + $("#open").val();
			$.ajax({
				url : url, // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
					var result = eval(jsonData);
					if (result.status == "200") {
						layer.msg("操作成功");
					}},
				error: function(){
					layer.msg("操作失败");
				}
				
			});

		}
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>清缓存</title>
</head>

<body>
<div class="top_btn">
		<a class="btn btn-primary" href="javascript:;" onclick="DataClearCache()">清除数据字典缓存</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="btn btn-primary" href="javascript:;" onclick="AreaClearCache()">清除地区缓存</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="btn btn-primary" href="javascript:;" onclick="UserClearCache()">清除用户缓存</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="btn btn-primary" href="javascript:;" onclick="RuleClearCache()">清除规则缓存</a>
</div>
		
		
		<script type="text/javascript">
		//清除数据字典缓存
		function DataClearCache(){
			var url="/claimcar/clearCache/dataDictionaryClerarCache.do";
			$.ajax({
				url : url, // 后台校验
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				async : false,
				success : function(jsonData) {// 回调方法，可单独定义
					var result = eval(jsonData);
					if (result.status == "200") {
						layer.msg("清除缓存成功");
					}},
				error: function(){
					layer.msg("清除缓存失败");
				}
				
			});

		}
		
		//清除地区代码缓存
		function AreaClearCache(){
			var url="/claimcar/clearCache/areaClerarCache.do";
			$.ajax({
				url  :url,
				type : 'post',
				async : false,
				success : function (jsonData){
				var result=eval(jsonData);
				if(result.status =="200"){
					layer.msg("清除缓存成功");
				}},
				error : function(){
					layer.msg("清除缓存失败");
				}
			});
		}
		
		//清除用户数据缓存
		function UserClearCache(){
			var url="/claimcar/clearCache/userClearCache.do";
			$.ajax({
				url  :url,
				type :'post',
				async :false,
				success : function(jsonData){					
					var result=eval(jsonData);
					if(result.status=="200"){
						layer.msg("清除缓存成功");
					}},
					error : function(){
						layer.msg("清除缓存失败");
					}
			});
		}
		
		//清除规则数据缓存
		function RuleClearCache(){
			var url="/claimcar/clearCache/ruleClearCache.do";
			$.ajax({
				url: url,
				type: 'post',
				async: false,
				success: function(jsonData){
					var result=eval(jsonData);
					if(result.status=="200"){
						layer.msg("清除缓存成功");
					}},
					error : function(){
						layer.msg("清除缓存失败");
					}
				
			});
			
		} 
		
		</script>
</body>
</html>
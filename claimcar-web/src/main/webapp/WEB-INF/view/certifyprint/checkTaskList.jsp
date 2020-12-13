<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查勘报告打印</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
						<div class="row cl">
							<label for=" " class="form_label col-2 col-offset-3">报案号</label>
							<div class="form_input col-3">
								<input id="registNo" type="text" class="input-text" name="registNo"/>
							</div>
						</div>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							   <button class="btn btn-primary" onclick="certifyPrintp();" type="button">打印</button>
						</div>
					</div>
				</form>
			</div>
		</div>
</div>
	<!--
	<script src="/js/policyQuery/CheckList.js"></script>
	-->
	<script type="text/javascript">
		/* function certifyPrint(){
			registNo = $("#registNo").val();
			
			var params = "?registNo=" + registNo;
			var url = "/claimcar/certifyPrint/checkTask.ajax";
			var index = layer.open({
				type : 2,
				title : "查勘报告",
				maxmin : true, // 开启最大化最小化按钮
				content : url + params
			});
			layer.full(index);
			
		} */
		function certifyPrintp(){
			var registNo=$("#registNo").val();
			var mainId="";
			var compensateNo="";
			var index="B";
			certifyPrintType(mainId,registNo,compensateNo,index);
			
		}
	</script>
</body>
</html>

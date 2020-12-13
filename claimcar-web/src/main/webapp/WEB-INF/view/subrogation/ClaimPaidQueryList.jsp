<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>代位案件理赔信息查询</title>
<link href="../css/TaskQuery.css" rel="stylesheet" />

</head>
<body>

	<div class="page_wrap">
		<div class="table_title">代位案件理赔信息查询</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form"
						method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c">
							 	报案号
							</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  id="registNo" name="registNo"  datatype="*"  id="registNo"/>
							<font color="red">*</font>
							</div>
							<label class="form-label col-2 text-c"> 险别</label>
							<div class="formControls col-3 mt-5">
								<span class="select-box"> <app:codeSelect
										codeType="CarRiskCode" type="select" id="comCode"
										name="riskCodeSub" lableType="code-name" />
								</span>
							</div>
						</div>
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 结算码</label>
							<div class="formControls col-3">
								<input type="text" class="input-text"  name="recoveryCode" id="recoveryCode"/>
							
							</div>
						</div>
							<br />
						<div class="line"></div>
					
						<div class="text-c">
								<input type="button" class="btn btn-primary btn-outline btn-search" onclick="show()" disabled value="查询">
								
							</span><br />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="hidden" id="showListDiv"></div>
		<script type="text/javascript">
		
		
		
		function show(){
			if(isBlank($("#registNo").val())){
				layer.msg("报案号不能为空");
				return ;
			}
			var a= $("#registNo").val();
			var b =  $("#comCode").val();
			var c =  $("#recoveryCode").val();
			var url  = "?registNo="+a+"&riskCodeSub="+b+"&recoveryCode="+c+"&types="+"base";
			var goUrl ="/claimcar/subrogationQuery/claimRecoverySearch.do"+url;
			openTaskEditWin("代位求偿理赔查询",goUrl);
		/* 	layer.open({
			    type: 2,
			    title: false,
			    shade: false,
				shadeClose : true,
				scrollbar : false,
			    skin: 'yourclass',
			    area: ['1220px', '600px'],
			    content: ["/claimcar/subrogationQuery/claimRecoverySearch.do"+url]
			});  */
			
		}
		</script>
</body>
</html>

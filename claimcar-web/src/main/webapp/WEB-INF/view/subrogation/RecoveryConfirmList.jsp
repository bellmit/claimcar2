<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>开始追偿确认</title>

</head>
<body>

	<div class="page_wrap">
		<div class="table_title">开始追偿确认</div>
		<div class="table_wrap">
			<div class="table_cont pd-10">
				<div class="formtable f_gray4">
					<form id="form" name="form" class="form-horizontal" role="form" method="post">
						<div class="row mb-3 cl">
							<label class="form-label col-2 text-c"> 报案号 </label>
							<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" name="registNo" datatype="n" maxlength="22" style="width:97%"/>
							</div>
							<label class="form-label col-2 text-c">结算码 </label>
								<div class="formControls col-3 mt-5">
								<input type="text" class="input-text" name="accountsNo" datatype="*"  style="width:97%"/>
							</div>
						</div>
						
						<div class="line"></div>
					
						<div class="text-c">
							<input type="submit" class="btn btn-primary" id="button" value="开始追偿" />
							</span><br />
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
			bindValidForm($('#form'), search);
		});

		function search() {
			var load = layer.load(0, {shade : [ 0.8, '#393D49' ]});
			$.ajax({
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				url : "/claimcar/subrogationEdit/startRecoverySearch",
				data : $("#form").serialize(),
				async : true,
				success : function(data) {
					layer.alert(data.data);
					layer.close(load);
				},
				error : function(data){
					layer.alert(data.data);
					layer.close(load);
				}
			});

		}
	</script>
</body>
</html>

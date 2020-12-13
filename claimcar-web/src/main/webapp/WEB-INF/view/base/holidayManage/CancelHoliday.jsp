<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>撤销休假</title>
</head>
<body>
		<form id="form" role="form" method="post" name="fm">
			<input type="hidden" name="holidayId" value="${prpLUserHolidayVo.id}" /> 
			<div class="table_wrap">
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-3">休假起期：</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prpLUserHolidayVo.startDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
							<label class="form_label col-3">休假止期：</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prpLUserHolidayVo.endDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-3">销假日期：</label>
							<div class="form_input col-2">
								<fmt:formatDate value="${prpLUserHolidayVo.leaveDate}" pattern="yyyy-MM-dd HH:mm:ss" />
							</div>
							<label class="form_label col-3">休假天数：</label>
							<div class="formControls col-2">
								<input type="text" class="input-text" value="${prpLUserHolidayVo.daysNum }"/>
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-3">休假原因：</label>
							<div class="formControls col-2">
								<input type="text" class="input-text" value="${prpLUserHolidayVo.cause }"/>
							</div>
						</div>
					</div>
					<div class="text-c mt-10">
						<input class="btn btn-primary ml-5" id="submit" type="button" value="确定"> 
						<input class="btn btn-primary ml-5" onclick="closeLayer()" type="button" value="返回">
					</div>
				</div>
			</div>
		</form>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript">
$(function() {
	$("input").attr("readonly",true);
	var ajaxEdit = new AjaxEdit($('#form'));
	ajaxEdit.targetUrl = "/claimcar/holidayManage/cancelHoliday.do";
	ajaxEdit.afterSuccess = function(result) {
		if (result.status == "200") {
			$("#submit").hide(); //保存成功防止重复提交
			layer.msg("提交成功");
		}
	};
	ajaxEdit.bindForm();
});

function closeLayer() {
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}

//提交表单
$("#submit").click(function() {
	$("#form").submit();
});
</script>
</body>
</html>
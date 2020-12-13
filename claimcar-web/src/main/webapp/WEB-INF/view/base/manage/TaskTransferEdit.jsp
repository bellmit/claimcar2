<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>任务平级移交</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<form id="defossform" role="form" method="post" name="fm">
			<div class="table_cont">
				<!-- 基本信息    -->
				<div class="table_wrap">
				<div class="table_title f14">移交目标</div>
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<label class="form_label col-2">报案号</label>
								<div class="form_input col-3">
									501071514401710001600 
								</div>
								<label class="form_label col-2">环节</label>
								<div class="form_input col-3">
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">目标人员机构</label>
								<div class="form_input col-2">
								<input type="text" class="input-text" name="" value="" placeholder="双击域"/>
								</div>
								<label class="form_label col-3">目标人员</label>
								<div class="form_input col-2">
								<input type="text" class="input-text" name="" value="" placeholder="双击域"/>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">损失方</label>
								<div class="form_input col-3"></div>
							</div>
						</div>
					</div>
				</div>
				<!-- 输入信息    -->
					<div class="table_title f14">移交原因</div>
					<div class="row cl">
						<div class="col-8">
							<textarea  class="textarea" name=" " datatype="*0-500" > </textarea> 
							
						</div>
						<div class="col-1"><font class="must">*</font></div>
					</div>
				
			</div>
		</form>

		<div class="text-c mt-10">
			<input class="btn btn-primary ml-5" id="save" onclick=" " type="submit" value="任务移交"> 
		</div>
	</div>

	<script type="text/javascript">
		$(function() {

		});
	</script>
</body>

</html>
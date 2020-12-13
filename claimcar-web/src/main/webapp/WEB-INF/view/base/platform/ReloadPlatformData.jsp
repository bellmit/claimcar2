<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>平台交互补送</title>
</head>
<body>
	<div class="page_wrap">
		<div class="table_wrap">
			<div class="table_title f14">平台数据补送</div>
			<div class="table_cont">
				<div class="row mb-3 cl">
					<label class="form-label col-1 text-c">上传节点</label>
					<div class="formControls col-3">
						<app:codeSelect type="select" codeType="" lableType="name"
						name="uploadNode" dataSource="${uploadNodeMap}"/>
						<font class="must">*</font>
					</div>
					<label class="form-label col-1 text-c">业务号列表</label>
					<div class="formControls col-6">
						<textarea class="textarea" name="bussNoList" style="height: 150px;"
						 placeholder="请输入..."></textarea>
					</div>
				</div>
				<div class="row mb-3 cl">
					<label class="form-label col-1 text-c"></label>
					<div class="formControls col-3">
					</div>
					<label class="form-label col-1 text-c">说明<font class="must">*</font></label>
					<div class="formControls col-6">
						输入多个业务号时，请按回车键进行分隔！
					</div>
				</div>
			</div>
		</div>
		<div class="table_wrap">
			<div class="table_title f14">返回列表</div>
			<div class="table_cont">
				<div class="row mb-3 cl">
					<div class="formControls col-12">
						<textarea class="textarea" style="height: 200px; margin-top: 2px"
						name="returnBussNoList" placeholder="返回信息.."></textarea>
					</div>
				</div>
			</div>
		</div>
		<!-- 底部按钮 -->
		<input style="margin-left: 45%;" class="btn btn-primary btn-kk"
			type="button" onclick="reUpload()" value="数据补送" />
	</div>
	<script type="text/javascript">
		function reUpload() {
			var uploadNode = $("option:selected", $("select[name='uploadNode']")).val();
			var bussNoList = $("textarea[name='bussNoList']").val();
			if (isBlank(uploadNode) || isBlank(bussNoList)) {
				layer.msg("上传节点不能为空 或 业务号列表不能为空！");
				return;
			}
			var goUrl = "/claimcar/platformAlternately/dataReloadSend.ajax";
			var loadIdx = layer.load(0, {
				time : 30 * 2000
			}, {
				shade : 0
			});
			$.ajax({
				url : goUrl, // 后台处理程序
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : {
					"uploadNode" : uploadNode,
					"bussNoArray" : bussNoList
				}, // 要传递的数据
				async : true,
				success : function(jsonData) {// 回调方法，可单独定义
					var text = jsonData.statusText;
					layer.close(loadIdx);
					layer.alert(text);
					$("textarea[name='returnBussNoList']").val(jsonData.data);
				},error : function(jsonData) {
					var text = jsonData.statusText;
					layer.close(loadIdx);
					layer.alert(text);
					$("textarea[name='returnBussNoList']").val(jsonData.data);
				}
			});
		}
		function closeLayer() {
			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			parent.layer.close(index); // 执行关闭
		}
	</script>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>平台交互编辑</title>
</head>
<body>
	<div class="page_wrap">
		<div class="table_cont">
			<div class="table_wrap">
				<div class="table_title f14">基本信息</div>
				<div class="table_cont">
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">上传节点:</label>
						<div class="formControls col-3">${logVo.requestName}</div>
						<label class="form-label col-1 text-c">归属机构:</label>
						<div class="formControls col-3">
							<app:codetrans codeType="ComCode" codeCode="${logVo.comCode}" />
						</div>
						<label class="form-label col-1 text-c">上传状态:</label>
						<div class="formControls col-3">
							<c:if test="${logVo.status eq '1'}">上传成功</c:if>
							<c:if test="${logVo.status eq '0'}">上传失败</c:if>
						</div>
					</div>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">理赔编码:</label>
						<div class="formControls col-5">${logVo.claimSeqNo}</div>
						<label class="form-label col-1 text-c">业务号:</label>
						<div class="formControls col-3">${logVo.bussNo}</div>
					</div>
					<div class="row mb-3 cl">
						<label class="form-label col-1 text-c">错误原因:</label>
						<div class="formControls col-11">${logVo.errorMessage}</div>
					</div>
				</div>
			</div>
			<div class="table_wrap">
				<div class="table_title f14">请求报文</div>
				<textarea class="textarea" readonly="readonly" placeholder="请输入...">${reqXml}</textarea>
			</div>
			<div class="table_wrap">
				<div class="table_title f14">返回报文</div>
				<textarea class="textarea" readonly="readonly" placeholder="请输入...">${resXml}</textarea>
			</div>
			<!-- 底部按钮 -->
			<input style="margin-left: 40%;" class="btn btn-primary btn-kk"
				type="button" onclick="reUpload('${logVo.id}')" value="补传" /> <input
				class="btn btn-primary btn-kk" type="button" onclick="closeLayer()"
				value="返回" />
		</div>
	</div>
	<script type="text/javascript">
		function reUpload(logId){
			var url = "/claimcar/platformAlternately/platformReload.do";
			var loadIdx = layer.load(0, {time: 30*2000},{shade: 0});
			$.ajax({
				url : url, // 后台处理程序
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : {"logId" : logId}, // 要传递的数据
				async : true,
				success : function(jsonData) {// 回调方法，可单独定义
					var text = jsonData.statusText;
					layer.close(loadIdx);
					layer.alert(text);
				},
				error : function(jsonData){
					var text = jsonData.statusText;
					layer.close(loadIdx);
					layer.alert(text);
				}
			});
		}
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
			parent.layer.close(index); // 执行关闭
		}
	</script>
</body>
</html>

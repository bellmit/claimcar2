<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>补传平台</title>
</head>
<body>
<div class="page_wrap">
<div class="table_cont pd-10">
<form action="#" id="editform">
   <div class="table_wrap">
		<div class="table_title f14">单证前的节点未全部上传平台，补送成功后可提交单证，错误信息如下：</div>
		<div class="row cl">
			<div class="table_cont">
				<table class="table table-bordered table-bg">
					<thead class="text-c">
						<tr>
							<th width="200px">上传失败环节</th>
							<th width="480px">上传错误原因</th>
							<th width="100px">操作</th>
						</tr>
					</thead>
					<tbody class="text-c">
					<c:forEach var="ciClaimPlatformLogVo" items="${noPassPlatformNodeBeforeCertifyList}" varStatus="certifyStatus">
						<tr>
							<td width="200px">${ciClaimPlatformLogVo.requestName}</td>
							<td width="480px">${ciClaimPlatformLogVo.errorMessage}</td>
							<td width="100px"><input class="btn btn-primary ml-5" onclick="reUpload('${ciClaimPlatformLogVo.id}','${ciClaimPlatformLogVo.requestType}','${ciClaimPlatformLogVo.comCode}','${ciClaimPlatformLogVo.bussNo}');" type="button" value="补传"> </td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
	    </div>
   </div>
   <c:if test="${showUpdateVin }">
   <div class="table_wrap">
      <input hidden="hidden" id="vinsSize" value="${fn:length(PrpLDlossCarMainVoList) }"/>
		<div class="row cl">
			<div class="table_cont">
				<table class="table table-bordered table-bg">
					<thead class="text-c">
						<tr>
							<th width="100px">损失方</th>
							<th width="120px">车牌号</th>
							<th width="230x">原VIN码</th>
							<th width="230x">修改后的VIN码</th>
							<th width="100px">操作</th>
						</tr>
					</thead>
					<tbody class="text-c">
					<c:forEach var="carVo" items="${PrpLDlossCarMainVoList}" varStatus="vinStatus">
						<tr>
							<td width="100px">${carVo.deflossCarType }</td>
							<td width="120px">${carVo.licenseNo }</td>
							<td width="230x">${carVo.vinNo }</td>
							<td width="230x"><input type="text" class="input-text" name="carList[${vinStatus.index}].vinNo"  
									  errormsg="请输入正确的VIN码！" nullmsg="请输入正确的VIN码！"  maxlength="17" onkeyup="toUpperValue(this)"/></td>
							<td width="100px"><input class="btn btn-primary ml-5" onclick="updateVIN('${carVo.registNo}','${carVo.serialNo}','${vinStatus.index}','${carVo.licenseNo }', '${carVo.vinNo }');" type="button" value="修改"> </td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
	    </div>
   </div>
   </c:if>
   <div class="btn-footer clearfix text-c">
		<input class="btn btn-primary ml-5" id="closeButton" onclick="closeLayer();" type="button" value=" 返回  "> 
	</div>
</form>

</div>
</div>
<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
<script type="text/javascript" src="/claimcar/js/common/common.js"></script>
<script type="text/javascript">
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
	parent.layer.close(index);// 执行关闭
}
function reUpload(logId,requestType,comCode,bussNo){
	var url = "/claimcar/platformAlternately/platformReupload.do";
	var loadIdx = layer.load(0, {time: 30*2000},{shade: 0});
	$.ajax({
		url : url, // 后台处理程序
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : {"logId" : logId,"requestType" : requestType ,"comCode" : comCode, "bussNo":bussNo}, // 要传递的数据
		async : true,
		success : function(jsonData) {// 回调方法，可单独定义
			var text = jsonData.statusText;
			layer.close(loadIdx);
			layer.alert(text);
			location.reload();
			layer.load();
		},
		error : function(jsonData){
			var text = jsonData.statusText;
			layer.close(loadIdx);
			layer.alert(text);
		}
	});
}
function updateVIN(registNo, serialNo, index,licenseNo,oldVinNo){
	/* var vinsSize = parseFloat($("#vinsSize").val());
	if(vinsSize > 1){
		for(var i=0;i<vinsSize-1;i++){
			var vinNo = $("input[name='carList["+i+"].vinNo']").val();
			for(var j=i+1;j<vinsSize;j++){
				var nextVinNo = $("input[name='carList["+j+"].vinNo']").val();
				if(vinNo == nextVinNo && vinNo != null && vinNo != ""){
					layer.msg("VIN码不能相同！");
					return false;
				}
			}
		}
	} */
	var newVinNo = $("input[name='carList["+index+"].vinNo']").val();
	if(newVinNo == null || newVinNo == "" || $.trim(newVinNo) == ""){
		layer.msg("修改后的VIN码不能为空！");
		return false;
	}
	if(oldVinNo == newVinNo){
		layer.msg("VIN码不能相同！");
		return false;
	}
	var url =  "/claimcar/updateVIN/updateVINByRegistNoAndSerialNo.do";
	var loadIdx = layer.load(0, {time: 30*2000},{shade: 0});
	$.ajax({
		url : url, // 后台处理程序
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : {"registNo" : registNo,"serialNo" : serialNo ,"newVinNo" : newVinNo,"licenseNo":licenseNo,"oldVinNo" : oldVinNo}, // 要传递的数据
		async : true,
		success : function(jsonData) {// 回调方法，可单独定义
			var text = jsonData.statusText;
			layer.close(loadIdx);
			layer.alert(text);
			location.reload();
			layer.load();
		},
		error : function(jsonData){
			var text = jsonData.statusText;
			layer.close(loadIdx);
			layer.alert(text);
		}
	});
}
</script>
</body>

</html>
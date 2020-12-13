<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<html>
<head>
<meta charset="utf-8">
<title>反洗钱冻结</title>
</head>
<body>
<div class="page_wrap" id="fu">
		<!--查询条件 开始-->
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
					<div class="formtable f_gray4">
					<c:if test="${!empty amlVo.identifyNumber }">
						<div class="row mb-3 cl">
							<label class="form_label col-2">被保险人名称:</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" value="${amlVo.insuredName}" readonly="readonly"/>
								
							</div>
							<label class="form_label col-2">证件号码:</label>
							<div class="form_input col-3">
								<input  type="text" class="input-text" value="${amlVo.identifyNumber}" readonly="readonly"/>
							</div>
							<div class="form_input col-2">
								 <input type="button" class="btn  btn-primary" id="insuredName" onclick="payfreezeUp('${amlVo.insuredName}','${amlVo.identifyNumber}','insuredName')" value="冻结"/>
							</div>
						</div>
						</c:if>
						<c:if test="${!empty amlVo.favoreeIdentifyCode }">
						<div class="row mb-3 cl">
							<label class="form_label col-2">受益人名称:</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" value="${amlVo.favoreeName}" readonly="readonly"/>
								
							</div>
							<label class="form_label col-2">证件号码:</label>
							<div class="form_input col-3">
								<input  type="text" class="input-text" value="${amlVo.favoreeIdentifyCode}" readonly="readonly"/>
							</div>
							<div class="form_input col-2">
								 <input type="button" class="btn  btn-primary"  id="favoreeName" onclick="payfreezeUp('${amlVo.favoreeName}','${amlVo.favoreeIdentifyCode}','favoreeName')" value="冻结"/>
							</div>
						</div>
						</c:if>
						<c:if test="${!empty amlVo.authorityNo }">
						<div class="row mb-3 cl">
							<label class="form_label col-2">授办人名称:</label>
							<div class="form_input col-3">
								<input type="text" class="input-text" value="${amlVo.authorityName}" readonly="readonly"/>
								
							</div>
							<label class="form_label col-2">证件号码:</label>
							<div class="form_input col-3">
								<input  type="text" class="input-text" value="${amlVo.authorityNo}" readonly="readonly"/>
							</div>
							<div class="form_input col-2">
								 <input type="button" class="btn  btn-primary" id="authorityName" onclick="payfreezeUp('${amlVo.authorityName}','${amlVo.authorityNo}','authorityName')" value="冻结"/>
							</div>
						</div>
						</c:if>
						<p>
						<div class="line"></div>
						<div class="row cl text-c">
							<a class="btn btn-primary ml-5" id="cancelBtn" onclick="closeLayer()">返回</a>
						</div>
					</div>
				</form>
			</div>
		</div>
		<!--案查询条件 结束-->
</div>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript">
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);	
	
}

function payfreezeUp(name,code,sd){
	
	
	var url="/claimcar/payCustom/payfreezeUp.ajax";
	var params = {
			"name" : name,
			"code" : code
		};
	
$.ajax({
		
		url : url, // 后台校验
		type : 'post', // 数据发送方式
		dataType : 'json', // 接受数据格式
		data : params, // 要传递的数据
		async : false,
		success : function(result) {// 回调方法，可单独定义	
		var text=result.statusText;
		  if (result.status == "200") {
				   var taskId=result.data;
				   
					if(taskId=='1'){
						alert(text);
						/* if(sd=='insuredName'){
							$("#insuredName").removeAttr("onclick");
							$("#insuredName").removeClass("btn-primary");
							$("#insuredName").addClass("btn-disabled");
						}else if(id=='favoreeName'){
							$("#favoreeName").removeAttr("onclick");
							$("#favoreeName").removeClass("btn-primary");
							$("#favoreeName").addClass("btn-disabled");
						}else if(id=='authorityName'){
							$("#authorityName").removeAttr("onclick");
							$("#authorityName").removeClass("btn-primary");
							$("#authorityName").addClass("btn-disabled");
						} */
					}
					
			}else{
				alert(text);
			}
		}
	});
 
 

}

</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>邮件发送人信息编辑页面</title>
</head>
<body>
<div class="fixedmargin page_wrap">
	<form id="form" role="form" method="post" name="fm">
		<div class="table_cont">
			<div class="formtable">
						<table class="table table-bordered table-bg">
							<thead class="text-c">
								<tr>
									<th>姓名</th>
									<th>邮箱地址</th>
									<th>类型</th>
									<th>归属机构</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody class="text-c" id="infoTbody">
								<input type="hidden" name="userComCode" value="${userComCode}" id="userComCode"/>
							    <input type="hidden" id="TBodySize" value="${fn:length(prpdEmailVoList)}">
								<%@include file="SenderInfoEdit_TBody.jsp"%>
							</tbody>
							</table>
							<button type="button" class="btn  btn-primary mt-5" onclick="addInfor()" >增加</button>
				</div>	
			</div>
		</form>
			<div class="text-c mt-10">
			<input class="btn btn-primary ml-5" id="submit"  type="submit" value="提交"> 
			<a class="btn btn-primary" onclick="closeLayer()" >关闭</a>
		</div>
	</div>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript">
//提交表单
$("#submit").click(function(){
	$("#form").submit();
	});
	
function closeLayer(){
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
}

$(function (){	
	var ajaxEdit = new AjaxEdit($('#form'));	
	ajaxEdit.targetUrl = "/claimcar/mailModel/updateSenderInfo.do";
	ajaxEdit.afterSuccess=function(result){
		if(result.status=="200"){
			$("input").attr("disabled",true);
			$("select").prop("disabled",true);
			$("button").prop("disabled",true);
			$("#submit").hide();
			layer.msg("提交成功");
		}
	}; 
	//绑定表单
	ajaxEdit.bindForm();
});

function addInfor(){
	var $tbody = $("#infoTbody");
	// 获取当前行号
	var size = $("#TBodySize").val();
	var comCodeHtml = ""; 
	var userCode = $("#userComCode").val();

	if(userCode == "00000000" ||userCode == "00010000"){
		comCodeHtml = "<td>"
		+ "<span class='select-box'> "
		+ "<app:codeSelect codeType='ComCodeLv2' name='prpdEmailVo["+ size + "].comCode' clazz='must' id='caseType' type='select' />"
		+ "</span></td>";
	} else {
		comCodeHtml = "<td><input type ='hidden' name='prpdEmailVo["+ size + "].comCode' value = '" + userCode + "' />"
			+ "<span class='select-box'> "
			+ "<app:codeSelect codeType='ComCodeLv2' name='prpdEmailVo["+ size + "].comCode' value = '" + userCode + "' clazz='must' disabled ='true' id='caseType' type='select' />"
			+ "</span></td>";
	}
	var html = "<tr class='text-c'>"
	+ "<td>"
	+"<input type='hidden'  name='prpdEmailVo["+ size + "].id' />"
	+ "<input type='text' class='input-text' name='prpdEmailVo["+ size + "].name' dataType='*'  />"
	+ "</td>"
	+ "<td>"
	+ "<input type='text' datatype='e' class='input-text' name='prpdEmailVo["+ size + "].email' /></td>"
	+ "<td>"
	+ "<span class='select-box'> "
	+ "<app:codeSelect codeType='Mold' name='prpdEmailVo["+ size + "].caseType' clazz='must' id='caseType' type='select' />"
	+ "</span></td>"
	+ comCodeHtml
	+ "<td>"
	+ "<button type='button' class='btn  btn-primary mt-5' onclick='deleteInfo(this)' name= 'deleteBtn_" + size +"' >删除</button>"
	+ "</td>"
	+ "</tr>";
	$tbody.append(html);
	$("#TBodySize").val(parseInt(size)+1);
	var test = $("select[name = 'prpdEmailVo["+ size +"].comCode']");
	$("select[name = 'prpdEmailVo["+ size +"].comCode'] option").each(function(){
		 var val = $(this).val();
         if(val == userCode){
        	 $(this).attr("selected",true);
        	 return false;
         }
	});
};
function deleteInfo(element) {
	var indexName = "deleteBtn_";
	var proposalPrefix = "prpdEmailVo";
	var index = $(element).attr("name").split("_")[1];// 下标
	var $parentTr = $(element).parent().parent();
	var $subRiskSize = $("#TBodySize");
	var subRiskSize = parseInt($subRiskSize.val(), 10);
//	$subRiskSize.val(parseInt(subRiskSize) - 1);// 删除一条prplReplevyDetailVos_
    $("#TBodySize").val(subRiskSize - 1);// 删除一条
	delTr(subRiskSize, index, indexName, proposalPrefix);
	$parentTr.remove();
}
</script>
</body>
</html>
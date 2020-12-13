<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>索赔清单</title>
</head>
<body>
<div class="fixedmargin page_wrap">
<div class="top_btn">
    <div  class="f-r mr-50">
    <input class="btn btn-primary ml-5" id="saveButton" onclick="saveCertifyList();" type="button" value="保存"> 
	<input class="btn btn-primary ml-5" id="closeButton" onclick="closeCertifyList();" type="button" value="关闭"> 
    </div>
</div>
<div class="table_cont">
<form action="#" id="editform">
   <input type="hidden" value="${registNo}" name="registNo">
   <input type="hidden" value="${prpLWfTaskVo.subNodeCode}" name="subNodeCode">
   <input type="hidden" value="${prpLWfTaskVo.handlerStatus}" id="handlerStatus">
   <input type="hidden" value="${certifyMakeup}" id="certifyMakeup">
   <c:forEach var="prpLCertifyCodeVo" items="${prpLCertifyCodeVoList}" varStatus="status">
    <div class="table_wrap">
       <%--  <input type="hidden" name="certifyDirects[${status.index + size}].certifyTypeCode" value="${prpLCertifyCodeVo.certifyTypeCode}">
        <input type="hidden" name="prpLCertifyItems[${status.index + size}].certifyTypeName" value="${prpLCertifyCodeVo.certifyName}"> --%>
		<div class="table_title f14">${prpLCertifyCodeVo.certifyTypeName}</div>
		<div class="row cl">
			<div class="table_cont col-6-5">
				<table class="table table-bordered table-bg">
					<c:if test="${prpLCertifyCodeVo.certifyTypeCode != 'C099'}">
					<thead class="text-c">
						<tr>
							<th width="100px">是否勾选</th>
							<th width="300px">单证</th>
						</tr>
					</thead>
					<tbody class="text-c">
					<c:forEach var="certifyCodeVo" items="${prpLCertifyCodeVo.prpLCertifyCodeVoList}" varStatus="certifyStatus">
						<tr>
							<td width="100px">
							<c:if test="${certifyCodeVo.isSelected != '1'}">
								<input type="checkbox" class="checkbox" 
								name="certifyDirects"
								value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.certifyTypeName}-${prpLCertifyCodeVo.certifyTypeCode}-${prpLCertifyCodeVo.certifyTypeName}-${certifyCodeVo.mustUpload}-${certifyCodeVo.validStatus}"
								/>
							</c:if>
							<c:if test="${certifyCodeVo.isSelected eq '1'}">
							    <input type="hidden" value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.upCertifyCode}-${certifyCodeVo.id}" name="allCertify">
							    <c:if test="${certifyCodeVo.disabled eq '1'}">
							    <input type="checkbox" class="checkbox"  checked="checked"  disabled/>
							    <input type="hidden" name="checkCertify"  value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.upCertifyCode}-${certifyCodeVo.id}">
							    </c:if>
							    <c:if test="${certifyCodeVo.disabled == null}">
							    <input type="checkbox" class="checkbox" name="checkCertify" checked="checked" value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.upCertifyCode}-${certifyCodeVo.id}"/>
							    </c:if>
							</c:if>
							</td>
							<td width="300px">${certifyCodeVo.certifyTypeName}</td>
							<!-- <td width="400px"><input type="text" class="input-text" name="otherCertify" maxlength="50" value="" /></td> -->
						</tr>
					</c:forEach>
					</tbody>
					</c:if>	
					<c:if test="${prpLCertifyCodeVo.certifyTypeCode eq 'C099'}">
					<thead class="text-c">
						<tr>
							<th width="100px">是否勾选</th>
							<th width="300px">单证</th>
							<th width="100px">操作</th>
						</tr>
					</thead>
					<tbody class="text-c">
					<c:forEach var="certifyCodeVo" items="${prpLCertifyCodeVo.prpLCertifyCodeVoList}" varStatus="certifyStatus">
					    <tr>
							<td width="100px">
							<c:if test="${certifyCodeVo.isSelected != '1'}">
							<input type="checkbox" class="checkbox" 
								name="certifyDirects"
								value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.certifyTypeName}-${prpLCertifyCodeVo.certifyTypeCode}-${prpLCertifyCodeVo.certifyTypeName}-${certifyCodeVo.mustUpload}-${certifyCodeVo.validStatus}"
							/>
							</c:if>
							<c:if test="${certifyCodeVo.isSelected eq '1'}">
							    <input type="hidden" value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.upCertifyCode}-${certifyCodeVo.id}" name="allCertify">
							    <c:if test="${certifyCodeVo.disabled eq '1'}">
							    <input type="checkbox" class="otherCertify checkbox"  checked="checked"  disabled/>
							    <input type="hidden" name="checkCertify"  value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.upCertifyCode}-${certifyCodeVo.id}">
							    </c:if>
							    <c:if test="${certifyCodeVo.disabled == null}">
							    <input type="checkbox" class="otherCertify checkbox" name="checkCertify" checked="checked" value="${certifyCodeVo.certifyTypeCode}-${certifyCodeVo.upCertifyCode}-${certifyCodeVo.id}"/>
							    </c:if>
							</c:if>
							</td>
							<td width="300px">${certifyCodeVo.certifyTypeName}</td>
							<td width="100px"></td>
						</tr>
					</c:forEach>	
					<tr id="otherCertifyTable">
					<td width="100px">
						  <button type="button" class="btn btn-plus Hui-iconfont Hui-iconfont-add" id="addCertify"onclick="addCertifyItem()" ></button> 
					</td>
					<td width="300px"></td>
					<td width="100px"></td>
					</tr>
				    </c:if>
				    </tbody>
				</table>
			</div>
	    </div>
   </div>
   </c:forEach>
</form>

</div>
</div>
<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
<script type="text/javascript">
$(function(){
	var handlerStatus = $("#handlerStatus").val();
	var certifyMakeup = $("#certifyMakeup").val();
	if(certifyMakeup != "yes"){
		if(handlerStatus == "3" || handlerStatus == "9"){//已处理，已注销的不可以新增
			$(".top_btn").hide();
			$("input:checkbox").attr("disabled",true);
			$("button").hide();
		}
	} 
});
var otherCerTiNum = $(".otherCertify").length;
function saveCertifyList(){
	var rules = {};
	var ajaxEdit = new AjaxEdit($('#editform'));
	ajaxEdit.beforeCheck = function() {// 校验之前
	};
	ajaxEdit.beforeSubmit = function() {// 提交前补充操作
		if (checkUnallowedChars()) {
			layer.alert("自定义单证中不允许录入半角空格、圆角空格、圆角斜杠、半角斜杠、半角反斜杠、圆角反斜杠、半角#、圆角#、￥、圆角＄、半角$、半角星号*、圆角星号×、半角&、圆角＆、中文冒号：、英文冒号:，请知悉！");
			return false;
		}
	};
	ajaxEdit.targetUrl = "/claimcar/certify/saveCertifyList.do";
	ajaxEdit.rules = rules;
	ajaxEdit.afterSuccess = function(data) {// 操作成功后操作
		layer.alert("保存成功",{closeBtn:0},function(){
			var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
			if (typeof(eval(parent.initCertifyItmes)) == "function") {
				parent.initCertifyItmes();
			}
			parent.layer.close(index);// 执行关闭
		});
	};
	// 绑定表单
	ajaxEdit.bindForm();
	$("#editform").submit();
}

function closeCertifyList(){
	var index = parent.layer.getFrameIndex(window.name);// 获取窗口索引
	parent.layer.close(index);// 执行关闭
}

function addCertifyItem(){
	var $otherCertifyTable = $("#otherCertifyTable");
	var html = "<tr><td width='100px'><input type='checkbox' class='checkbox' checked='checked'  disabled/></td>"
	+"<td width='300px'><input type='text' datatype='*' class='input-text' name='otherCertify' maxlength='50'/></td>"
	+"<td width='100px'> <button type='button' class='btn btn-plus Hui-iconfont Hui-iconfont-jianhao' onclick='delCertifyItem(this)'></button></td></tr>";
	$otherCertifyTable.before(html);
}

function delCertifyItem(e){
	var $del = $(e).parent().parent();
	$del.remove();
	
}

/**
 * 检查特殊字符 半角空格、圆角空格、圆角斜杠、半角斜杠、半角反斜杠、圆角反斜杠、半角#、圆角#、
 * ￥、圆角英文＄、半角$、半角星号*、圆角星号×、半角&、圆角＆、中文冒号：、英文冒号:
 * \\表示一个\
 */
function checkUnallowedChars() {
	var reg = new RegExp(/[ 　/／\\＼#＃￥＄$*＊×&＆：:]/);
	var hasUnallowedChars = false;
	$("input[name='otherCertify']").each(function() {
		var arr = [];
		var curValue = $(this).val();
		if (curValue != undefined && curValue.length != 0) {
			arr = curValue.split(reg);
		}
		var newValue = arr.join('');
		if (newValue.length < curValue.length) {
			hasUnallowedChars = true;
			return false;// 退出循环
		}
	});
	return hasUnallowedChars; // 方法返回值
}
</script>
</body>

</html>
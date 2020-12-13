<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<div class="table_cont">
	<div style="min-height:50px;">
		<div class="table_title f14">案件信息</div>
		<div class="formtable">
		<div class="table_cont">
			<div class="row cl">
				<label class="form_label col-2">报案号</label>
				<div class="form_input col-3">${param.bussNo }</div>
				<label class="form_label col-3">节点类型</label>
				<div class="form_input col-3">
					<app:codetrans codeType="FlowNode" codeCode="${param.nodeCode }"/>
				</div>
			</div>
			<div class="row cl">
				<label class="form_label col-2">操作员代码</label>
				<div class="form_input col-3">${user.userCode}</div>
				<label class="form_label col-3">操作员姓名</label>
				<div class="form_input col-3">${user.userName}</div>
			</div>
			</div>
		</div>
		<div class="table_title f14">留言信息</div>
		<div class="formtab">
		<ul class="commentList">
			<c:forEach var="sysMsgContent" items="${sysMsgContentVos}" varStatus="status">
				<c:if test="${user.userCode != sysMsgContent.createUser && status.index < 5}">
				<li class="item cl" id="${status.index }" name="${sysMsgContent.floorIndex }" tabindex="-1"> 	
				</c:if>
				<c:if test="${user.userCode == sysMsgContent.createUser && status.index < 5}">
				<li class="item cl comment-flip" id="${status.index }" name="${sysMsgContent.floorIndex }" tabindex="-1"> 	
				</c:if>
				<c:if test="${user.userCode != sysMsgContent.createUser && status.index >= 5}">
				<li class="item cl" style="display:none" id="${status.index }" name="${sysMsgContent.floorIndex }" tabindex="-1"> 	
				</c:if>
				<c:if test="${user.userCode == sysMsgContent.createUser && status.index >= 5}">
				<li class="item cl comment-flip" style="display:none" id="${status.index }" name="${sysMsgContent.floorIndex }" tabindex="-1"> 	
				</c:if>
			  	<a href="#">
			  		<i class="avatar size-L radius">
			  			<img alt="" src="/claimcar/images/avatar.gif">
			  		</i>
			  	</a>
			    <div class="comment-main">
			      <header class="comment-header" style="padding: 5px 5px">
			        <div class="comment-meta">
			        	<span><app:codetrans codeType="FlowNode" codeCode="${sysMsgContent.nodeCode }"/></span>
			        	<span><app:codetrans codeType="ComCode" codeCode="${sysMsgContent.userComCode}"/></span>
			        	<span><app:codetrans codeType="UserCode" codeCode="${sysMsgContent.createUser}"/></span> 发表于
			        	<span><fmt:formatDate value="${sysMsgContent.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
			            <a href="javascript:clickReMsg(this,'${sysMsgContent.id}')" style="color:#005ab5">回复</a>
			        </div>
			      </header>
			      <div class="comment-body" style="margin-top: 0px;padding: 5px 5px">
			        <span>${sysMsgContent.msgContents}</span>
			      </div>
			      <c:if test="${sysMsgContent.reMsgVo.createUser != null}">
			      <div class="f-12" style="margin: 20px;padding: 10px; border: 1px solid #ccd3e4;background-color: #f9f9f9;color: #666;" >
			       		 原留言&nbsp;&nbsp;
			       		<span><app:codetrans codeType="FlowNode" codeCode="${sysMsgContent.reMsgVo.nodeCode }"/></span>
			       		<span><app:codetrans codeType="ComCode" codeCode="${sysMsgContent.reMsgVo.userComCode}"/></span>
			       		<span><app:codetrans codeType="UserCode" codeCode="${sysMsgContent.reMsgVo.createUser}"/></span> 发表于
			        	<span><fmt:formatDate value="${sysMsgContent.reMsgVo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
			        	<br><span class="ml-30">${sysMsgContent.reMsgVo.msgContents}</span>
			      </div>
			      </c:if>
			    </div>
			  </li>
			  <c:if test="${status.index == 4}">
			      	<div id="seeMore" class="text-c"  >
			      		<div style="width:93%; margin: 20px;background-color: #f9f9f9;color: #666;">查看更多留言信息......</div>
			      	</div>
			  </c:if>
			</c:forEach>
		  
		</ul>
		</div>
	</div>
	<input type="hidden" name="focusInd" value="${focusInd }">
	<form id="sysMsgForm" name="fm">
		<input type="hidden" name="bussNo" value="${param.bussNo }">
		<input type="hidden" name="bussNoLink" >
		<input type="hidden" name="nodeCode" value="${param.nodeCode }">
		<input type="hidden" name="msgType" value="AJBZ">
		<input type="hidden" name="userCode" value="${userCode}" />
		<input type="hidden" name="createUser" value="${user.userCode}" />
		<input type="hidden" name="userComCode" value="${user.comCode}" />
		<input type="hidden" name="reMsgId" value="" />
		<div class="row cl mt-30" style="height:80px">
			<div class="form_input col-1">
				<img src="/claimcar/images/noavatar.png" alt="">
			</div>
			<div class="form_input col-11">
				<textarea  class="textarea" name="msgContents" datatype="*0-500" style="height:50px"></textarea>
			</div>
			<div class="btn-footer clearfix text-r">
				<a class="btn btn-primary ml-5" id="submit">发布</a>
				<a class="btn btn-primary ml-5" id="closeRm">关闭</a>
			</div>
		</div>
											
	</form>
</div>
<script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
<script type="text/javascript">
	var reSysMsgContent = null;
	
	
	$(function (){
		focusMsg();//检验是否是首页处理留言信息，如果是则显示并focus该条留言
		
		var ajaxEdit = new AjaxEdit($('#sysMsgForm'));
		ajaxEdit.targetUrl = "/claimcar/sysMsg/saveSysMsg.do"; 
		ajaxEdit.afterSuccess=function(reMsg){
			$("li").removeAttr("style");
			$("#seeMore").attr("style","display:none");
			var text = "";
			$("[name='msgContents']").val(text);
			$("[name='msgContents']").attr("placeholder","发布案件备注");
			$("[name='reMsgId']").val(text);
			
			addReBox(reMsg.data);
		}; 
		//绑定表单
		ajaxEdit.bindForm();
					
	});
	
	$("#closeRm").click(function(){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.layer.close(index);
	});
	
	
	$("#submit").click(function(){	//提交表单
		var con = $("[name='msgContents']").val();
		if(con==null||con==""){
			layer.msg("录入内容不能为空");
		}else{
			$("#sysMsgForm").submit();
		}
		});
	//添加回复框
	function addReBox(reId){
		var $tbody = $(".commentList");
		var params = {
				"reId" : reId,
			};
			var url = "/claimcar/sysMsg/addReBox.ajax";
			$.post(url, params, function(result) {
				$tbody.append(result);
			});
	}
	//点击回复，回复框获得焦点并给reMsgId赋值
	function clickReMsg(element,mId){
		$("[name='msgContents']").focus();
		$("[name='msgContents']").attr("placeholder","回复留言:");
		$("[name='reMsgId']").val(mId);
	}
	//点击查看更多留言信息
	var anIndex = 5;
	function showAllLi(){
		anIndex+=10;
		$("li").each(function(){
			var mid = $(this).attr("id");
			if(mid<anIndex){
				$(this).removeAttr("style");
				//移除不可见属性
			}
		});
		var seeMore = $("#seeMore");
		$("#seeMore").attr("style","display:none");
		var tmp = 0;
		$("li").each(function(){
			var sty = $(this).attr("style");
			if(sty!=null&&sty!=""&&sty!="undefined"){
				tmp++;//循环查看是否还有隐藏的li组件
			}
		});
		if(tmp!=0){
			//如果还有隐藏的组件，则显示查看更多
			$(".formtab").append(seeMore);
			$("#seeMore").removeAttr("style","display:none");
			//如果没有隐藏的li组件则tmp=0此时“点击查看更多”的div不需要再显示
		}
		
	}
	
	$("#seeMore").click(function(){
		showAllLi();
	});
	
	function focusMsg(){
		var fcFlag = $("[name='focusInd']").val();//获取隐藏域的值，看是否是从首页打开的留言
		if(!isBlank(fcFlag)){
			$("li").each(function(){
				var iName = $(this).attr("name");
				if(iName==fcFlag){
					var seeMore = $("#seeMore");
					$("#seeMore").attr("style","display:none");
					$(this).prevAll('li').removeAttr("style");
					$(this).removeAttr("style");
					//移除该元素之前所有li元素及该元素的不可见属性
					var tmp = 0;
					$("li").each(function(){
						var sty = $(this).attr("style");
						if(sty!=null&&sty!=""&&sty!="undefined"){
							tmp++;//循环查看是否还有隐藏的li组件
						}
					});
					if(tmp!=0){
						//如果还有隐藏的组件，则显示查看更多
						$(".formtab").append(seeMore);
						$("#seeMore").removeAttr("style","display:none");
						//如果没有隐藏的li组件则tmp=0此时“点击查看更多”的div不需要再显示
					}
					$(this).focus();//聚焦到该条留言
				}
			});
		}
	}

</script>


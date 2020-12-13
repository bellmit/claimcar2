<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
			<li class="item cl comment-flip"> 	
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
			       		<span><app:codetrans codeType="UserCode" codeCode="${sysMsgContent.reMsgVo.createUser}"/></span>发表于
			        	<span><fmt:formatDate value="${sysMsgContent.reMsgVo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
			        	<br><span class="ml-30">${sysMsgContent.reMsgVo.msgContents}</span>
			      </div>
		      </c:if>
		    </div>
		  </li>

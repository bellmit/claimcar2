<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>重开赔案任务发起</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<div class="top_btn">
				<a class="btn btn-primary" onclick="viewEndorseInfo('${registNo}')">保单批改记录</a>
				<a class="btn btn-primary" id="claimRemark" onclick="createRegistMessage('${registNo}', 'ReOpenApp')">案件备注</a>
				 <a class="btn  btn-primary" onclick="lookReCaseHis('${claimNo}')">历史重开信息</a>
				 <a class="btn  btn-primary" onclick="imageMovieScan('${registNo}')">信雅达影像查看</a>
			</div>
			<br />
		</div>
		<p>
		<form id="form" role="form" method="post" name="fm">
		    <input type="hidden"  name="flag" id="flag"  value="${flag}" />
		    <input type="hidden" id="handleStatus"  name="handleStatus"  value="${handleStatus}" />
		    <input type="hidden"  name="registNo"  value="${registNo}" />
		    <input type="hidden"  name="prpLReCaseVo.endCaseNo"  value="${endCaseNo}" />
			<div class="table_cont">
				<!-- 基本信息    -->
				<div class="table_wrap">
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<div class="form_input col-md-offset-2 col-3 c-blue">该案件已结案，点击提交按钮重开</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">立案号</label>
								
								<div class="row cl" style="padding-top: 15px">
									<c:choose>
										<c:when test="${handleStatus =='0' || handleStatus =='6'}">
											<c:forEach var="endCase" items="${endCaseList }"
												varStatus="recordStatus">
												<input type="checkbox" name="claimNoArr"
													value="${endCase.claimNo }" class="checkbox mr-5"
													 >${endCase.claimNo }
								            <c:if test="${endCase.riskCode == '1101' }">(交强)</c:if>
												<c:if test="${endCase.riskCode != '1101' }">(商业)</c:if>
											</c:forEach>
										</c:when>
										
										<c:otherwise>
											<input type="radio" disabled="disabled" checked="true">${claimNo }
							        	</c:otherwise>
									</c:choose>
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">重开赔案员</label>
								<div class="form_input col-3">
								    <input type="text" class="input-text"  name="prpLReCaseVo.openCaseUserName" 
								    value="${openCaseUserName}"  readonly="readonly" />
								</div>
								<label class="form_label col-2">重开赔案日期</label>
								<div class="form_input col-3">
								    <c:set var="openCaseDate">
									    <fmt:formatDate value="${openCaseDate}" pattern="yyyy-MM-dd" />
									</c:set>
								    <input type="text" class="input-text" name="prpLReCaseVo.openCaseDate"
										pattern="yyyy-MM-dd"  readonly="readonly" value="${openCaseDate}" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">交强险结案日期</label>
								<div class="form_input col-3">
								     <c:set var="endCompelCaseDate">
									    <fmt:formatDate value="${endCompelCaseDate}" pattern="yyyy-MM-dd" />
									</c:set>
								    <input type="text" class="input-text" name="endCompelCaseDate"
										pattern="yyyy-MM-dd"  readonly="readonly" value="${endCompelCaseDate}" />
								</div>
								<label class="form_label col-2">商业险结案日期</label>
								<div class="form_input col-3">
								    <c:set var="endBusinessCaseDate">
									    <fmt:formatDate value="${endBusinessCaseDate}" pattern="yyyy-MM-dd" />
									</c:set>
								    <input type="text" class="input-text" name="endBusinessCaseDate"
										pattern="yyyy-MM-dd" readonly="readonly" value="${endBusinessCaseDate}" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">重开赔案原因</label>
								<div class="form_input col-2">
									<span class="select-box"> 
									    <app:codeSelect codeType="OpenReasonCode" name="prpLReCaseVo.openReasonCode"
											 type="select" clazz="must" value="${prpLReCase.openReasonCode}"/>
								    </span>
									<font class="must">*</font>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 输入信息    -->
					<div class="table_title f14">重开原因说明</div>
					<div class="row cl">
						<div class="col-12">
							<textarea  class="textarea" name="prpLReCaseVo.openReasonDetail" id="openReason"
							datatype="*0-500" >${openReasonDetail}</textarea> 
							<font class="must">*</font>
						</div>
					</div>
				
				<!-- 意见列表  -->
				
				<div class="table_title f14">意见列表</div>
				<div class="formtable">
					<div class="table_cont">
						<table class="table table-bordered table-bg">
							<thead class="text-c">
								<tr>
									<th>角色</th>
									<th>操作人员</th>
									<th>机构</th>
									<th>发表意见时间</th>
									<th>意见</th>
									<th>意见说明</th>
									<th>审核状态</th>
								</tr>
							</thead>
							<tbody class="text-c">
								<c:forEach var="reCaseText" items="${reCaseTexts }" varStatus="status">
								    <tr>
								    <td>
								        <c:if test="${reCaseText.checkStatus =='4' }">
								                                   重开赔案员
								        </c:if>
								        <c:if test="${reCaseText.checkStatus !='4' }">
								                                   重开赔案审核员
								        </c:if>
								    </td>
								    <td>${reCaseText.operatorName }</td>
								    <td><app:codetrans codeType="ComCode" codeCode="${reCaseText.comName}"/></td>
								    <td><app:date date="${reCaseText.inputTime }" format="yyyy-MM-dd HH:mm:ss"/></td>
								    <td>
								        <c:choose> 
								            <c:when test="${reCaseText.checkStatus =='4'}">
											    ${reCaseText.checkOpinion }
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='5'}">
											    提交上级
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='6'}">
											   审核通过
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='7'}">
											   审核不通过
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='8'}">
											  退回修改
										    </c:when>
										</c:choose>
								    </td>
								    <td>
									<c:if test="${reCaseText.checkStatus =='4' }">
								         <div title="${reCaseText.openReasonDetail }" >
									     <c:choose> 
										<c:when test="${fn:length(reCaseText.openReasonDetail ) > 4}"> 
											${fn:substring(reCaseText.openReasonDetail, 0, 4)}......
										</c:when>
										<c:otherwise>  
											${reCaseText.openReasonDetail }
										</c:otherwise>  
										</c:choose> 
										</div>
								      </c:if>
								     <c:if test="${reCaseText.checkStatus !='4' }">
								         <div title="${reCaseText.checkOpinion }" >
									     <c:choose> 
										<c:when test="${fn:length(reCaseText.checkOpinion ) > 4}"> 
											${fn:substring(reCaseText.checkOpinion, 0, 4)}......
										</c:when>
										<c:otherwise>  
											${reCaseText.checkOpinion }
										</c:otherwise>  
										</c:choose> 
										</div>
								     </c:if>
								</td>
								<td>
								        <c:choose> 
								            <c:when test="${reCaseText.checkStatus =='4'}">
											 登记
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='5'}">
											    提交上级
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='6'}">
											   审核通过
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='7'}">
											   审核不通过
										    </c:when>
										    <c:when test="${reCaseText.checkStatus =='8'}">
											  退回修改
										    </c:when>
										</c:choose>
								    </td>
								    </tr>
								   </c:forEach>
								</tbody>
							</table>
					</div>
				</div>	
				
			</div>
		</form>

		<div class="text-c mt-10" >
			<input class="btn btn-primary ml-5" id="btnDiv" onclick="initReOpenSubmit()"  type="submit" value="提交"> 
		</div>
	</div>
    
	<script type="text/javascript" src="/claimcar/js/reOpenCase/reOpenCase.js"></script>
</body>

</html>
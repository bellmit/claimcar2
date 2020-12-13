<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>重开赔案审核界面（总/分公司）</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<!-- 按钮组 -->
		<div class="top_btn">
			<div class="top_btn">
				<a class="btn btn-primary" onclick="viewEndorseInfo('${prpLReCase.registNo }')">保单批改记录</a>
				<a class="btn btn-primary" id="claimRemark" onclick="createRegistMessage('${prpLReCase.registNo}', '${nodeCode}')">案件备注</a>
				 <a class="btn  btn-primary" onclick="lookReCaseHis('${prpLReCase.claimNo}')">历史重开信息</a>
				 <a class="btn  btn-primary" onclick="showFlow('${flowId}')">流程查询</a>
				  <a class="btn  btn-primary" onclick="imageMovieScan('${prpLReCase.registNo}')">信雅达影像查看</a>
			</div>
			<br />
		</div>
		<p>
		<form id="form" role="form" method="post" name="fm">
		    <input type="hidden"  name="flowTaskId"  value="${flowTaskId }" />
		    <input type="hidden"  name="auditStatus" id="auditStatus" value="${auditStatus }" />
		    <input type="hidden"  name="handlerStatus" id="handlerStatus" value="${handlerStatus }" />
		    <input type="hidden"  name="prpLReCaseVo.id"  value="${prpLReCase.id }" />
		    <input type="hidden"  name="prpLReCaseVo.endCaseNo"  value="${prpLReCase.endCaseNo }" />
		    <input type="hidden"  name="prpLReCaseVo.compensateNo"  value="${prpLReCase.compensateNo}" />
		    <input type="hidden"  name="prpLReCaseVo.registNo"  value="${prpLReCase.registNo }" />
		    <input type="hidden"  name="prpLReCaseVo.seriesNo"  value="${prpLReCase.seriesNo }" />
		    <input type="hidden"  name="prpLReCaseVo.openReasonCode"  value="${prpLReCase.openReasonCode }" />
		    <input type="hidden"  name="prpLReCaseVo.openReason"  value="${prpLReCase.openReason }" />
		    <input type="hidden"  name="prpLReCaseVo.openReasonDetail"  value="${prpLReCase.openReasonDetail }" />
		    <input type="hidden"  name="prpLReCaseVo.endCaseUserCode"  value="${prpLReCase.endCaseUserCode }" />
		    <input type="hidden"  name="prpLReCaseVo.status"  value="${prpLReCase.status }" />
		    <input type="hidden"  name="prpLReCaseVo.checkStatus"  value="${prpLReCase.checkStatus }" />
		    <input type="hidden"  name="prpLReCaseVo.createUser"  value="${prpLReCase.createUser }" />
		    <input type="hidden"  name="prpLReCaseVo.flag"  value="${prpLReCase.flag }" />
		    <input type="hidden"  name="prpLReCaseVo.insuredName"  value="${prpLReCase.insuredName }" />
		    <input type="hidden"  name="prpLReCaseVo.mercyFlag"  value="${prpLReCase.mercyFlag }" />
		    <input type="hidden"  name="prpLReCaseVo.remark"  value="${prpLReCase.remark }" />
		     <input type="hidden"  name="currentNode" id="currentNode" value="${currentNode }" />
		    <c:set var="createTime">
				<fmt:formatDate value="${prpLReCase.createTime}" pattern="yyyy-MM-dd" />
			</c:set>
		        <input type="hidden"  name="prpLReCaseVo.createTime"  value="${createTime }" />
				<!-- 基本信息    -->
				<div class="table_wrap">
					<div class="table_cont">
						<div class="formtable">
							<div class="row cl">
								<div class="form_input col-md-offset-2 col-3 c-blue">该案件已结案，点击提交按钮重开</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">立案号</label>
								<div class="form_input col-3" >
								    <input type="text" class="input-text" name="prpLReCaseVo.claimNo" 
								    value="${prpLReCase.claimNo }" readonly="readonly"/>
								    <c:if test="${prpLReCase.flag == '1101' }">(交强)</c:if>
								    <c:if test="${prpLReCase.flag != '1101' }">(商业)</c:if>
						        </div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">重开赔案员</label>
								<div class="form_input col-3">
								    <input type="text" class="input-text"  name="prpLReCaseVo.openCaseUserName" 
								    value="${prpLReCase.openCaseUserName}"  readonly="readonly" />
								</div>
								<label class="form_label col-2">重开赔案日期</label>
								<div class="form_input col-3">
								    <c:set var="openCaseDate">
									    <fmt:formatDate value="${prpLReCase.openCaseDate}" pattern="yyyy-MM-dd" />
									</c:set>
								    <input type="text" class="input-text" name="prpLReCaseVo.openCaseDate"
										pattern="yyyy-MM-dd"  readonly="readonly" value="${openCaseDate}" />
								</div>
							</div>
							<div class="row cl">
								<label class="form_label col-2">交强险结案日期</label>
								<div class="form_input col-3">
								    <c:if test="${prpLReCase.flag == '1101' }">
								        <c:set var="endCaseDate">
									        <fmt:formatDate value="${prpLReCase.endCaseDate}" pattern="yyyy-MM-dd" />
									    </c:set>
								        <input type="text" class="input-text" name="prpLReCaseVo.endCaseDate"
										    pattern="yyyy-MM-dd"  readonly="readonly" value="${endCaseDate}" />
								    </c:if>
								</div>
								<label class="form_label col-2">商业险结案日期</label>
								<div class="form_input col-3">
								    <c:if test="${prpLReCase.flag != '1101' }">
								        <c:set var="endCaseDate">
									        <fmt:formatDate value="${prpLReCase.endCaseDate}" pattern="yyyy-MM-dd" />
									    </c:set>
								        <input type="text" class="input-text" name="prpLReCaseVo.endCaseDate"
										    pattern="yyyy-MM-dd"  readonly="readonly" value="${endCaseDate}" />
								    </c:if>
								</div>
							</div>
							
						</div>
					</div>
				</div>
				<!-- 输入信息    -->
					<div class="table_title f14">审核意见</div>
					<div class="row cl">
						<div class="col-12">
							<textarea  class="textarea" name="prpLReCaseVo.checkOpinion" id="openReason"
							datatype="*0-500" >${prpLReCase.checkOpinion} </textarea> 
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
										<c:when test="${fn:length(reCaseText.openReasonDetail ) > 10}"> 
											${fn:substring(reCaseText.openReasonDetail, 0, 10)}......
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
										<c:when test="${fn:length(reCaseText.checkOpinion ) > 10}"> 
											${fn:substring(reCaseText.checkOpinion, 0, 10)}......
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

		<div id="submitDiv" class="text-c mt-10">
		<c:if test="${handlerStatus!='3' }">
		    <input class="btn btn-primary ml-5" onclick="submit('save')"  type="submit" value="暂存"> 
			<c:if test="${status != '0' }"> 
			    <input class="btn btn-primary ml-5" onclick="submit('superior')" id="audit" type="submit" value="提交上级">
			    <input class="btn btn-primary ml-5" onclick="submit('pass')" id="submit" type="submit" value="审核通过">
			</c:if>
			<input class="btn btn-primary ml-5" onclick="submit('failed')"  type="submit" value="审核不通过"> 
			<!--  <input class="btn btn-primary ml-5" onclick="submit('return')"  type="submit" value="退回修改"> -->
		</c:if>
		</div>
	</div>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript" src="/claimcar/js/reOpenCase/reOpenCheck.js"></script>
</body>

</html>
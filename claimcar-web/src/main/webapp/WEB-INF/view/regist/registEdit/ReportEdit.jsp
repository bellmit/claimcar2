<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>报案-案件查询</title>
		<link href="../css/reportEdit.css" rel="stylesheet" type="text/css">
	</head>
	<body>
	
		<%--@include file="ReportEdit_RiskDialog.jspf"--%>
		<!-- <div class="top_btn">
			<a class="btn btn-primary" href="javascript:;" onClick="caseDetails(${prpLCMain.registNo})">历史出险信息（4）</a>
			<a class="btn btn-danger"  onclick="createRegistRisk()">风险提示信息</a>
			<a class="btn btn-primary" id="checkRegistMsg">案件备注</a>
			<a href="/claimcar/regist/relationship.do" class="btn btn-disabled">多保单关联与取消</a>
			<a class="btn btn-primary" >无保转有保</a>
			<a class="btn btn-primary" >客户信息</a>
		</div> -->
	<input type="hidden" id="queryCode_URLS" value="${queryCode_URLS}">
	<input type="hidden" id="addCode_URLS" value="${addCode_URLS}">
	<input type="hidden" id="registNos" value="${prpLRegistVo.registNo}">	
	<ul class="nav-menu clearfix">
		<li class="navMenuItem">&nbsp;
			<button onclick="viewEndorseInfo('${prpLRegistVo.registNo}')"
					type="button" class="btn btn-primary">保单批改记录</button>
			<c:choose>
				<c:when test="${prpLRegistVo.registTaskFlag eq '1'}">
					<button class="btn btn-primary" onclick="reportPrint('${prpLRegistVo.registNo}')">单证打印</button>
				</c:when>
				<c:otherwise>
					<button class="btn btn-disabled">单证打印</button>
				</c:otherwise>
			</c:choose>
		</li>
		<li class="navMenuItem">
			&nbsp;<button class="btn btn-primary" onclick="caseDetails('${prpLRegistVo.registNo}')">历史出险信息</button>
			</li>
		<li class="navMenuItem">
		&nbsp;<button class="btn btn-primary" onclick="scoreInfo('${prpLRegistVo.registNo}')">反欺诈评分</button>
		</li>
		<li class="navMenuItem">
			<c:choose>
				<c:when test="${prpLRegistVo.tempRegistFlag eq '1'}">
					&nbsp;<button class="btn btn-disabled" disabled="disabled">风险提示信息</button>
				</c:when>
				<c:otherwise>
					&nbsp;<button class="btn btn-danger" onclick="createRegistRisk()">风险提示信息</button>
				</c:otherwise>
			</c:choose>
		</li>
		
		<li class="navMenuItem">
			<c:choose>
				<c:when test="${empty prpLRegistVo.registTaskFlag}">
					&nbsp;<button class="btn btn-disabled" disabled="disabled" onclick="createRegistMessage('${prpLRegistVo.registNo}', 'Regis')" id="checkRegistMsg">案件备注</button>
				</c:when>
				<c:otherwise>
					&nbsp;<button class="btn btn-primary" id="checkRegistMsg" onclick="createRegistMessage('${prpLRegistVo.registNo}', 'Regis')">案件备注</button>
				</c:otherwise>
			</c:choose>
		</li>
		<li class="navMenuItem">
			<c:choose>
				<c:when test="${ !(prpLRegistVo.tempRegistFlag eq '1') && !(prpLRegistVo.registTaskFlag eq '7') && !(empty prpLRegistVo.registTaskFlag) && !(isCheckNodeEnd eq '1') && !(claim eq '1')}">
					&nbsp;<button onclick="relationshipList('${prpLRegistVo.registNo}','regist')" type="button"   id="relationship" class="btn btn-primary">多保单关联与取消</button>
					 
				</c:when>
				<c:otherwise>
					&nbsp;<input value="多保单关联与取消" type="button" class="btn btn-disabled" />
				</c:otherwise>
			</c:choose>
		</li>
		<li class="navMenuItem">
			<c:choose>
				<c:when test="${ (prpLRegistVo.tempRegistFlag eq 1) && !(prpLRegistVo.registTaskFlag eq 7) && !empty prpLRegistVo.registTaskFlag}">
					&nbsp;<button onclick="gotoPage('/claimcar/regist/noPolicyFindNoList.do?registNo=${prpLRegistVo.registNo}')" type="button" class="btn btn-primary">无保转有保</button>
				</c:when>
				<c:otherwise>
					&nbsp;<input value="无保转有保" type="button" class="btn btn-disabled" />
				</c:otherwise>
			</c:choose>
		</li>
		<li class="navMenuItem">
		&nbsp;<button class="btn btn-primary" id="addserviceinfo" onclick="addserviceInfo()">增值服务信息</button>
		</li>
		 <li class="navMenuItem">
		&nbsp;<button class="btn btn-primary" id="accidentInfo" onclick="accidentInfo('${prpLRegistVo.registNo}')">事故信息查询</button>
		</li>
		<c:if test="${hasRegistTask && !empty prpLRegistVo.registNo }">
			<li class="navMenuItem">
			<%-- &nbsp;<button class="btn btn-primary" onclick="window.open('${queryCode_URL}${prpLRegistVo.registNo}')">关联工单</button> --%>
			&nbsp;<button class="btn btn-primary" onclick="send('1')">关联工单</button>
			</li>
			<li class="navMenuItem">
			<%-- &nbsp;<button class="btn btn-primary" onclick="window.open('${addCode_URL}${prpLRegistVo.registNo}')">新建工单</button> --%>
			&nbsp;<button class="btn btn-primary" onclick="send('2')">新建工单</button>
			</li>
		</c:if>
		<li class="navMenuItem">
			<c:choose>
				<c:when test="${empty prpLRegistVo.registNo}">
					&nbsp;&nbsp;<button class="btn btn-disabled" disabled="disabled">德联易控检测信息</button>
				</c:when>
				<c:otherwise>
					&nbsp;&nbsp;<button class="btn btn-primary" onclick="cedlinfoshow('${prpLRegistVo.registNo}')">德联易控检测信息</button>
				</c:otherwise>
			</c:choose>
		</li>
		<li class="navMenuItem">
			&nbsp;&nbsp;<button class="btn btn-primary" onclick="feeImageView()">人伤赔偿标准</button>
			<button class="btn btn-primary" onclick="warnView('${prpLRegistVo.registNo}')">山东预警推送</button>
		</li>
		<!-- <li class="navMenuItem nav-menu-more" ><a class="">无保转有保</a>
		  <ul class="subNavMenu">
		     <li><a>点击我咯！</a></li>
		     <li><a>点击我咯！</a></li>
		     <li><a>点击我咯！</a></li>
		     <li><a>点击我咯！</a></li>
		     <li><a>点击我咯！</a></li>
		  </ul>
		</li> -->
	</ul>
		<form action="#" id="editform" >
		
		<div class="fixedmargin page_wrap">
			<!--保单信息   开始-->
			<div class="table_wrap">
            	<div class="table_title f14">保单信息</div>
				<div class="table_cont table_list borderCss">
					<table class="table table-border table-hover">
				 		<thead>
				 			<tr class="text-c">
				 				<th title="勾选本报案关联的保单">报案登记</th>
				 				<th>保单号</th>
				 				<th>车牌号码</th>
				 				<th>车型名称</th>
				 				<th>被保险人</th>
				 				<th>客户等级</th>
				 				<th>业务板块</th>
				 				<th>业务分类</th>
				 				<th>核心客户</th>
				 				<th>起保日期</th>
				 				<th>终保日期</th>
				 				<th>承保机构</th>
				 				<th>保单类型</th>
				 				<th>保单抄件打印</th>
				 			</tr>
				 		</thead>
				 		<tbody>
				 		    <input type="hidden" id="serviceMobile" value="${serviceMobile}" />
				 		    <input type="hidden" id="repairId" value="${repairId}" />
				 			<input type="hidden" name="prpLCMains" value="${prpLCMains}" />
				 			<input type="hidden" id="switchMap" name="switchMap" value="${switchMap}" />
				 			<input type="hidden" id="onlyReadySign" value="${onlyReadySign}" />
				 			<c:forEach var="prpLCMain" items="${prpLCMains}" varStatus="status">
				 				<tr class="text-c">
				 					<td>
				 						<c:choose>
									       <c:when test="${prpLCMain.riskCode eq '1101'}">
				 								<input name="checkCode" id="CIPolicyNo"  CIComCode="${prpLCMain.comCode}" CIRiskCode="${prpLCMain.riskCode}" value="${prpLCMain.policyNo}" checked="checked" group="ids" type="checkbox" policyType="CI" />
									       </c:when>
									       <c:otherwise>
									       		<input name="checkCode"  id="BIPolicyNo"  BIComCode="${prpLCMain.comCode}" BIRiskCode="${prpLCMain.riskCode}"  value="${prpLCMain.policyNo}" checked="checked" group="ids" type="checkbox" policyType="BI" />
									       </c:otherwise>
										</c:choose>
				 					</td>
		
				 					<td><a id="policyNo_${status.index }" data-hasqtip="0" href="/claimcar/policyView/policyView.do?policyNo=${prpLCMain.policyNo }&registNo=${prpLRegistVo.registNo}"
				 					 		target="_blank">${prpLCMain.policyNo}</a>
				 					<div class="hide" id="RiskKindLayer_${status.index }" style="display:none">
											 <table class="table table-border table-hover" >
											 		<thead>
											 			<tr>
											 				<th style="white-space: nowrap;">代码</th>
											 				<th style="white-space: nowrap;">险别名称</th>
											 				<th style="white-space: nowrap;">保险金额</th>
											 			</tr>
											 		</thead>
											 		<tbody>
									 					<c:forEach var="prpCItemKind" items="${prpLCMain.prpCItemKinds}" varStatus="status">
									 						<tr>
												 				<td>${prpCItemKind.kindCode}</td>
												 				<td nowrap="nowrap">${prpCItemKind.kindName}</td>
												 				<c:choose>
												 				<c:when test="${prpCItemKind.kindCode eq 'B' && weekDayriskFlag eq '1' }">
												 				<td>${prpCItemKind.amount/2}</td>
												 				</c:when>
												 				<c:otherwise>
												 				<td>${prpCItemKind.amount}</td>
												 				</c:otherwise>
												 				</c:choose>
												 				
												 				
											 				</tr>	
									 					</c:forEach>
											 			
											 		</tbody>
											 </table>
									</div>
				 					
				 					</td>										
				 					<td>${prpLCMain.prpCItemCars[0].licenseNo}</td>
				 					<td>${prpLCMain.prpCItemCars[0].brandName}</td>
				 					<c:forEach var="prpCInsured" items="${prpLCMain.prpCInsureds}" varStatus="status">
					 					<c:if test="${prpCInsured.insuredFlag eq '1'}">
					 						<td>${prpCInsured.insuredName}</td>
					 					</c:if>
				 					</c:forEach>
				 					<td></td>
				 					<td>
				 						<app:codetrans codeType="BusinessPlate" codeCode="${prpLCMain.businessPlate}"/>
				 					</td>
				 					<td>
				 					<font <c:if test="${prpLCMain.memberFlag=='1'}">class="c-red"</c:if >>
				 						<app:codetrans codeType="BusinessClass" codeCode="${prpLCMain.businessClass}"/>
				 					</font>
				 					</td>
									<td>
										<c:choose>
											<c:when test="${prpLCMain.isCoreCustomer eq '1'}">
												是
											</c:when>
											<c:otherwise>
												否
											</c:otherwise>
										</c:choose>
									</td>
				 					<td><fmt:formatDate value="${prpLCMain.startDate}" pattern="yyyy-MM-dd"/></td>
		 							<td><fmt:formatDate value="${prpLCMain.endDate}" pattern="yyyy-MM-dd"/></td>
				 					<td>
				 						<app:codetrans codeType="ComCodeFull" codeCode="${prpLCMain.comCode}"/>
				 					</td>
				 					<td>
				 						<c:choose>
									       <c:when test="${prpLCMain.riskCode eq '1101'}">
									    		交强
									    		<%-- <input type="hidden" id="CIPolicyNo" value="${prpLCMain.policyNo}" /> --%>
									       </c:when>
									       <c:otherwise>
									       		商业
									       		<%-- <input type="hidden" id="BIPolicyNo" value="${prpLCMain.policyNo}" /> --%>
									       </c:otherwise>
										</c:choose>
									</td>
									<c:choose>
									<c:when test="${prpLRegistVo.tempRegistFlag eq '1' }">
				 					<td><button  class="btn btn-disabled" >打印</button></td>
				 					</c:when>
				 					<c:otherwise>
				 					<td><button   class="btn btn-primary btn-outline btn-search" onclick="baocertifyPrint('${prpLCMain.policyNo}')" >打印</button></td>
				 					</c:otherwise>
				 					</c:choose>
				 				</tr>
				 				<!-- 险别信息  开始-->					 										 					
				 					
				 					<!-- 险别信息  结束-->
							</c:forEach>
				 		</tbody>
					</table>
				</div>
			</div>
			
			
			<!--保单信息     结束-->
			
			<!-- 增值服务信息   开始
			<div class="table_wrap">
            	<div class="table_title f14">增值服务信息</div>
				<div class="table_cont table_list borderCss">
					 <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th>增值服务项目</th>
					 				<th>可用次数</th>
					 				<th>已使用次数</th>
					 				<th>收费标准</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 	   </tbody>
					 </table>
				</div>
			</div>
			增值服务信息  结束 -->
			
			<!-- 报案主表隐藏域 开始 -->
			<input type="hidden" id="prpUrl" value="${prpUrl}"/>
			<input type="hidden" name="prpLRegistVo.flowId" value="${prpLRegistVo.flowId}"/>
			<input type="hidden" name="prpLRegistVo.policyNo" value="${prpLRegistVo.policyNo}"/>
			<input type="hidden" name="prpLRegistVo.riskCode" value="${prpLRegistVo.riskCode}"/>
			<input type="hidden" name="prpLRegistVo.comCode" value="${prpLCMains[0].comCode}"/>
			<input type="hidden" name="prpLRegistVo.firstRegComCode" value="${prpLRegistVo.firstRegComCode}"/>
			<input type="hidden" name="prpLRegistVo.tempRegistNo" value="${prpLRegistVo.tempRegistNo}"/>
			<input type="hidden" id="tempRegistFlag" name="prpLRegistVo.tempRegistFlag" value="${prpLRegistVo.tempRegistFlag}"/>
			<input type="hidden" id="registTaskFlag" name="prpLRegistVo.registTaskFlag" value="${prpLRegistVo.registTaskFlag}"/>
			<input type="hidden" id="reportType" name="prpLRegistVo.reportType" value="${prpLRegistVo.reportType}"/>
			<input type="hidden" name="prpLRegistVo.callId" value="${prpLRegistVo.callId}"/>
			<input type="hidden" name="prpLRegistVo.mercyFlag" value="${prpLRegistVo.mercyFlag}"/>
			<input type="hidden" name="prpLRegistVo.customerLevel" value="${prpLRegistVo.customerLevel}"/>
			<input type="hidden" name="prpLRegistVo.cancelFlag" value="${prpLRegistVo.cancelFlag}"/>
			<input type="hidden" name="prpLRegistVo.cancelTime" value="${prpLRegistVo.cancelTime}"/>
			<input type="hidden" name="prpLRegistVo.isMajorCase" value="${prpLRegistVo.isMajorCase}"/>
			<input type="hidden" name="prpLRegistVo.isoffSite" value="${prpLRegistVo.isoffSite}"/>
			<input type="hidden" name="prpLRegistVo.tpFlag" value="${prpLRegistVo.tpFlag}"/>
			<input type="hidden" name="prpLRegistVo.tpDisposeFlag" value="${prpLRegistVo.tpDisposeFlag}"/>
			<input type="hidden" name="prpLRegistVo.tpOffSiteCom" value="${prpLRegistVo.tpOffSiteCom}"/>
			<input type="hidden" name="prpLRegistVo.submitSource" value="${prpLRegistVo.submitSource}"/>
			<input type="hidden" name="prpLRegistVo.qdcaseType" value="${prpLRegistVo.qdcaseType}"/>
			<input type="hidden" name="prpLRegistVo.caseFlag" value="${prpLRegistVo.caseFlag}"/>
			<input type="hidden" id="paicReportNo" name="prpLRegistVo.paicReportNo" value="${prpLRegistVo.paicReportNo}"/>
			<input type="hidden" name="prpLRegistVo.createUser" value="${prpLRegistVo.createUser}"/>
			<input type="hidden" name="prpLRegistVo.createTime" value="<fmt:formatDate value='${prpLRegistVo.createTime}' type="both" />"/>
			<input type="hidden" name="prpLRegistVo.updateUser" value="${prpLRegistVo.updateUser}"/>
			<input type="hidden" name="prpLRegistVo.updateTime" value="<fmt:formatDate value='${prpLRegistVo.updateTime}' type="both" />"/>
			<!-- 报案主表隐藏域 结束 -->
			
			<!-- 报案扩展表隐藏域 开始 -->
			<input type="hidden" name="prpLRegistExtVo.registNo" value="${prpLRegistExtVo.registNo}"/>
			<input type="hidden" name="prpLRegistExtVo.policyNoLink" value="${prpLRegistExtVo.policyNoLink}"/>
			<input type="hidden" name="prpLRegistExtVo.licenseNo" value="${prpLCMains[0].prpCItemCars[0].licenseNo}"/>
			<input type="hidden" name="prpLRegistExtVo.frameNo" value="${prpLCMains[0].prpCItemCars[0].frameNo}"/>
			<c:forEach var="prpCInsured" items="${prpTempLCMain.prpCInsureds}" varStatus="status">
				<c:if test="${prpCInsured.insuredFlag eq '1'}">
					<input type="hidden" name="prpLRegistExtVo.insuredCode" value="${prpCInsured.insuredCode}"/>
					<input type="hidden" name="prpLRegistExtVo.insuredName" value="${prpCInsured.insuredName}"/>
				</c:if>
			</c:forEach>
			<input type="hidden" name="prpLRegistExtVo.updateUser" value="${prpLRegistExtVo.updateUser}"/>
			<input type="hidden" name="prpLRegistExtVo.updateTime" value="<fmt:formatDate value='${prpLRegistExtVo.updateTime}' type="both" />"/>
			<!-- 报案扩展表隐藏域 结束 -->
			
			<!-- 报案其他隐藏域 开始 -->
			<input type="hidden" name="tiJiao" id="tiJiao" value="0"/>
			<input type="hidden" name="policyNos" id="policyNos" />
			<input type="hidden" name="nodeCode" id="nodeCode" value="Regis"/>
			<!-- 报案其他隐藏域 结束 -->
			
			<!--案件人员信息    开始-->
			<div class="table_wrap">
            	<div class="table_title f14">案件人员信息</div>
				<div class="table_cont borderCss">
					<div class="formtable detailInformation">
						<div class="row cl">
							<div>
								<label class="form_label col-1">报案人</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" onblur="writeNull(1)" name="prpLRegistVo.reportorName"  id="reportorName" value="${prpLRegistVo.reportorName}" maxlength="15" datatype="s1-30" />
									<font class="form-text" color="red">*</font>
								</div>
							</div>
							
							<div>
								<label for="reportorPhone" class="form_label col-1">报案人电话</label>
								<div class="form_input col-3">
									<input type="text" id="reportorPhone" style="width: 80%"  onblur="writeNull(2)" class="input-text"  name="prpLRegistVo.reportorPhone" value="${prpLRegistVo.reportorPhone}" datatype="mlandline" />
									<font class="form-text" color="red">*</font>
								</div>
							</div>
							<div>
								<label class="form_label col-1">被保险人电话</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" id="insuredPhone"  name="prpLRegistVo.insuredPhone" value="${prpLRegistVo.insuredPhone}" datatype="mlandline" />
									<font class="form-text" color="red" id="insuredFlag">*</font>
								</div>
							</div>
							<%-- <div>
								<label class="form_label col-1">报案人身份证</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" onblur="writeNull(3)" id="reportorIdfNo" maxlength="18" name="prpLRegistVo.reportorIdfNo" value="${prpLRegistVo.reportorIdfNo}" ignore="ignore" errormsg="身份证格式不正确！"
									datatype="idNo" />
								</div>
							</div> --%>
						</div>
						<%--<div class="row cl">

							 <div>
								<label class="form_label col-1">驾驶员电话</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" id="driverPhone"  name="prpLRegistVo.driverPhone" value="${prpLRegistVo.driverPhone}" datatype="m|/^0\d{2,3}-\d{7,8}$/" ignore="ignore"/>
								</div>
							</div>
							<div>
								<label class="form_label col-1">驾驶员身份证</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" id="driverIdfNo" name="prpLRegistVo.driverIdfNo" maxlength="18" value="${prpLRegistVo.driverIdfNo}" ignore="ignore" datatype="idcard" />
								</div>
							</div>
						</div>--%>
						<div class="row cl">
							<div>
								<label class="form_label col-1">联系人</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" id="linkerName" name="prpLRegistVo.linkerName" value="${prpLRegistVo.linkerName}" maxlength="15" datatype="s1-30" />
									<font class="form-text" color="red">*</font>
								</div>
							</div>
							<div>
								<label class="form_label col-1">联系人电话1</label>
								<div class="form_input col-3">
									<c:choose>
									<c:when test="${(prpLRegistVo.tempRegistFlag eq '1') || (subComCode eq '11')}">
									<input type="text" style="width: 80%" class="input-text" id="linkerMobile"  name="prpLRegistVo.linkerMobile" value="${prpLRegistVo.linkerMobile}" datatype="m" />
									</c:when>
									<c:otherwise>
									<input type="text" style="width: 80%" class="input-text" id="linkerMobile"  name="prpLRegistVo.linkerMobile" value="${prpLRegistVo.linkerMobile}" datatype="mlandline" />
									</c:otherwise>
									</c:choose>
									<font class="form-text" color="red">*</font>
								</div>
							</div>
							<div>
								<label class="form_label col-1">联系人电话2</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" id="linkerPhone"  name="prpLRegistVo.linkerPhone" value="${prpLRegistVo.linkerPhone}" datatype="mlandline"  ignore="ignore"/>
								</div>
							</div>
						</div>
						
						<div class="row cl">
						<div>
								<label for="prpLRegistVo.driverName" class="form_label col-1">驾驶员</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" id="driverName" name="prpLRegistVo.driverName" value="${prpLRegistVo.driverName}" maxlength="15" datatype="s1-30"/>
									<font class="form-text" color="red">*</font>
								</div>
							</div>
							<%-- <div>
								<label class="form_label col-1">与被保人关系</label>
								<div class="form_input col-3">
									<span class="select-box"  style="width: 80%">
										<app:codeSelect style="width: 100%" codeType="InsuredIdentity" id="reportorRelation" name="prpLRegistVo.reportorRelation" value="${prpLRegistVo.reportorRelation}" type="select" />

									</span>
									<font class="must">*</font>
								</div>
							</div> --%>

						</div>
					</div>
				</div>
			</div>
			<!--案件人员信息    结束-->
			
			<!--报案信息   开始-->
			<div class="table_wrap">
            	<div class="table_title f14">报案信息</div>
				<div class="table_cont borderCss">
					<div class="formtable">
				 		<div class="row cl">
			 				<label class="form_label col-1">报案号</label>
							<div class="form_input col-3">
								${prpLRegistVo.registNo}
								<input type="hidden" id="registNo" name="prpLRegistVo.registNo" value="${prpLRegistVo.registNo}" />
							</div>
							<label class="form_label col-1">初登座席工号</label>
							<div class="form_input col-3">
								${prpLRegistVo.firstRegUserCode}
								<input type="hidden" name="prpLRegistVo.firstRegUserCode" value="${prpLRegistVo.firstRegUserCode}" />
							</div>
							<label class="form_label col-1">初登座席姓名 </label>
							<div class="form_input col-3">
								${prpLRegistVo.firstRegUserName}
								<input type="hidden" name="prpLRegistVo.firstRegUserName" value="${prpLRegistVo.firstRegUserName}" />
							</div>
				 		</div>
					</div>
					<div class="formtable">
				 		<div class="row cl">
			 				<label class="form_label col-1">报案时间</label>
							<div class="form_input col-3">
								<fmt:formatDate value="${prpLRegistVo.reportTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								<input type="hidden" name="prpLRegistVo.reportTime" value="<fmt:formatDate value='${prpLRegistVo.reportTime}' pattern='yyyy-MM-dd HH:mm:ss' />" />
							</div>
							<label class="form_label col-1">出险时间 </label>
							<div class="form_input col-3">
								<fmt:formatDate value="${prpLRegistVo.damageTime}" pattern="yyyy-MM-dd HH:mm"/>
								<input type="hidden" name="prpLRegistVo.damageTime" value="<fmt:formatDate value='${prpLRegistVo.damageTime}' pattern='yyyy-MM-dd HH:mm'/>" />
								<input type="hidden" id="damageTime" name="prpLRegistVo.damageTime" value="<fmt:formatDate value='${prpLRegistVo.damageTime}' pattern='yyyy-MM-dd HH:mm'/>" />
							</div>
							<label class="form_label col-1">事故原因</label>
							<div class="form_input col-3 clearfix">
								<span class="select-box" style="width: 60%">
									<app:codeSelect codeType="AccidentReasonCode" name="prpLRegistVo.accidentReason" value="${prpLRegistVo.accidentReason}" type="select"/>
									</span>
				 		</div>

					</div>
					<div class="formtable">
				 		<div class="row cl">
					<label class="form_label col-1">是否现场报案</label>
							<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" nullToVal="1" name="prpLRegistExtVo.isOnSitReport"  value="${prpLRegistExtVo.isOnSitReport}" 
								clazz="mr-5" id="isOnSitReport"/>
								<!-- <font class="form-text" color="red">*</font> -->
							</div>
							</div>
							</div>
					<div class="formtable ">
				 		<div class="row cl">
			 				<label class="form_label col-1">出险原因</label>
							<div class="form_input col-3 clearfix">
								<span class="select-box" style="width: 45%">
									<app:codeSelect id="damage_Code" codeType="DamageCode" name="prpLRegistVo.damageCode" value="${prpLRegistVo.damageCode}" type="select" clazz="must" onchange="damageSelect(this)"/>
								</span>
								<font class="form-text" color="red">*</font>
								<span class="select-box hidden" style="width: 45%"> <app:codeSelect
										codeType="DamageCode2" type="select" lableType="name" clazz="must"
										id="dam_other" name="prpLRegistVo.damageOtherCode" 
										value="${prpLRegistVo.damageOtherCode}" />
								</span>
							</div>
				 		</div>
					</div>
					<div class="formtable ">
				 		<div class="row cl accident-over-add">
			 				<label class="form_label col-1">出险所在地</label>
							<div class="form_input col-11 clearfix selectDiv">
							    <div id="damageAddressDiv" style="display:inline">
								<app:areaSelect targetElmId="damageAreaCode" areaCode="${prpLRegistVo.damageAreaCode}" showLevel="3" handlerStatus="${handlerStatus}"/>
								</div>
								<input type="hidden" id="damageAreaCode" name="prpLRegistVo.damageAreaCode" value="${prpLRegistVo.damageAreaCode}" />
								<input type="hidden" id="damageMapCode" name="prpLRegistVo.damageMapCode" value="${prpLRegistVo.damageMapCode}" />
								<input type="text" id="damageAddress" readonly name="prpLRegistVo.damageAddress" value="${prpLRegistVo.damageAddress}" class="input-text mr-10 w_25" placeholder="详细地址" datatype="*" style="font-size: 13px;"/>
								<input id="selfDefinareaCode" name="prpLRegistVo.selfDefinareaCode" value="${prpLRegistVo.selfDefinareaCode}" type="hidden" />
								<input id="isOrMarkCode" value="0" type="hidden" />
								
								<font class="form-text" color="red">*</font>
								<input type="button" id="switchMapes" class="btn btn-zd" onclick="openDamageMap()" value="电子地图" />
								<!-- <input type="button" class="btn btn-zd" value="地名字典" /> -->
								<!-- <input type="button" class="btn btn-zd" onclick="changeDAStatus(this)" value="地点输入开关(关)" /> -->
							</div>
				 		</div><br/>
						<c:if test="${fn:substring(prpLCMains[0].comCode,0,2) eq '22'}">
							<div class="row cl accident-over-add">
								<label class="form_label col-1">上海出险地点</label>
								
								<div style="display: inline">
									<c:if test="${empty prpLRegistVo.shDamageAreaCode  }">
										<app:areaSelect targetElmId="ShDamageAreaCode" areaCode="310101" showLevel="3" clazz="must" handlerStatus="${handlerStatus}"/>
									</c:if>
									<c:if test="${!empty prpLRegistVo.shDamageAreaCode  }">
										<app:areaSelect targetElmId="ShDamageAreaCode" areaCode="${prpLRegistVo.shDamageAreaCode }" showLevel="3" clazz="must" handlerStatus="${handlerStatus}"/>
									</c:if>
								</div>
								<input type="hidden" id=ShDamageAreaCode name="prpLRegistVo.shDamageAreaCode" value="${prpLRegistVo.shDamageAreaCode }" />
								<input type="hidden" name="prpLRegistVo.shDamageAddress" value="${prpLRegistVo.shDamageAreaCode }" />
								<font class="form-text" color="red">*</font>
							</div>
						</c:if>
					</div>					 
				</div>
			</div>
			<!--报案信息   结束-->
			<div class="table_wrap">
			<div class="table_title f14">损失信息</div>
			<div class="panel panel-primary">
				<div class="panel-header">
					<div class="formtable ">
				 		<div class="row cl">
				 			<label class="form_label col-1">是否有车损</label>
							<div class="form_input col-3 clearfix">
								<label class="radio-box">
									<input type="radio" name="car" checked="checked" disabled="disabled" id="isCarLossY" class="SunSiRadio mr-5" value="1" onclick="lossOrNot(1,'CheSun')" />是
								</label>
								<label class="radio-box">
									<input type="radio" name="car" id="isCarLossN" disabled="disabled" class="SunSiRadio mr-5" value="0" onclick="lossOrNot(0,'CheSun')" />否
								</label>
								<input id="CheSun" name="prpLRegistExtVo.isCarLoss" type="hidden" value="1" />
							</div>								
				 			<%-- <label class="form_label col-1">是否有人伤</label>
				 			<div class="form_input col-3 clearfix">
								  <label class="radio-box">
									<input type="radio" name="person" id="isPersonLossY" class="SunSiRadio mr-5" <c:if test="${prpLRegistExtVo.isPersonLoss eq '1'}">checked="checked"</c:if> value="1" onclick="lossOrNot(1,'RenShang')" />是
								  </label>
								  <label class="radio-box">
									<input type="radio" name="person" id="isPersonLossN" class="SunSiRadio mr-5" <c:if test="${prpLRegistExtVo.isPersonLoss != '1'}">checked="checked"</c:if> value="0" onclick="lossOrNot(0,'RenShang')" />否
								  </label>
								<input id="RenShang" name="prpLRegistExtVo.isPersonLoss" type="hidden" value="${prpLRegistExtVo.isPersonLoss}" />
							</div>
							<label class="form_label col-1">是否有物损</label>
				 			<div class="form_input col-3 clearfix">
								<label class="radio-box">
									<input type="radio" name="prop" id="isPropLossY" class="SunSiRadio mr-5" value="1" <c:if test="${prpLRegistExtVo.isPropLoss eq '1'}">checked="checked"</c:if> onclick="lossOrNot(1,'WuSun')" />是
								</label>
								<label class="radio-box">
									<input type="radio" name="prop" id="isPropLossN" class="SunSiRadio mr-5" checked="checked" value="0" <c:if test="${prpLRegistExtVo.isPropLoss != '1'}">checked="checked"</c:if> onclick="lossOrNot(0,'WuSun')" />否
								</label>
								<input id="WuSun" name="prpLRegistExtVo.isPropLoss" type="hidden" value="${prpLRegistExtVo.isPropLoss}" />
							</div> --%>
				 		</div>
				 	</div>
				 </div>
				<div class="panel-body">
					<div class="table_wrap hide" id="CheSunTable">
						<%@include file="ReportEdit_CheSun.jspf" %>
						<!-- <div class="line_dashed">
						</div> -->
					</div>
					<br/>
					<div class="panel panel-primary">
				    <div class="panel-header">
					<div class="formtable ">
					<div class="row cl">
				 			<label class="form_label col-1"><font color="blue" size="2">是否有人伤</font></label>
				 			<div class="form_input col-3 clearfix">
								  <label class="radio-box">
									<input type="radio" name="person" id="isPersonLossY" class="SunSiRadio mr-5" <c:if test="${prpLRegistExtVo.isPersonLoss eq '1'}">checked="checked"</c:if> value="1" onclick="lossOrNot(1,'RenShang')" /><font color="blue">是</font>
								  </label>
								  <label class="radio-box">
									<input type="radio" name="person" id="isPersonLossN" class="SunSiRadio mr-5" <c:if test="${prpLRegistExtVo.isPersonLoss != '1'}">checked="checked"</c:if> value="0" onclick="lossOrNot(0,'RenShang')" /><font color="blue">否</font>
								  </label>
								<input id="RenShang" name="prpLRegistExtVo.isPersonLoss" type="hidden" value="${prpLRegistExtVo.isPersonLoss}" />
							</div>
					</div>
					</div></div>
					<div class="table_wrap hide" id="RenShangTable">
						<%@include file="ReportEdit_RenShang.jspf" %>
						<!-- <div class="line_dashed">
						</div> -->
					</div>
					</div>
					<br/>
					<div class="panel panel-primary">
				    <div class="panel-header">
					<div class="formtable ">
					<div class="row cl">
					   <label class="form_label col-1"><font color="blue" size="2">是否有物损</font></label>
				 			<div class="form_input col-3 clearfix">
								<label class="radio-box">
									<input type="radio" name="prop" id="isPropLossY" class="SunSiRadio mr-5" value="1" <c:if test="${prpLRegistExtVo.isPropLoss eq '1'}">checked="checked"</c:if> onclick="lossOrNot(1,'WuSun')" /><font color="blue">是</font>
								</label>
								<label class="radio-box">
									<input type="radio" name="prop" id="isPropLossN" class="SunSiRadio mr-5"  value="0" <c:if test="${prpLRegistExtVo.isPropLoss != '1'}">checked="checked"</c:if> onclick="lossOrNot(0,'WuSun')" /><font color="blue">否</font>
								</label>
								<input id="WuSun" name="prpLRegistExtVo.isPropLoss" type="hidden" value="${prpLRegistExtVo.isPropLoss}" />
							</div>
					</div>
					</div></div>		
					<div class="table_wrap hide" id="WuSunTable">
						<%@include file="ReportEdit_WuSun.jspf" %>
					</div>
				</div>
			</div>
			</div>
			</div>
			<!-- 联共保 -->
		   	<c:if test="${!empty prpLCMains && prpLCMains[0].coinsFlag !=0 && prpLCMains[0].coinsFlag != null}">		   	
				<%@include file="ReportEdit_IsGB.jspf"%>
		   	</c:if>
			<!--事故处理信息   开始-->
			<div class="table_wrap aciidentWrap">
            	<div class="table_title f14">事故处理信息</div>
				<div class="table_cont ">
					 <div class="formtable ">
					 	<div class="row cl">
					 		<label class="form_label col-2">事故类型</label>
							<div class="form_input col-3">
								<span class="select-box" style="width: 80%">
									<app:codeSelect id="AccidentTypes" codeType="AccidentDutyType" name="prpLRegistExtVo.accidentTypes" value="${prpLRegistExtVo.accidentTypes}" type="select" clazz="must"/>
								</span>
							</div>
							<label class="form_label col-2">事故处理类型</label>
					 		<div class="form_input col-3">
					 			<span class="select-box" style="width: 80%">
									<app:codeSelect codeType="AccidentManageType" name="prpLRegistExtVo.manageType" value="${prpLRegistExtVo.manageType}" type="select" clazz="must"/>
								</span>
								<font class="form-text" color="red">*</font>
					 		</div>
					 	</div>
					 </div>
					 
					 <div class="formtable ">
					 	<div class="row cl">
							<label class="form_label col-2">是否报警</label>
							<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" nullToVal="1" name="prpLRegistExtVo.isAlarm"  value="${prpLRegistExtVo.isAlarm}" 
									clazz="mr-5"/>
								<!-- <font class="form-text" color="red">*</font> -->
							</div>
							<label class="form_label col-2">事故责任划分</label>
					 		<div class="form_input col-3">
					 			<span class="select-box" style="width: 80%">
									<app:codeSelect codeType="IndemnityDuty" name="prpLRegistExtVo.obliGation" id="obliGation" value="${prpLRegistExtVo.obliGation}" type="select" clazz="must"/>
								</span>
								<font class="form-text" color="red">*</font>
					 		</div>
					 	</div>
					 </div>
					 
					 <div class="formtable ">
					 	<div class="row mb-5 cl">
					 		<label class="form_label col-2">车辆是否可行 </label>
							<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" nullToVal="1" name="prpLRegistExtVo.isCantravel"  value="${prpLRegistExtVo.isCantravel}" 
									clazz="mr-5" />
								<!-- <font class="form-text" color="red">*</font> -->
							</div>
							<label class="form_label col-2">查勘类型</label>
					 		<div class="form_input col-3">
					 			<span class="select-box" style="width: 80%">
									<app:codeSelect codeType="CheckType" id ="checkType" name="prpLRegistExtVo.checkType" value="${prpLRegistExtVo.checkType}" type="select" clazz="must" />
								</span>
								<font class="form-text" color="red">*</font>
					 		</div>
					 	</div>
					 	<div class="row mb-5 cl">
					 		<label class="form_label col-2">是否进行相关施救</label>
					 		<div class="form_input col-3">
						 		<app:codeSelect codeType="YN10" type="radio" name="prpLRegistExtVo.isRescue"  value="${prpLRegistExtVo.isRescue}" 
										clazz="mr-5"  nullToVal="0" disabled='true'/>
								<!-- <font class="form-text" color="red">*</font> -->
					 		</div>
					 		<label class="form_label col-2">自助查勘</label>
					 		<div class="form_input col-4 is-you-Check">
					 			<app:codeSelect codeType="YN10" type="radio" name="prpLRegistExtVo.isCheckSelf"  value="${prpLRegistExtVo.isCheckSelf}" 
										clazz="mr-5"  nullToVal="0"/>
								<a class="btn btn-zd fl isyoucheck">判断自主查勘</a>
					 		</div>
					 	</div>
					 	<div class="row cl">
					 		<label class="form_label col-2">是否互碰自赔</label>
					 		<div class="form_input col-3">
					 			<app:codeSelect codeType="YN10" type="radio" name="prpLRegistExtVo.isClaimSelf"  value="${prpLRegistExtVo.isClaimSelf}" 
										clazz="mr-5"  nullToVal="0"/>
					 		</div>
					 		<label class="form_label col-2">是否代位求偿</label>
					 		<div class="form_input col-3">
					 			<app:codeSelect codeType="YN10" type="radio" name="prpLRegistExtVo.isSubRogation" value="${prpLRegistExtVo.isSubRogation}" 
										clazz="mr-5"  nullToVal="0"/>
					 		</div>
					 	</div>
					 	<div class="row cl">
					 		<label class="form_label col-2">约定查勘地点</label>
					 		<div class="form_input col-10 selectDiv">
					 		    <div style="display:inline" id="checkAddressDiv">
					 			<app:areaSelect targetElmId="checkAddressCode" areaCode="${prpLRegistExtVo.checkAddressCode}" showLevel="3" handlerStatus="${handlerStatus}"/>
					 			</div>
					 			<input type="hidden" id="checkAddressCode" name="prpLRegistExtVo.checkAddressCode" value="${prpLRegistExtVo.checkAddressCode}" >
								<input id="checkAddress" readonly name="prpLRegistExtVo.checkAddress"  value="${prpLRegistExtVo.checkAddress}" type="text" class="input-text w_25 mr-5" placeholder="详细地址" datatype="*" />
								<input id="checkAddressMapCode" name="prpLRegistExtVo.checkAddressMapCode" value="${prpLRegistExtVo.checkAddressMapCode}" type="hidden" />
								
								<font class="form-text" color="red">*</font>
								<input type="button" id="switchMaps" class="btn btn-zd" onclick="openCheckMap()" value="电子地图" />
					 		</div>
					 	</div>
					 </div>
				</div>
			</div>			
			<!--事故处理信息   结束-->
		    <!--巨灾-->
		    <%@include file="ReportEditDisaster.jspf"%>
		  
			<!--底部信息 开始-->
			<div class="row cl bottomNotice">
				<!--出险经过   开始-->
				<div class="form_input col-6 noticeLeft">
					<div class="notice_title clearfix">
						<div class="f-l f14"><strong class="acc-over">出险经过<font class="form-text" color="red">*</font></strong></div>
						<div class="f-r pr-20 mr-10 mb-5"><input class="btn mspy btn-secondary size-MINI" type="button"  value="自动生成出险经过" onclick="createDangerRemark()"></div>
					</div>
                    <textarea class="textarea w90" name="prpLRegistExtVo.dangerRemark" maxlength="1000" datatype="*1-1000" placeholder="请输入...">${prpLRegistExtVo.dangerRemark}</textarea>
                    
				</div>
				<!--出现经过   结束-->
				<!--备注  开始-->
				<div class="form_input col-6 noticeRight">
					<div class="notice_title clearfix">
						<div class="fl f14 mb-5"><strong class="acc-over">备注</strong></div>
					</div>
					<textarea class="textarea w90" name="prpLRegistExtVo.registRemark" maxlength="200" datatype="*0-200" placeholder="请输入...">${prpLRegistExtVo.registRemark}</textarea>
				</div>
				<!--备注   结束-->
			</div>
			<!--底部信息 结束-->
			<!--报案注销开始-->
			<c:if test="${ ((prpLRegistVo.registTaskFlag eq 1) || (prpLRegistVo.registTaskFlag eq 7))&&(flags eq 0)}">
				<%-- <div class="table_wrap">
            	<div class="table_title f14">案件人员信息</div>
				<div class="table_cont borderCss">
					<div class="formtable detailInformation">
						<div class="row cl">
							<div>
								<label class="form_label col-1">报案人</label>
								<div class="form_input col-3">
									<input type="text" style="width: 80%" class="input-text" onblur="writeNull(1)" name="prpLRegistVo.reportorName" id="reportorName" value="${prpLRegistVo.reportorName}" maxlength="30" datatype="s1-30" />
									<font class="form-text" color="red">*</font>
								</div>
							</div> --%>
				<%-- <div class="row cl bottomNotice">
				<c:if test="${(prpLRegistVo.registTaskFlag eq 1&&(flags eq 0)&&(claim eq 0))||(!empty prpLRegistVo.cancelTime) }">
					<div class="btn-footer clearfix">
						<div class="f-l f14"><strong class="acc-over">报案注销</strong></div>
					</div>
				</c:if>
				</div> --%>
				<div class="row cl bottomNotice">
					<div  class="btn-footer clearfix">
						<div class="row cl">
							<div>
								<div class="form_input col-2">
								</div>
								<%-- <c:if test="${prpLRegistVo.registTaskFlag eq 1&&(flags eq 0)&&(claim eq 0)}">
									<label class="form_label col-1">注销原因：</label>
									<div class="form_input col-4">
										<app:codeSelect codeType="ReportCancel" type="select" value="${prpLRegistExtVo.cancelReason}" clazz="mr-5" />
									</div>
								</c:if>  --%>
								<div class="form_input col-1">
								</div>
								<div id="cancelIn" class="form_input col-3">
									<%--  <c:choose>
										<c:when test="${prpLRegistVo.registTaskFlag eq '1' && (flags eq '0') && (claim eq '0')}">
											<input id="reportCancel" class="btn btn-primary btn-cancelBtn" type="button" value="注销" />
										</c:when>
										<c:otherwise>
										<c:if test="${!empty prpLRegistVo.cancelTime} ">
											<%@include file="ReportEdit_CancelReturn.jsp"%>
										</c:if>
										</c:otherwise>
										 <c:when test="${empty prpLRegistVo.cancelTime} ">
											<%@include file="ReportEdit_CancelReturn.jsp"%>
										</c:when>
									</c:choose>  --%>

									<c:if test="${(!empty prpLRegistVo.cancelTime) }">
										<%@include file="ReportEdit_CancelReturn.jsp"%>
									</c:if>
								</div>
							</div>
							<%-- <div class="f-l pl-20 ml-10 mb-5">注销原因：</div>
							<app:codeSelect codeType="ReportCancel" type="select" clazz="mr-5" />
							<input id="pend" class="btn btn-primary" type="button" value="注销" /> --%>
						</div>
					</div>
				</div>
			</c:if>
			<!--报案注销开始-->
			
			<div class="btn-footer clearfix">
				<input class="btn btn-primary" style="margin-left: 35%;" type="button" value="短信处理" />
				<%-- <c:if test="${ !(prpLRegistVo.registTaskFlag eq 1) && !(prpLRegistVo.registTaskFlag eq 7)}"> --%>
				<c:if test="${ !(workState eq 3) && !(workState eq 9)}">
					<input id="pend" class="btn btn-success btn-saveBtn" type="button" value="暂存" />
					<!-- <input id="submit" name="registSubmit" class="btn btn-success btn-saveBtn" type="submit" value="提交" /> -->
					 <input id="submit" name="registSubmit" class="btn btn-success btn-saveBtn" type="button" value="提交" />
				</c:if>
				<c:choose>
					<c:when test="${empty prpLRegistVo.registTaskFlag}">
						<button class="btn btn-disabled" type="button" >多保单关联与取消轨迹</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-primary" type="button" onclick="caseDetailss('多保单关联与取消轨迹','/claimcar/regist/relationshipHis.do?registNo=${prpLRegistVo.registNo}','1')">多保单关联与取消轨迹</button>
					</c:otherwise>
				</c:choose>
				<c:if test="${prpLRegistVo.registTaskFlag eq '1'}">
					<button id="reportCancel" class="btn btn-primary btn-cancelBtn" type="button"  onclick="caseCancle()">注销</button>
				</c:if>
				<div id="RegistRisk"></div>
			</div>
			
		</div>
		<input type="hidden" value="1" id="lossesA"/>
		<input type="hidden" value="${completeClaimFlag}" id="completeClaimFlag"/>
</form>
<script type="text/javascript" src="/claimcar/js/registEdit/ReportEdit.js"></script>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript">
		$(function() {
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon",
					"current", "click", "0");
			
			disTwChange();
			//判断地图有没有关，1为关闭,出险所在地可以手动录入
			if($("#switchMap").val()=="1"){
				$("#damageAddress").removeAttr("readonly");
				$("#checkAddress").removeAttr("readonly");
				$('#switchMapes').attr("disabled",true); 
				$('#switchMapes').addClass("btn-disabled");
				$('#switchMaps').attr("disabled",true); 
				$('#switchMaps').addClass("btn-disabled");
			}
			$("[name$='loss']").each(function(){
				var selVal = $(this).prev("input").val();
					$(this).find("option").each(function(){
						if(selVal==$(this).val()){
						$(this).attr("selected","selected");
						}
					});
			});
			
			//广西保单或者无保单报案，联系人电话必须为手机号码
			/* var comcode = $("[name='prpLRegistVo.comCode']").val();
			var tempRegistFlag = $("#tempRegistFlag").val();
			if(comcode.substring(0,2)=="11"||tempRegistFlag=="1"){
				$("#linkerMobile").removeAttr("datatype");
				$("#linkerMobile").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				$("#linkerMobile").attr("datatype","m|/^1\d{10}$/");
				//$("#linkerMobile").attr("datatype","/^1\d{10}$/|/^0\d{2,3}-\d{7,8}$/");
			} */
			
			//与被保险人关系选择是【本人】的话【被保险人电话】系统自动带入报案人电话，如果报案人和被保险人关系是除【本人】外的其他选项则不自动带入，且把此项设置为非必填项
			var reportorRelation = $("#reportorRelation").val();
			if(reportorRelation=="01"){
			var reportorPhone = $("#reportorPhone").val();
			$("#insuredPhone").val(reportorPhone);
			$("#insuredFlag").removeClass("hidden");
			//$("[name='prpLRegistVo.insuredPhone']").attr("datatype","m");
			}else{
				$("[name='prpLRegistVo.insuredPhone']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
				$("#insuredFlag").addClass("hidden");
			}
			
			
		});
		$("#checkRegistMsg").click(function checkRegistMsgInfo() {//打开案件备注之前检验报案号和节点信息是否为空
			var registNo = $("#registNo").val();
			var nodeCode = $("#nodeCode").val();
			createRegistMessage(registNo, nodeCode);

		});
		$("input[name$='isOnSitReport']").click(function xuan(){
			var isOnSitReport = $("input[name$='isOnSitReport']:checked").val();
			if(isOnSitReport==1){
			
				$("#checkType").val(3);
		/* 		$("[name$='checkType']").find("option").each(function(){
					var checkType = $(this).val();
					
					if(checkType==3){
						alert("312312");
						//this.attr("selected","selected");
						this.prop("selected",true);
					}
				}); */
			}else{
				$("#checkType").val(4);
			/* 	$("[name$='checkType']").find("option").each(function(){
					var checkType = $(this).val();
					if(checkType==4){
						alert("312312");
						$("#checkType").val(4);
						//this.attr("selected","selected");
						this.prop("selected",true);
					}
				}); */
			}
		});
	/* 	$("[name$='damageCode']").click(function xuan(){
			
			alert(this.val());
		}); */
		
		$("#loss").click(function xuan(){
			var loss = $("#loss").val();
			if(loss=='其他'){
				$("#losses").removeClass("hidden");
			}else{
				$("#losses").addClass("hidden");
			}
		});
	/* 	$("[name$='loss']").click(function xuan(){
			alert("3123");
			var loss = $("#loss").val();
			if(loss=='其他'){
				$("#losses").removeClass("hidden");
			}
		}); */
		function LossXuan(a){
			var loss = $("#Loss"+a).val();
			if(loss=='其他'){
				$("#Loss_"+a).removeClass("hidden");
			}else{
				$("#Loss_"+a).addClass("hidden");
			}
			
		}
		if($("#switchMap").val()!="1"){
			$("input[name$='isOnSitReport']").click(function panDuan(){
				var isOnSitReport = $(this).val();
				if(isOnSitReport==0){
					$("#damageAddress").removeAttr("readonly");
				}else{
					$("#damageAddress").attr("readonly","readonly");
					$("#damageAddress").val("");
					damageAddressFlags=0;
				}
			});
		}
		//与被保险人关系选择是【本人】的话【被保险人电话】系统自动带入报案人电话，如果报案人和被保险人关系是除【本人】外的其他选项则不自动带入，且把此项设置为非必填项
		$("#reportorRelation").change(function(){
			var reportorRelation = $(this).val();
			if(reportorRelation=="01"){
			var reportorPhone = $("#reportorPhone").val();
			$("#insuredPhone").val(reportorPhone);
			$("#insuredFlag").removeClass("hidden");
			//$("[name='prpLRegistVo.insuredPhone']").attr("datatype","m");
			}else{
				$("#insuredFlag").addClass("hidden");
				$("#insuredPhone").val("");
			$("[name='prpLRegistVo.insuredPhone']").removeAttr("datatype").removeClass("Validform_error").qtip('destroy', true);
			}
		});
		//报案人不是被保险人的情况下，“与被保险人关系”不要默认为本人，需清空
		$("input[name='prpLRegistVo.reportorName']").change(function(){
			var insuredName = $("input[name='prpLRegistExtVo.insuredName']").val();
			if(insuredName!=$(this).val()){
				$("select[name='prpLRegistVo.reportorRelation'] option").each(function(){
						if($(this).val()==""){
							$(this).attr("selected",true); 
						}
					});
			}else{
				$("select[name='prpLRegistVo.reportorRelation'] option").each(function(){
						if($(this).val()=="01"){
							$(this).attr("selected",true); 
						}
					});
			}

		});
		
	
		//保单打印
	function baocertifyPrint(policyNo){
	 /* var url = "http://10.0.47.101:7024/prpcar/prpc/copyPrintFormat?policyNo="+policyNo;  */
		var prpUrl=$("#prpUrl").val();
		var url = prpUrl+"/prpc/copyPrintFormat?policyNo="+policyNo;
        openTaskEditWin("保单打印", url);
        
        
		
	}
		function certifyPrint(registNo){
			var params = "?registNo=" + registNo;
			var url = "/claimcar/certifyPrint/prpLRegist.ajax";
			var index = layer.open({
				type : 2,
				title : "机动车辆保险报案记录",
				maxmin : true, // 开启最大化最小化按钮
				content : url + params
			});
			layer.full(index);
		}
		
		//增值服务信息
		function addserviceInfo(){
			
			var url = "/claimcar/regist/addserviceInfo.do";
			layer.open({
				type : 2,
				title : "增值服务信息",
				scrollbar : true,
				maxmin : true, // 开启最大化最小化按钮
				area: ['95%', '70%'],
				content : url
				
			});
			//layer.full(index);
		}
		
		//事故信息查询接口
		function accidentInfo(registNo){
			$.ajax({
				url : '/claimcar/regist/accidentInfo.do?registNo=' + registNo,
				type : 'post',
				success : function(json) {// json是后端传过来的值
					if(json.status==200){
						if(json.data==1){
							layer.alert("查询成功!");
						}else{
							layer.alert("查询失败!");
						}
					}else{
						layer.alert("查询失败!");
					}
				},
				error : function() {
					layer.alert("查询失败!");
				}
			});
		}
		
		function send(flag){
			var registNo = $("#registNos").val();
			var addCode_URL = $("#addCode_URLS").val();
			var queryCode_URL = $("#queryCode_URLS").val();
			
			if(flag == 1){
				//关联工单
				window.parent.postMessage({"reportno":registNo,"active":"queryorder"},queryCode_URL);
			}else{
				//新建工单
				window.parent.postMessage({"reportno":registNo,"active":"addorder"},addCode_URL);
			}
		}
</script>


</body>
</html>

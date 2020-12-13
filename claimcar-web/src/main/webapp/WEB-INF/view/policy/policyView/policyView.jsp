<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>保单信息查看</title>
<style type="text/css">
.a_menuBar{
  margin-top: -55px;
  display: inline-block;
  position: absolute;
}
body{
font-size:14px;
}
.col-1{
width:10%;
}
.col-3{
width:23%;
}
</style>
</head>
<body>
	<div>
		<div class="top_btn">
			<div style="display: inline;font-weight:bold;" class="f-18 c-black">保单信息</div>
			<a class="btn size-L" href="#ywsj">业务数据</a> 
			<a class="btn size-L" href="#khxx">客户信息</a> 
			<a class="btn size-L" href="#clxx">车辆信息</a> 
			<c:if test="${!empty prpLCMainC}">
			<a class="btn size-L" href="#jqxht">交强险合同</a>
			</c:if>
			<c:if test="${!empty prpLCItemKindVo_B}">
			<a class="btn size-L" href="#syxht">商业险合同</a>
			</c:if>
			<c:if test="${!empty policyNoNewSign }">
			<a class="btn btn-warning">${policyNoNewSign}</a>
			</c:if>
		</div>
	</div>

	<!-- 业务信息开始 -->
	<a id="ywsj" class="a_menuBar"></a>
	<div class="table_wrap page_wrap" style="margin-top:50px">
		<div class="table_title f14">业务信息</div>
		<div class="table_cont ">
			<div class="formtable ">
			    <c:if test="${!empty prpLCMainB}">
				<div class="row  cl">
					<label class="form_label col-1">商业险保单号：</label>
					<div class="form_input col-3">
						${prpLCMainB.policyNo}
					</div>
					<label class="form_label col-1">商业险投保单号：</label>
					<div class="form_input col-3">
						${prpLCMainB.proposalNo}
					</div>
					<label class="form_label col-1">商业险上期保单：</label>
					<div class="form_input col-3">
					<c:if test="${not empty define2 }">
						${define2}
						<c:if test="${empty prpLCMainB.renewal }">非续保：</c:if>
					    </c:if>
					</div>
				</div>
				</c:if>
				<c:if test="${!empty prpLCMainC}">
				<div class="row  cl">
					<label class="form_label col-1">交强险保单号：</label>
					<div class="form_input col-3">
						${prpLCMainC.policyNo}
					</div>
					<label class="form_label col-1">交强险投保单号：</label>
					<div class="form_input col-3">
						${prpLCMainC.proposalNo}
					</div>
					<label class="form_label col-1">交强险上期保单：</label>
					<div class="form_input col-3">
						<c:if test="${not empty define1 }">
						${define1}
						<c:if test="${empty prpLCMainC.renewal }">非续保：</c:if>
					    </c:if>
					</div>
				</div>
				</c:if>
				<div class="row  cl">
					<label class="form_label col-1">团车协议号：</label>
					<div class="form_input col-3">
					   ${prpLCMain.contractNo}
					</div>
					<label class="form_label col-1">归属部门：</label>
					<div class="form_input col-3">
					<app:codetrans codeType="ComCodeFull" codeCode="${prpLCMain.comCode}" />
					</div>
					<label class="form_label col-1">业务员：</label>
					<div class="form_input col-3">
						${prpLCMain.handler1Code}-${prpLCMain.handler1Name}
					</div>
				</div>

				<div class="row cl">
					<label class="form_label col-1">联共保类型：</label>
					<div class="form_input col-3">
					<app:codetrans codeType="CoinsFlag" codeCode="${prpLCMain.coinsFlag}" />
					</div>
					<label class="form_label col-1">业务来源：</label>
					<div class="form_input col-3">
					<app:codetrans codeType="BusinessNature"
					codeCode="${prpLCMain.businessNature}" />
					</div>
					<label class="form_label col-1">业务板块：</label>
					<div class="form_input col-3">
					<app:codetrans codeType="BusinessPlate"
					codeCode="${prpLCMain.businessPlate}" />
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-1">代理人/经纪人：</label>
					<div class="form_input col-3">
					${prpLCMain.agentCode}--${prpLCMain.agentName }
					</div>
					<label class="form_label col-1">代理协议号：</label>
					<div class="form_input col-3">
						${prpLCMain.agreementNo}
					</div>
					<label class="form_label col-1">再保分入业务：</label>
					<div class="form_input col-3">
					    <c:if test="${prpLCMain.businessFlag=='1'}">是</c:if>
				        <c:if test="${prpLCMain.businessFlag=='0'}">否</c:if>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-1">分入业务类型：</label>
					<div class="form_input col-3">
					</div>
					<label class="form_label col-1">分出公司保单号码：</label>
					<div class="form_input col-3">
					</div>
					<label class="form_label col-1">投保日期：</label>
					<div class="form_input col-3">
						<fmt:formatDate value='${prpLCMain.operateDate}' pattern='yyyy-MM-dd'/>
					</div>
				</div>
				
				<div class="row cl">
					<label class="form_label col-1">业务标识：</label>
					<div class="form_input col-3">
					<c:if test="${prpLCMain.shareHolderFlag=='1'}">
					<span class='badge'>股东业务</span>
				    </c:if>
				    <c:if test="${prpLCMain.shareHolderFlag=='0'}">
					<span class='badge'>非股东业务</span>
				    </c:if>
				<span class='badge'><app:codetrans codeType="BusinessClass"
						codeCode="${prpLCMain.businessClass}" /></span>
				
				<span class='badge'><app:codetrans
						codeType="FarmFlag" codeCode="${prpLCMain.farmFlag}" /> </span>

				<c:if test="${!empty prpLCMain.otherBusinessFlag}">
					<span class='badge'><app:codetrans
							codeType="OtherBusinessFlag"
							codeCode="${prpLCMain.otherBusinessFlag}" /> </span>
				</c:if>
					</div>
					<c:if test="${prpLCMain.shareHolderFlag=='1'}">
					<label class="form_label col-1">股东单位：</label>
				    <div class="form_input col-3">${prpLCMain.stockHolderCode }--${prpLCMain.stockHolderName }
				    </div>
			        </c:if>
					<label class="form_label col-1">争议解决方式：</label>
					<div class="form_input col-3">
						<c:if test="${prpLCMain.argueSolution=='1'}">诉讼</c:if>
				        <c:if test="${prpLCMain.argueSolution!='1'}">仲裁</c:if>
			  		</div>
			  		<c:if test="${prpLCMain.argueSolution!='1'}">
				         <div class="form_label col-1">争议仲裁机构名称：</div>
				        <div class="form_input col-4">${prpLCMainC.arbitBoardName }
				        </div>
			        </c:if>
				</div>
				<div class="row cl">
					<c:if test="${prpLCMainBRepairPhone == '1'}">
						<label class="form_label col-1">推送修手机号码(商业)：</label>
						<div class="form_input col-3">
							<div class="form_input col-4">${prpLCMainB.serviceMobile }
							</div>
						</div>
					</c:if>
					<c:if test="${prpLCMainCRepairPhone == '1'}">
						<label class="form_label col-1">推送修手机号码(交强)：</label>
						<div class="form_input col-3">
							<div class="form_input col-4">${prpLCMainC.serviceMobile }
						</div>
					</c:if>
				</div>
			</div>
			</div>
		</div>
		<!-- 业务信息结束 -->
		
		<!-- 投保人信息开始 -->
		<a id="khxx" class="a_menuBar"></a>
		<div class="table_wrap page_wrap">
			<div class="table_title f14">投保人</div>
			<div class="table_cont ">
				<div class="formtable ">
					<div class="row  cl">
						<label class="form_label col-1">客户类型：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="InsuredType"
					codeCode="${prpLCICustomVo.insuredType }" />
						</div>
						<label class="form_label col-1">性别：</label>
						<div class="form_input col-3">
						 	<c:if test="${prpLCICustomVo.sex eq '1'}">
				             	<span>男</span>
				           	</c:if>
				            <c:if test="${prpLCICustomVo.sex eq '2'}">
				             	<span>女</span>
				           	</c:if>
						  <%--  <app:codetrans codeType="Sex" codeCode="${prpLCICustomVo.sex }" /> --%>
						</div>
						<label class="form_label col-1">客户性质：</label>
						<div class="form_input col-3">
						   <c:if test="${prpLCICustomVo.identifyType eq '01'}">
				             	<span>居民</span>
				           </c:if>
				           <c:if test="${!prpLCICustomVo.identifyType eq '01'}">
				             	<span>非居民</span>
				           </c:if>
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">客户代码：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.insuredCode}
								<%-- <app:codetrans codeType="InsuredCode"
					codeCode="${prpLCICustomVo.insuredCode }" /> --%>
						</div>
						<label class="form_label col-1"><app:codetrans codeType="ExtendChar1"
					codeCode="${prpLCICustomVo.identifyType }" nullVal="证件号码"/>：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.identifyNumber}
						</div>
						<label class="form_label col-1">投保人名称：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.insuredName}
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">固定电话：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.phoneNumber}
						</div>
						<label class="form_label col-1">移动电话：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.mobile}
						</div>
						<label class="form_label col-1">投保人地址：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.insuredAddress}
						</div>
					</div>

					<div class="row cl">
						<label class="form_label col-1">邮政编码：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.postCode}
						</div>
						<label class="form_label col-1">电子邮箱：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.email}
						</div>
						<label class="form_label col-1">证件有效期：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.possessNature}
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">联系人姓名：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.linkerName}
						</div>
						<label class="form_label col-1">年龄：</label>
						<div class="form_input col-3">
							${prpLCICustomVo.age}
						</div>
						<c:if test="${ prpLCICustomVo.insuredType =='1'}">
						<label class="form_label col-1">职业类型：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="OccupationCode"
						codeCode="${prpLCICustomVo.occupationCode }" />
						</div>
						</c:if>
						<c:if test="${ prpLCICustomVo.insuredType =='2'}">
						<label class="form_label col-1">行业类型：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="BusinessSource"
						codeCode="${prpLCICustomVo.businessSource }" />
						</div>
						</c:if>
					</div>
				</div>
			</div>
	    </div>
		<!-- 投保人信息结束 -->
		
		<!-- 被保险人信息开始 -->
		<div class="table_wrap page_wrap">
			<div class="table_title f14">被保险人</div>
			<div class="table_cont ">
				<div class="formtable ">
					<div class="row  cl">
						<label class="form_label col-1">客户类型：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="InsuredType"
					codeCode="${prpLCInsuredVo.insuredType }" />
						</div>
						<label class="form_label col-1">性别：</label>
						<div class="form_input col-3">
						   <c:if test="${prpLCICustomVo.sex eq '1'}">
				             	<span>男</span>
				           	</c:if>
				            <c:if test="${prpLCICustomVo.sex eq '2'}">
				             	<span>女</span>
				           	</c:if>
						</div>
						<label class="form_label col-1">客户性质：</label>
						<div class="form_input col-3">
						   <c:if test="${prpLCInsuredVo.identifyType eq '01'}">
				             	<span>居民</span>
				           </c:if>
				           <c:if test="${!prpLCInsuredVo.identifyType eq '01'}">
				             	<span>非居民</span>
				           </c:if>
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">客户代码：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.insuredCode}
								<%-- <app:codetrans codeType="InsuredCode"
					codeCode="${prpLCICustomVo.insuredCode }" /> --%>
						</div>
						<label class="form_label col-1"><app:codetrans codeType="ExtendChar1"
					codeCode="${prpLCInsuredVo.identifyType }" nullVal="证件号码" />
				：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.identifyNumber}
						</div>
						<label class="form_label col-1">被保险人名称：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.insuredName}
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">固定电话：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.phoneNumber}
						</div>
						<label class="form_label col-1">移动电话：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.mobile}
						</div>
						<label class="form_label col-1">被保险人地址：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.insuredAddress}
						</div>
					</div>

					<div class="row cl">
						<label class="form_label col-1">邮政编码：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.postCode}
						</div>
						<label class="form_label col-1">电子邮箱：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.email}
						</div>
						<label class="form_label col-1">证件有效期：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.possessNature}
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">联系人姓名：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.linkerName}
						</div>
						<label class="form_label col-1">年龄：</label>
						<div class="form_input col-3">
							${prpLCInsuredVo.age}
						</div>
						<c:if test="${ prpLCInsuredVo.insuredType =='1'}">
						<label class="form_label col-1">职业类型：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="OccupationCode"
						codeCode="${prpLCInsuredVo.occupationCode }" />
						</div>
						</c:if>
						<c:if test="${ prpLCInsuredVo.insuredType =='2'}">
						<label class="form_label col-1">行业类型：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="BusinessSource"
						codeCode="${prpLCInsuredVo.businessSource }" />
						</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<!-- 被保险人信息结束 -->
		
		<!-- 车辆信息开始 -->
		<a id="clxx" class="a_menuBar"></a>
		<div class="table_wrap page_wrap">
			<div class="table_title f14">车辆信息</div>
			<div class="table_cont">
				<div class="formtable ">
					<div class="row  cl">
						<label class="form_label col-1">车牌号：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.licenseNo }
						<c:if test="${fn:substring(prpLCItemCarVo.otherNature, 6, 7)!='0' }">未上牌</c:if>
						</div>
						<label class="form_label col-1">车架号/VIN：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.frameNo }
						</div>
						<label class="form_label col-1">发动机号：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.engineNo }
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">初登日期：</label>
						<div class="form_input col-3">
						<fmt:formatDate value='${prpLCItemCarVo.enrollDate }' pattern='yyyy-MM-dd'/>
						</div>
						<label class="form_label col-1">车主：</label>
						<div class="form_input col-3">
						(<app:codetrans codeType="CarAttachNature"
					codeCode="${prpLCItemCarVo.carAttachNature }" />)${prpLCItemCarVo.carOwner }
						</div>
						<label class="form_label col-1">使用性质：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="UseKind" codeCode="${prpLCItemCarVo.useKindCode}" />
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">品牌型号：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.brandName }
						</div>
						<label class="form_label col-1">品牌型号打印：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.carDealerName }
						</div>
						<label class="form_label col-1">号牌种类：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="LicenseKindCode"
					      codeCode="${prpLCItemCarVo.licenseKindCode}" />
						</div>
					</div>

					<div class="row cl">
						<label class="form_label col-1">被保人与车辆关系：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="CarInsuredRelation"
					codeCode="${prpLCItemCarVo.carInsuredRelation}" />
						</div>
						<label class="form_label col-1">车身颜色：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="ColorCode" codeCode="${prpLCItemCarVo.colorCode }" />
						</div>
						<label class="form_label col-1">号牌底色：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="LicenseColorCode"
					codeCode="${prpLCItemCarVo.licenseColorCode}" />
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">新车购置价：</label>
						<div class="form_input col-3">
						    <fmt:formatNumber value="${prpLCItemCarVo.purchasePrice}" pattern="#,##0.00"></fmt:formatNumber>元
						</div>
						<label class="form_label col-1">使用年数：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.useYears}年
						</div>
						<label class="form_label col-1">核定载客：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.seatCount}人
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">排量/功率：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.exhaustScale}L/KW
						</div>
						<label class="form_label col-1">核定载质量：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.tonCount}千克
						</div>
						<label class="form_label col-1">总质量：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.allMass}
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">整备质量：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.wholeWeight}千克
						</div>
						<label class="form_label col-1">车辆种类：</label>
						<div class="form_input col-3">
						    <app:codetrans codeType="CarType" codeCode="${prpLCItemCarVo.carType }" />
						</div>
						<label class="form_label col-1">交管类型：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="VehicleStyle"
						codeCode="${prpLCItemCarVo.vehicleStyle }" />
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">行驶区域：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="RunAreaCode"
					codeCode="${prpLCItemCarVo.runAreaCode }" />
						</div>
						<label class="form_label col-1">外地车标志：</label>
						<div class="form_input col-3">
							<c:if test="${fn:substring(prpLCItemCarVo.otherNature, 7, 8)=='0'}">本地车</c:if>
							<c:if test="${fn:substring(prpLCItemCarVo.otherNature, 7, 8)=='1'}">外地车</c:if>
							<c:if test="${fn:substring(prpLCItemCarVo.otherNature, 7, 8)!='0' && fn:substring(carPo.otherNature, 7, 8)!='1'}">港澳车</c:if>
						</div>
						<label class="form_label col-1">进口/国产类：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="CountryNature"
					codeCode="${prpLCItemCarVo.countryNature}" />
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">能源类型：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="FuelType" codeCode="${prpLCItemCarVo.fuelType}" />
						</div>
						<label class="form_label col-1">是否车贷投保多年：</label>
						<div class="form_input col-3">
							<c:if test="${prpLCItemCarVo.carLoanFlag=='1' }">是</c:if>
				            <c:if test="${prpLCItemCarVo.carLoanFlag!='1' }">否</c:if>
						</div>
						<label class="form_label col-1">特殊车投保标志：</label>
						<div class="form_input col-3">
							${prpLCItemCarVo.specialCarFlag}
						</div>
					</div>
					
					<div class="row cl">
						<label class="form_label col-1">车辆过户：</label>
						<div class="form_input col-3">
							<c:if test="${fn:substring(prpLCItemCarVo.otherNature, 4, 5)=='1' }">是</c:if>
				            <c:if test="${fn:substring(prpLCItemCarVo.otherNature, 4, 5)!='1' }">否</c:if>
						</div>
						<c:if test="${fn:substring(prpLCItemCarVo.otherNature, 4, 5)=='1' }">
				      	<label class="form_label col-1">过户登记日期：</label>
				        <div class="form_input col-3">${prpLCItemCarVo.transferDate }</div> 
				       <%--  <div class="form_input col-3"><fmt:formatDate value='${prpLCItemCarVo.enrollDate }' pattern='yyyy-MM-dd'/></div> --%> 
		               	</c:if>
						<label class="form_label col-1">异型车辆标志：</label>
						<div class="form_input col-3">
						</div>
					</div>
					<hr>
					<div class="row cl">
						<label class="form_label col-1">验车情况：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="CarcheckStatus"
					codeCode="${prpLCItemCarVo.carCheckStatus}" />
						</div>
						<label class="form_label col-1">免验原因：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="NoCheckReason"
						codeCode="${prpLCItemCarVo.carCheckReason }" split="," />
						<c:if test="${prpLCItemCarVo.carCheckStatus!='2' }">
				          <label class="form_label col-1">验车人：</label>
				          	${prpLCItemCarVo.carChecker }--
					      <app:codetrans codeType="UserCode"
						 codeCode="${prpLCItemCarVo.carChecker }" />
				 <label class="form_label col-1">验车时间：</label>
				 ${prpLCItemCarVo.carCheckTime}
			          </c:if>
			          </div>
						<label class="form_label col-1">备注：</label>
						<div class="form_input col-3">
						${prpLCMain.remark}
						</div>
					</div>
			</div>
		</div>
		</div>
		<!-- 车辆信息结束 -->
		
		<!-- 交强险合同开始 -->
		<c:if test="${!empty prpLCMainC}">
		<a id="jqxht" class="a_menuBar"></a>
		<div class="table_wrap page_wrap">
			<div class="table_title f14">交强险合同     &nbsp;&nbsp;&nbsp; <b style="color: #5433FF"> 保险期限 
					<fmt:formatDate value='${prpLCMainC.startDate}' pattern='yyyy-MM-dd'/>&nbsp;${prpLCMainC.startHour} 时 &nbsp;00分至 
					<fmt:formatDate value='${prpLCMainC.endDate}' pattern='yyyy-MM-dd'/>&nbsp;${prpLCMainC.endHour} 时&nbsp;00分
			</b> <b style="color: #FF3333">保费<fmt:formatNumber
					value="${prpLCItemKindVo_C.premium }" pattern="#,##0.00" />
				元
			</b></div>
			<div class="table_cont ">
			     <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th width="30%">条款代码/条款名称</th>
					 				<th  width="10%">赔偿限额</th>
					 				<th  width="10%">责任限额(元)</th>
					 				<th  width="10%">标准保费(元)</th>
					 				<th  width="10%">费率系数</th>
					 				<th  width="10%">调整系数</th>
					 				<th  width="10%">实交保费(元)</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 			<tr>
					 				<td>${prpLCItemKindVo_C.kindCode}-${prpLCItemKindVo_C.kindName}</td>
					 				<td><a id="checkLimit" href="javascript:void(0)">查看</a></td>
					 				<td><fmt:formatNumber
									value="${prpLCItemKindVo_C.amount}" pattern="#,##0.00" /></td>
					 				<td><fmt:formatNumber
									value="${prpLCItemKindVo_C.basePremium}" pattern="#,##0.00" /></td>
					 				<td><fmt:formatNumber
									value="${prpLCItemKindVo_C.discount*100}" pattern="#,##0.00" />%</td>
					 				<td><fmt:formatNumber
									value="${prpLCItemKindVo_C.adjustRate*100}" pattern="#,##0.00" />%</td>
					 				<td><fmt:formatNumber
									value="${prpLCItemKindVo_C.premium }" pattern="#,##0.00" /></td>
					 			</tr>
					 		</tbody>
					 </table>
					 
					 <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th>特约名称</th>
					 				<th>特约内容</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 		<c:if test="${empty prpLCengageVoMap_C }">
							<tr>
								<td colspan="2" align="center">交强险无特别约定</td>
							</tr>
					       	</c:if>
						    <c:if test="${!empty prpLCengageVoMap_C }">
							<c:forEach var="prpCengage" items="${prpLCengageVoMap_C}">
								<tr>
									<td width="25%">${prpCengage.key}</td>
									<td width="75%">${prpCengage.value }</td>
								</tr>
							</c:forEach>
						    </c:if>
					 	    </tbody>
					 </table>
				<div class="formtable ">
					<div class="row  cl">
						<label class="form_label col-1">酒后驾驶调整系数：</label>
						<div class="form_input col-3">
							${prpLCMainC.peccancyCoeff}
						</div>
						<label class="form_label col-1">交通事故调整系数：</label>
						<div class="form_input col-3">
							${prpLCMainC.claimCoeff}
						</div>
						<label class="form_label col-1">交强险不浮动原因：</label>
						<div class="form_input col-3">
							${prpLCMainC.rateFloatFlag}
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">交强险币别：</label>
						<div class="form_input col-3">
							${prpLCMainC.currency}
						</div>
						<label class="form_label col-1">短期费率方式：</label>
						<div class="form_input col-3">
						    <app:codetrans codeType="ShortRateFlag"
							codeCode="${prpLCItemKindVo_C.shortRateFlag }" />
						</div>
						<label class="form_label col-1">短期费率：</label>
						<div class="form_input col-3">
							${prpLCItemKindVo_C.shortRate}%
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">保额额小计：</label>
						<div class="form_input col-3">
						<fmt:parseNumber value="${prpLCItemKindVo_C.amount}" var="amount" />
						<fmt:formatNumber
								value="${amount/10000}" pattern="#,##0.00"/>
							万元
						</div>
						<label class="form_label col-1">折扣保费合计：</label>
						<div class="form_input col-3">
						<fmt:formatNumber
								value="${prpLCMainC.sumDisCountPremium }" pattern="#,##0.00" />
							元
						</div>
						<label class="form_label col-1">实交保费合计：</label>
						<div class="form_input col-3">
							<fmt:formatNumber
								value="${prpLCItemKindVo_C.premium }" pattern="#,##0.00" />
							元
						</div>
					</div>

					<%-- <div class="row cl">
						<label class="form_label col-1">手续/经纪费比例：</label>
						<div class="form_input col-3">
							${prpLCMainC.disRate }%
						</div>
						 <label class="form_label col-1">手续/经纪费金额：</label>
						<div class="form_input col-3">
							<fmt:formatNumber
								value="${prpLCItemKindVo_C.premium*prpLCMainC.disRate/100.0 }"
								pattern="#,##0.00" /> 元
						</div> 
					</div> --%>
			</div>
		</div>
		<div class="hide" id="limitLayer" style="width:300px">
		     <div class="table_cont "  >
		             <h5 class='c-warning lh-16'>交强险-赔偿限额</h5>
				 	<c:choose>
						<c:when test="${prpLCItemKindVo_C.amount>122000}">
							<table class="table table-border table-hover" >
								<thead>
								<tr>
									<th style="white-space: nowrap;">限额类型</th>
									<th style="white-space: nowrap;">限额</th>
								</tr>
								</thead>
								<tbody>
								<tr>
									<td>死亡伤残赔偿限额</td>
									<td>180000</td>
								</tr>
								<tr>
									<td>医疗费用赔偿限额</td>
									<td>18000</td>
								</tr>
								<tr>
									<td>财产损失赔偿限额</td>
									<td>2000</td>
								</tr>
								<tr>
									<td>无责任死亡伤残赔偿限额</td>
									<td>18000</td>
								</tr>
								<tr>
									<td>无责任医疗费用赔偿限额</td>
									<td>1800</td>
								</tr>
								<tr>
									<td>无责任财产损失赔偿限额</td>
									<td>100</td>
								</tr>
								</tbody>
							</table>
						</c:when>
						<c:otherwise>
							<table class="table table-border table-hover" >
								<thead>
								<tr>
									<th style="white-space: nowrap;">限额类型</th>
									<th style="white-space: nowrap;">限额</th>
								</tr>
								</thead>
								<tbody>
								<tr>
									<td>死亡伤残赔偿限额</td>
									<td>110000</td>
								</tr>
								<tr>
									<td>医疗费用赔偿限额</td>
									<td>10000</td>
								</tr>
								<tr>
									<td>财产损失赔偿限额</td>
									<td>2000</td>
								</tr>
								<tr>
									<td>无责任死亡伤残赔偿限额</td>
									<td>11000</td>
								</tr>
								<tr>
									<td>无责任医疗费用赔偿限额</td>
									<td>1000</td>
								</tr>
								<tr>
									<td>无责任财产损失赔偿限额</td>
									<td>100</td>
								</tr>
								</tbody>
							</table>
						</c:otherwise>
					</c:choose>
				</div>
		</div>
		</div>
		</c:if>
		<!-- 交强险合同结束 -->
		
		<!-- 商业险合同开始 -->
		<c:if test="${!empty prpLCItemKindVo_B}">
		<a id="syxht" class="a_menuBar"></a>
		<div class="table_wrap page_wrap">
			<div class="table_title f14">商业险合同       &nbsp;&nbsp;&nbsp; <b style="color: #5433FF"> 保险期限 
					<fmt:formatDate value='${prpLCMainB.startDate}' pattern='yyyy-MM-dd'/>&nbsp;${prpLCMainB.startHour} 时 &nbsp;00分至 
					<fmt:formatDate value='${prpLCMainB.endDate}' pattern='yyyy-MM-dd'/>&nbsp;${prpLCMainB.endHour} 时&nbsp;00分
			</b> <b style="color: #FF3333">保费<%-- <fmt:formatNumber
					value="${prpLCItemKindVo_B[0].premium }" pattern="#,##0.00" /> --%>
					<fmt:formatNumber
					value="${prpLCMainB.sumItemPremium }" pattern="#,##0.00" />
				元
			</b></div>
			<div class="table_cont ">
			     <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th width="30%">主险 条款代码--条款名称</th>
									<c:if test="${define3=='0' }">
										<th width="10%">不计免赔</th>
									</c:if>
									<c:if test="${define3=='1' }">
										<th width="10%">绝对免赔率</th>
									</c:if>
					 				<th width="10%">责任限额(元)</th>
					 				<th width="10%">费率</th>
					 				<th width="10%">标准保费(元)</th>
					 				<th width="10%">调整系数</th>
					 				<th width="10%">实交保费(元)</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 		<c:forEach var="prpLCitemKind" items="${prpLCItemKindVo_B}">
					 		    <c:choose>
					 		        <c:when test="${prpLCitemKind.calculateFlag=='Y'}">
							 			<tr>
							 				<td>${prpLCitemKind.kindCode}--${prpLCitemKind.kindName}
							 				<c:if
												test="${prpLCitemKind.modeCode=='1' }">
												<span class="badge" title="多次事故免赔率特约"> 免</span>
											</c:if>
											<c:if test="${prpLCitemKind.kindCode=='D12' }">
													<fmt:formatNumber value="${prpLCitemKind.unitAmount}"
														pattern="#,##0.00" /> 元/座 ${prpLCitemKind.quantity} 座
											</c:if>
											<c:if test="${prpLCitemKind.kindCode=='A' }">
												免赔额${prpLCitemKind.deductible}
											</c:if>
							 				</td>
							 				<td>
							 				   <c:if test="${prpLCitemKind.noDutyFlag=='1' }">
											    <input type="checkbox" checked disabled='disabled'/>
											   </c:if>
											    <c:if test="${prpLCitemKind.noDutyFlag=='0' }">
											    <input type="checkbox" disabled='disabled'/>
											   </c:if>
							 				</td>
							 				<td><fmt:formatNumber
												value="${prpLCitemKind.amount}" pattern="#,##0" /></td>
							 				<td><fmt:formatNumber
												value="${prpLCitemKind.rate}" pattern="#,##0.00" /> %</td>
							 				<td><fmt:formatNumber
												value="${prpLCitemKind.benchMarkPremium}" pattern="#,##0.00" /></td>
							 				<td><fmt:formatNumber 
							 				        value="${prpLCitemKind.discount*100}" pattern="#,##0.00" />%</td>
							 				<td><fmt:formatNumber
												value="${prpLCitemKind.premium}" pattern="#,##0.00" /></td>
							 			</tr>
					 			    </c:when>
					 			</c:choose>
					 		</c:forEach>
					 		</tbody>
					 </table>
					 
					 <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th width="30%">附加险条款代码--条款名称</th>
									<c:if test="${define3=='0' }">
										<th width="10%">不计免赔</th>
									</c:if>
									<c:if test="${define3=='1' }">
										<th width="10%">绝对免赔率</th>
									</c:if>
					 				<th width="10%">责任限额(元)</th>
					 				<th width="10%">费率</th>
					 				<th width="10%">标准保费(元)</th>
					 				<th width="10%">调整系数</th>
					 				<th width="10%">实交保费(元)</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 		<c:forEach var="prpLCitemKind" items="${prpLCItemKindVo_B}">
								<c:choose>
									<c:when test="${prpLCitemKind.calculateFlag=='N'}">
							 			<tr>
							 				<td>${prpLCitemKind.kindCode}--${prpLCitemKind.kindName}
							 				<c:if test="${prpLCitemKind.kindCode=='C' }">
														<fmt:formatNumber value="${prpLCitemKind.unitAmount}"
															pattern="#,##0.00" /> 元/天 ${prpLCitemKind.quantity} 天
											</c:if>
											<c:if test="${prpLCitemKind.kindCode=='M1' }">
														<fmt:formatNumber value="${prpLCitemKind.value}"
															pattern="#,##0.00" /> 元(免赔金额)
											</c:if>
											<c:if test="${prpLCitemKind.kindCode=='NY' }">
													${prpLCitemKind.value}% 协定比例
											</c:if>
										    <c:if test="${prpLCitemKind.kindCode=='S' }">
													半径为${prpLCitemKind.model=='1'?'200':(prpLCitemKind.model=='2'?'500':'1000')}公里
											</c:if>
											<c:if test="${prpLCitemKind.kindCode=='T' }">
														<fmt:formatNumber value="${prpLCitemKind.unitAmount}"
															pattern="#,##0.00" /> 元/天 ${prpLCitemKind.quantity} 天
											</c:if>
											<c:if test="${prpLCitemKind.kindCode=='Z2' }">
													${prpLCitemKind.modeCode=='1'?'意外事故':'意外事故或自身故障'}
										    </c:if>
											<c:if test="${prpLCitemKind.kindCode=='Z3' }">
												              上浮比例 ${prpLCitemKind.modeCode}% 
											</c:if>
											<c:if test="${prpLCitemKind.kindCode=='RF' }">
														<fmt:formatNumber value="${prpLCitemKind.unitAmount}"
															pattern="#,##0.00" /> 元/天 ${prpLCitemKind.quantity} 天 
									        </c:if>
									        <c:if test="${prpLCitemKind.kindCode=='F' }">
									          <c:if test="${prpLCitemKind.modeCode == '2' }">(进口玻璃)</c:if>
									        </c:if>
							 				
							 				</td>
							 				<td>
												<c:if test="${define3=='0' }">
													<c:if test="${prpLCitemKind.noDutyFlag=='1' }">
														<input type="checkbox" checked disabled='disabled'/>
													</c:if>
													<c:if test="${prpLCitemKind.noDutyFlag=='0' }">
														<input type="checkbox" disabled='disabled'/>
													</c:if>
												</c:if>
												<c:if test="${define3=='1' }">
													<c:if test="${prpLCitemKind.kindCode =='AG' or prpLCitemKind.kindCode =='BG' or prpLCitemKind.kindCode =='D11G' or prpLCitemKind.kindCode =='D12G' or prpLCitemKind.kindCode =='GG'}">
														<c:if test="${prpLCitemKind.value != null and prpLCitemKind.value != 0}">
															<fmt:formatNumber
																	value="${prpLCitemKind.value}" pattern="#,##0.00" /> %
														</c:if>
													</c:if>
												</c:if>

							 				</td>
							 				<td>
							 				<c:if
													test="${prpLCitemKind.amount == null or prpLCitemKind.amount == 0 }">--</c:if>
												<c:if
													test="${prpLCitemKind.amount != null and prpLCitemKind.amount != 0 }">
													<fmt:formatNumber value="${prpLCitemKind.amount}"
														pattern="#,##0.00" />
												</c:if>
                                            </td>
							 				<td><fmt:formatNumber
													value="${prpLCitemKind.rate}" pattern="#,##0.00" /> %</td>
							 				<td><fmt:formatNumber
													value="${prpLCitemKind.benchMarkPremium}" pattern="#,##0.00" /></td>
							 				<td><fmt:formatNumber 
							 				        value="${prpLCitemKind.discount*100}" pattern="#,##0.00" />%</td>
							 				<td><fmt:formatNumber
													value="${prpLCitemKind.premium}" pattern="#,##0.00" /></td>
							 			</tr>
					 		        </c:when>
					 			</c:choose>
					 		</c:forEach>
					 		</tbody>
					 </table>
					 
					 <table class="table table-border table-hover table-bg">
					 		<tbody>
					 			<tr class="warning">
					 				<td class="text-c" width="40%">商业险合计</td>
					 				<td width="10%"><fmt:formatNumber
								value="${prpLCMainB.sumItemAmount}" pattern="#,##0.00" /></td>
					 				<td width="10%"></td>
					 				<td width="10%"><fmt:formatNumber
								value="${prpLCMainB.sumBenchmarkPremium}"
								pattern="#,##0.00" /></td>
					 				<td width="10%"><fmt:formatNumber
								value="${prpLCItemKindVo_B[0].discount*100}"
								pattern="#,##0.00" />%</td>
					 				<td width="10%"><fmt:formatNumber
								value="${prpLCMainB.sumItemPremium}"
								pattern="#,##0.00" /></td>
					 			</tr>
					 		</tbody>
					 </table>
					 
					 
					 <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th>商业险特约名称</th>
					 				<th>商业险特约内容</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 		<c:if test="${empty prpLCengageVoMap_B }">
							<tr>
								<td colspan="2" align="center">商业险无特别约定</td>
							</tr>
					       	</c:if>
						    <c:if test="${!empty prpLCengageVoMap_B }">
							<c:forEach var="prpCengage" items="${prpLCengageVoMap_B}">
								<tr>
									<td width="25%">${prpCengage.key}</td>
									<td width="75%">${prpCengage.value }</td>
								</tr>
							</c:forEach>
						    </c:if>
					 		</tbody>
					 </table>
					 
					 
					 <!-- <table class="table table-border table-hover">
					 		<thead>
					 			<tr>
					 				<th>优惠优惠名称</th>
					 				<th>优惠系数项目</th>
					 				<th>优惠值</th>
					 			</tr>
					 		</thead>
					 		<tbody>
					 			<tr>
					 				<td>无赔款优待及上年赔款记录</td>
					 				<td>新保或上年发生1次赔款</td>
					 			    <td>100.00%</td>
					 			</tr>
					 		</tbody>
					 </table> -->
				<div class="formtable ">
					<div class="row  cl">
						<label class="form_label col-1">商业险币别：</label>
						<div class="form_input col-3">
						<app:codetrans codeType="CurrencyCode"
							codeCode="${prpLCMainB.currency}" />
						</div>
						<label class="form_label col-1">短期费率方式：</label>
						<div class="form_input col-3">
							<app:codetrans codeType="ShortRateFlag"
							codeCode="${prpLCItemKindVo_B[0].shortRateFlag }" />
						</div>
						<label class="form_label col-1">短期费率：</label>
						<div class="form_input col-3">
							${prpLCItemKindVo_B[0].shortRate}%
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">保额合计：</label>
						<div class="form_input col-3">
							<%-- <fmt:formatNumber
								value="${prpLCMainB.sumItemAmount/10000}"
								pattern="#,##0.00" />万元  --%>
								<fmt:formatNumber
								value="${prpLCMainB.sumItemAmount}"
								pattern="#,##0.00" />元 
						</div>
						<label class="form_label col-1">基准保费合计：</label>
						<div class="form_input col-3">
							<fmt:formatNumber
								value="${prpLCMainB.sumBenchmarkPremium}"
								pattern="#,##0.00" />元 
						</div>
						<label class="form_label col-1">调整系数合计：</label>
						<div class="form_input col-3">
							<fmt:formatNumber value="${prpLCItemKindVo_B[0].discount*100}"
							pattern="#,##0.00" />
						%
						</div>
					</div>
					<div class="row  cl">
						<label class="form_label col-1">折扣保费合计：</label>
						<div class="form_input col-3">
							<fmt:formatNumber
								value="${prpLCMainB.sumDisCountPremiumBI}"
								pattern="#,##0.00" />元
						</div>
						<label class="form_label col-1">实交保费合计：</label>
						<div class="form_input col-3">
							<fmt:formatNumber
								value="${prpLCMainB.sumItemPremium}" pattern="#,##0.00" />元
						</div>
						<%-- <label class="form_label col-1">手续/经纪费比例：</label>
						<div class="form_input col-3">
							${prpLCMainB.disRate }%
						</div> --%>
					</div>

					<%-- <div class="row cl">
						<label class="form_label col-1">手续/经纪费金额：</label>
						<div class="form_input col-3">
							<fmt:formatNumber
								value="${prpLCMainB.sumItemPremium*prpLCMainB.disRate/100.0}"
								pattern="#,##0.00" />元
						</div>
					</div> --%>
				</div>
			</div>
		</div>
		</c:if>
		<!-- 商业险合同结束 -->
		
		<!-- 核保信息开始 
		<a id="hbht" class="a_menuBar"></a>
		<div class="table_wrap page_wrap">
			<div class="table_title f14">核保信息</div>
			<div class="table_cont ">
				<div class="formtable ">
					<div class="row  cl">
						<label class="form_label col-1">商业险投保单</label>
						<div class="form_input col-3">
							<input type="text" class="input-text" />
						</div>
						<label class="form_label col-1">交强险投保单</label>
						<div class="form_input col-3">
							<input type="text" class="input-text" />
						</div>
					</div>
					<div class="row  cl">
					1、出单员 （2100000423，孙明霞） 提交核保 核保意见：自核提示： 1、投保车辆损失险、第三者责任险、车上人员责任险、盗抢险、玻璃单独破碎险、自燃险、划痕险、不计免赔险种外险别（总则） 2015年10月14日 15时30分28秒 2、分公司1 （2100000010，刘秀川） 处理完成 核保意见：同意承保 2015年10月14日 15时30分44秒 3、审核通过 核保意见： 2015年10月14日 15时30分44秒
					</div>
				</div>
		</div>
		</div>
	 核保信息结束 -->
	
	<script type="text/javascript">
	$(function(){
		$("#checkLimit").click(function(){
			layer.tips($("#limitLayer").html(),$(this),{
			});
		});
	});
	</script>
</body>
</html>

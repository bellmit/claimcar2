<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>追加定损</title>
		<style type="text/css">
			.text-r{
				background-color: #F5F5F5
			}
		</style>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
<script type="text/javascript" src="/claimcar/js/propLoss/LaunchEdit.js"></script>
	</head>
	<body>
	<form action="" method="post" id="propModifyLForm"> 
		<input type="hidden" id="propMainId" name="businessId" value="${lossPropMainVo.id }"/>
		<input type="hidden" id="deflossFlag" value="${deflossFlag }"/>
		<input type="hidden" id="registNo" name="lossPropMainVo.registNo" value="${lossPropMainVo.registNo }"/>
		
		<div class="fixedmargin page_wrap">
			<!--保单信息   开始-->
			<div class="table_wrap">
				<div class="table_title f14">基本信息</div>
				<div class="table_cont ">
					 <table class="table table-border">
					 		<tbody>
					 			<tr>
					 				<td class="text-r" width="11.111%" >
					 					报案号
					 				</td>
					 				<td width="22.222%">
					 					${lossPropMainVo.registNo }
					 				</td>
					 				<td class="text-r" width="11.111%">
					 					赔案类别
					 				</td>
					 				<td width="22.222%">
					 					<app:codetrans codeType="CaseCode" codeCode="${lossPropMainVo.claimType }"/>  
					 				</td>
					 				<td class="text-r" width="11.111%">
					 					车牌号码
					 				</td>
					 				<td width="22.222%">
					 					<c:if test="${lossPropMainVo.lossType eq('0') }">
					 						地面(无)
					 					</c:if>
					 					<c:if test="${lossPropMainVo.lossType ne('0') }">
					 						${lossPropMainVo.license }
					 					</c:if>
					 				</td>
					 			</tr>
					 			<tr>
					 				<td class="text-r" width="11.111%">
					 					定损员
					 				</td>
					 				<td width="22.222%">
					 					${lossPropMainVo.handlerName }
					 				</td>
					 				<td class="text-r" width="11.111%">
					 					被保险人
					 				</td>
					 				<td width="22.222%">
					 					${prpLregistVo.prpLRegistExt.insuredName }
					 				</td>
					 				<td class="text-r" width="11.111%">
					 					报案日期
					 				</td>
					 				<td width="22.222%">
					 					<fmt:formatDate value='${prpLregistVo.reportTime }' pattern='yyyy-MM-dd HH:mm:ss'/>
					 				</td>
					 				</tr>
					 			<tr>
					 			<td class="text-r" width="11.111%">
					 					保单号
					 				</td>
					 				<td width="22.222%">
					 					${prpLregistVo.policyNo }
					 				</td>
					 				<td class="text-r" width="11.111%">
					 					出险地点
					 				</td>
					 				<td width="22.222%">
					 					${prpLregistVo.damageAddress }
					 				</td>
					 			</tr>
					 		</tbody>
					 </table>
				</div>
			</div>
			<div class="table_wrap">
				<div class="table_title f14">追加财产定损</div>
				<div class="table_cont ">
					<table class="table table-border">
						<thead class="text-c">
							<tr>
								<th>定损类型</th>
	   						<th>损失方</th>
	   						<th>定损内容</th>
	   						<th>损失项备注说明</th>
   						</tr>
  					</thead>
						<tbody>
							<tr class="text-c">
								<td>追加财产定损</td>
								<td>
									<app:codetrans codeType="DefLossItemType" codeCode="${lossPropMainVo.lossType }"/>
								    (${lossPropMainVo.license})
								</td>
								<td>
								<input class="input-text" name="itemContent" datatype="*"/><font class="must">*</font>
								</td>
							    <td>
							    	<input name="remark" class="input-text" />
							    </td>
						    </tr>
						    
						  </tbody>

					 </table>
				</div>
			</div>
			<div class="text-c">
				<c:if test="${handleStatus ne '1' }">
				<input class="btn btn-primary" type="button" onclick="launchPropDloss()"  value="提交" id="subVerifyLch">
				</c:if>
			</div>
		</div>
		</form>
		
		
	</body>

</html>
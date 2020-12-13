<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
	<head>
		<title>财产修改定损</title>
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
		<input type="hidden" name="lossPropMainVo.claimType" value="${lossPropMainVo.claimType }"/>
		<input type="hidden" id="deflossFlag" value="${deflossFlag }"/>
		<input type="hidden" id="registNo" name="lossPropMainVo.registNo" value="${lossPropMainVo.registNo }"/>
		<input type="hidden" name="lossPropMainVo.license" value="${lossPropMainVo.license }"/>
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
					 				<td class="text-r">
					 					定损员
					 				</td>
					 				<td colspan="5">
					 						${lossPropMainVo.handlerName}
					 				</td>
					 			</tr>
					 		</tbody>
					 </table>
				</div>
			</div>
			
			<div class="table_wrap">
            	<div class="table_title f14">财产定损修改</div>
				<div class="table_cont ">
					 <table class="table table-border">
					 		<thead class="text-c">
   									 <tr>
		   									 <th>定损类型</th>
		   									 <th>损失方</th>
   									 </tr>
  							</thead>
						  <tbody>
						    <tr class="text-c">
									<td>财产定损修改  </td>
								  <td>
								  	<app:codetrans codeType="DefLossItemType" codeCode="${lossPropMainVo.lossType }"/>
								    (${lossPropMainVo.license})
								   </td>
						    </tr>
						    
						  </tbody>

					 </table>
				</div>
			</div>
			<div class="text-c">
				<c:if test="${handleStatus ne '1' }">
					<input class="btn btn-primary " onclick="launchPropDloss()" type="button" value="提交" id="subPropModifyLch" />
				</c:if>
			</div>
		</div>
		</form>
		
		
	</body>

</html>
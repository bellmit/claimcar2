<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>阳杰报价信息</title>
</head>
<body>
<form name="claimYJ" id="claimYJ" class="form-horizontal" role="form">
<div class="fixedmargin page_wrap" style="margin-top:0px">
				<h3 class="panel-title">
				<font color="blue">
				 *基础信息	
				 </font>
				</h3>
				<div class="table_cont ">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-2">阳杰询价单号:</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" readonly  value="${prplyjPartoffersVo.enquiryId}"/>
							</div>	
							<label class="form_label col-2">三方价单编号:</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" readonly  value="${prplyjPartoffersVo.enquiryno}" />
							</div>	
							<label class="form_label col-2">是否为非现货:</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" readonly  value="${prplyjPartoffersVo.nosTock eq '1'? '是':'否'}" />
							</div>
						</div>
						<div class="row cl">
						<label class="form_label col-2">品牌:</label>
							<div class="form_input col-2">
								<input type="text" class="input-text" readonly  value="${prplyjPartoffersVo.brand}" />
							</div>	
						</div>
						</div>
						</div>
						<h3 class="panel-title">
						<font color="blue">
					     *报价一  
					     </font>
				        </h3>
				       <div class="table_cont ">
					   <div class="formtable">
						<div class="row cl">
							<label class="form_label col-2">配件第一品质:</label>
							<div class="form_input col-2">
							<c:choose>
							<c:when test="${prplyjPartoffersVo.quality eq '1'}">
							   <input type="text" class="input-text" readonly value="原厂价" />
							</c:when>
							<c:when test="${prplyjPartoffersVo.quality eq '2'}">
							<input type="text" class="input-text" readonly value="精品同质价" />
							</c:when>
							<c:when test="${prplyjPartoffersVo.quality eq '3'}">
							<input type="text" class="input-text" readonly value="合格市场价" />
							</c:when>
							<c:when test="${prplyjPartoffersVo.quality eq '4'}">
							<input type="text" class="input-text" readonly value="定制协商" />
							</c:when>
							<c:otherwise>
							<input type="text" class="input-text" readonly value="" />
							</c:otherwise>
							</c:choose>
							</div>
							<label class="form_label col-2">第一含税报价:</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"  readonly value="${prplyjPartoffersVo.priceWithtax}"/>
							</div>	
							<label class="form_label col-2">第一不含税报价:</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"  readonly value="${prplyjPartoffersVo.priceWithouttax}"/>
							</div>
						 </div>
						   
							<div class="row cl">
							  <label class="form_label col-2">出货时间(工作日):</label>
							   <div class="form_input col-2">
								 <input type="text" class="input-text"  readonly value=""/>
							   </div>
							   <label class="form_label col-2">质保时间(工作日):</label>
							   <div class="form_input col-2">
								  <input type="text" class="input-text"  readonly value=""/>
							   </div>
							</div>
						</div>
						</div>
						<h3 class="panel-title">
						<font color="blue">
					     *报价二  
					     </font>
				        </h3>	
				       <div class="table_cont ">
					   <div class="formtable">
						<div class="row cl">
							<label class="form_label col-2">配件第二品质:</label>
					 		<div class="form_input col-2">
								<c:choose>
							   <c:when test="${prplyjPartoffersVo.quality2 eq '1'}">
							     <input type="text" class="input-text" readonly value="原厂价" />
							   </c:when>
							  <c:when test="${prplyjPartoffersVo.quality2 eq '2'}">
							     <input type="text" class="input-text" readonly value="精品同质价" />
							   </c:when>
							   <c:when test="${prplyjPartoffersVo.quality2 eq '3'}">
							      <input type="text" class="input-text" readonly value="合格市场价" />
							   </c:when>
							  <c:when test="${prplyjPartoffersVo.quality2 eq '4'}">
							     <input type="text" class="input-text" readonly value="定制协商" />
							  </c:when>
							 <c:otherwise>
							      <input type="text" class="input-text" readonly value="" />
							  </c:otherwise>
							 </c:choose>
						  </div>	
							<label class="form_label col-2">第二含税报价:</label>
							 <div class="form_input col-2">
								<input type="text" class="input-text" readonly  value="${prplyjPartoffersVo.pricewithtax2}" />
							</div>
							<label class="form_label col-2">第二不含税报价:</label>
							 <div class="form_input col-2">
								<input type="text" class="input-text" readonly  value="${prplyjPartoffersVo.priceWithouttax2}" />
							</div>	
								
						</div>
						<div class="row cl">
							  <label class="form_label col-2">出货时间(工作日):</label>
							   <div class="form_input col-2">
								 <input type="text" class="input-text"  readonly value=""/>
							   </div>
							   <label class="form_label col-2">质保时间(工作日):</label>
							   <div class="form_input col-2">
								  <input type="text" class="input-text"  readonly value=""/>
							   </div>
						</div>
						<!--撑开页面  开始  -->
						<div class="row cl"><div class="form_input col-6"></div></div>
						<div class="row cl"><div class="form_input col-6"></div></div>
						<!-- 结束 -->	
				 </div>
				 <div class="btn-footer clearfix text-c">
				     <a class="btn btn-primary" onclick="closeLayer()" >关闭</a>
				 </div>
			     </div>
			     
		</div>
</form>
<script type="text/javascript">
 function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
</script>
</body>
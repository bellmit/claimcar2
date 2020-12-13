<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>调解信息</title>
<style>
.tableoverlable label.form_label.col-1{width:9.3333%;padding-right:0;}
.tableoverlable .form_input.col-3{  width: 23%;margin-left: 1%;}
.tableoverlable .form_input.col-2{  width: 15%;margin-left: 0.6%;}
.btn.garage{
  width: 44%;
  line-height: 31px;
  padding: 0;
}
#mns {margin-left: -4px;}
</style>
</head>
<body>
	<div class="page_wrap">
		<form id="saveCar" class="saveForm" role="form" >
			<div class="table_wrap">
				<div class="table_title f14">调解信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">调解号:</label>
							<div class="form_input col-2">
								${courtMediationVo.mediationnum}
							</div>
							<label class="form_label col-1">调解类型:</label>
							<div class="form_input col-2">
								<app:codetrans codeType="mediationtype" codeCode="${courtMediationVo.mediationtype}" />
							</div>
							<label class="form_label col-1">申请日期:</label>
							<div class="form_input col-2">
								 <fmt:formatDate  value="${courtMediationVo.applydate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">受理日期:</label>
							<div class="form_input col-2">
							 <fmt:formatDate  value="${courtMediationVo.acceptdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">调解机构:</label>
							<div class="form_input col-2">
								${courtMediationVo.mediation}
							</div>
							<label class="form_label col-1">调解员名称:</label>
							<div class="form_input col-2">
								${courtMediationVo.handler}
							</div>
							<label class="form_label col-1">调解地点:</label>
							<div class="form_input col-2">
								${courtMediationVo.mediationaddr}
							</div>
							<label class="form_label col-1">调解日期:</label>
							<div class="form_input col-2">
							 <fmt:formatDate  value="${courtMediationVo.mediationdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">调解状态:</label>
							<div class="form_input col-2">
								${courtMediationVo.mediationstatus}
							</div>
							<label class="form_label col-1">是否申请鉴定:</label>
							<div class="form_input col-2">
								<app:codetrans codeType="IsValid" codeCode="${courtPartyVo.isappraisal}" />
							</div>
							<label class="form_label col-1">赔偿年度:</label>
							<div class="form_input col-2">
								${courtMediationVo.pcnd}
							</div>
							<label class="form_label col-1">赔偿地:</label>
							<div class="form_input col-2">
								${courtMediationVo.pcd}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">履行时限:</label>
							<div class="form_input col-2">
								 <fmt:formatDate  value="${courtMediationVo.lxsx}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">是否司法确认:</label>
							<div class="form_input col-2">
								<app:codetrans codeType="IsValid"
								codeCode="${courtMediationVo.issfqr}" />
							</div>
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1 text-c">就医情况</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;" id="dangerRemark" disabled ="disabled"
								value="${courtMediationVo.jyqk}">${courtMediationVo.jyqk}</textarea>
						</div>
					</div>
				<div class="row cl">
				<label class="form_label col-1 text-c">垫付情况：</label>
					<div class="form_input col-10">
						<textarea class="textarea" style="height: 80px;" id="dangerRemark" disabled ="disabled"
							value="${courtMediationVo.dfqk}">${courtMediationVo.dfqk}</textarea>
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-1 text-c">继承人情况</label>
					<div class="form_input col-10">
						<textarea class="textarea" style="height: 80px;" id="dangerRemark"  disabled ="disabled"
							value="${courtMediationVo.jcrqk}">${courtMediationVo.jcrqk}</textarea>
					</div>
				</div>
				
				<div class="row cl">
				<label class="form_label col-1 text-c">申请事项：</label>
					<div class="form_input col-10">
						<textarea class="textarea" style="height: 80px;" id="dangerRemark" disabled ="disabled"
							value="${courtMediationVo.apply}">${courtMediationVo.apply}</textarea>
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-1 text-c">调解结果</label>
					<div class="form_input col-10">
						<textarea class="textarea" style="height: 80px;" id="dangerRemark"  disabled ="disabled"
							value="${courtMediationVo.dealresult}">${courtMediationVo.dealresult}</textarea>
					</div>
				</div>
				<div class="row cl">
					<label class="form_label col-1 text-c">调解协议内容</label>
					<div class="form_input col-10">
						<textarea class="textarea" style="height: 80px;" id="dangerRemark"  disabled ="disabled"
							value="${courtMediationVo.mediationcontent}">${courtMediationVo.mediationcontent}</textarea>
					</div>
				</div>
			</div>
			</div>
		</form>
	</div>
</body>
</html>
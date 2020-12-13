<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>鉴定详细信息</title>
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
				<div class="table_title f14">鉴定信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">鉴定编号：</label>
							<div class="form_input col-3">
								${courtIdentifyVo.appraisalno}
							</div>
							<label class="form_label col-1">申请人姓名：</label>
							<div class="form_input col-3">
									${courtIdentifyVo.applicantname}
							</div>
							<label class="form_label col-1">申请日期：</label>
							<div class="form_input col-3">
								<fmt:formatDate  value="${courtIdentifyVo.applicantdate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">鉴定机构：</label>
							<div class="form_input col-3">
									${courtIdentifyVo.appraisal}
							</div>
							<label class="form_label col-1">接收时间：</label>
							<div class="form_input col-3">
								<fmt:formatDate  value="${courtIdentifyVo.rexeptiontime}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">鉴定预估时间：</label>
							<div class="form_input col-3">
								<fmt:formatDate  value="${courtIdentifyVo.appraisaldate}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">鉴定费用：</label>
							<div class="form_input col-3">
								${courtIdentifyVo.appraisalsum}
							</div>
							<label class="form_label col-1">鉴定地点：</label>
							<div class="form_input col-3">
								${courtIdentifyVo.appraisaladdr}
							</div>
							<label class="form_label col-1">鉴定人：</label>
							<div class="form_input col-3">
								${courtIdentifyVo.appraiser}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">鉴定项目：</label>
							<div class="form_input col-3">
								${courtIdentifyVo.appraisalproj}
							</div>
							<label class="form_label col-1">鉴定类型：</label>
							<div class="form_input col-3">
								${courtIdentifyVo.appraisaltype}
							</div>
							<label class="form_label col-1">在场人员：</label>
							<div class="form_input col-3">
								${courtIdentifyVo.presence}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">伤残等级：</label>
								<div class="form_input col-3">
									<app:codetrans codeType="SCDJ" codeCode="${courtIdentifyVo.scdj}" />
								</div>
						</div>
					</div>
					<div class="row cl">
						<label class="form_label col-1 text-c">案件摘要：</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;"   disabled="disabled"
								value="${courtIdentifyVo.casesummary}">${courtIdentifyVo.casesummary}</textarea>
							</div>
						</div>
						<div class="row cl">
						<label class="form_label col-1 text-c">病例摘要：</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;"   disabled="disabled"
								value="${courtIdentifyVo.medicalsummary}">${courtIdentifyVo.medicalsummary}</textarea>
							</div>
						</div>
						<div class="row cl">
						<label class="form_label col-1 text-c">分析说明：</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;"   disabled="disabled"
								value="${courtIdentifyVo.analyexplain}">${courtIdentifyVo.analyexplain}</textarea>
							</div>
						</div>
						<div class="row cl">
						<label class="form_label col-1 text-c">鉴定意见：</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;"   disabled="disabled"
								value="${courtIdentifyVo.appraisalopinion}">${courtIdentifyVo.appraisalopinion}</textarea>
							</div>
						</div>
						<div class="row cl">
						<label class="form_label col-1 text-c">鉴定报告：</label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;"   disabled="disabled"
								value="${courtIdentifyVo.appraisalrep}">${courtIdentifyVo.appraisalrep}</textarea>
						</div>
						</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
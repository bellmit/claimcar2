<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>诉前调解信息</title>
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
				<div class="table_title f14">诉前调解信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">调解字号:</label>
							<div class="form_input col-3">
								${courtLitigationVo.tjzh}
							</div>
							<label class="form_label col-1">起诉人:</label>
							<div class="form_input col-3">
									${courtLitigationVo.qsr}
							</div>
							<label class="form_label col-1">被起诉人:</label>
							<div class="form_input col-3">
								${courtLitigationVo.bqsr}
							</div> 
						</div>
						<div class="row  cl">
							<label class="form_label col-1">进入调解日期:</label>
							<div class="form_input col-3">
								 <fmt:formatDate  value="${courtLitigationVo.jrtjrq}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">调解完成日期:</label>
							<div class="form_input col-3">
								 <fmt:formatDate  value="${courtLitigationVo.tjwcrq}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">立案案由代码:</label>
							<div class="form_input col-3">
								${courtLitigationVo.laaydm}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">经办法院代码:</label>
							<div class="form_input col-3">
								${courtLitigationVo.jbfydm}
							</div>
							<label class="form_label col-1">调解状态:</label>
							<div class="form_input col-3">
								${courtLitigationVo.tjzt}
							</div>
							<label class="form_label col-1">指导法官:</label>
							<div class="form_input col-3">
									${courtLitigationVo.zdfg}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">收件人员:</label>
							<div class="form_input col-3">
								${courtLitigationVo.sjry}
							</div>
							<label class="form_label col-1">调解类型:</label>
							<div class="form_input col-3">
								${courtLitigationVo.tjlx}
							</div>
							<label class="form_label col-1">是否小额诉讼:</label>
							<div class="form_input col-3">
								<app:codetrans codeType="IsValid" codeCode="${courtLitigationVo.isxess}" />
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">起始日期:</label>
							<div class="form_input col-3">
								<fmt:formatDate  value="${courtLitigationVo.qsrq}" pattern="yyyy-MM-dd HH:mm:ss"/>
							</div>
							<label class="form_label col-1">终止日期:</label>
							<div class="form_input col-3">
								<span class="select-box">
								<fmt:formatDate  value="${courtLitigationVo.jzrq}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</span>
							</div>
							<label class="form_label col-1">调解地点:</label>
							<div class="form_input col-3">
								${courtLitigationVo.tjdd}
							</div>
						</div>
					</div>
					<div class="formtable tableoverlable">
							<div class="row cl">
								<label class="form_label col-1">调解部门:</label>
								<div class="form_input col-3">
									${courtLitigationVo.tjbm}
								</div>
								<label class="form_label col-1">调解人员:</label>
								<div class="form_input col-3">
									${courtLitigationVo.tjry}
								</div>
							</div>
						</div>
						<div class="formtable tableoverlable">
							<div class="row cl">
								<label class="form_label col-1">申请日期:</label>
								<div class="form_input col-3">
									<fmt:formatDate  value="${courtLitigationVo.sqrq}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</div>
								<label class="form_label col-1">受理日期:</label>
								<div class="form_input col-3">
									<fmt:formatDate  value="${courtLitigationVo.slrq}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</div>
							</div>
						</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
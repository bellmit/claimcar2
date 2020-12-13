<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>赔偿信息</title>
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
				<div class="table_title f14">赔偿信息</div>
				<div class="table_cont ">
					<div class="formtable tableoverlable">
						<div class="row  cl">
							<label class="form_label col-1">当事人证件号:</label>
							<div class="form_input col-2">
								${courtCompensationVo.dsr}
							</div>
							<label class="form_label col-1">医疗费:</label>
							<div class="form_input col-2">
									${courtCompensationVo.ylf}
							</div>
							<label class="form_label col-1">住院伙食补助费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.zyhsbz}
							</div>
							<label class="form_label col-1">后续治疗费:</label>
							<div class="form_input col-2">
									${courtCompensationVo.hxzlf}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">整容费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.zrf}
							</div>
							<label class="form_label col-1">护理费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.hlf}
							</div>
							<label class="form_label col-1">残疾赔偿金:</label>
							<div class="form_input col-2">
								${courtCompensationVo.cjpcj}
							</div>
							<label class="form_label col-1">死亡赔偿金:</label>
							<div class="form_input col-2">
								${courtCompensationVo.swpcj}
							</div>
						</div>
						<div class="row  cl">
							<label class="form_label col-1">丧葬费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.szf}
							</div>
							<label class="form_label col-1">家属误工费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.jswgf}
							</div>
							<label class="form_label col-1">家属交通费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.jsjtf}
							</div>
							<label class="form_label col-1">家属住宿费:</label>
							<div class="form_input col-2">
									${courtCompensationVo.jszsf}
							</div>
						</div>
						<div class="row cl">
							<label class="form_label col-1">被扶养人生活费:</label>
							<div class="form_input col-2">
								<span class="select-box">
								${courtCompensationVo.bfyrshf}
								</span>
							</div>
							<label class="form_label col-1">精神损失抚慰金:</label>
							<div class="form_input col-2">
								${courtCompensationVo.jsshfwj}
							</div>
							<label class="form_label col-1">残疾辅助器具费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.cjfzqjf}
							</div>
							<label class="form_label col-1">交通费:</label>
							<div class="form_input col-2">
								${courtCompensationVo.jtf}
							</div>
						</div>
					</div>
					<div class="formtable tableoverlable">
							<div class="row cl">
								<label class="form_label col-1">衣物损失费:</label>
								<div class="form_input col-2" >
										${courtCompensationVo.ywssf}
								</div>
								<label class="form_label col-1">车辆施救费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.clsjf}
								</div>
								<label class="form_label col-1">车辆修理费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.clxlf}
								</div>
								<label class="form_label col-1">评估费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.pgf}
								</div>
							</div>
						</div>
						<div class="formtable tableoverlable">
							<div class="row cl">
								<label class="form_label col-1">其他财务损失费:</label>
								<div class="form_input col-2">
										${courtCompensationVo.cwssf}
								</div>
								<label class="form_label col-1">鉴定费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.jdf}
								</div>
								<label class="form_label col-1">住院用品费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.zyypf}
								</div>
								<label class="form_label col-1">住宿费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.zsf}
								</div>
							</div>
						</div>
						<div class="formtable tableoverlable">
							<div class="row cl">
								<label class="form_label col-1" >停运损失费:</label>
								<div class="form_input col-2" >
										${courtCompensationVo.tyssf}
								</div>
								<label class="form_label col-1">车辆贬损费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.clbsf}
								</div>
								<label class="form_label col-1">律师代理费:</label>
								<div class="form_input col-2">
									${courtCompensationVo.lsdlf}
								</div>
								<label class="form_label col-1">其他:</label>
								<div class="form_input col-2">
									${courtCompensationVo.qt}
								</div>
							</div>	
							<div class="row cl">
								<label class="form_label col-1" >营养费:</label>
								<div class="form_input col-2" >
										${courtCompensationVo.yyf}
								</div>
							</div>
						</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>
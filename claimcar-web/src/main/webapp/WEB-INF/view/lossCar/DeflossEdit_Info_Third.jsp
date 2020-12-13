<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<div class="table_wrap">
			<div class="table_title f14">定损信息</div>
				<div class="table_cont " id="deflossInfo">
					<div class="formtable  tableoverlable">
						<div class="row cl">
							<label class="form_label col-1">定损方式</label>
							<div class="form_input col-3">
								<span class="select-box moddisable">
									<app:codeSelect codeType="CertainLossType"  clazz="must" name="lossCarMainVo.cetainLossType"  value="${lossCarMainVo.cetainLossType}" id="certainType" onchange="changeCertainLoss()"  type="select" />
					 			</span>
					 			<input type="hidden" name="lossCarMainVo.offLineHanding" value="${lossCarMainVo.offLineHanding }">
							</div>
							<label class="form_label col-1">定损车种</label>
							<div class="form_input col-3">
								<span class="select-box">
									<app:codeSelect codeType="DefModelType" name="carInfoVo.lossCarKindCode" value="${carInfoVo.lossCarKindCode }" type="select" clazz="must"/>
					 			</span>
							</div>
							<label class="form_label col-1">出险时间</label>
							<div class="form_input col-3">
								<app:date  date="${commonVo.damageDate }"/>
								<input type="hidden" id="damageDate" value="<app:date  date='${commonVo.damageDate }'/>">
							</div>
					 	</div>
						<div class="row cl">
							<label class="form_label col-1">修理厂代码</label>
							<div class="col-3">
								<div class="form_input col-9">
									<input type="text" class="input-text" name="lossCarMainVo.repairFactoryCode" value="${lossCarMainVo.repairFactoryCode }" readonly="readonly" id="factoryCode"/>
								</div>
								<c:if test="${jy2Flag=='0'}">
									<div class="form_input col-3 checkBtn">
										<input class="btn btn-primary" type="button" value="查询" id="check">
									</div>
								</c:if>
							</div>
							<!-- <div> -->
								<div>
								<label class="form_label col-1">修理厂名称</label>
								<div class="form_input col-3">
									<input type="text" class="input-text" name="lossCarMainVo.repairFactoryName" placeholder="请点击查询" readonly="readonly" value="${lossCarMainVo.repairFactoryName}" id="factoryName"/>
									<font class="must">*</font>
								</div>
								</div>
							<!-- </div> -->
							<!-- <div> -->
							<label class="form_label col-1">定损日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate backDisabled" datatype="*" style="width: 96%;" name="lossCarMainVo.deflossDate"  id="deflossDate" 
									value="<app:date date='${lossCarMainVo.deflossDate }'/>" onfocus="WdatePicker({maxDate:'%y-%M-%d'})" nullmsg="请填写定损日期！"/>
								<font class="must">*</font> 	
							</div>
							<!-- </div> -->
					 	</div>
					 
					 	<div class="row cl">
					 		<label class="form_label col-1">修理厂类型</label>
							<div class="form_input col-3">
								<span class="select-box">
									<app:codeSelect codeType="RepairFactoryType" id="repairFactoryType"
										value="${lossCarMainVo.repairFactoryType }" type="select" />
									<input type="hidden" name="lossCarMainVo.repairFactoryType" id="factoryType_hid" value="${lossCarMainVo.repairFactoryType }">
								</span>
							</div>
					 		<label class="form_label col-1">修理厂电话</label>
							<div class="form_input col-3">
								<input type="text" class="input-text ready-only" id="factoryMobile" name="lossCarMainVo.factoryMobile"  value="${lossCarMainVo.factoryMobile }" datatype="m|/^0\d{2,3}-\d{7,8}$/"  ignore="ignore" />
							</div>
							<label class="form_label col-1">推修方式</label>
							<div class="form_input col-3">
								<span class="select-box moddisable">
									<app:codeSelect codeType="SendRepairType" name="lossCarMainVo.repairType"  value="${lossCarMainVo.repairType}"  type="select" />
					 			</span>
							</div>
					 	</div>
					</div> 	
					<div class="formtable tableoverlable">
					 	<div class="row cl">
					 		<label class="form_label col-1">定损地点</label>
							<div class="form_input col-3">
								<input type="text" class="input-text " id="defSite" name="lossCarMainVo.defSite" value="${lossCarMainVo.defSite}"/>
								<font class="must">*</font> 
							</div>
							<label class="form_label col-1">损失程度</label>
							<div class="form_input col-3">
								<span class="select-box">
									<app:codeSelect codeType="LossLevel" id="carInfo_lossLevel" name="lossCarMainVo.lossLevel" type="select" clazz="must"/>
								</span>
							</div>
							<label class="form_label col-1">推修时间</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate " style="width: 96%;" name="lossCarMainVo.sendDate"  id="sendDate" 
									value="<app:date date='${lossCarMainVo.sendDate }'/>"  onfocus="WdatePicker({maxDate:'%y-%M-%d'})">
							</div>
					 	</div>
					 </div> 	
					<div class="formtable tableoverlable">	
					 	<div class="row cl">
					 		<c:choose>
								<c:when test="${taskVo.subNodeCode =='DLChk' }">
									<label class="form_label col-1">复检人员</label>
									<div class="form_input col-3">
										<input type="text" class="input-text" name="lossCarMainVo.reCheckName" id="handlerName" datatype="*" value="${lossCarMainVo.reCheckName }"/>
										<font class="must">*</font> 
										<input type="hidden" class="input-text" name="lossCarMainVo.reCheckCode" value="${lossCarMainVo.reCheckCode }"/>
									</div>
									<div>
										<label class="form_label col-1">身份证号码</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="lossCarMainVo.reCheckIdNo" id="defIdNo" datatype="idcard" value="${lossCarMainVo.reCheckIdNo }"
											onchange="toUpperCase(this)"/>
											<font class="must">*</font> 
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<label class="form_label col-1">定损员</label>
									<div class="form_input col-3">
										<input type="text" class="input-text" name="lossCarMainVo.handlerName" id="handlerName" datatype="*" value="${lossCarMainVo.handlerName }"/>
										<font class="must">*</font> 
										<input type="hidden" class="input-text" name="lossCarMainVo.handlerCode" value="${lossCarMainVo.handlerCode }"/>
									</div>
									<div>
										<label class="form_label col-1">身份证号码</label>
										<div class="form_input col-3">
											<input type="text" class="input-text" name="lossCarMainVo.handlerIdNo" id="defIdNo" datatype="idcard" value="${lossCarMainVo.handlerIdNo }"
											onchange="toUpperCase(this)"/>
											<font class="must">*</font> 
										</div>
									</div>
					 			</c:otherwise>
					 		</c:choose>
					 		<label class="form_label col-1">预计修理完成日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate " style="width: 96%;" name="lossCarMainVo.estimatedDate" value="<app:date date='${lossCarMainVo.estimatedDate }'/>"
								 id="estimatedDate" onfocus="WdatePicker()">
							</div>
					 	</div>
					 	<div class="row cl">
					 		
					 		<label class="form_label col-1">事故责任比例</label>
									<div class="col-3">
										<div class="form_input col-6">
											<span class="select-box">
												<app:codeSelect codeType="IndemnityDuty" id="indemnityDuty" name="lossCarMainVo.indemnityDuty" onchange="changeDuty()"
													value="${lossCarMainVo.indemnityDuty }"  type="select" />
											</span>
										</div>
										<div class="form_input col-6">
											<input type="text" class="input-text" id="indemnityDutyRate" style="width: 92%;" name="lossCarMainVo.indemnityDutyRate" onchange="compareDutyRate(this)"
												datatype="/^(\d{1,2}(\.\d{1,2})?|100(\.0{1,2})?)$/" value="${lossCarMainVo.indemnityDutyRate}"  maxlength="3"/>
												 <font class="must">*</font>
										</div>
									</div>
							<label class="form_label col-1">定损类别</label>
							<div class="form_input col-3">
								<app:codetrans codeType="InterMediaryType" codeCode="${lossCarMainVo.intermFlag }"/>
								<input type="hidden" name="lossCarMainVo.intermFlag" value="${lossCarMainVo.intermFlag }">
							</div>
					
					    <div id="interMediaryDiv">
							<c:if test="${lossCarMainVo.intermFlag=='1' }">
									<label class="form_label col-1">公估公司</label>
									<div class="form_input col-3">
										<input type="hidden" name="lossCarMainVo.intermCode" value="${lossCarMainVo.intermCode}">
										 <app:codetrans codeType="GongGuPayCode" codeCode="${lossCarMainVo.intermCode}"/>
									</div>
								</c:if>
							
						</div>
			  </div>
						
			   <div class="row cl">
				<label class="form_label col-1">是否无责代赔</label>
				<div class="form_input col-3">
					<span class="radio-box"> <app:codeSelect codeType="IsValid"
							type="radio" value="${lossCarMainVo.isNodutypayFlag}"
							name="lossCarMainVo.isNodutypayFlag" />
					</span><font class="must">*</font>
			    </div>
				
					<label class="form_label col-1">是否水淹</label>
				<div class="form_input col-3">
					<span class="radio-box"> <app:codeSelect codeType="IsValid"
							type="radio" nullToVal="0" value="${lossCarMainVo.isWaterFloaded}"
							name="lossCarMainVo.isWaterFloaded"  onchange="isWaterFlooded()"/>
					</span><font class="must">*</font>
				</div>

				<div id = "waterFloodedLevel">
					<label class="form_label col-1">水淹等级</label>
					<div class="form_input col-3">
						<span class="select-box" style="width: 95%"> <app:codeSelect
								codeType="WaterFloodedLevel" type="select" lableType="name"
								name="lossCarMainVo.waterFloodedLevel" clazz="select2 must" 
								value="${lossCarMainVo.waterFloodedLevel}" />
						</span><font class="must">*</font>
					</div>
				</div>
			</div>
			<div class="row cl">
			<label class="form_label col-1">是否火自爆</label>
				<div class="form_input col-3">
					<span class="radio-box"> <app:codeSelect codeType="IsValid"
							type="radio" nullToVal="0" value="${lossCarMainVo.isHotSinceDetonation}"
							name="lossCarMainVo.isHotSinceDetonation" />
					</span><font class="must">*</font>
			    </div>
			<label class="form_label col-1">是否全损</label>
					<div class="form_input col-3">
						<span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" nullToVal="0" value="${lossCarMainVo.isTotalLoss}"
								name="lossCarMainVo.isTotalLoss"  />
						</span><font class="must">*</font>
					</div>
					<div class="row cl">
					<label class="form_label col-1">是否车物减损</label>
					<div class="form_input col-3">
						<span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" value="${lossCarMainVo.isWhethertheloss}" 
								name="lossCarMainVo.isWhethertheloss" />
						</span><font class="must">*</font>
				    </div>
				</div>
			</div>		
					 	<div class="row  cl lossPart">
						<label class="form_label col-1 losspartTit">损失部位</label> 
							<app:codeSelect codeType="LossPart" name="lossCarMainVo.lossPart" value="${lossCarMainVo.lossPart }" datatype="*" type="checkbox"/>
							<label class="check-box f1"><input type="checkbox"  class="allLossCbx "  id="allLossPart" style="vertical-align:middle" onclick="allCheck(this)">全车</label>
					</div>
				</div>
			</div>
		</div>	
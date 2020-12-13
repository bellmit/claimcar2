<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
		<div class="table_wrap">
			<div class="table_title f14">定损信息</div>
				<div class="table_cont " id="deflossInfo">
					<div class="formtable  tableoverlable">
						<div class="row cl">
						
							<label class="form_label col-1">损失类别</label>
							<div class="form_input col-3">
								<span class="select-box moddisable">
									<app:codeSelect codeType="LossFeeType" name="lossCarMainVo.lossFeeType"  value="${lossCarMainVo.lossFeeType}" id="lossFeeType" type="select" />
					 			</span>
							</div>
							
							<label class="form_label col-1">定损方式</label>
							<div class="form_input col-3">
								<span class="select-box moddisable">
									<app:codeSelect codeType="CertainLossType"  clazz="must" name="lossCarMainVo.cetainLossType"  value="${lossCarMainVo.cetainLossType}" id="certainType" onchange="changeCertainLoss()"  type="select" />
					 			</span>
							</div>
							<label class="form_label col-1">定损车种</label>
							<div class="form_input col-3">
								<span class="select-box">
									<app:codeSelect codeType="DefModelType" name="carInfoVo.lossCarKindCode" value="${carInfoVo.lossCarKindCode }" type="select" clazz="must"/>
					 			</span>
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
								
							<label class="form_label col-1">出险时间</label>
							<div class="form_input col-3">
								<app:date  date="${commonVo.damageDate }"/>
								<input type="hidden" id="damageDate" value="<app:date  date='${commonVo.damageDate }'/>">
							</div>
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
								<input type="text" class="input-text ready-only" id="factoryMobile" name="lossCarMainVo.factoryMobile"  value="${lossCarMainVo.factoryMobile }" datatype="m|/^0\d{2,3}-\d{7,8}$/"  ignore="ignore"/>
							</div>
							
							<label class="form_label col-1">定损日期</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate backDisabled" datatype="*" style="width: 96%;" name="lossCarMainVo.deflossDate"  id="deflossDate" 
									value="<app:date date='${lossCarMainVo.deflossDate }'/>" onfocus="WdatePicker({maxDate:'%y-%M-%d'})" nullmsg="请填写定损日期！"/>
								<font class="must">*</font> 	
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
							<label class="form_label col-1">推修时间</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate " style="width: 96%;" name="lossCarMainVo.sendDate"  id="sendDate" 
									value="<app:date date='${lossCarMainVo.sendDate }'/>"  onfocus="WdatePicker({maxDate:'%y-%M-%d'})">
							</div>
					 	</div>
					 	<div class="row cl">
					 		<label class="form_label col-1">预计修理完成</label>
							<div class="form_input col-3">
								<input type="text" class="Wdate " style="width: 96%;" name="lossCarMainVo.estimatedDate" value="<app:date date='${lossCarMainVo.estimatedDate }'/>"
								 id="estimatedDate" onfocus="WdatePicker()">
							</div>
							
							<label class="form_label col-1">车损险保额</label>
								<div class="form_input col-3">
									<input type="hidden" class="input-text"  id="carAmount" value="${commonVo.carAmount}"/>￥
									${commonVo.carAmount}
								</div>
							
							<label class="form_label col-1">定损类别</label>
							<div class="form_input col-3">
								<app:codetrans codeType="InterMediaryType" codeCode="${lossCarMainVo.intermFlag }"/>
								<input type="hidden" name="lossCarMainVo.intermFlag" value="${lossCarMainVo.intermFlag }">
							</div>
							
					 	</div>
					 	<div class="row cl">
					 	<label class="form_label col-1">有特种车资格证</label>
			                <div class="form_input col-3">
						       <span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" value="${lossCarMainVo.isSpecialcarFlag}"
								name="lossCarMainVo.isSpecialcarFlag"  />
						      </span><font class="must">*</font>
			                </div>
				 			<label class="form_label col-1">互碰自赔</label>
								<div class="form_input col-3">
								<span class="radio-box">
									<app:codeSelect codeType="YN10" type="radio" name="lossCarMainVo.isClaimSelf" value="${lossCarMainVo.isClaimSelf }"/>
								</span><font class="must">*</font> 
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
			<label class="form_label col-1">有营业车资格证</label>
			  <div class="form_input col-3">
						       <span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" value="${lossCarMainVo.isBusinesscarFlag}"
								name="lossCarMainVo.isBusinesscarFlag"  />
						      </span><font class="must">*</font>
			</div>
			<label class="form_label col-1">是否单证齐全</label>
			  <div class="form_input col-3">
						       <span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" value="${lossCarMainVo.directFlag}"
								name="lossCarMainVo.directFlag"  />
						      </span><font class="must">*</font>
			</div>
			<label class="form_label col-1">是否全损</label>
					          <div class="form_input col-3">
						       <span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" nullToVal="0" value="${lossCarMainVo.isTotalLoss}"
								name="lossCarMainVo.isTotalLoss"  />
						      </span><font class="must">*</font>
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
								name="lossCarMainVo.waterFloodedLevel" clazz="must" 
								value="${lossCarMainVo.waterFloodedLevel}" />
						</span><font class="must">*</font>
					</div>
				</div>
			</div>
			<div class="row cl">
				
			<label class="form_label col-1"><h5><B>重大赔案上报</B></h5></label>
			  <div class="form_input col-3">
				  <span class="radio-box"> <app:codeSelect codeType="IsValid" type="radio" nullToVal="0"
						value="${lossCarMainVo.isMajorCase}" name="lossCarMainVo.isMajorCase" onchange="isMajorCase()"/>
			  </span><font class="must">*</font>
			  </div>
			<c:if test="${caseFlag eq '1' || caseFlag eq '3' }">
			<label class="form_label col-1">商业拒赔</label>
			 <div class="form_input col-3">
						       <span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" nullToVal="0" value="${lossCarMainVo.isBInotpayFlag}"
								name="lossCarMainVo.isBInotpayFlag"  />
						      </span><font class="must">*</font>
			</div>
			</c:if>
			<c:if test="${caseFlag eq '2' || caseFlag eq '3' }">
			<label class="form_label col-1">交强拒赔</label>
			 <div class="form_input col-3">
						       <span class="radio-box"> <app:codeSelect codeType="IsValid"
								type="radio" nullToVal="0" value="${lossCarMainVo.isCInotpayFlag}"
								name="lossCarMainVo.isCInotpayFlag"  />
						      </span><font class="must">*</font>
			</div>
			</c:if>
			</div>
			<div class="row cl">
			
			
			 <c:if test="${existEndCase eq 'N' }">
							<label class="form_label col-1">是否线下处理</label>
							<div class="form_input col-3">
								<span class="radio-box"> <app:codeSelect codeType="IsValid"
									type="radio" nullToVal="0" value="${lossCarMainVo.offLineHanding}"
									name="lossCarMainVo.offLineHanding"  onchange="changeOffLineHanding()" />
								</span><font class="must">*</font>
							</div>
			  </c:if>
			  
			 <div id="payCause" class="hide">
			    <label class="form_label col-1">拒赔原因</label>
				 <div class="form_input col-3">
				 <span class="select-box">
				 <app:codeSelect codeType="DZNOTPAY" type="select" value="${lossCarMainVo.notpayCause}" name="lossCarMainVo.notpayCause" clazz="must"/>
				 <font class="c-red">*</font>
				 </span>
				 </div>
			 </div>
			
		      <div id="otherPayCause" class="hide">
			      <label class="form_label col-1">其它原因</label>
			      <div class="form_input col-3">
			         <input type="text" class="input-text" name="lossCarMainVo.otherNotpayCause" id="otherNotpayCause" value="${lossCarMainVo.otherNotpayCause}" /><font class="c-red">*</font>
			      </div>
			  </div>
			 
			</div>
			<div class="row cl">
				<div id="isWhethertheloss">
			      <label class="form_label col-1">是否车物减损</label>
			      <div class="form_input col-3">
					<span class="radio-box"> <app:codeSelect codeType="IsValid"
						type="radio" value="${lossCarMainVo.isWhethertheloss}"
						name="lossCarMainVo.isWhethertheloss"/>
					</span><font class="must">*</font>
				  </div>
			  </div>
			  <div id="isGlassBroken">
			      <label class="form_label col-1">是否玻璃单独破碎</label>
			      <div class="form_input col-3">
					<span class="radio-box"> <app:codeSelect codeType="IsValid"
						type="radio" value="${lossCarMainVo.isGlassBroken}"
						name="lossCarMainVo.isGlassBroken"/>
					</span><font class="must" id="isGlassBrokenClass">*</font>
				  </div>
			  </div>
			  <div id="isNotFindThird">
			      <label class="form_label col-1">是否属于无法找到第三方</label>
			      <div class="form_input col-3">
					<span class="radio-box"> <app:codeSelect codeType="IsValid"
						type="radio" value="${lossCarMainVo.isNotFindThird}"
						name="lossCarMainVo.isNotFindThird"/>
					</span><font class="must" id="isNotFindThirdClass">*</font>
				  </div>
			  </div>
			</div>
			<div class="row  cl lossPart">
						<label class="form_label col-1 losspartTit">损失部位</label> 
							<app:codeSelect codeType="LossPart" name="lossCarMainVo.lossPart" value="${lossCarMainVo.lossPart }" datatype="*" type="checkbox"/>
							<label class="check-box f1"><input type="checkbox"  class="allLossCbx "  id="allLossPart" style="vertical-align:middle" onclick="allCheck(this)">全车</label>
			</div>
									<!-- 重大赔案意见 -->
					<div class="row cl" id="claimTexts">
					<div>
						<label class="form_label col-1"><h5><B>重大赔案意见</B></h5></label>
						<div class="form_input col-10">
							<textarea class="textarea" style="height: 80px;margin-left: 10px;margin-top:6px"
							name="lossCarMainVo.contexts" datatype="*0-500" value="${lossCarMainVo.contexts}" id="majorText"
							placeholder="请输入...">${lossCarMainVo.contexts}</textarea><font class="must">*</font>
					</div><br/><br/><br/>
					</div>
					<br/><br/>
					</div>
				
			</div>
		</div>	
		</div>
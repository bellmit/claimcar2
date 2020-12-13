<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!-- 4s店信息维护 -->
<!DOCTYPE HTML>
<html>
<head>
<title>4s店信息维护</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

</head>
<body>
	<input type="hidden" name="flag" id="flag" value="${flag}"/>
	<form  id="FourSShopEditForm">
		<div class="table_cont">
			<!-- 4s店信息维护  开始 -->
			<div class="table_wrap">
				<div class="table_title f14">出单合作店代码编辑</div>
				<div class="table_cont">
					<div class="formtable">
					<!-- 隐藏域 -->
						<input type="hidden" name="prpLFourSShopInfoVo.id" value="${prpLFourSShopInfo.id}"/>
						<input type="hidden" name="prpLFourSShopInfoVo.comName" value="${prpLFourSShopInfo.comName}"/>
						<input type="hidden" name="prpLFourSShopInfoVo.busiNatureName" value="${prpLFourSShopInfo.busiNatureName}" datatype="*"/>
					<!-- 隐藏域 -->
					
						<div class="row cl">
							<label class="form_label col-3">出单合作店名称：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
									name="prpLFourSShopInfoVo.fourSShopName" value="${prpLFourSShopInfo.fourSShopName}" datatype="*" />
							</div>
							<span class="c-red col-1">*</span> 
							<label class="form_label col-2">归属机构：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="ComCode" name="prpLFourSShopInfoVo.comCode"
											 type="select" clazz="must" value="${prpLFourSShopInfo.comCode}"/>
								</span>
							</div>
							<span class="c-red col-1">*</span>
						</div>
						<div class="row cl">
							<label class="form_label col-3">有效无效标志：</label>
							<div class="form_input col-2">
								<span class="select-box">
									<select class=" select must" style="vertical-align:middle" 
										name="prpLFourSShopInfoVo.validStatus" >
										<option value="1" selected>有效</option>
										<option value="2">无效</option>
									</select>
								</span>
							</div>
						</div>
						<div class="row cl">
						<label class="form_label col-3">业务来源：</label>
						<div class="form_input col-9">
							<app:codeSelect type="checkbox" codeType="BusinessNature" name="prpLFourSShopInfoVo.busiNatureNo" value="${prpLFourSShopInfo.busiNatureNo }" />
						</div>
						</div>
						
					</div>
				</div>
				
				<div class="table_title f14">业务信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-3">合作起始时间：</label>
							<div class="form_input col-2">
								<c:set var="startTime">
										<fmt:formatDate value="${prpLFourSShopInfo.startTime}" pattern="yyyy-MM-dd"/>
								</c:set>
									<input type="text" class="Wdate"  id="dgDateMin" style="width:97%"
											name="prpLFourSShopInfoVo.startTime" value="${startTime}"
											onfocus="WdatePicker({maxDate:'\'%y-%M-%d\'}',dateFmt:'yyyy-MM-dd'})" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
							<label class="form_label col-2">业务维护员联系手机：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.businessLinkManPhone" value="${prpLFourSShopInfo.businessLinkManPhone}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
						</div>
						
						<div class="row cl">
							<label class="form_label col-3">出单合作店等级：</label>
							<div class="form_input col-2">
								<span class="select-box"> 
									<app:codeSelect codeType="FourSLevel" name="prpLFourSShopInfoVo.foursLevel"
											 type="select" clazz="must" value="${prpLFourSShopInfo.foursLevel}"/>
								</span>
							</div>
							<span class="c-red col-1">*</span>
						</div>
						
						<div class="row cl">
							<label class="form_label col-3">所属省/市/区：</label>
							<div class="form_label col-8" style="text-align: left">
								<app:areaSelect targetElmId="fourSAreaCode" areaCode="${prpLFourSShopInfo.areaCode }" showLevel="3" style="width:190px" />
								<input type="hidden" id="fourSAreaCode" name="prpLFourSShopInfoVo.areaCode" value="${prpLFourSShopInfo.areaCode }" datatype="*"/>
								<font class="must">*</font>
							</div>
						</div>
					</div>
				</div>
				
				<div class="table_title f14">返修信息</div>
				<div class="table_cont">
					<div class="formtable">
						<div class="row cl">
							<label class="form_label col-3">修理厂代码：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.factoryCode" id="factoryCode"  value="${prpLFourSShopInfo.factoryCode}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
							<label class="form_label col-2">修理厂名称：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.factoryName" id="factoryName"  value="${prpLFourSShopInfo.factoryName}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
						</div>
						
						<div class="row cl">
							<label class="form_label col-3">出单合作店送修联系手机：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.pushRepairPhone" value="${prpLFourSShopInfo.pushRepairPhone}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
							<label class="form_label col-2">出单合作店送修联系人：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.pushRepairLinkMan" value="${prpLFourSShopInfo.pushRepairLinkMan}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
						</div>
						
						<div class="row cl">
							<label class="form_label col-3">推荐送修支持：</label>
							<div class="form_input col-3">
								<app:codeSelect codeType="YN10" type="radio" name="prpLFourSShopInfoVo.sendRepair" value="${prpLFourSShopInfo.sendRepair}"/>
								<font class="must">*</font>
							</div>
							<label class="form_label col-2">出单合作店修理厂地址：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.pushRepairAddress" value="${prpLFourSShopInfo.pushRepairAddress}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
						</div>
						
						<div class="row cl">
							<label class="form_label col-3">主营品牌名称：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.carBrandName" value="${prpLFourSShopInfo.carBrandName}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
							<label class="form_label col-2">主营品牌代码：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.carBrandCode" value="${prpLFourSShopInfo.carBrandCode}" datatype="*"/>
							</div>
							<span class="c-red col-1">*</span>
						</div>
						
						<div class="row cl">
							<label class="form_label col-3">定损时限要求：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.deflossLimit" value="${prpLFourSShopInfo.deflossLimit}" />
							</div>
							<label class="form_label col-3">特殊承诺：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.specialCommitMent" value="${prpLFourSShopInfo.specialCommitMent}" />
							</div>
						</div>
						
						<div class="row cl">
							<label class="form_label col-3">工时折扣：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.workingHours" value="${prpLFourSShopInfo.workingHours}" />
							</div>
							<label class="form_label col-3">备注：</label>
							<div class="form_input col-2">
								<input type="text" class="input-text"
										name="prpLFourSShopInfoVo.remark" value="${prpLFourSShopInfo.remark}" />
							</div>
						</div>
					</div>
				</div>
				<div class="table_title f14">可修品牌</div>
				<div class="table_cont">
					<div class="formtable">
						
							<span id="checkboxSpan">
								<app:codeSelect type="checkbox" codeType="CarBrand" name="prpLFourSShopInfoVo.carBrands" value="${prpLFourSShopInfo.carBrands }" />
							</span>
						
					</div>
					<div class="formtable">
						<div class="row cl">
							<input type="button" class="btn btn-primary" onclick="addBrand()" value="增加其他品牌" />
						</div>
					</div>
				</div>
			</div>
			<div class="btn-footer clearfix text-c">
				<a class="btn btn-primary" id="submit">保存</a>
				<input type="button" class="btn btn-primary" onclick="closeLayer()" value="关闭" />
			</div>
		</div>
	</form>
	<div id="addBrandDiv" style="display:none">
		<div class="table_cont">
			<div class="row cl">
				<label class="form_label col-3">品牌名称：</label>
				<div class="form_input col-9">
					<input type="text" class="input-text" id="addBreadName" />
				</div>
			</div>
			<div class="row cl text-c">
				<input type="button" class="btn btn-primary mt-5" onclick="addCheckBox()" value="确认" />
			</div>
		</div>
		
	</div>

	

	<script type="text/javascript">	
					
		$("#submit").click(function(){	//提交表单
			var comName = $("select[name='prpLFourSShopInfoVo.comCode']  option:selected").text();
			$("input[name='prpLFourSShopInfoVo.comName']").val(comName);//给comName隐藏域赋值
			$("#FourSShopEditForm").submit();
					
			});
		
		$(function (){
			
			var ajaxEdit = new AjaxEdit($('#FourSShopEditForm'));
			ajaxEdit.targetUrl = "/claimcar/fourSShop/saveFourSShopInfo.do"; 
			ajaxEdit.afterSuccess=function(result){
				if(result.status=="200"){
					$("#submit").prop("disabled",true);	//保存成功防止重复提交
				}	
			}; 
			//绑定表单
			ajaxEdit.bindForm();
			readonlyYN();
						
		});
		
		$("input[name='prpLFourSShopInfoVo.busiNatureNo']").click(function(){
			//选中业务来源之后给业务来源名称隐藏域赋值
			var bstr="";
			$("input[name='prpLFourSShopInfoVo.busiNatureNo']").each(function(){
				if($(this).prop('checked')){
					bstr+=$(this).parent().text()+" ";
				}
			});
			$("input[name='prpLFourSShopInfoVo.busiNatureName']").val(bstr);
			
		});
		
		function readonlyYN(){//如果状态为只展示则所有控件disable
			var flagS = $("#flag").val();
			if(flagS=="s"){
				$("input").attr("disabled","disabled");
				$("select").attr("disabled","disabled");
				$(".btn").attr("style","display:none");
			}
		}
		
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
		
		//修理厂查询，调用losscar模块的CheckFactoryList
		$("input[name='prpLFourSShopInfoVo.factoryCode']").click(function (){
			layer.open({
			    type: 2,
			    title:"选择修理厂",
			    closeBtn: 1,
			    skin: 'yourclass',
			    area: ['90%', '70%'],
			    content:"/claimcar/defloss/findViewList.do",
			});
		});
		var aIndex;
		function addBrand(){
			aIndex=layer.open({
			    type: 1,
			    title:"增加其他品牌",
			    closeBtn: 1,
			    area: ['30%', '30%'],
			    content:$("#addBrandDiv"),
			    end:function(){
			    	
			    }
			});
		}
		
		function addCheckBox(){
			var bName = $("#addBreadName").val();
			$("#checkboxSpan").append("<label class='check-box f1 ml-27'>"
					+ "<input type='checkbox' checked name='prpLFourSShopInfoVo.carBrands' value='"+bName+"' class='' originvalue=''>"
					+ ""+bName+"</label>");
			layer.close(aIndex);
			}
		
	</script>

</body>
</html>

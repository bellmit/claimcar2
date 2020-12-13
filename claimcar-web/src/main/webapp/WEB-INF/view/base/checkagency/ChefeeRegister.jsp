<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<title>发票(查勘费)登记页面</title>
</head>
<body>
<div class="page_wrap">
<!--查询条件 开始-->
<div class="table_wrap">
<div class="table_cont pd-10">
<div class="formtable f_gray4">
  <!-- 请求是否来自发票登录页面 rgisterFlag等于1是，其它为否-->
  <table border="1" class="table table-border table-hover data-table">
  <tr>
  <th colspan="10" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;发票信息</font></th>
  </tr>
  <tr style="height: 30px;" class="text-c">
    <th>发票号码</th>
	<th>发票代码</th>
	<th>开票日期</th>
	<th>销方名称</th>
	<th>销方纳税人识别号</th>
	<th>发票不含税金额</th>
	<th>发票税额</th>
	<th>发票税率</th>
	<th>发票价税合计</th>
	<th>已登记金额</th>
  </tr>
  <tr class="text-c" style="height: 30px;">
  <td>${vatQueryViewVo.billNo}</td>
  <td>${vatQueryViewVo.billCode}</td>
  <td><fmt:formatDate value='${vatQueryViewVo.billDate}' pattern='yyyy-MM-dd HH:mm'/></td>
  <td>${vatQueryViewVo.saleName}</td>
  <td>${vatQueryViewVo.saleNo}</td>
  <td>${vatQueryViewVo.billNnum}</td>
  <td>${vatQueryViewVo.billSnum}</td>
  <td>${vatQueryViewVo.billSlName}</td>
  <td id="billNum">${vatQueryViewVo.billNum}</td>
  <td id="registerNum">${vatQueryViewVo.registerNum}</td>
  </tr>
      
  </table>
  </div>
  </div>
       <!--标签页 开始-->
	   <div class="tabbox">
			<div id="tab-system" class="HuiTab">
				<div class="tabCon clearfix">
				   <c:if test="${vatQueryViewVo.registerStatus ne '1' }">
				    <form action="#" id="billregister">
					<table class="table table-border table-hover data-table" id="DataTable_0">
						<thead>
						<tr>
						    <th colspan="11" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;已选费用</font>
						    &nbsp;&nbsp;&nbsp;&nbsp;
						    <a class="btn btn-primary" onclick="reject()">剔除</a>
						    &nbsp;&nbsp;&nbsp;&nbsp;
						    <a class="btn btn-primary" onclick="dataSubmit()">提交</a>
						    </th>
						    <input type="hidden" name="billId" id="billId" value="${vatQueryViewVo.billId}"/>
                            <input type="hidden" id="interCodeId" name="interCodeId" value="${interCodeId}"/>
                            <input type="hidden" id="taskNo" name="taskNo" value="${taskNo}"/>
                            
						</tr>    
						<tr class="text-c">
						      <th style="width: 10px;">选择</th>
							  <th>报案号</th>
							  <th>保单号</th>
							  <th>立案号</th>
							  <th>计算书号</th>
							  <th>险别</th>
							  <th>被保险人</th>
							  <th>已赔付金额</th>
							  <th>任务详情</th>
							  <th>费用金额</th>
							  <th>摘要</th>
						</tr>
						</thead>
						<tbody class="text-c" id="atbody">
						
				        </tbody>
			</table>
			</form>
			</br>
					 <!--标签页 结束-->
	                <!--table   结束-->
					<table class="table table-border table-hover data-table" id="DataTable_1">
						<thead>
						<tr>
						    <th colspan="11" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;未选费用</font>
						     &nbsp;&nbsp;&nbsp;&nbsp;
						     <a class="btn btn-primary" onclick="choose()">选中</a>
						    </th>
						</tr>      
						<tr class="text-c">
						      <th style="width: 10px;">选择</th>
							  <th>报案号</th>
							  <th>保单号</th>
							  <th>立案号</th>
							  <th>计算书号</th>
							  <th>险别</th>
							  <th>被保险人</th>
							  <th>已赔付金额</th>
							  <th>任务详情</th>
							  <th>费用金额</th>
							  <th>摘要</th>
						</tr>
						</thead>
					<tbody class="text-c" id="btboby">
					  <c:forEach var="checkFee" items="${cheFeeVos}" varStatus="status">
					    <tr  id="che_${status.index}">
					    <td style="width: 10px;"><input type="checkbox" name="bmoreChooseF1" id="check_${status.index}"/></td>
						<input type="hidden" name="checkFeeVo[${status.index}].id" value="${checkFee.id}"/>
						<td>${checkFee.registNo }</td>
						<td>${checkFee.policyNo }
						<c:if test="${!empty checkFee.policyNoLink}"> </br>${checkFee.policyNoLink}</c:if>
						</td>
						<td>${checkFee.claimNo }</td>
						<td>${checkFee.compensateNo }</td>
						<td><app:codetrans codeType="KindCode" codeCode="${checkFee.kindCode }"/></td>
						<td>${checkFee.insurename }</td>
						<c:choose>
						<c:when test="${checkFee.taskStatus eq '2' }">
						<td>已退票</td>
						</c:when>
						<c:otherwise>
						<td>${checkFee.payAmount }</td>
						</c:otherwise>
						</c:choose>
						<td>${checkFee.taskDetail }</td>
						<td id="amount_${status.index}">
							${checkFee.amount}
						</td>
						<td>
						  ${checkFee.remark}
						</td>
					    </tr>
				      </c:forEach> 
					</tbody>
				  </table> 
				  </c:if>
				  <c:if test="${vatQueryViewVo.registerStatus eq '1' }">
				    <!--标签页 结束-->
	                <!--table   结束-->
	                <form action="#" id="billregister">
					<table class="table table-border table-hover data-table" id="DataTable_0">
						<thead>
						<tr>
						    <th colspan="10" style="height: 30px;"><font size="3" color="blue">&nbsp;&nbsp;绑定的计算书</font>
						    </th>
						</tr>      
						<tr class="text-c">
							  <th>报案号</th>
							  <th>保单号</th>
							  <th>立案号</th>
							  <th>计算书号</th>
							  <th>险别</th>
							  <th>被保险人</th>
							  <th>已赔付金额</th>
							  <th>任务详情</th>
							  <th>费用金额</th>
							  <th>摘要</th>
						</tr>
						</thead>
					<tbody class="text-c">
					  <c:forEach var="checkFee" items="${chefeeList}" varStatus="status">
					    <tr>
						<input type="hidden" name="checkFeeVo[${status.index}].id" value="${checkFee.id}"/>
						<td>${checkFee.registNo }</td>
						<td>${checkFee.policyNo }
						<c:if test="${!empty checkFee.policyNoLink}"> </br>${checkFee.policyNoLink}</c:if>
						</td>
						<td>${checkFee.claimNo }</td>
						<td>${checkFee.compensateNo }</td>
						<td><app:codetrans codeType="KindCode" codeCode="${checkFee.kindCode }"/></td>
						<td>${checkFee.insurename }</td>
						<c:choose>
						<c:when test="${checkFee.taskStatus eq '2' }">
						<td>已退票</td>
						</c:when>
						<c:otherwise>
						<td>${checkFee.payAmount }</td>
						</c:otherwise>
						</c:choose>
						<td>${checkFee.taskDetail }</td>
						<td>
							${checkFee.amount}
						</td>
						<td>
						  ${checkFee.remark}
						</td>
					    </tr>
				      </c:forEach> 
					</tbody>
				  </table>
				  </form>
				  </c:if>
				</div>
			</div>
			
		</div>
	               
	             
   </div>
   </div>
  <script type="text/javascript" src="/claimcar/js/common/AjaxEdit.js"></script>
  <script type="text/javascript">
  $(function() {
	  $.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		var ajaxRegisterEdit = new AjaxEdit($('#billregister'));
		ajaxRegisterEdit.targetUrl = "/claimcar/bill/savecheBillEdit.do";
		ajaxRegisterEdit.afterSuccess=function(result){
			if(result.stats='200'){
				layer.msg("保存成功");
				window.location.reload();
			}else{
				layer.msg("操作失败："+result.statusText);
			}
			
			
		}; 
		//绑定表单
		ajaxRegisterEdit.bindForm();
		
		
  });
  
   
  

      
       //选中
       function choose(){
    	   var params="";
    	   $("#DataTable_1  input[name='bmoreChooseF1']").each(function(){
    		   var propvalue=$(this).prop("checked");
    		   if(propvalue){
    			   params=params+$(this).prop("id");
    		   }
    	   });
    	   if(isBlank(params)){
    		   layer.alert("请选择数据记录！");
    		   return false;
    	   }
    	  
    	   $("#DataTable_1  input[name='bmoreChooseF1']").each(function(){
   			var propvalue=$(this).prop("checked");
   			var propId=$(this).prop("id");
   			var arry=propId.split("_");
   			if(propvalue){
   				$("#atbody").append($("#che_"+arry[1]));
   				var registerNum=$("#registerNum").text();
   				if(isBlank(registerNum)){
   					registerNum=0;
   				}
   				var amount=$("#amount_"+arry[1]).text();
   				if(isNaN(Number(amount)) || Number(amount)<=0){
   					layer.alert("选中绑定的 计算书费用金额必须大于0！");
   					return false;
   				}
   				$("#registerNum").text(Number(registerNum)+Number(amount));
   				$("#DataTable_1 #che_"+arry[1]).remove();
   			}
   			
   		    });
       }
       //剔除
       function reject(){
    	   var params="";
    	   $("#DataTable_0  input[name='bmoreChooseF1']").each(function(){
    		   var propvalue=$(this).prop("checked");
    		   if(propvalue){
    			   params=params+$(this).prop("id");
    		   }
    	   });
    	   if(isBlank(params)){
    		   layer.alert("请选择数据记录！");
    		   return false;
    	   }
    	   $("#DataTable_0  input[name='bmoreChooseF1']").each(function(){
      			var propvalue=$(this).prop("checked");
      			var propId=$(this).prop("id");
      			var arry=propId.split("_");
      			if(propvalue){
      				$("#btboby").append($("#che_"+arry[1]));
      				var registerNum=$("#registerNum").text();
       				if(isBlank(registerNum)){
       					registerNum=0;
       				}
      				var amount=$("#amount_"+arry[1]).text();
      				$("#registerNum").text(Number(registerNum)-Number(amount));
      				$("#DataTable_0 #che_"+arry[1]).remove();
      			}
      			
      		    });
       }
       //提交
       function dataSubmit(){
    	   var registerNum=$("#registerNum").text();//总登记金额
    	   var billNum=$("#billNum").text();//价税合计金额
    	   if(isBlank(registerNum)){
    		   registerNum=0;
    	   }
    	   if(Number(billNum)==Number(registerNum)){
    		   layer.confirm('确定要提交数据吗?', {
    			      btn : [ '确定', '取消' ]
    		           },function(){
    			         $("#billregister").submit();
    		           },function(){
    			
    		           });
    	   }else{
    		   layer.alert("发票价税合计金额必须等于已登记金额！");
    		   return false;
    	   }
       }
        
  </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>
<div class="table_wrap">
	<div class="table_title f14">发票</div>
	<div class="table_cont  table_list">
		<table class="table table-border">
			<thead class="text-c">
			    <tr>
				   <th colspan="13" style="height: 30px;" class="text-l">
				   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="btn btn-primary" onclick="billRegister()">登记</a>&nbsp;&nbsp;&nbsp;
				   <a class="btn btn-primary" onclick="billdelete()">删除</a>&nbsp;&nbsp;&nbsp;
				   <a class="btn btn-primary" onclick="openBill()">解绑</a>
				   </th>
				</tr>
				<tr>
				   <th>单选</th>
				   <th>发票号码</th>
				   <th>发票代码</th>
				   <th>开票日期</th>
				   <th>销方名称</th>
				   <th>销方纳税人识别号</th>
				   <th>发票不含税金额</th>
				   <th>发票税额</th>
				   <th>发票税率</th>
				   <th>发票价税合计</th>
				   <th>验真状态</th>
				   <th>登记状态</th>
				   <th>打回原因</th>
				</tr>
			</thead>
			<tbody class="text-c">
				<c:forEach var="vatQueryViewVo" items="${vatQueryViewVos}" varStatus="status">
					<tr>
						<td><input type="radio" name="chooseReg" id="${status.index}_${vatQueryViewVo.billNo}_${vatQueryViewVo.billCode}_${assessorMainVo.taskId}" value="${vatQueryViewVo.vidflag}_${vatQueryViewVo.billContId}_${vatQueryViewVo.registerStatus}_${intermMainVo.id}"/></td>
						<c:choose>
						<c:when test="${vatQueryViewVo.registerStatus eq '1' }">
						<td><a class="btn" onclick="assFeebillView('${vatQueryViewVo.billContId}')">${vatQueryViewVo.billNo }</a></td>
						</c:when>
						<c:otherwise>
						<td>${vatQueryViewVo.billNo }</td>
						</c:otherwise>
						</c:choose>
						<td>${vatQueryViewVo.billCode }</td>
						<td><fmt:formatDate value="${vatQueryViewVo.billDate}" pattern="yyyy-MM-dd HH:mm" /></td>
						<td>${vatQueryViewVo.saleName }</td>
						<td>${vatQueryViewVo.saleNo }</td>
						<td>${vatQueryViewVo.billNnum }</td>
						<td>${vatQueryViewVo.billSnum }</td>
						<td>${vatQueryViewVo.billSlName }</td>
						<td>${vatQueryViewVo.billNum }</td>
						<td>${vatQueryViewVo.vidflagName }</td>
						<td>${vatQueryViewVo.registerStatusName }</td>
						<c:choose>
						<c:when test="${vatQueryViewVo.backFlag eq '1' }">
						   <td><font color="red">${vatQueryViewVo.backCauseInfo}</font></td>
						</c:when>
						<c:otherwise>
						 <td></td>
						</c:otherwise>
						</c:choose>
				 </tr>
				</c:forEach> 
			</tbody>
		</table>
	</div>
</div>
<input type="text"  class="input-text" id="underWriteFlag"  name="assessorMainVo.underWriteFlag" value="${assessorMainVo.underWriteFlag}" hidden="hidden">

<script type="text/javascript">

/**
 * 登记
 */
function billRegister(){
	//公估费任务审核状态
	var underWriteFlag = $("#underWriteFlag").val();
	if (underWriteFlag != '3'){
		layer.alert("公估费任务未审核完毕，不能绑定发票！");
		return false;
	}
	//被选中的单选框
	var chooseValue=$("input:radio[name='chooseReg']:checked").val();
	if(chooseValue == '' || chooseValue == undefined || chooseValue == null || chooseValue==""  ){
		layer.alert("请选择要登记的数据记录！");
		return false;
	}
	var arry=chooseValue.split("_");
	if(arry[2]!='0' || arry[0]!='1'){
	  layer.alert("只能选择验真状态为成功且未登记状态的记录进行登记！");
	  return false;
	}
	var chooseId=$("input:radio[name='chooseReg']:checked").attr("id");
	var arry1=chooseId.split("_");
	 var goUrl ="/claimcar/assessors/assFeeRegister.do?taskNo="+arry1[3]+"&billId="+arry[1]+"&interCodeId="+arry[3];
	 openTaskEditWin("发票(公估费)登记",goUrl);
}
/**
 * 跳转到发票与计算书绑定展示页面
 */
function assFeebillView(billId){
	 var goUrl ="/claimcar/assessors/assFeebillView.do?billId="+billId;
	 openTaskEditWin("发票(公估费)信息",goUrl);
}


/**
 * 删除数据记录
 */
function billdelete(){
	//被选中的单选框
	var chooseValue=$("input:radio[name='chooseReg']:checked").val();
	if(chooseValue == '' || chooseValue == undefined || chooseValue == null || chooseValue==""  ){
		layer.alert("请选择要删除的数据记录！");
		return false;
	}
	var arry=chooseValue.split("_");
	if(arry[2]=='1'){
	  layer.alert("不能删除已登记的记录！");
	  return false;
	}
	var chooseId=$("input:radio[name='chooseReg']:checked").attr("id");
	layer.confirm('确定要删除数据记录吗?', {
		btn : [ '确定', '取消' ]
	},function(){
		$.ajax({
			url : '/claimcar/bill/assChebilldelete.ajax?chooseValue='+chooseValue+"&chooseId="+chooseId,
			dataType : "json",// 返回json格式的数据
			type : 'post',
			success : function(json) {// json是后端传过来的值
				var result = eval(json);
				if (result.status == "200") {
					layer.msg("删除记录成功");
					window.location.reload();
				}else{
					layer.msg("删除记录失败:"+result.statusText);
				}
			},
			error : function() {
				layer.msg("获取地址数据异常");
			}
		});

	},function(){
		
	});
}

/**
 * 解绑(公估)
 */
function openBill(){
	//被选中的单选框
	var chooseValue=$("input:radio[name='chooseReg']:checked").val();
	if(chooseValue == '' || chooseValue == undefined || chooseValue == null || chooseValue==""  ){
		layer.alert("请选择要解绑的数据记录！");
		return false;
	}
	var arry=chooseValue.split("_");
	if(arry[2]!='1'){
	  layer.alert("只能解绑已登记的记录！");
	  return false;
	}
	var chooseId=$("input:radio[name='chooseReg']:checked").attr("id");
	var arry1=chooseId.split("_");
	layer.confirm('确定要解绑吗?', {
		btn : [ '确定', '取消' ]
	},function(){
		$.ajax({
			url : '/claimcar/bill/assCheopenBillToVat.ajax?billId='+arry[1]+"&taskNo="+arry1[3],
			dataType : "json",// 返回json格式的数据
			type : 'post',
			success : function(json) {// json是后端传过来的值
				var result = eval(json);
				if (result.status == "200") {
					layer.alert("操作成功");
					window.location.reload();
				}else{
					layer.msg("操作失败:"+result.statusText);
				}
			},
			error : function() {
				layer.msg("获取地址数据异常");
			}
		});

	},function(){
		
	});
}
</script>
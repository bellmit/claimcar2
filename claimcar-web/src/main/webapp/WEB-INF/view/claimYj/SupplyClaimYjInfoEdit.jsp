<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE HTML>
<html>
<head>
<title>阳杰配件下单</title>
</head>
<body>
	<div class="fixedmargin page_wrap">
		<form id="defossform" role="form" method="post" name="fm">
		    <!-- 隐藏域 -->
		    
		    <input type="hidden" name="lossMainId" value="${lossMainId}"/>
		    <input type="hidden" id="sizeFlag" value="${sizeFlag}"/>
		<!-- 隐藏域 -->
			<div class="table_cont">
				<!-- 基本信息    -->
				<div class="table_title f14">阳杰配件下单</div>
				<div class="formtable">
					<div class="table_cont">
						<table class="table table-bordered table-bg">
							<thead class="text-c">
								<tr class="text-c">
									<th>操作</th>
									<th>零部件名称</th>
									<th>定损单价</th>
									<th>定损数量</th>
									<th>定损残值</th>
									<th>是否回收</th>
									<th>阳杰报价</th>
									<th>下单状态</th>
								</tr>
							</thead>
							<tbody class="text-c" id=YJbody>
								<c:forEach var="supplyYjVo" items="${supplyYjVos}" varStatus="status">
								<tr class="text-c">
								 <input type="hidden" name="supplyYjVos[${status.index}].partcode" value="${supplyYjVo.partcode}" disabled="disabled"/>
								 <input type="hidden" name="supplyYjVos[${status.index}].priceType" value="${supplyYjVo.priceType}" disabled="disabled"/>
                                 <input type="hidden" name="supplyYjVos[${status.index}].originalId" value="${supplyYjVo.originalId}" disabled="disabled"/>
                                 <input type="hidden" name="supplyYjVos[${status.index}].quotationAmount" value="${supplyYjVo.quotationAmount}" disabled="disabled"/>
                                 <input type="hidden" name="supplyYjVos[${status.index}].thirdpartenquiryid" value="${supplyYjVo.thirdpartenquiryid}" disabled="disabled"/>
                                 <input type="hidden" name="supplyYjVos[${status.index}].recycleFlag" value="${supplyYjVo.recycleFlag}" disabled="disabled"/>
                                 <input type="hidden" name="supplyYjVos[${status.index}].status" value="${supplyYjVo.status}" disabled="disabled"/>
								 <td><input type="checkbox" name="supplyYjName" value="${status.index}" onclick="chooseSupply(this)"/></td>
								 <td><input type="text" name="supplyYjVos[${status.index}].partName" value="${supplyYjVo.partName}" class="text-c" disabled="disabled" readonly="readonly" /></td>
								 <td><input type="text" name="supplyYjVos[${status.index}].dlossPrice" value="${supplyYjVo.dlossPrice}" class="text-c" disabled="disabled" readonly="readonly"/></td>
								 <td><input type="text" name="supplyYjVos[${status.index}].dlossNums" value="${supplyYjVo.dlossNums}" class="text-c" disabled="disabled" readonly="readonly" /></td>
								 <td><input type="text" name="supplyYjVos[${status.index}].dlossRestFee" value="${supplyYjVo.dlossRestFee}" class="text-c" disabled="disabled" readonly="readonly" /></td>
								  <c:choose>
								 <c:when test="${supplyYjVo.recycleFlag eq '1' }">
								 <td>是</td>
								 </c:when>
								 <c:otherwise>
								 <td>否</td>
								 </c:otherwise>
								 </c:choose>
								 <td><input type="text" name="supplyYjVos[${status.index}].yjPrice" value="${supplyYjVo.yjPrice}" class="text-c" disabled="disabled" readonly="readonly" /></td>
								 <td>
								 <c:choose>
								 <c:when test="${supplyYjVo.status eq '1' }">
								    已下单
								 </c:when>
								 <c:otherwise>
								    未下单
								 </c:otherwise>
								 </c:choose>
								 </td> 
								</tr>
								 </c:forEach>
									
							</tbody>
							</table>
					</div>
				</div>	
					
				
			</div>
		</form>

		<div class="text-c mt-10">
			<input class="btn btn-primary ml-5" id="submit" type="submit" value="下单" /> 
			<a class="btn btn-primary" onclick="closeLayer()" >关闭</a>
		</div>
	</div>

	<script type="text/javascript">
	
	function chooseSupply(element){
		var ctr=$(element).prop("checked");
		if(ctr){
			$(element).parent().parent().find(":text").each(function(){
				$(this).removeAttr("disabled");
	
			});
			
		  }else{
			  $(element).parent().parent().find(":text").each(function(){
					$(this).attr("disabled","disabled");
				});
		  }
		
	}
		
		//提交表单
		$("#submit").click(function(){
			var chooseFlag='0';
			$(":checkbox").each(function(){
				var checkFlag=$(this).prop("checked");
				if(checkFlag){
					chooseFlag='1';
				}
			});
			if(chooseFlag=='0'){
				layer.alert("至少选一个配件才可下单！");
				return false;
			}
			layer.confirm('是否下单勾选的配件?', {
				btn : [ '是', '否' ]
				}, function(index) {
					$("#defossform").submit();
					layer.close(index);
				}, function(index) {
					layer.close(index);
				});
			
			});
		
		
		
		$(function (){	
		    var sizeFlag=$("#sizeFlag").val();
			if(sizeFlag=='0'){
				$("#submit").hide();
			}
			$("input[name$='status']").each(function(){
				var statusValue=$(this).val();
				if(statusValue=='1'){
					$(this).parent().find("input").attr("disabled","disabled");
					$(this).parent().find(":checkbox").attr('checked',false);
					
				}
			});
			
			var ajaxEdit = new AjaxEdit($('#defossform'));	
			ajaxEdit.targetUrl = "/claimcar/yjclaimcar/supply.do";
			ajaxEdit.beforeSubmit=function(){
				layer.load(0, {shade : [0.8, '#393D49']});
			};
			ajaxEdit.afterSuccess=function(result){
				var strIndexFlag='0';
				$("input[name$='status']").each(function(){
					var statusValue=$(this).val();
					if(statusValue=='0'){
						strIndexFlag='1';
						}
					});
				if(strIndexFlag=='0'){
					$("#submit").hide();//保存成功防止重复提交
				}
				   var resultValue= eval(result);
				   if(resultValue.data=='1'){
					   layer.alert("下单成功");
					   location.reload();
				   }else{
					   layer.alert("下单失败:"+resultValue.statusText);
				   }
			}; 
			//绑定表单
			ajaxEdit.bindForm();
						
		});
		
		function closeLayer(){
			var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			parent.layer.close(index);
		}
	</script>
</body>

</html>
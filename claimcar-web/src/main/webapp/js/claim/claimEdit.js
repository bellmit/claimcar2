var endCaseFlag = "";	
		$(function() {
			initClaimModify();
			createRegistRisk();
			claimTextSet();
			$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
			var ajaxEdit = new AjaxEdit($('#editClaimForm'));
			ajaxEdit.targetUrl = "/claimcar/claim/saveClaimModify.do"; 
			ajaxEdit.afterSuccess=function(returnData){
				layer.msg("立案修改成功！");
				//立案修改成功后金额输入框只读并且隐藏提交按钮
				$("input").attr("readonly","readonly");
				$("#submitDiv").addClass("hide");
				//location.reload(true);
			}; 
			//绑定表单
			ajaxEdit.bindForm();

		});
		
		//弹出调整原因输入框
		var asIndex = null;
		function adjustReasonTextShow(textAreaName){
			if(asIndex == null){
				$resDiv = $("[name='"+textAreaName+"']").parent("div");
				$resDiv.removeAttr("class");
				asIndex = layer.open({
					  type: 1,
					  shade: false,
					  title: "调整原因", //不显示标题
					  closeBtn: true,
					  content: $resDiv,
					  btn: ['确定'],
					  yes: function(index, layero){
						  var text = $resDiv.find("textarea").val();
						  if(text!=null&&text!=""){
							  layer.close(asIndex);
							  asIndex = null;
							  changeAllBtnColor();
						  }else{
							  layer.msg("请填写调整原因");
							  changeAllBtnColor();
						  }
						  
					  },cancel: function(){
						  layer.close(asIndex);
						  asIndex = null;
						  changeAllBtnColor();
					  }
					});
			}
			
		}
		//填写过调整原因的btn变色
		function changeAllBtnColor(){
			$("[name$='adjustReason']").each(function(){
				if($(this).val()!=null&&$(this).val()!=""){
					$(this).parent().prev("input").removeClass("btn-default");
					$(this).parent().prev("input").addClass("btn-primary");
				}else{
					$(this).parent().prev("input").removeClass("btn-primary");
					$(this).parent().prev("input").addClass("btn-default");
				}
			});
		}
		
		function clickReasonTextShow(element){
			textAreaName = $(element).next("div").find("textarea").attr("name");
			adjustReasonTextShow(textAreaName);
		}
		//得到进行调整的立案数据的输入框name
		var changeInputNameArray=new Array();
		var arrIndex = 0;
		function getChangeElement(element){
			//获取修改的金额
			var amt = $(element).val();
			//获取修改金额的险别
			var kindCode = "";
			$(element).parents("tr").find("input").each(function(){
				if($(this).attr("id")=="kindCode"){
					kindCode = $(this).val();
				}
			});
			//判断修改金额能否允许修改
			var amtModifyFlag =  amtModifyCompareComp(amt,kindCode);
			if(amtModifyFlag=="YES"){
				//得到当前调整金额的立案数据对应的调整原因输入框的名字
				var textAreaName = $(element).parents("tr").find("textarea").attr("name");
				//alert(inputName);
				changeInputNameArray[arrIndex] = textAreaName;
				arrIndex++;
				if($(element).attr("id")!="payFees"){
					adjustReasonTextShow(textAreaName);
				}
			}else{
				layer.msg("修改金额大于理算核赔金额");
				$(element).val("");
			}
		}
		//查询当前修改金额的立案是否存在已理算的计算书且修改的金额是否小于理算的核赔金额
		function amtModifyCompareComp(amt,kindCode){
			var claimNo = $("[name='prplClaimText.bussNo']").val();
			//alert(claimNo+amt+kindCode);
			var amtModifyFlag = "YES";
			var params = {
					"claimNo":claimNo,"amt":amt,"kindCode":kindCode
				};
			$.ajax({
				url : "/claimcar/claim/amtModifyCompareComp.ajax",
				type : "post",
				data : params,
				async: false,
				success : function(htmlData){
					amtModifyFlag = htmlData.data;
					//alert(amtModifyFlag);
				}
			});
			return amtModifyFlag;
		}
		//检查调整金额的立案数据是否填写了调整原因
		function checkReasonBox(){
			for(inx in changeInputNameArray){
				var reason = $("[name='"+changeInputNameArray[inx]+"']").val();
				//alert(reason);
				if(reason==null||reason==""){
					layer.msg("调整立案数据请填写调整原因");
					return false;
				}
			}
			return true;
		}
		$("#submit").click(function(){
			if(endCaseFlag=="noEnd"){
				var claimText = $("[name='prplClaimText.description']").val();
				claimText = claimText.replace(/(^\s*)|(\s*$)/g, "");
				if(isBlank(claimText)){
					layer.msg("请填写立案修改意见");
				}else{
					if(checkReasonBox()){
						$('#editClaimForm').submit();
					}
				}
			}else{
				layer.msg("已结案不能修改立案");
			}
			
		});
		$(".layui-layer-close").click(function() {//关闭报案风险提示信息
			$(".layui-layer").hide();
		});
		//初始化已结案案件不能修改
		function initClaimModify(){
			endCaseFlag = $("[name='endCaseFlag']").val();
			if(endCaseFlag=="end"){
				$("input").prop("readonly",true);
				$("textarea").prop("readonly",true);
				$("input").removeAttr("onclick");
			}
		}
		
		//立案修改意见截取显示并赋值
		function claimTextSet(){
			$("[name='claimText']").each(function(){
				var text = $(this).text();
				if(text.length>15){
					var shortName = text.substring(0,15)+"...";
					$(this).attr("title",text);
					$(this).text(shortName);
				}
			});
		}
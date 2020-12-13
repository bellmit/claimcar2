/**
 * 理算任务发起js
 */
$(function() {
			initBtn();//初始化产生理算按钮
			var ajaxEdit = new AjaxEdit($('#compLaunForm'));
			ajaxEdit.targetUrl = "/claimcar/compensate/compensateLaunchSubmit.do"; 
			ajaxEdit.afterSuccess=function(result){
				if(result.statustext=="OK"){
					layer.close(uIndex);
					//返回的快速链接窗口添加到界面的div中
					//$("div#compLinkDiv").append(result);
					//快速链接layer打开
					compLinklayer(result);
					//alert(eval(result).status);
					$("#submitAll").attr("disabled","disabled");//同时发起按钮置灰
				}else{
				//}else{
					layer.msg(result.data);
					layer.close(uIndex);
				}	
			}; 
			//绑定表单
			ajaxEdit.bindForm();

		});
		var bcFlag = "";
		var checkFlag = "N";
		var policyBINo = "";
		var policyCINo = "";
		var registNo = "";
		$("#submitOne").click(function(){//单个发起理算
			$("[name='faqi']").each(function(){
				if($(this).prop("checked")==true){
					checkFlag = "Y";
					bcFlag = $(this).attr("id");
					$("input[name='bcFlag']").val(bcFlag);
					//alert(bcFlag);
					if(bcFlag=="BI"){
						policyBINo = $(this).val();
						$("input[name='prpLCompensate[1].policyNo']").val(policyBINo);
						registNo = $("input[name='prpLCompensate[1].registNo']").val();
						//alert(policyBINo);
					}
					if(bcFlag=="CI"){
						policyCINo = $(this).val();
						$("input[name='prpLCompensate[0].policyNo']").val(policyCINo);
						registNo = $("input[name='prpLCompensate[0].registNo']").val();
						//alert(policyCINo);
					}
					$("div#layerDiv").append($("div#oneDiv"));
					$("div#oneDiv").removeClass('hide');
					userLayer();
					//compLaunCheck(registNo,bcFlag);
				}
			});
			if(checkFlag=="N"){
				layer.msg("请选择保单号");
			}
		});
		$("#submitAll").click(function(){
			bcFlag = "BC";
			$("input[name='bcFlag']").val(bcFlag);
			$("div#layerDiv").append($("div#allDiv"));
			$("div#allDiv").removeClass('hide');
			registNo = $("input[name='prpLCompensate[0].registNo']").val();
			//compLaunCheck(registNo,bcFlag);
			userLayer();
		});
		$("#submitAuto").click(function(){
			//自动产生商业和交强理算计算书，并自动提交到核赔环节
		/*	alert("321321");
			var ajaxEdit = new AjaxEdit($('#compLaunForm'));
			ajaxEdit.setTargetUrl("/claimcar/compensate/compensateLaunchAutoSubmit.do");
			ajaxEdit.afterSuccess=function(result){
				
			};
			$("#compLaunForm").submit();*/
			var params = $("#compLaunForm").serialize();
			$.ajax({
				url : "/claimcar/compensate/compensateLaunchAutoSubmit.do", // 后台处理程序
				type : 'post', // 数据发送方式
				dataType : 'json', // 接受数据格式
				data : params, // 要传递的数据
				async:false, 
				success : function(jsonData) {// 回调方法，可单独定义
					//alert("sss"+jsonData.data);
//					var result = eval(jsonData);
//					resMsg = result.data;
					}
				});
		});
		var uIndex;
		function userLayer(){
			uIndex = layer.open({
				type : 1,
				skin : 'layui-layer-rim', // 加上边框
				area : [ '400px', '200px' ], // 宽高
				content : $("#layerDiv"),
				btn : [ '提交任务', '取消任务处理' ],
				yes : function(index) {
					$("#compLaunForm").submit();
				},
				cancel : function(index) {
					//parent.window.location.reload();
					layer.close(uIndex);
				}
			});
		}
		
		function compLaunCheck(registNo,bcFlag){
			$.ajax({
				url:"/claimcar/compensate/compLaunCheck.ajax",
				type:"post",
				data:{"registNo":registNo,"bcFlag":bcFlag},
				dataType:"json",
				success:function(result){
					if(result.data=="OK"){
						//校验通过可提交理算任务
						userLayer();
					}else{
						if(result.data=="padPayFlag"){
							//设置是否诉讼字段为是
							$("input[name='prpLCompensate[0].lawsuitFlag']").val("1");
							userLayer();
						}else{
							layer.msg(result.data);
						}
					}
					
				}
			});
		}
		
		function initBtn(){
			var endCaseCI = $("input[name='endCaseCI']").val();
			var endCaseBI = $("input[name='endCaseBI']").val();
			var autoFlag = $("input[name='autoFlag']").val();
			//保单号只有一个的时候同时发起置灰
			var policyNum = 0;
			$("input[type='radio']").each(function(){
				policyNum++;
			});
			if(policyNum<2){
				$("#submitAll").attr("disabled","disabled");//同时发起按钮置灰
			}
			if(endCaseCI=="Y"){
				$("#CI").attr("disabled","disabled");//保单号对应radio置灰
				$("#submitAll").attr("disabled","disabled");//同时发起按钮置灰
			}
			if(endCaseBI=="Y"){
				$("#BI").attr("disabled","disabled");
				$("#submitAll").attr("disabled","disabled");//同时发起按钮置灰
			}
			if(autoFlag=="N"){
				$("#submitAuto").attr("disabled","disabled");//自动发起理算按钮置灰
			}
		}
		var clIndex;
		function compLinklayer(result){
			var yewuNo = result.data;
			var compMap = result.datas;
			var Bi = "";
			var Ci = "";
			for(var key in compMap){
				//alert(compMap[key]);
				if(key=="BI"){
					Bi = compMap[key];
				}
				if(key=="CI"){
					Ci = compMap[key];
				}
			}
			clIndex = layer.open({
				/*type:1,
				title:'提示信息',
				skin : 'layui-layer-rim', // 加上边框
				area : [ '500px', '500px' ], // 宽高
*/				//content : $("#compLinkDiv"),
				type: 2,
				title: "提示信息",
				shade: false,
				skin: 'yourclass',
				area: ['710px', '295px'],
				content : ("/claimcar/compensate/showCompLink.do?yewuNo="+yewuNo+"&Bi="+Bi+"&Ci="+Ci),
				
			});
		}
		function closeCompLayer(){
			layer.close(clIndex);
		}
		function compLink(compNo){//点击处理进入计算任务处理界面
	    	window.location.href="/claimcar/compensate/compensateEdit.do?compNo="+compNo; 
		}
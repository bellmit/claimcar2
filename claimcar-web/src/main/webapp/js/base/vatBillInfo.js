 $(function() {
		$.Huitab("#tab-system .tabBar span", "#tab-system .tabCon", "current", "click", "0");
		bindValidForm($('#billComInfo'), search);
		var ajaxRegisterEdit = new AjaxEdit($('#billregister'));
		ajaxRegisterEdit.targetUrl = "/claimcar/bill/saveoneBillEdit.do";
		ajaxRegisterEdit.beforeSubmit = saveBeforeCheck;
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
		search();
  });
  
  
  
  
  var columns = [
     			{
     				"data" : "indexNo"
     			},{//序号
     				
     				"data" : "registNo"
     			}, //报案号
     			{
     				"data" : "compensateNo"
     			}, //计算书号
     			{
     				"data" : "bussName"
     			}, //业务类型
     			{
     				"data" : "feeName"
     			}, //费用类型
     			{
     				"data" : "comName"
     			}, //机构名称
     			{
     				"data" : "underwriteDate"
     			} ,//核损通过时间
     			{
     				"data" : "payName" 
     			},//收款人
     			{
     				"data" :"accountNo"
     			},//收款人账号
     			{
     				"data" : "sumAmt"
     			} ,//赔付金额
     			{
     				"data" :"registerNum"
     			},//已登记金额
     			{
     				"data" : "registerNum1"
     			}//本张发票的登记金额
     			];

		   			function rowCallback(row, data, displayIndex, displayIndexFull) {
		   				var rgisterFlag=$("#rgisterFlag").val();
		   				var registerStatus=$("#registerStatus").val();//已登记页面不用输入框
		   				if(rgisterFlag=='1' && registerStatus!='1'){
		   				   $('td:eq(2)',row).html("<a   onclick=linkedBillView('"+data.registNo+"','"+data.compensateNo+"','"+data.bussType+"','"+data.feeCode+"','"+data.payId+"','"+data.sumAmt+"','"+data.registerNum+"','"+data.bussId+"')>"+data.compensateNo+"</a>");
		   				   $('td:eq(11)',row).html("<input type='text' name='vatQueryViewVo["+(Number(data.indexNo)-Number(1))+"].registerNum'  onchange=vaildnum('"+data.registerNum+"','"+data.sumAmt+"',this) datatype='amount' ignore='ignore'  value=''/><input type='hidden' name='vatQueryViewVo["+(Number(data.indexNo)-Number(1))+"].billContId' value='"+data.billContId+"'/>");
		   				   
		   				}
		   				
		   			}
       
		   			function search() {
		   				var ajaxList = new AjaxList("#resultDataTable"); 
		   				ajaxList.targetUrl = '/claimcar/bill/vatBillfoSerach.do';
		   				ajaxList.postData = $("#billComInfo").serializeJson();
		   				ajaxList.columns = columns;
		   				ajaxList.rowCallback = rowCallback;
		   				ajaxList.query();
		   			}
		               
		            function vaildnum(registerNum,sumAmt,obj){
		            	var nowRegisterNum=$(obj).val();//本次登记金额
		            	if(!isBlank(nowRegisterNum) && isNaN(Number(nowRegisterNum))){
		            		layer.alert("录入登记金额类型只能为数值！");
		            		$(obj).val(null);
		            		return false;
		            	}
		            	if(!isBlank(nowRegisterNum) && Number(nowRegisterNum)<=0){
		            		layer.alert("录入登记金额数值必须大于0！");
		            		$(obj).val(null);
		            		return false;
		            	}
		            	if(!isBlank(nowRegisterNum)){
		            		if((Number(registerNum)+Number(nowRegisterNum))>Number(sumAmt)){
		            			layer.alert("总登记金额不能大于赔付金额");
		            			$(obj).val(null);
		            			return false;
		            		}
		            		
		            	}
		            }
		            
		            function savevat(){
		            	layer.confirm('确定要提交数据吗?', {
							btn : [ '确定', '取消' ]
						},function(){
							$("#billregister").submit();
						},function(){
							
						});
					
		            }
		            
		            function saveBeforeCheck(){
		            	var sumAmout=$("#billNum").val();
		            	var sumFee=0;
		            	$("input[name$='registerNum']").each(function(){
		            		var feeAmount=$(this).val();
		            		if(!isBlank(feeAmount)){
		            			sumFee=sumFee+Number(feeAmount);
		            		}
		            		
		            	});
		            	if(Number(sumAmout)!=Number(sumFee)){
		            		layer.alert("发票必须全额登记！");
		            		return false;
		            	}
		            }
		            
		            //展示对应的关联发票信息
					function linkedBillView(registNo,compensateNo,bussType,feeCode,payId,sumAmt,registerNum,bussId){
						 var goUrl ="/claimcar/bill/compenInfoList.do?registNo="+registNo+"&compensateNo="+compensateNo+"&bussType="+bussType+"&feeCode="+feeCode+"&payId="+payId+"&sumAmt="+sumAmt+"&registerNum="+registerNum+"&bussId="+bussId;
						 openTaskEditWin("计算书(发票)关联信息",goUrl);
					}
	
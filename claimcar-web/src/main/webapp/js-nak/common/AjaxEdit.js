/**
 * Ajax模式Edit页面支持JS
 */

var submitInProgress = false;
function AjaxEdit(selector) {
	this.selector = selector;
	this.rules = {};
	this.targetUrl = null;
	this.beforeCheck = null;
	this.beforeSubmit = null;
	this.afterSuccess = null;
	this.afterFailure = null;
}
AjaxEdit.prototype.setTargetUrl = function(url) {
	this.targetUrl=url;
};
AjaxEdit.prototype.getTargetUrl = function() {
	return this.targetUrl;
};
AjaxEdit.prototype.bindForm = function() {
	var innerForm = $(this.selector);
	var innerFormValidRules = this.rules;
	var innerFormBeforeCheck = this.beforeCheck;
	var innerFormBeforeSubmit = this.beforeSubmit;
	var innerFormAfterSuccess = this.afterSuccess;
	var innerFormAfterFailure = this.afterFailure;
	//下面两个临时变量的值，用于callback中使用。
	var innerXMLHttpRequest="";
	var innerTextStatus="";
	var ajaxEdit=this;
	
	
	
	var validDataOpt={
		label:".form_label",
		showAllError:true,//所以校验一起执行
		ajaxPost:false,
		ignoreHidden:true,
		tiptype:function(msg,o,cssctl){
			//msg：提示信息;
			//o:{obj:*,type:*,curform:*}, obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
			//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
			if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
				if(o.type==3){
					o.obj. attr("title", msg);
					$(o.obj).qtip({ // Grab some elements to apply the tooltip to
						hide : false,
						overwrite : false,
					    content: {
					        text: msg
					    },
					    position: {
					        my: 'top left',  // Position my top left...
					        at: 'bottom left', // at the bottom right of...
					        target:$(o.obj) // my target
					    },
					    show : {
							event : false,
							ready : true,
							tips : false,
						}
					});
				}else{
					$(o.obj).qtip('destroy');
				}
			}else{//全部验证通过
				layer.msg('ALL OK');
			}
		},
		beforeCheck:function(curform){
			//在表单提交执行验证之前执行的函数，curform参数是当前表单对象。
			//这里明确return false的话将不会继续执行验证操作;
			if(innerFormBeforeCheck!=null){ 
				innerFormBeforeCheck();
			}
		},
		beforeSubmit:function(curform){
			//在验证成功后，表单提交前执行的函数，curform参数是当前表单对象。
			//这里明确return false的话表单将不会提交;
			if(innerFormBeforeSubmit!=null){ 
				return innerFormBeforeSubmit();
			}
		},
		callback:function(curform){//
			if (submitInProgress) {
				return false;
			}

			submitInProgress = true;
			
			$.ajax({
				// 提交数据的类型 POST GET
				type : "POST",
				// 提交的网址
				url : ajaxEdit.getTargetUrl(),
				// 提交的数据
				data : innerForm.serialize(),
				// 返回数据的格式
				datatype : "json",
				// "xml", "html", "script", "json", "jsonp", "text".
				// 在请求之前调用的函数
				beforeSend : function() {
					layer.load();
				},
				// 成功返回之后调用的函数
				success : function(data) {
					var result = eval(data);
					var messageText = "";
					var success = true;
					layer.closeAll('loading');
					if (result.status == "200") {
						messageText = '操作成功';
						layer.msg(messageText);
						if(innerFormAfterSuccess!=null){ 
							innerFormAfterSuccess(result);
						}
					} else {
						success= false;
						messageText = '操作失败：' + result.statusText;
						layer.alert(messageText);
						if(innerFormAfterFailure!=null){ 
						    innerFormAfterFailure(innerXMLHttpRequest, innerTextStatus);
						}
					}
					
					innerXMLHttpRequest = null;
					innerTextStatus = null;
					
				},
				// 调用执行后调用的函数
				complete : function(XMLHttpRequest, textStatus) {
					//alert(XMLHttpRequest.responseText);
					//alert(textStatus);
					submitInProgress = false;
					//调用成功后设置这两个临时变量的值，用于callback中使用。
					innerXMLHttpRequest=XMLHttpRequest;
					innerTextStatus = textStatus;
				},
				// 调用出错执行的函数
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					layer.alert(XMLHttpRequest.responseText);
					//layer.msg("Ajax提交错误");
					layer.closeAll('loading');
				},
				clearForm : true,
			});
			return false;
		}
	};
	var fromValid=$(this.selector).Validform(validDataOpt);
	if(innerFormValidRules!=null)	fromValid.addRule(innerFormValidRules);
	
};

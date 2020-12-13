/**应用专用js，仅适合当前应用**/

/**
 * 页面跳转使用的等待方法，如果非跳转页面，弹出框将不回关闭
 */
function waiting(msg){
	var showMsg = "操作进行中，请稍候...";
	if(msg != null && msg != '') {
		showMsg = msg;
	}
	var msgOption = {
			backdrop: true,
			message : showMsg,
			closeButton : false,
			animate : false,
			buttons : {}
		};
	return bootbox.dialog(msgOption);
}

/**将一个对象禁用几秒**/
function disabledSec(obj,sec){
	$(obj).attr({"disabled":"true"});
	setTimeout(function(){ 
		$(obj).removeAttr("disabled");
	},sec*1000); 
}

/**
 * 计算使用年数
 * @param startDateStr 起保日期
 * @param startHour 起保小时
 * @param endHour 终保小时
 * @param enrollDateStr 初等日期
 */
function getUseYear(startDateStr,startHour,endHour,enrollDateStr){
	var startDate = DateUtils.strToDate(startDateStr);
	startDate.setHours(parseInt(startHour,10), 0, 0, 0);//设置小时
	var enrollDate = DateUtils.strToDate(enrollDateStr);
	enrollDate.setHours(parseInt(0), 0, 0, 0);//设置小时
	var startMonth = startDate.getMonth();
	var enrollMonth = enrollDate.getMonth();
	var months = (startDate.getFullYear() - enrollDate.getFullYear())*12 + (startMonth - enrollMonth);//相隔月份
	if(enrollDate.getDate() - startDate.getDate() >0){
		months -=1;
	}
	var useYears = Math.floor(months/12);//使用年数(取整)
	enrollDate = enrollDate.addYears(useYears);
	//如果相差大于等于337天,按一年算
	var days = DateUtils.calcDiffDate(startDate, enrollDate);
	if(days >= 337){
		useYears += 1;
	}
	return useYears;
}

/**
 * 费改使用年数
 * @param startDateStr
 * @param startHour
 * @param endHour
 * @param enrollDateStr
 * @returns
 */
function getUseYearPremium(startDateStr,startHour,endHour,enrollDateStr){
	var startDate = DateUtils.strToDate(startDateStr);
	startDate.setHours(parseInt(startHour,10), 0, 0, 0);//设置小时
	var enrollDate = DateUtils.strToDate(enrollDateStr);
	enrollDate.setHours(parseInt(0), 0, 0, 0);//设置小时
	var startMonth = startDate.getMonth();
	var enrollMonth = enrollDate.getMonth();
	var months = (startDate.getFullYear() - enrollDate.getFullYear())*12 + (startMonth - enrollMonth);//相隔月份
	if((startMonth == enrollMonth)&&(enrollDate.getDate() - startDate.getDate() >0)){
		months -=1;
	}
	var useYears = Math.floor(months/12);//使用年数(取整)
	if(useYears < 0){
		useYears = 0;
	}
	return useYears;
}

/**去掉红框
 * @param obj
 */
function rmError(obj){
	var c = obj.className;
	if(obj.value != ""){
		if(c != null && c.indexOf('error') > -1){
			obj.className = c.replace('error', '');
		}
	}
}

//在控件边上显示Tip提示
function showQtip(elem,msg){
	$(elem).attr("title", msg);
	var corners = [ 'bottom left', 'top left' ];// 'left center', 'right
	// center'
	var flipIt = $(elem).parents('span.right').length > 0;
	var elemOpt = {
			overwrite : false,
			content : msg,
			position : {
				my : corners[flipIt ? 0 : 1],
				at : corners[flipIt ? 1 : 0],
				viewport : $(window)
			},
			show : {
				event : false,
				ready : true,
				tips : false
			},
			hide : false,
			style : {
				classes : 'tips qtip qtip-default qtip-shadow qtip-rounded'
			}
	};
	$(elem).qtip(elemOpt);
}

//关闭Tip提示
function closeQtip(elem){
	$(elem).attr("title", "");
	$(elem).qtip('destroy');
}

/**
 * 删除TR
 * @param size 大小
 * @param index 当前TR序号
 * @param btnPrefix 删除按钮名称前缀
 * @param inputPrefix 调整:input名称前缀
 */
function delTr(size,index,btnPrefix,inputPrefix){
	for(var i =size-1 ;i>index;i--){
		var j = i - 1;
		var $del = $("[name='"+btnPrefix+i+"]']:eq(0)");
		if(typeof($del)== "undefined"  ||$del == null || $del.length==0){
			$del = $("[name='"+btnPrefix+i+"']:eq(0)");
			$del.attr("name",btnPrefix+j);
		}else{
			$del.attr("name",btnPrefix+j+"]");
		}
		changeIndex(inputPrefix, i, j);//重新设置input的下标
	}
	//将flag属性重置
	$(":input[name^='"+inputPrefix+"']").each(function(){
		$(this).attr("flag","");//删除完之后重置
	});
}

/**
 * 改变input,select,textarea,button的下标
 * @param prefix input前缀
 * @param oldIndex 原下标
 * @param newIndex 新下标
 */
function changeIndex(prefix,oldIndex,newIndex){
	$(":input[name^='"+prefix+"["+oldIndex+"]']").each(function(){
		var flag = $(this).attr("flag");
		if(flag == "1"){
			return;
		}
		var name = $(this).attr("name");
		$(this).attr("name",name.replace(oldIndex,newIndex));
		$(this).attr("flag","1");// 每次改变后就不允许再次改变
	});
}

/**
 * 调整TR的顺序
 * @param table
 * @param tr 需要调整顺序的TR
 * @param index 调整的顺序
 */
function changeTrIndex(table,tr,index){
	if(index > 0){
		var row = $('tr:eq('+index+')',table);
		$(tr).insertAfter($(row).next());
	}
}

/**
 * 初始化保费计算变更信息
 * @param elements 保费计算页面调整元素(隐藏域的值)
 * @param calPremiumFlagName 是否已进行保费计算标识(隐藏域的名称)
 */
function initPremiumElements(elements,calPremiumFlagName){
	if(typeof(elements) != 'undefined' && elements != null && elements != ""){
		var arr = elements.split(",");
		for(var i=0;i<arr.length;i++){
			var tempArr = arr[i].split("-");
			if(typeof(tempArr) == "undefined" || tempArr == null || tempArr == ""){
				continue;
			}
			var tableDiv = $("#" +tempArr[0]);
			var elementId = tempArr[1];
			clearPremiumFlag(tableDiv, elementId,calPremiumFlagName);
		}
		//带calPremiumFlag样式的是带下标的部分，这些地方用样式查找
//		$(".calPremiumFlag").each(------) change(function(){
//			$.input("[name='"+calPremiumFlagName+"']").val('0');
//		});
	}
}

/**
 * 给元素添加清空保费计算标识事件
 * @param tableDiv
 * @param elementId
 */
function clearPremiumFlag(tableDiv,elementId,calPremiumFlagName){
	var arr = elementId.split("[");
	var prefix = elementId;
	var suffix = "";
	var $element = null;
	if(arr.length > 1){
		prefix = elementId.split("[").shift();//前缀
		suffix = elementId.split("[").pop().split("]").pop();//后缀
		var className = (prefix+suffix).replace(/\./g,"\\.");
		//样式名称中带有"."需要转义
		$element = $("."+className,tableDiv);
	}else{
		$element = $.input("[name='"+elementId+"']");
	}
	$($element).change(function(){
		//将计算标识置为0
		$.input("[name='"+calPremiumFlagName+"']").val('0');
	});
}

/**
 * 初始化popover,默认为当前元素右边
 * @param element 元素
 * @param 显示内容
 */
function initPopover(element,htmlData){
	initPopover(element, htmlData, "right");
}

/**
 * 初始化popover
 * @param element 元素
 * @param htmlData 显示内容
 * @param placement 显示位置
 */
function initPopover(element,htmlData,placement){
	initPopover(element, htmlData, placement, "popover-medium");
}

/**
 * 初始化popover
 * @param element 元素
 * @param htmlData 显示内容
 * @param placement 显示位置
 * @param css 样式
 */
function initPopover(element,htmlData,placement,css){
	initPopover(element, htmlData, placement, css, true);
}

function initPopover(element,htmlData,placement,css,showTitle){
	//只对按钮标识
	var tagName = $(element).prop("tagName");//此返回的都是大小
	var type = $(element).attr("type");
	if(tagName == "BUTTON" || type == "button" || tagName =="SPAN"){
		var flag = $(element).attr("flag");//标识是否已打开窗口
		if(flag == "1"){//如果已打开，点击则关闭
			$(element).popover('destroy');
			$(element).attr("flag","0");//标志置为0,已关闭
			return;
		}
	}
	var display = "";
	if(showTitle == false){
		display = "display : none";
	}
	if(placement == null || placement == ""){
		placement = "right";
	}
	if(css == null || css == ""){
		css = "popover-medium";
	}
	var options = {
		delay: { show: 500, hide: 100 },
		html : true,
		placement : placement,
		title : "title",
		content : htmlData,
		container: 'body',
		template : '<div name="dragPop" class="popover '+css+'"><div class="arrow"></div><div class="popover-inner">'+
			'<div id="dragPopHead" class="popover-head">'+
			'<button type="button" class="close" data-dismiss="popover" aria-hidden="true">&times;</button>'+
			'<h3 class="popover-title" style="'+display+'"></h3></div><div class="popover-content"><p></p></div></div></div>'
	};
	var p = $(element).popover(options);
	p.popover('show');
	$(element).attr("flag","1");//标识为已打开
	$('html').on('click', '[data-dismiss="popover"]', function (e) {
		$('.btn').popover('destroy');
		$(element).popover('destroy');
	});
	try{
		var anotherElement = document.getElementById("dragPopHead");
		DragDrop.bind(document.getElementsByName("dragPop")[0], {
			// The anchor; myElement moves when anotherElement is dragged
			anchor: anotherElement,
			// The draggable element will now stay bound within it's offsetParent
			boundingBox: 'offsetParent'
		});
	}catch(e){}
}

/**
 * 查看车型信息
 * @param element 品牌型号element
 * @param comCode 机构代码
 * @param policyNo 保单号
 * @param proposalNo 投保单号
 * @param modelCode 车型代码
 */
function showBrandTool(element,comCode,policyNo,proposalNo,modelCode){
	//查看车型信息
	var url = contextPath + "/loadAjaxPage/PrpTEdit/showBrandTool.ajax";
	var params = {"comCode":comCode,"policyNo":policyNo,"proposalNo":proposalNo,"modelCode":modelCode};
	if(comCode != "" && (modelCode != "" || policyNo != "" || proposalNo != "")){
		initPopover(element, "正在加载...","bottom","popover-brand",false);
		$.ajax({
			// 提交数据的类型 POST GET
			type : "POST",
			// 提交的网址
			url : url,
			// 提交的数据
			data : params,
			// 返回数据的格式
			datatype : "html",
			success : function(htmlData){
				$(element).popover("destroy");
				initPopover(element, htmlData,"bottom","popover-brand",false);
			}
		});
	}
}

/**
 * 给name有下标的元素增加去掉下标name的样式
 * @param element
 */
function addClass2SubIndexElement(element){
	//初始化表单给有下标的元素增加无下标的样式
	$(element).find("input,select,textarea,button").each(function(){
		var name = $(this).attr("name");
		if(typeof(name) != "undefined" && name != null && name != ""){
			var arr = name.split("[");
			if(arr.length > 1){
				var prefix = name.split("[").shift();//前缀
				var suffix =name.split("[").pop().split("]").pop();//后缀
				$(this).addClass(prefix+suffix);//添加无下标的样式
			}
		}
	});
}
$(function() {
	// 删除tab
	$(document).on('click', '.tabBar span i.delete', function() {
		var tabBar = $(this).parent().parent();
		var span = $(this).parent();
		var length = tabBar.children('span').length;
		if (length > 1) {
			var i = tabBar.children('span').index(span);
			span.remove();
			var persTracesNum = parseInt($("#persTracesNum").val(), 10);
			$("#persTracesNum").val(persTracesNum - 1);// 伤亡人员数量-1
			tabBar.parent().find('.tabCon').eq(i).remove();
			// 循环改变下面的tabCon输入框和按钮的tabPageNo
			changeTabConIndex("persInfoTab", "prpLDlossPersTraceVos", persTracesNum - 1, i);

			if (span.attr('class') == 'current') {
				if (length - 1 == i) {
					i--;
				}
				$.Huitab("#persInfoTab .tabBar span", "#persInfoTab .tabCon", "current", "click", i);
			}
		} else {
			layer.alert("不能删除，最少录入一条人伤信息！");
		}
	});

	
});

//删除tab页中的tr
function delTabConTr(size,index,btnPrefix,inputPrefix){
	for(var i =size-1 ;i>index;i--){
		var j = i - 1;
		var $del = $("[name='"+btnPrefix+i+"]']:eq(0)");
		if(typeof($del)== "undefined"  ||$del == null || $del.length==0){
			$del = $("[name='"+btnPrefix+i+"']:eq(0)");
			$del.attr("name",btnPrefix+j);
		}else{
			$del.attr("name",btnPrefix+j+"]");
		}
		changeTabConIndex(inputPrefix, i, j);//重新设置input的下标
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
function changeTabConIndex(prefix,oldIndex,newIndex){
	$(":input[name^='"+prefix+"["+oldIndex+"]']").each(function(){
		var flag = $(this).attr("flag");
		if(flag == "1"){
			return;
		}
		var name = $(this).attr("name");
		var index = name.lastIndexOf(oldIndex);
		name=name.substring(0,index)+newIndex+name.substring(index+1,name.length);
		$(this).attr("name",name);
		$(this).attr("flag","1");// 每次改变后就不允许再次改变
	});
}

// 新增tab
function addTab(topTabId) {
	var topTab = $('#' + topTabId);
	var tabBar = topTab.find('.tabBar');
	var persTracesNum = parseInt($("#persTracesNum").val(), 10);
	var registNo = $("input[name='prpLDlossPersTraceMainVo.registNo']:first").val();
	$("#persTracesNum").val(persTracesNum + 1);// 伤亡人员数量+1
	var params = {
		"persTracesNum" : persTracesNum,
		"registNo" : registNo
	};
	var url = "/claimcar/loadAjaxPage/loadCasualtyTabCon.ajax";
	$.post(url, params, function(result) {
		tabBar.append("<span>新增伤亡人员<i class='delete'></i></span>");
		$("#persInfoTab").append(result);
		$.Huitab("#persInfoTab .tabBar span", "#persInfoTab .tabCon", "current", "click", persTracesNum);
	});
}

// 删除tab改变tabPageNo
function changeTabConIndex(tabid, prefix, size, index) {
	for (var i = index; i < size; i++) {// 从删除的index位置开始改变
		var oldIndex = i + 1;
		var tabCon = $("#" + tabid + "").find('.tabCon').eq(i);
		// 改变当前tab的tabPageNo
		tabCon.find("input[name='tabPageNo']").val(i);
		// 改变tab里面所有表格tbody的ID
		tabCon.find("tbody[id^='" + oldIndex + "_']").each(function() {
			var id = $(this).attr("id");
			var newid = i + id.substring(1, id.length);
			$(this).attr("id", newid);
		});
		// 改变所有表单元素的name tabPageNo下标
		tabCon.find(":input[name^='" + prefix + "']").each(function() {
			var name = $(this).attr("name");
			var index = name.indexOf(oldIndex);
			var newname = name.substring(0, index) + i + name.substring(index + 1, name.length);
			$(this).attr("name", newname);
		});
		// 改变所有新增和删除按钮的tabPageNo下标
		tabCon.find(":button[name^='" + oldIndex + "_']").each(function() {
			var name = $(this).attr("name");
			var newname = i + name.substring(1, name.length);
			$(this).attr("name", newname);
		});
	}
}
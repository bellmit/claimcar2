/**
 * 功能代码树形展示参数设置
 */
var setting = {
		check:{
			enable: false
		},
 		view: {
			selectedMulti: true,
			fontCss : {color:"#428bca"}
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};
$(function() {
	//功能代码树状展示页面加载所有功能代码
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	//1.控件事件
	$("button.btn-return").click(function() {
		history.go(-1);
	});
});
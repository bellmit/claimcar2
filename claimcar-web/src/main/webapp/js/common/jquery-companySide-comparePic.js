/*
 * 公司端使用时需要引入此文件
 * 注意：
 *	 1、在使用本文件的同时需要引用jQuery JavaScript要求1.4以上版本
 *   2、本脚本为编码GBK方式，传送json数据为UTF-8编码方式
 * Date: 2016-11-20
 * version：V1.0.0
 */

/*
 *  ajax图像对比接口函数传参数说明
 *
 *  patternMath(comparePicURL, username, password, claimSequenceNo, claimPeriod)	
 * 	- comparePicURL: 系统登录链接地址，由平台统一提供
 *  	- username：接口系统公司用户名
 *  	- password：接口系统公司密码
 *  	- claimSequenceNo：要进行图像比对案件的理赔编码
 *     - claimPeriod：要进行图像比对案件的理赔阶段
 *
 * 公司端调用案例：
 *  $("compareID").click(function(){
 *    var comparePicURL = "http://xxxxxx";
 *    var username="123";
 *    var password="123";
 *    var claimSequenceNo = '100001234';
 *    var claimPeriod = '01';
 *    $("compareID").patternMath(comparePicURL, username, password, claimSequenceNo, claimPeriod);   <--调用方法
 *  });
 */
$(function(){
	$.fn.extend({
		"patternMath":function(comparePicURL, username, password, claimSequenceNo, claimPeriod) {
			function openWindow(url) {
				var dwidth =1000;
				var dheight = 500;
				var dtop = ((window.screen.availHeight - dheight) / 2);
				var dleft = ((window.screen.availWidth - dwidth) / 2);
				open (url, "_blank",'height='+dheight+',width='+dwidth+',top='+dtop+',left='+dleft+',toolbar=no, menubar=no, z-look=yes, fullscreen=no,'+""
					+'depended=yes, scrollbars=yes, titlebar=yes, resizable=yes, location=no, status=no, alwaysRaised=yes, directories=no, hotkeys=yes,');
			}
			
			//str表示原字符串变量，flg表示要插入的字符串，sn表示要插入的位置
			function insert_flg(str, flg, sn) {
				var newstr = "";
				for (var i = 0; i < str.length; i += sn) {
					var tmp = str.substring(i, i + sn);
					newstr += tmp + flg;
				}
				return newstr;
			}
			
			var $this = $(this);
			$.ajax({
				url: comparePicURL,
				type: "POST",
				dataType: "jsonp",
				data: {sysUserCode:username, sysPassWord:password, claimSequenceNo:claimSequenceNo, claimPeriod:claimPeriod},
				jsonp: "callbackComparePic",
				beforeSend : function() {
					// 处理以前需要做的功能  
					$this.attr("disabled", true);
	            },  
				success: function( data ) { 
					if ("0000" == data.code) {
						comparePicURL = comparePicURL+"?ReqType="+data.message+"&t="+new Date().getTime();
						openWindow(comparePicURL);
					} else {
						alert("【调用异常】\n1、返回错误代码：" + data.code + ";\n2、返回错误提示：" + data.message);
					}
				},
				error: function(jqXHR, textStatus, errorThrown) {
					alert("【调用异常】\n1、返回的状态：" + textStatus + ";\n2、服务器抛出返回的错误信息：" + errorThrown);
				},
				complete : function() {
					// 处理完成后需要做的功能  
					$this.attr("disabled", false);
	            }			
		});
		}
	});
});
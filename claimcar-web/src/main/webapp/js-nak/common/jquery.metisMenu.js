;(function ($, window, document, undefined) {

    var pluginName = "metisMenu",
        defaults = {
            toggle: true
        };
        
    function Plugin(element, options) {
        this.element = element;
        this.settings = $.extend({}, defaults, options);
        this._defaults = defaults;
        this._name = pluginName;
        this.init();
    }

    Plugin.prototype = {
        init: function () {

            var $this = $(this.element),
                $toggle = this.settings.toggle;
			$this.find('li.menuUrl').children('a').each(function(){
				if($(this).attr('href') == getCookie('menu_cookie') + ''){
					$(this).parent().addClass('active open');
					findParent($(this).parent());;
				}
			});
			
			if (this.isIE() <= 9) {

                $this.find("li.active").has("ul").children("ul").collapse("show");
                $this.find("li").not(".active").has("ul").children("ul").collapse("hide");
            } else {
                $this.find("li.active").has("ul").children("ul").addClass("collapse in");
                $this.find("li").not(".active").has("ul").children("ul").addClass("collapse");
            }
			
            $this.find('li').has('ul').children('a').on('click', function (e) {
                e.preventDefault();

                $(this).parent('li').toggleClass('active').children('ul').collapse('toggle');

                if ($toggle) {
                    $(this).parent('li').siblings().removeClass('active').children('ul.in').collapse('hide');
                }
            });
			$this.find('li.menuUrl').children('a').on('click', function (e) {
               setCookie('menu_cookie',$(this).attr('href'),0);
            });
        },
    
	    isIE:function () {
	        var Sys = {};
	        var ua = navigator.userAgent.toLowerCase();
	
	        var s;
	        (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1]  : 0;
	
	        if (Sys.ie) return Sys.ie;
	    }
    };
	
	function findParent(p){
		p.parent().parent().addClass('active');
		pp = p.parent().parent();
		if(pp.is('li')){
			findParent(pp);
		}
	}
	
	function setCookie(objName, objValue, objHours,path) {// 添加cookie
		var str = objName + "=" + escape(objValue);
		if (objHours > 0) {// 为0时不设定过期时间，浏览器关闭时cookie自动消失
			var date = new Date();
			var ms = objHours * 3600 * 1000;
			date.setTime(date.getTime() + ms);
			str += "; expires=" + date.toGMTString();
		}
		str += "; path=/";
		document.cookie = str;
	}

	function getCookie(name) {
		var strCookie = document.cookie;
		var arrCookie = strCookie.split("; ");
		for ( var i = 0; i < arrCookie.length; i++) {
			var arr = arrCookie[i].split("=");
			if (arr[0] == name)
				return unescape(arr[1]);
		}
		return "";
	} 

    $.fn[ pluginName ] = function (options) {
        return this.each(function () {
            if (!$.data(this, "plugin_" + pluginName)) {
                $.data(this, "plugin_" + pluginName, new Plugin(this, options));
            }
        });
    };

})(jQuery, window, document);

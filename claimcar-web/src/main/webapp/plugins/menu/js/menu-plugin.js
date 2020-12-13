$(document).ready(function(){
	var $liCur = $(".first-menu .ul .li.first"),
	  curP = $liCur.position().left,
	  curW = $liCur.outerWidth(true),
	  $slider = $(".curBg"),
	  $navBox = $(".first-menu");
	 $targetEle = $(".first-menu .ul .li a"),
	$slider.animate({
	  "left":curP+40,
	  "width":curW-30
	});
	$targetEle.mouseenter(function () {
	  var $_parent = $(this).parent(),
		_width = $_parent.outerWidth(true),
		posL = $_parent.position().left;
	  $slider.stop(true, true).animate({
		"left":posL+40,
		"width":_width-30
	  }, "fast");
	});
	$navBox.mouseleave(function (cur, wid) {
	  cur = curP;
	  wid = curW;
	  $slider.stop(true, true).animate({
		"left":cur+40	,
		"width":wid-30
	  }, "fast");
	});
})

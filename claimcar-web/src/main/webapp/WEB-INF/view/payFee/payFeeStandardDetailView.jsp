<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/layout/common/taglib.jspf"%>

<!DOCTYPE html>
<html>
<style>
/* .img-xys {
  display: inline-block;
  height: auto;
  max-width: 100%;
}
.img-xyh {
  display: inline-block;
  height:5%;
  width:5%;
} */
  .img{
   height: auto;
   max-width: 100%;
   position:relative
   }
  img{ display:block}
  #buttonBox{
        width:200px;
        margin:20px auto;
        }
</style>
<head>
<meta name="referrer" content="never" charset="utf-8">
<title>赔偿标准图片展示</title>
</head>
<body>
<div class="page_wrap" id="fu">
	<div class="table_wrap">
			<div class="table_cont mb-10">
				<form id="form" class="form-horizontal" role="form" method="post">
				   <c:forEach var="prplFeeStandardVo" items="${prplFeeStandardVos}" varStatus="status">
					 <input type="hidden" id="image_${status.index}" value="${prplFeeStandardVo.imageUrlView}">
				 	</c:forEach>
					<div class="img" style="text-align:center;overflow:auto;">
                    <img id="img0" src="" alt="响应式图像" title="人伤赔偿标准图表" width="800">
					</div>
					<p> 
					<div id="buttonBox" style="text-align:center;">
					<a class="btn btn-primary" onclick="preImage()">上一张</a>
					&nbsp;&nbsp;
                    <a class="btn btn-primary" onclick="nextImage()">下一张</a>
					
					</div>
					<div class="img" style="text-align:right;">
					<img id="img1" src=".././imageFees/kd.jpg"  alt="响应式图像"  title="扩大" width="30" height="30">&nbsp;&nbsp;
					<img id="img2" src=".././imageFees/sx.jpg"  alt="响应式图像"  title="缩小" width="30" height="30">&nbsp;&nbsp;
					<img id="img3" src=".././imageFees/xz.jpg"  alt="响应式图像"  title="旋转" width="30" height="30">&nbsp;&nbsp;
                    <img id="img4" src=".././imageFees/refresh.jpg"  alt="响应式图像"  title="刷新" width="30" height="30">&nbsp;&nbsp;
                    <!-- <img id="img5" src=".././imageFees/close.jpg"  alt="响应式图像"  title="关闭" width="30" height="30">&nbsp;&nbsp; -->
					</div>
					
			   </form>
			   </div>
			</div>
	</div>
		
</div>
<script type="text/javascript" src="/claimcar/js/common/application.js"></script>
	<script type="text/javascript">
	var imageArr=new Array();
	var imageArrIndex=0;
    $("input[id^='image_']").each(function(){
    	imageArr[imageArrIndex]=$(this).val();
    	imageArrIndex++;
    });
	    var imaindex=0;
	    function nextImage(){
	    	imaindex++;
	        if(imaindex==imageArr.length){
	        	imaindex=0;
	        }
	        document.getElementById("img0").src=imageArr[imaindex];
	        
	    }
	    function preImage(){
	    	imaindex--;
	        if(imaindex<0){
	        	imaindex=imageArr.length-1;
	        }
	        document.getElementById("img0").src=imageArr[imaindex];
	    }
		
	$(function(){
	    window.onload = function(){
	     
	        //扩大
	        document.getElementById('img1').onclick = function(){
	        	var imgsrc = document.getElementById("img0");
	    		 imgsrc.width = imgsrc.width * 1.1;
	             imgsrc.height = imgsrc.height * 1.1;
	        };
	    	
	        //缩小
	        document.getElementById('img2').onclick = function(){
	        	var imgsrc = document.getElementById("img0");
	        	imgsrc.width = imgsrc.width / 1.1;
	            imgsrc.height = imgsrc.height / 1.1;
	        };
	    
	    	//旋转
		    var current = 0;
		    document.getElementById('img3').onclick = function(){
		            current = (current+90)%360;
		            document.getElementById('img0').style.transform = 'rotate('+current+'deg)';
		    };
		       
	    	//刷新
		    document.getElementById('img4').onclick = function(){
	        	window.location.reload();
	        };
	        /* //关闭
	        document.getElementById('img5').onclick = function(){
	        	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	        	parent.layer.close(index);
	        };  */
	        
	        
	    };
	    var picArr=new Array();
        var imageIndex=0;
        $("input[id^='image_']").each(function(){
        	picArr[imageIndex]=$(this).val();
        	imageIndex++;
        });
        if(picArr.length>0){
        	$("#img0").attr("src",picArr[0]);
        }
       
	});
	
	</script>
</body>
</html>

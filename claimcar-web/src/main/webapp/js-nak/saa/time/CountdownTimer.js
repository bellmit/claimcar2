var times = 5;//定义倒计时时间
var timerID;//定时器ID
//执行倒计时函数
var doCountDown = function(){
    if(times == 0){
    	window.clearInterval(timerID);//结束定时器
    	history.go(-1);
    }
    //显示当前倒计数
    document.getElementById("countDown").innerHTML = times;
    times --;
}
$(function() {
	doCountDown();
    timerID=window.setInterval("doCountDown();",1000);  
});
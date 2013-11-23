/**
 * @author royguo1988@gmail.com
 */

// 系统全局对象
var play = {}

play.utils = {}
play.instances = {}

$(function(){
	play.instances.tips = new play.utils.Tips();
});
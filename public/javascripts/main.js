/**
 * @fileoverview Init global variables and namespaces.
 * @author royguo1988@gmail.com
 */

// Global namespace.
var play = {}

play.models = {}
play.utils = {}
play.instances = {}

$(function(){
	// Instances that will be used everywhere.
	play.instances.tips = new play.utils.Tips();
});
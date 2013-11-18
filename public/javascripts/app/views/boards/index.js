/**
 * 主题的Model
 */
var Topic = function(id, title, content) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.selected = false;
    this.replies = [];
    this.user = null;
    // 默认允许的内容长度
    this.contentLength= 240;
};

Topic.prototype.contentChanged = function() {
    this.contentLength = 240 - this.content.length;
};

/**
 * 回复的Model
 */
var Reply = function(content) {
    this.content = content;
    this.contentLength= 240;
};

Reply.prototype.contentChanged = function() {
    this.contentLength = 240 - this.content.length;
};

/**
 * 跟Topic或Reply关联的User Model.
 */
var User = function(nickName) {
    this.nickName = nickName;
};

/**
 * 板块首页的Controller
 */
var boardApp = angular.module('boardApp', []);
function BoardIndexCtrl($scope, $http) {
    // 主题列表模型
    $scope.topics = [];
    // 创建Topic时用的模型
    $scope.topic = new Topic();
    // 创建回复时用的模型
    $scope.reply = new Reply();
    // 打开Topic用的模型
    $scope.openedTopic;
    // 分页大小
    $scope.topicPage = 1;
    $scope.topicPageSize = 10;
    // 显示板块介绍or主题内容
    $scope.hideBoardDetail = false;
    $scope.hideOpenedTopic = true;
    
    
    /**
     * 初始化页面
     */
    $scope.init = function() {
        // 获得Topic列表
        $http({
            method: 'GET',
            url: ctx + 'topics/loadJSON',
            params: {page: $scope.topicPage, pageSize: $scope.topicPageSize},
        }).success(function(topics){
            $scope.topics
            for(var i = 0; i < topics.length; i++) {
                $scope.topics.push(new Topic(topics[i].id, topics[i].title));
            }
        });
    };
    
    /**
     * 保存Topic
     */
    $scope.saveTopic = function() {
        $http({
            method: 'POST',
            url: ctx + 'topics/save',
            data: $.param({"topic.title":$scope.topic.title, "topic.content":$scope.topic.content}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(data){
            // 插入列表最上面同时删除末尾的元素.
            $scope.topics.splice(0, 0, new Topic(data.id, data.title, ""));
            if($scope.topics.length == $scope.topicPageSize) {
                $scope.topics.pop();
            }
            $("#editModal").modal('hide');
            // 清空创建Topic时用的表单
            $scope.topic = new Topic();
        });
    };

    /**
     * 打开Topic.
     */
    $scope.openTopic = function(id) {
        // 获取Topic信息
        $http({
            method: 'GET',
            url: ctx + 'topics/getJSON',
            params: {id: id},
        }).success(function(topic){
            $scope.openedTopic = new Topic(topic.id, topic.title, topic.content);
            $scope.openedTopic.replies = topic.replies;
            $scope.openedTopic.user = topic.user;
            $scope.hideOpenedTopic = false;
            $scope.hideBoardDetail = true;
            // 遍历主题列表，把打开的主题标记为高亮
            for(var i in $scope.topics) {
                if($scope.topics[i].id == topic.id) {
                    $scope.topics[i].selected = true;
                }else{
                    $scope.topics[i].selected = false;
                }
            }
        });
    };
    
    /**
     * 回复主题.
     */
    $scope.replyTopic = function(topicID) {
        $http({
            method: 'POST',
            url: ctx + 'replies/save',
            data: $.param({"reply.content":$scope.reply.content, "topicID":topicID}),
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        }).success(function(reply){
            $scope.openedTopic.replies.push(reply);
            // 回复完成后情况回复框内容以及回复预览.
            $scope.reply = new Reply();
            $("#wmd-preview-reply").empty();
        });
    };
}
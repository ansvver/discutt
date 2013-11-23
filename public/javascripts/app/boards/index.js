/**
 * @fileoverview 这是论坛板块页面需要的核心文件，所有的页面操作都在该Controller下完成.
 * @author royguo1988@gmail.com(Roy Guo)
 */

// Controllers

/**
 * 板块首页的Controller
 */
var boardApp = angular.module('boardApp', ['ngSanitize']);
function BoardIndexCtrl($scope, $http) {
  // 创建Topic时用的模型
  $scope.topic = new Topic();
  $scope.createTopicError;

  // 创建回复时用的模型
  $scope.reply = new Reply();
  $scope.replyError;

  // 打开Topic用的模型
  $scope.openedTopic;
  
  // 主题分页
  $scope.topics = [];   // 主题列表模型
  $scope.topicPage = 1;
  $scope.topicPageSize = 15;
  $scope.topicPages = [];

  // 回复分页
  $scope.replies = [];   // 主题列表模型
  $scope.replyPage = 1;
  $scope.replyPageSize = 20;
  $scope.replyPages = [];
  
  // 显示板块介绍or主题内容
  $scope.hideBoardDetail = false;
  $scope.hideOpenedTopic = true;
  
  
  /**
   * 初始化页面.
   */
  $scope.init = function(boardID) {
    // 获得Topic列表
    $scope.listTopics(boardID, 1);
  };
  
  /**
   * 获得指定分页的主题列表.
   */
  $scope.listTopics = function(boardID, page) {
    $scope.topicPage = page;
    $http({
      method: 'GET',
      url: ctx + 'topics/loadJSON',
      params: {page: $scope.topicPage, pageSize: $scope.topicPageSize, boardID:boardID}
    }).success(function(topics){
      $scope.topics = topics;
    });
    // 重新计算分页数组
    $http({
      method: 'GET',
      url: ctx + 'topics/countAll',
      params: {boardID:boardID}
    }).success(function(count){
      $scope.topicPages = CountPages($scope.topicPages, 
                    count, $scope.topicPageSize, $scope.topicPage, 7);
      if(page == -1) {
        $scope.topicPage = $scope.topicPages[$scope.topicPages.length - 1];
      }
    });
  };
  
  /**
   * 获得指定主题的回复列表.
   */
  $scope.listReplies = function(topicID, page) {
    // 先定位到页面顶端
    $scope.replyPage = page;
    $http({
      method: 'GET',
      url: ctx + 'replies/loadJSON',
      params: {page: $scope.replyPage, pageSize: $scope.replyPageSize, topicID:topicID}
    }).success(function(replies){
      $scope.replies = replies;
      for(var i = 0; i < replies.length; i++) {
        replies[i].content = MakeHTML(replies[i].content);
      }
    });
    // 重新计算分页数组
    $http({
      method: 'GET',
      url: ctx + 'replies/countAll',
      params: {topicID:topicID}
    }).success(function(count){
      $scope.replyPages = CountPages($scope.replyPages, 
                    count, $scope.replyPageSize, $scope.replyPage, 7);
      if(page == -1) {
        $scope.replyPage = $scope.replyPages[$scope.replyPages.length - 1];
      }
    });
  };
  
  $scope.deleteReply = function(replyID) {
    if(!confirm("确认删除么？")) return;
    $http({
      method: 'GET',
      url: ctx + 'replies/delete',
      params: {id:replyID}
    }).success(function(count){
      $scope.listReplies($scope.openedTopic.id, $scope.replyPage);
      $scope.openedTopic.replyCount--;
    });
  };
  
  /**
   * 保存Topic.
   */
  $scope.saveTopic = function(boardID) {
    if(!$scope.topic.title || $scope.topic.title.length > 25 || $scope.topic.title.length < 5) {
      $scope.createTopicError = '标题长度必须在5到25个字符之间！';
      return;
    }
    if(!$scope.topic.content || $scope.topic.contentLength < 0 || $scope.topic.contentLength > 235) {
      $scope.createTopicError = '内容长度必须在10到240个字符之间！';
      return;
    }
    
    $http({
      method: 'POST',
      url: ctx + 'topics/save',
      data: $.param({"topic.title":$scope.topic.title, 
               "topic.content":$scope.topic.content,
               "boardID": boardID}),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(topic){
      // 插入列表最上面同时删除末尾的元素.
      $scope.topics.splice(0, 0, topic);
      if($scope.topics.length > $scope.topicPageSize) {
        $scope.topics.pop();
      }
      // 清空创建Topic时用的表单
      $("#editModal").modal('hide');
      $("#wmd-preview-topic").empty();
      $scope.topic = new Topic();
      // 打开刚创建的Topic
      $scope.openTopic(topic.id);
    });
  };

  /**
   * 打开Topic.
   */
  $scope.openTopic = function(topic) {
    // 获取Topic信息
    $http({
      method: 'GET',
      url: ctx + 'topics/getJSON',
      params: {id: topic.id}
    }).success(function(data){
      // 把主题列表的Topic与返回的完整Topic关联起来
      topic.content = MakeHTML(data.content);
      topic.user = data.user;
      topic.date = data.date;
      $scope.openedTopic = topic;
      $scope.openedTopic.user = topic.user;
      // 打开主题，关闭板块首页
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
      // 加载该Topic下的Reply
      $scope.listReplies(topic.id, 1);
    });
  };
  
  /**
   * 回复主题.
   */
  $scope.replyTopic = function(topicID) {
    if($scope.reply.content == null 
      || $scope.reply.content.length < 3 
      || $scope.reply.content.length > 240) {
      $scope.replyError = "回复内容必须在3到240个字符之间";
      return;
    }

    $http({
      method: 'POST',
      url: ctx + 'replies/save',
      data: $.param({"reply.content":$scope.reply.content, "topicID":topicID}),
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function(reply){
      reply.content = MakeHTML(reply.content);
      $scope.openedTopic.replyCount++;
      $scope.listReplies(topicID, 1);
      // 回复完成后情况回复框内容以及回复预览.
      $scope.reply = new Reply();
      $("#wmd-preview-reply").empty();
    });
  };

  /**
   * 关闭主题
   */
  $scope.closeTopic = function() {
    $scope.hideBoardDetail = false;
    $scope.hideOpenedTopic = true;
  };
  
  /**
   * 删除主题
   */
  $scope.deleteTopic = function(boardID, topicId) {
    if(!confirm("确认删除么？")) return;
    $http({
      method: 'GET',
      url: ctx + 'topics/delete',
      params: {id: topicId}
    }).success(function(data){
      $scope.closeTopic();
      $scope.listTopics(boardID, $scope.topicPage);
    });
  };
}

//Utils
/**
 * 将Markdown转换成html的工具.
 */
function MakeHTML(source) {
  if(!source)
    return;
  this.converter = Markdown.getSanitizingConverter();
  this.converter.hooks.chain("preBlockGamut", function (text, rbg) {
    return text.replace(/^ {0,3}""" *\n((?:.*?\n)+?) {0,3}""" *$\n/gm, function (whole, inner) {
      return "<blockquote>" + rbg(inner) + "</blockquote>\n";
    });
  });
  return this.converter.makeHtml(source);
};

/**
 * 计算主题或回复分页用的页码数组.
 * @param {Array} arr Current pages number array.
 * @param {Number} elementCount Total number of the element.
 * @param {Number} pageSize Size of every page.
 * @param {Number} currentPage.
 * @param {Number} Total number of pages want to store in arr.
 * 
 * @return {Array} New pages number array.
 */
function CountPages(arr, elementCount, pageSize, currentPage, pageLimit) {
  // 计算总分页数量
  var totalPages = Math.floor(elementCount / pageSize);
  if(elementCount % pageSize > 0){
    totalPages++;
  }
  
  // 控制总分页在pageLimit个页码以下
  if(totalPages < pageLimit 
      || (totalPages >= pageLimit && arr.length == 0)
      || currentPage == 1
      ) {
    // 初始化第一组分页
    arr = [];
    for(var i = 1; i <= totalPages && i <= pageLimit; i++){
      arr.push(i);
    }
  }else if(currentPage == -1){
    // 初始化最末尾一组分页
    arr = [];
    for(var i = totalPages; i >= 1 && i > totalPages - pageLimit; i--){
      arr.splice(0,0,i);
    }
  }else if(currentPage == arr[arr.length - 1]) {
    // 点击分页最后一个，页码数组左移1/2
    for(var i = 1; i <= pageLimit/2; i++) {
      if(currentPage + i > totalPages) 
        break;
      arr.push(currentPage + i);
      arr.splice(0,1);
    }
  }else if(currentPage == arr[0]) {
   // 点击分页第一个，页码数组右移1/2
    for(var i = 1; i <= pageLimit/2; i++) {
      if(currentPage - i < 1) 
        break;
      arr.pop();
      arr.splice(0,0, currentPage - i);
    }
  }
  return arr;
}

// Models

/**
 * 主题的Model.
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
$(function() {
    initWMDEditors();
});

/**
 * 初始化文本框, Usage:
 * <div id="wmd-preview" class="wmd-panel wmd-preview"></div>
 * <div class="wmd-panel">
 *   <div id="wmd-button-bar"></div>
 *   <textarea class="wmd-input" id="wmd-input"></textarea>
 * </div>
 */
function initWMDEditors() {
    var converter = Markdown.getSanitizingConverter();
    converter.hooks.chain("preBlockGamut", function (text, rbg) {
        return text.replace(/^ {0,3}""" *\n((?:.*?\n)+?) {0,3}""" *$\n/gm, function (whole, inner) {
            return "<blockquote>" + rbg(inner) + "</blockquote>\n";
        });
    });
    $("textarea[id^='wmd-input-']").each(function(i, e){
      var postfix = $(e).attr("id").replace("wmd-input","");
      var editor = new Markdown.Editor(converter, postfix);
      editor.run();
    });

}
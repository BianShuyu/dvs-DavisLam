layui.use(['element', 'layer', 'form'], function () {
    var element = layui.element,
        layer = layui.layer,
        form = layui.form,
        $ = layui.jquery;

    //监听折叠
    element.on('collapse(test)', function (data) {
        if (data.show) {
            console.log($(data.content));
        }
    });

});

function pta() {
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.open({
            title: 'PTA',
            type: 2,
            area: ["1000px","500px"],
            content: 'https://www.baidu.com'
        });
    });
}

function chaoxing() {
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.open({
            title: '超星',
            type: 2,
            area: ["1000px","500px"],
            content: 'https://cn.bing.com'
        });
    });
}

function yuketang() {
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.open({
            title: '雨课堂',
            type: 2,
            area: ["1000px","500px"],
            content: 'https://weibo.com'
        });
    });
}
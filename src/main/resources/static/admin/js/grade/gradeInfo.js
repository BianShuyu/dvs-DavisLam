layui.use(['element', 'layer', 'form'], function () {
    var element = layui.element;
    var layer = layui.layer;
    var form = layui.form;

    //监听折叠
    element.on('collapse(test)', function (data) {
        if (data.show) {
            layer.msg(data)
        }
        layer.msg('展开状态：' + data.show);
    });
});
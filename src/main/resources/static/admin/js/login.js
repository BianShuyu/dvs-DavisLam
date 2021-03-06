layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer;
    $ = layui.jquery;

    //登录按钮事件

    form.on("submit(login)", function (data) {
        console.log("调用了自带的");
        var loadIndex = layer.load(2, {shade: [0.3, '#333']});
        if ($('form').find('input[type="checkbox"]')[0].checked) {
            data.field.rememberMe = true;
        } else {
            data.field.rememberMe = false;
        }
        $.post(data.form.action, data.field, function (res) {
            layer.close(loadIndex);
            if (res.success) {
                layui.data('local', {key: 'token', value: res.token});
                location.href = "/" + res.url + "?token=" + res.token;
            } else {
                layer.msg(res.message);
            }
        });
        return false;
    });

});

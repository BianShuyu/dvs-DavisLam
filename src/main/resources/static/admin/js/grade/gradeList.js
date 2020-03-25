layui.use(['layer', 'form', 'table'], function () {
    var layer = layui.layer,
        $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        t;                  //表格数据变量
    console.log(layui.data('local'));
    t = {
        elem: '#gradeTable',
        url: '/grade/list',
        where: layui.data('local'),
        method: 'post',
        page: true,
        width: $(parent.window).width() - 223,
        cols: [[
            {type: 'checkbox'},
            {
                field: 'name',
                title: '年级',
                width: '60%',
            },
            {fixed: 'right', align: 'center', toolbar: '#gradeBar'}
        ]]
    };
    table.render(t);

    table.on('row(gradeList)', function (obj) {
        var data = obj.data;
        var editIndex = layer.open({
            title: data.name,
            type: 2,
            content: "/grade/info?id=" + data.id,
            success: function (layero, index) {
                setTimeout(function () {
                    layer.tips('点击此处返回会员列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500);
            }
        });
        $(window).resize(function () {
            layer.full(editIndex);
        });
        layer.full(editIndex);
    });


    //监听工具条
    table.on('tool(gradeList)', function (obj) {
        var data = obj.data;
        console.log(obj.event);
        if (obj.event === 'edit') {
            var editIndex = layer.open({
                title: "编辑用户",
                type: 2,
                content: "/grade/edit?id=" + data.id,
                success: function (layero, index) {
                    setTimeout(function () {
                        layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500);
                }
            });
            $(window).resize(function () {
                layer.full(editIndex);
            });
            layer.full(editIndex);
        }

        if (obj.event === "del") {

            layer.confirm("你确定要删除该课程么？", {btn: ['是的,我确定', '我再想想']},
                function () {
                    $.post("/grade/delete", {"id": data.id}, function (res) {
                        if (res.success) {
                            layer.msg("删除成功", {time: 1000}, function () {
                                table.reload('gradeTable', t);
                            });
                        } else {
                            layer.msg(res.message);
                        }
                    });
                }
            );
        }
    });

    //功能按钮
    var active = {
        deleteSome: function () {                        //批量删除
            var checkStatus = table.checkStatus('gradeTable'),
                data = checkStatus.data;
            console.log(data);
            if (data.length > 0) {
                layer.confirm("你确定要删除这些用户么？", {btn: ['是的,我确定', '我再想想']},
                    function () {
                        var deleteindex = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
                        $.ajax({
                            type: "POST",
                            url: "/grade/deleteSome",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(data),
                            success: function (res) {
                                layer.close(deleteindex);
                                layer.msg("共删除成功" + res.count + "门课程", {time: 1000}, function () {
                                    table.reload('gradeTable', t);
                                });
                            }
                        });
                    }
                )
            } else {
                layer.msg("请选择需要删除的用户", {time: 1000});
            }
        }
    };

    $('.layui-inline .layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
});






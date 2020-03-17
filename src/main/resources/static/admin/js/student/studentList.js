layui.use(['layer', 'form', 'table'], function () {
    var layer = layui.layer,
        $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        t;                  //表格数据变量

    t = {
        elem: '#studentTable',
        url: '/student/list',
        method: 'post',
        page: true,
        width: $(parent.window).width() - 223,
        cols: [[
            {type: 'checkbox'},
            {field: 'studentNum', title: '学号', event: 'info', width: '25%'},
            {field: 'name', title: '姓名', event: 'info', width: '25%'},
            {field: 'class', title: '班级', event: 'info', width: '25%'},
            {fixed: 'right', align: 'center', toolbar: '#studentBar'}
        ]]
    };
    table.render(t);

    //监听工具条
    table.on('tool(studentList)', function (obj) {
        var data = obj.data;
        if (obj.event === 'edit') {
            var editIndex = layer.open({
                title: "编辑用户",
                type: 2,
                content: "/admin/system/student/edit?id=" + data.id,
                success: function (layero, index) {
                    setTimeout(function () {
                        layer.tips('点击此处返回会员列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500);
                }
            });
            //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
            $(window).resize(function () {
                layer.full(editIndex);
            });
            layer.full(editIndex);
        }

        if (obj.event === "del") {
            layer.confirm("你确定要删除该用户么？", {btn: ['是的,我确定', '我再想想']},
                function () {
                    $.post("/admin/system/student/delete", {"id": data.id}, function (res) {
                        if (res.success) {
                            layer.msg("删除成功", {time: 1000}, function () {
                                table.reload('userTable', t);
                            });
                        } else {
                            layer.msg(res.message);
                        }

                    });
                }
            );
        }

        if (obj.event == 'info') {
            var editIndex = layer.open({
                title: "学生信息",
                type: 2,
                content: "/student/info?id=" + data.id,
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
        }
    });

    //功能按钮
    var active = {
        addUser: function () {
            var addIndex = layer.open({
                title: "添加用户",
                type: 2,
                content: "/admin/system/student/add",
                success: function (layero, addIndex) {
                    setTimeout(function () {
                        layer.tips('点击此处返回会员列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500);
                }
            });
            //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
            $(window).resize(function () {
                layer.full(addIndex);
            });
            layer.full(addIndex);
        },
        deleteSome: function () {                        //批量删除
            var checkStatus = table.checkStatus('userTable'),
                data = checkStatus.data;
            if (data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var d = data[i];
                    if (d.adminUser) {
                        layer.msg("不能删除超级管理员");
                        return false;
                    }
                }
                layer.confirm("你确定要删除这些用户么？", {btn: ['是的,我确定', '我再想想']},
                    function () {
                        var deleteindex = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
                        $.ajax({
                            type: "POST",
                            url: "/admin/system/student/deleteSome",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(data),
                            success: function (res) {
                                layer.close(deleteindex);
                                if (res.success) {
                                    layer.msg("删除成功", {time: 1000}, function () {
                                        table.reload('userTable', t);
                                    });
                                } else {
                                    layer.msg(res.message);
                                }
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

    //搜索
    form.on("submit(searchForm)", function (data) {
        t.where = data.field;
        table.reload('userTable', t);
        return false;
    });

});
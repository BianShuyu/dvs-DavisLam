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
            {field: 'number', title: '学号', event: 'info', width: '25%'},
            {field: 'name', title: '姓名', event: 'info', width: '25%'},
            {field: 'clazz', title: '班级', event: 'info', width: '25%'},
            {fixed: 'right', align: 'center', toolbar: '#studentBar'}
        ]]
    };
    table.render(t);

    //监听工具条
    table.on('tool(studentList)', function (obj) {
        var data = obj.data;
        console.log(data);
        if (obj.event === 'edit') {
            layer.msg("功能开发中");
        }

        if (obj.event === "del") {
            layer.msg("功能开发中");
        }

        if (obj.event == 'info') {
            var editIndex = layer.open({
                title: "学生信息",
                type: 2,
                content: "/student/info?id=" + data.id,
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
    });

    //功能按钮
    var active = {
        addUser: function () {
            layer.msg("功能开发中");
        },
        deleteSome: function () {                        //批量删除
            layer.msg("功能开发中");
        }
    };

    $('.layui-inline .layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //搜索
    form.on("submit(searchForm)", function (data) {
        t.where = data.field;
        table.reload('studentTable', t);
        return false;
    });

});
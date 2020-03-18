layui.use(['layer', 'form', 'table'], function () {
    var layer = layui.layer,
        $ = layui.jquery,
        form = layui.form,
        table = layui.table,
        t;                  //表格数据变量

    t = {
        elem: '#courseTable',
        url: '/course/list',
        method: 'post',
        page: true,
        width: $(parent.window).width() - 223,
        cols: [[
            {type: 'checkbox'},
            {field: 'name', title: '课程名', width: '40%'},
            {fixed: 'right', align: 'center', toolbar: '#courseBar'}
        ]]
    };
    table.render(t);

    var first = true;

    //监听工具条
    table.on('tool(courseList)', function (obj) {
        var course = obj.data;

        if (obj.event === 'upFile') {

            var xlf = document.getElementById('xlf');
            function importFile(files) {
                var loadIndex = layer.load(2, {
                    shade: [0.3, '#333']
                });
                var f = files[0];
                var fname = f.name;
                var obj = {};
                var url = "";
                obj["token"] = "tokentest";
                if (fname.startsWith("PTA")) {
                    obj["type"] = "pta";
                    url = "/pta/upload";
                } else if (fname.startsWith("超星")) {
                    obj["type"] = "chaoxing";
                    url = "/chaoxing/upload";
                } else if (fname.startsWith("期末考试")) {
                    obj["type"] = "final";
                    url = "/final/upload";
                } else if (fname.startsWith("雨课堂")) {
                    obj["type"] = "yuketang";
                    url = "/yuketang/upload";
                }

                var reader = new FileReader();
                reader.onload = function (e) {
                    var data = e.target.result;
                    var workbook = XLSX.read(data, {type: "binary"});
                    var sheetNames = workbook.SheetNames;
                    var numSheets = obj["type"] === "final" ? 1 : sheetNames.length;
                    var sheets = {};
                    for (var i = 0; i < numSheets; i++) {
                        var sheetName = sheetNames[i];
                        var sheet = workbook.Sheets[sheetName];
                        var sheetData = XLSX.utils.sheet_to_json(sheet, {header: 1});
                        sheets[sheetName] = sheetData;
                    }
                    obj["data"] = sheets;
                    obj["courseId"] = course.id;

                    console.log(obj);
                    $.ajax({
                        type: 'post',
                        dataType: 'json',
                        contentType: 'application/json',
                        url: url,
                        data: JSON.stringify(obj),
                        success: function(res) {
                            layer.close(loadIndex);
                            if (res.success) {
                                parent.layer.msg("数据上传成功!", {time: 1500}, function () {
                                    //刷新父页面
                                    parent.location.reload();
                                });
                            } else {
                                parent.layer.msg("数据上传失败!", {time: 1500}, function () {
                                    //刷新父页面
                                    parent.location.reload();
                                });
                            }
                        }
                    });
                };

                reader.readAsBinaryString(f);
            }

            function handleFile(e) {
                importFile(e.target.files);
            }
            if (first) {
                xlf.addEventListener('change', handleFile, false);
                first = false;
            }
        }

        if (obj.event === 'edit') {
            var editIndex = layer.open({
                title: "编辑用户",
                type: 2,
                content: "/course/edit?id=" + course.id,
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

        if (obj.event === "del") {
            layer.confirm("你确定要删除该课程么？", {btn: ['是的,我确定', '我再想想']},
                function () {
                    $.post("/course/delete", {"id": course.id}, function (res) {
                        if (res.success) {
                            layer.msg("删除成功", {time: 1000}, function () {
                                table.reload('courseTable', t);
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
        addCourse: function () {
            var addIndex = layer.open({
                title: "添加课程",
                type: 2,
                content: "/course/add",
                success: function (layero, addIndex) {
                    setTimeout(function () {
                        layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
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
            var checkStatus = table.checkStatus('courseTable'),
                data = checkStatus.data;
            console.log(data);
            if (data.length > 0) {
                layer.confirm("你确定要删除这些用户么？", {btn: ['是的,我确定', '我再想想']},
                    function () {
                        var deleteindex = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
                        $.ajax({
                            type: "POST",
                            url: "/course/deleteSome",
                            dataType: "json",
                            contentType: "application/json",
                            data: JSON.stringify(data),
                            success: function (res) {
                                layer.close(deleteindex);
                                layer.msg("共删除成功" + res.count + "门课程", {time: 1000}, function () {
                                    table.reload('courseTable', t);
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
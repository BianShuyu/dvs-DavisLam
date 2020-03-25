layui.use(['element', 'layer', 'form', 'table'], function () {
    var element = layui.element,
        layer = layui.layer,
        form = layui.form,
        table = layui.table,
        $ = layui.$;


    var t = {
        elem: '#studentOverview',
        url: '/student/overview',
        method: 'post',
        page: true,
        where: {
            studentId: $("#studentId").val()
        },
        width: $(parent.window).width() / 2,
        cols: [[
            {title: '序号', width: '10%', align: 'left', type: 'numbers'},
            {field: 'name', title: '课程名', align: 'left', width: '70%'},
            {field: 'score', title: '得分', align: 'left', width: '20%'},
        ]]
    };

    table.render(t);


    $(document).on('click', '#pta', function () {
        var studentId = $("#studentId").val();
        var courseId = document.getElementById("course").value;

        $("#figContainer").empty();
        $("#figContainer").append(oneFig("PTA各题型得分", "fig"));

        var obj = {};
        obj["token"] = "123";
        obj["courseId"] = courseId;
        obj["studentId"] = studentId;
        console.log(obj);
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/pta/studentInfo",
            data: JSON.stringify(obj),
            success: function (res) {
                //准备数据
                res = res.data;
                var source = [["", $("#studentName").val(), "平均分", "最高分"]];

                for (var key in res.cur) {
                    source.push([key, res.cur[key], res.aver[key], res.max[key]]);
                }
                console.log(source);


                var myChart = echarts.init(document.getElementById("fig"));
                var option = {
                    legend: {},
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    dataset: {
                        source: source
                    },
                    xAxis: {type: 'category'},
                    yAxis: {},
                    // Declare several bar series, each will be mapped
                    // to a column of dataset.source by default.
                    series: [
                        {type: 'bar'},
                        {type: 'bar'},
                        {type: 'bar'}
                    ]
                };

                myChart.setOption(option);
            }
        });


    });
    $(document).on('click', '#chaoxing', function () {
        var studentId = $("#studentId").val();
        var courseId = document.getElementById("course").value;
        $("#figContainer").empty();

        var obj = {};
        obj["token"] = "123";
        obj["courseId"] = courseId;
        obj["studentId"] = studentId;
        console.log(obj);
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/chaoxing/studentInfo",
            data: JSON.stringify(obj),
            success: function (res) {
                console.log(res);

                var head = ["讨论得分", "视频得分", "作业得分", "测验得分", "考试得分", "总分", "等级"];
                var detail = res.detail;
                var tableData = [
                    [detail.discussScore, detail.videoScore, detail.workScore,
                     detail.quizScore, detail.examScore, detail.score, detail.level]
                ];

                staticTable("成绩组成", "#figContainer", head, tableData);

                $("#figContainer").append(twoFig("章节测验", "视频观看", "fig1", "fig2"));
                linePlot(res.chapter, "章节测验", "分", "fig1");
                linePlot(res.video, "视频观看", "%", "fig2");
                $("#figContainer").append(oneFig("作业分数", "fig3"));
                linePlot(res.work, "作业分数", "分", "fig3");

            }
        });
    });
    $(document).on('click', '#yuketang', function () {
        var studentId = $("#studentId").val();
        var courseId = document.getElementById("course").value;
        $("#figContainer").empty();

        var obj = {};
        obj["token"] = "123";
        obj["courseId"] = courseId;
        obj["studentId"] = studentId;
        console.log(obj);
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/ykt/studentInfo",
            data: JSON.stringify(obj),
            success: function (res) {
                console.log(res);

                $("#figContainer").append(oneFig("推送正答率", "fig1"));
                linePlot(res.rightRatio, "推送正答率", "%", "fig1");

                var tableData = [];
                var l1 = res.missingClass;
                var l2 = res.unreadAnnouncement;

                var len = Math.min(l1.length, l2.length);
                for (var i = 0; i < len; i++) {
                    tableData.push([l1[i], l2[i]]);
                }
                for (var i = 0; i < l2.length - len; i++) {
                    tableData.push(["", l2[i + len]]);
                }

                for (var i = 0; i < l1.length - len; i++) {
                    tableData.push([l1[i + len], ""]);
                }


                staticTable("缺课和未读公告统计", "#figContainer", ["缺课", "未读公告"], tableData);
            }
        });
    });

    $(document).on('click', '#lidar', function () {
        var studentId = $("#studentId").val();
        var courseId = document.getElementById("course").value;
        $("#figContainer").empty();

        var obj = {};
        obj["token"] = "123";
        obj["courseId"] = courseId;
        obj["studentId"] = studentId;
        console.log(obj);
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/student/lidar",
            data: JSON.stringify(obj),
            success: function (res) {
                console.log(res);
                $("#figContainer").append(oneFig("各平台分数雷达图", "fig1"));
                var chart = echarts.init(document.getElementById("fig1"));


                chart.setOption({
                    radar: [
                        {
                            indicator: [
                                { text: '期末考试', max: 100 },
                                { text: 'PTA', max: 100 },
                                { text: '超星', max: 100 },
                                { text: '雨课堂', max: 100 },
                            ],
                            startAngle: 90,
                            splitNumber: 4,
                            shape: 'circle',
                            name: {
                                formatter: '【{value}】',
                                textStyle: {
                                    color: '#72ACD1'
                                }
                            },
                            splitArea: {
                                areaStyle: {
                                    color: ['rgba(114, 172, 209, 0.2)',
                                        'rgba(114, 172, 209, 0.4)', 'rgba(114, 172, 209, 0.6)',
                                        'rgba(114, 172, 209, 0.8)', 'rgba(114, 172, 209, 1)'],
                                    shadowColor: 'rgba(0, 0, 0, 0.3)',
                                    shadowBlur: 10
                                }
                            },
                            axisLine: {
                                lineStyle: {
                                    color: 'rgba(255, 255, 255, 0.5)'
                                }
                            },
                            splitLine: {
                                lineStyle: {
                                    color: 'rgba(255, 255, 255, 0.5)'
                                }
                            }
                        }
                    ],
                    series: [
                        {
                            type: 'radar',
                            data: [
                                {
                                    value: [res.final, res.pta, res.chaoxing, res.yuketang],
                                    label: {
                                        show: true,
                                        formatter: function(params) {
                                            return Math.floor(params.value);
                                        }
                                    }
                                },
                            ]
                        }
                    ]
                });
            }
        });
    });
});







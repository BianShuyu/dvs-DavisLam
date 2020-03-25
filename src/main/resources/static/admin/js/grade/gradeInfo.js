layui.use(['element', 'layer', 'form', 'slider'], function () {
    var element = layui.element;
    var layer = layui.layer;
    var form = layui.form;
    var $ = layui.$;
    var slider = layui.slider;

    //设置步长
    slider.render({
        elem: '#slideTest4',
        min: 20,
        max: 60,
        step: 10 //步长
    });

    function boxplot(data, xaxis, yaxis, figid) {
        data = echarts.dataTool.prepareBoxplotData(data);
        var option = {
            tooltip: {
                trigger: 'item',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '10%',
                right: '10%',
                bottom: '15%'
            },
            xAxis: {
                type: 'category',
                data: xaxis,
                boundaryGap: true,
                nameGap: 30,
                splitArea: {
                    show: false
                },
                splitLine: {
                    show: false
                }
            },
            yAxis: {
                type: 'value',
                name: yaxis,
                splitArea: {
                    show: true
                }
            },
            series: [
                {
                    name: 'boxplot',
                    type: 'boxplot',
                    data: data.boxData,
                    tooltip: {
                        formatter: function (param) {
                            return [
                                param.name + ': ',
                                '最高分: ' + param.data[5],
                                '上四分位: ' + param.data[4],
                                '中位数: ' + param.data[3],
                                '下四分位: ' + param.data[2],
                                '最低分: ' + param.data[1]
                            ].join('<br/>');
                        }
                    }
                },
                {
                    name: '离群值',
                    type: 'scatter',
                    data: data.outliers
                }
            ]
        };
        var chart = echarts.init(document.getElementById(figid));
        chart.setOption(option);
    }

    function histPlot(data, figId) {
        var bins = ecStat.histogram(data).data.slice(1);
        var halfWidth = (bins[1][0] - bins[0][0]) / 2;
        var option = {
            toolbox: {
                // y: 'bottom',
                feature: {
                    magicType: {
                        type: ['stack', 'tiled']
                    },
                    dataView: {},
                    saveAsImage: {
                        pixelRatio: 2
                    }
                }
            },
            tooltip: {
                formatter: function (params) {
                    return (params.value[0] - halfWidth) +
                        "-" + (params.value[0] + halfWidth) +
                        " 人数：" + params.value[1];
                }
            },
            xAxis: {
                type: "value",
                scale: true,
                splitLine: {
                    show: false
                }
            },
            yAxis: {
                type: "value",
            },
            series: [{
                name: "height",
                type: "bar",
                barWidth: "99.3%",
                label: {
                    normal: {
                        show: true,
                        position: "insideTop",
                        formatter: function (params) {
                            return params.value[1];
                        }
                    }
                },
                data: bins
            }],
            animationEasing: 'elasticOut',
        };
        var chart = echarts.init(document.getElementById(figId));
        chart.setOption(option);
    }

    $(document).on('click', '#final', function () {
        $("#figContainer").empty();
        $("#figContainer").append(twoFig("甜甜圈", "堆叠条形图", "fig1", "fig2"));
        $("#figContainer").append(oneFig("箱型图", "fig3"));

        var obj = {};
        obj["token"] = layui.data('local').token;
        obj["courseId"] = document.getElementById("course").value;
        obj["gradeId"] = $("#gradeId").val();
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/finalExam/summary",
            data: JSON.stringify(obj),
            success: function (result) {
                //准备数据
                console.log(result);
                var res = result.data.segmentData;
                console.log(res);
                var segmenets = ["[0, 60)", "[60, 70)", "[70, 80)", "[80, 90)", "[90, 100]"];
                var classes = [];
                for (var key in res) {
                    classes.push(key);
                }
                classes.sort();
                var total = [0, 0, 0, 0, 0];
                for (var key in res) {
                    for (var i = 0; i < segmenets.length; i++) {
                        total[i] += res[key][segmenets[i]];
                    }
                }
                var option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    legend: {
                        left: 'center',
                        top: 'bottom',
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: {show: true},
                            dataView: {show: true, readOnly: false},
                            magicType: {
                                show: true,
                                type: ['pie', 'funnel']
                            },
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    series: [
                        {
                            name: '分数段',
                            type: 'pie',
                            avoidLabelOverlap: false,
                            label: {
                                show: false,
                                position: 'center'
                            },
                            radius: ['50%', '70%'],
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: '200%',
                                    fontWeight: 'bold'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            data: [
                                {value: total[0], name: segmenets[0]},
                                {value: total[1], name: segmenets[1]},
                                {value: total[2], name: segmenets[2]},
                                {value: total[3], name: segmenets[3]},
                                {value: total[4], name: segmenets[4]},
                            ]
                        }
                    ]
                };
                var chart = echarts.init(document.getElementById("fig1"));
                chart.setOption(option);

                var stackData = [];
                for (var i = 0; i < segmenets.length; i++) {
                    var cur = {
                        name: segmenets[i],
                        type: 'bar',
                        stack: '总量',
                        label: {
                            show: true,
                            position: 'inside'
                        },
                    };
                    var curData = [];
                    for (var j = 0; j < classes.length; j++) {
                        curData.push(res[classes[j]][segmenets[i]]);
                    }
                    cur.data = curData;
                    stackData.push(cur);
                }
                option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: segmenets
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'category',
                        data: classes
                    },
                    series: stackData
                };
                chart = echarts.init(document.getElementById("fig2"));
                chart.setOption(option);

                boxplot(result.data.boxData, classes, "分数", "fig3");


            }
        });
    });

    $(document).on('click', '#pta', function () {
        $("#figContainer").empty();
        var obj = {};
        obj["token"] = layui.data('local').token;
        obj["courseId"] = document.getElementById("course").value;
        obj["gradeId"] = $("#gradeId").val();
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/pta/summary",
            data: JSON.stringify(obj),
            success: function (result) {

                $("#figContainer").append(twoFig("直方图", "箱型图", "fig1", "fig2"));
                console.log(result.data);
                var classData = result.data.classData;
                var typeData = result.data.typeData;

                var classes = [];
                var total = [];
                for (var key in classData) {
                    classes.push(key);
                    total = total.concat(classData[key]);
                }
                classes.sort();
                histPlot(total, "fig1");

                var boxData = [];
                for (var i = 0; i < classes.length; i++) {
                    boxData.push(classData[classes[i]]);
                }
                console.log(boxData);
                boxplot(boxData, classes, "分数", "fig2");

                var types = [];
                for (var key in typeData) {
                    types.push(key);
                }

                function drawBarNegative(curData, figid) {
                    console.log(curData)
                    var questionNums = [];
                    var correct = [];
                    var incorrect = [];
                    for (var key in curData) {
                        questionNums.push(key);
                        correct.push(curData[key][1]);
                        incorrect.push(-curData[key][0]);
                    }
                    var option = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            },
                            formatter: function (param) {
                                var str = param[0].name + "<br>";
                                var n = param.length;
                                for (var i = 0; i < n; i++) {
                                    str += param[i].marker + " " + param[i].seriesName + ": " + ((i == n - 1 ? -1 : 1) * param[i].data) + "<br>";
                                }
                                return str;
                            }
                        },
                        legend: {
                            data: ['正确', '不正确']
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'value'
                            }
                        ],
                        yAxis: [
                            {
                                type: 'category',
                                axisTick: {
                                    show: false
                                },
                                data: questionNums
                            }
                        ],
                        series: [
                            {
                                name: '正确',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: true
                                },
                                data: correct
                            },
                            {
                                name: '不正确',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: true,
                                    position: 'left',
                                    formatter: function (param) {
                                        return -param.data;
                                    },
                                },
                                data: incorrect
                            }
                        ]
                    };

                    var chart = echarts.init(document.getElementById(figid));
                    chart.setOption(option);
                }

                console.log(types);
                for (var i = 0; i + 1 < types.length; i += 2) {
                    var first = "fig" + (i + 3);
                    var second = "fig" + (i + 4);
                    $("#figContainer").append(twoFig(types[i], types[i + 1], first, second));
                    drawBarNegative(typeData[types[i]], first);
                    drawBarNegative(typeData[types[i + 1]], second);
                }
                if (types.length % 2 === 1) {
                    var curId = "fig" + (types.length + 2);
                    $("#figContainer").append(oneFig(types[types.length - 1], curId));
                    drawBarNegative(typeData[types[types.length - 1]], curId);
                }


            }
        });
    });

    $(document).on('click', '#chaoxing', function () {
        $("#figContainer").empty();
        var obj = {};
        obj["token"] = layui.data('local').token;
        obj["courseId"] = document.getElementById("course").value;
        obj["gradeId"] = $("#gradeId").val();
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/chaoxing/summary",
            data: JSON.stringify(obj),
            success: function (result) {
                console.log(result);
                $("#figContainer").append(twoFig("视频观看", "作业完成", "fig1", "fig2"));
                $("#figContainer").append(twoFig("章节测验", "访问详情", "fig3", "fig4"));

                {
                    var threshold = 60;
                    var videoData = result.video;
                    var videonames = [];
                    for (var key in videoData) {
                        videonames.push(key);
                    }
                    var reach = [];
                    var noReach = [];
                    for (var key in videoData) {
                        var cur = 0;
                        for (var i = 0; i < threshold / 10; i++) {
                            cur += videoData[key][i];
                        }
                        noReach.push(cur);
                        cur = 0;
                        for (var i = threshold / 10; i < 10; i++) {
                            cur += videoData[key][i];
                        }
                        reach.push(cur);
                    }
                    console.log(reach);
                    console.log(noReach);

                    var option = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                            }
                        },
                        legend: {
                            data: ['观看百分比超过60%', '观看百分比未达到60%']
                        },
                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        xAxis: [
                            {
                                type: 'category',
                                axisTick: {
                                    show: false
                                },
                                data: videonames
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value'
                            }

                        ],
                        dataZoom: [
                            {   // 这个dataZoom组件，默认控制x轴。
                                type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
                                start: 30,      // 左边在 10% 的位置。
                                end: 50         // 右边在 60% 的位置。
                            }
                        ],
                        series: [
                            {
                                name: '观看百分比超过60%',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: true
                                },
                                data: reach
                            },
                            {
                                name: '观看百分比未达到60%',
                                type: 'bar',
                                stack: '总量',
                                label: {
                                    show: true,
                                },
                                data: noReach
                            }
                        ]
                    };
                    var chart = echarts.init(document.getElementById("fig1"));
                    chart.setOption(option);
                }

                function drawWorkOrChapter(data, figId) {
                    var names = [];
                    var avers = [];
                    var zeros = [];
                    var non_zeros = [];
                    for (var key in data) {
                        names.push(key);
                        avers.push(data[key][0] / (data[key][1] + data[key][2]));
                        zeros.push(data[key][1]);
                        non_zeros.push(data[key][2]);
                    }
                    console.log(avers);
                    console.log(zeros);
                    console.log(non_zeros);

                    var legend = ['根本没做', '做了的', '平均分'];

                    var option = {
                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'cross',
                                crossStyle: {
                                    color: '#999'
                                }
                            },
                            formatter: function (param) {
                                var str = param[0].name + "<br>";
                                var n = param.length;
                                for (var i = 0; i < n - 1; i++) {
                                    str += param[i].marker + " " + param[i].seriesName + ": " + param[i].data + "<br>";
                                }
                                str += param[n - 1].marker + " " + param[n - 1].seriesName + ": " +
                                    param[n - 1].data.toFixed(2) + "<br>";
                                return str;
                            }
                        },
                        toolbox: {
                            feature: {
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        legend: {
                            data: legend
                        },
                        xAxis: [
                            {
                                type: 'category',
                                data: names,
                                axisPointer: {
                                    type: 'shadow'
                                },
                            }
                        ],
                        yAxis: [
                            {
                                type: 'value',
                                name: '人数',
                                splitLine: false,
                            },
                            {
                                type: 'value',
                                name: '分数',
                                splitLine: false,
                            }
                        ],
                        dataZoom: [
                            {   // 这个dataZoom组件，默认控制x轴。
                                type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
                                start: 0,      // 左边在 10% 的位置。
                                end: 50         // 右边在 60% 的位置。
                            }
                        ],
                        series: [
                            {
                                name: legend[0],
                                type: 'bar',
                                data: zeros
                            },
                            {
                                name: legend[1],
                                type: 'bar',
                                data: non_zeros
                            },
                            {
                                name: legend[2],
                                type: 'line',
                                yAxisIndex: 1,
                                data: avers
                            }
                        ]
                    };
                    var chart = echarts.init(document.getElementById(figId));
                    chart.setOption(option);
                }

                drawWorkOrChapter(result.work, "fig2");
                drawWorkOrChapter(result.chapter, "fig3");

                {
                    var accessData = result.access;

                    chart = echarts.init(document.getElementById("fig4"));

                    function getSeries(name, data) {
                        return {
                            name: name,
                            type: 'line',
                            data: data,
                            markLine: {
                                silent: true,
                                data: [{
                                    yAxis: 50
                                }, {
                                    yAxis: 100
                                }, {
                                    yAxis: 150
                                }, {
                                    yAxis: 200
                                }, {
                                    yAxis: 300
                                }]
                            }
                        }
                    }

                    var seriesData = [];
                    var times = [];

                    for (var key in accessData) {
                        if (key === "dates") continue;
                        times.push(key);
                    }

                    times.sort();
                    var selectedStatus = {};
                    for (var i = 0; i < times.length; i++) {
                        if (i == 0) {
                            selectedStatus[times[i]] = true;
                        } else {
                            selectedStatus[times[i]] = false;
                        }

                        seriesData.push(getSeries(times[i], accessData[times[i]]));
                    }
                    console.log(selectedStatus);
                    console.log(seriesData);
                    chart.setOption(option = {
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: times,
                            selected: selectedStatus
                        },
                        xAxis: {
                            data: accessData.dates
                        },
                        yAxis: {
                            splitLine: {
                                show: false
                            }
                        },
                        dataZoom: [{
                            startValue: '2014-06-01'
                        }, {
                            type: 'inside'
                        }],
                        visualMap: {
                            top: 10,
                            right: 10,
                            pieces: [{
                                gt: 0,
                                lte: 50,
                                color: '#096'
                            }, {
                                gt: 50,
                                lte: 100,
                                color: '#ffde33'
                            }, {
                                gt: 100,
                                lte: 150,
                                color: '#ff9933'
                            }, {
                                gt: 150,
                                lte: 200,
                                color: '#cc0033'
                            }, {
                                gt: 200,
                                lte: 300,
                                color: '#660099'
                            }, {
                                gt: 300,
                                color: '#7e0023'
                            }],
                            outOfRange: {
                                color: '#999'
                            }
                        },
                        series: seriesData
                    });
                }

                $("#figContainer").append(twoFig("考试得分", "综合成绩", "fig5", "fig6"));

                {
                    var summary = result.summary;
                    var classes = [];
                    for (var key in summary) {
                        classes.push(key);
                    }
                    classes.sort();
                    var examBoxData = [];

                    for (var i = 0; i < classes.length; i++) {
                        examBoxData.push(summary[classes[i]]["examScore"]);
                    }
                    boxplot(examBoxData, classes, "分数", "fig5");

                    var levelData = [];
                    levelData.push(["class"].concat(classes));
                    var levels = ["A", "B", "C", "D", "E"];
                    var levelToIndex = {
                        "A": 1, "B": 2, "C": 3, "D": 4, "E": 5
                    };
                    for (var i = 0; i < levels.length; i++) {
                        var curArray = [levels[i]];
                        for (var j = 0; j < classes.length; j++) {
                            curArray.push(0);
                        }
                        levelData.push(curArray);
                    }

                    for (var i = 0; i < classes.length; i++) {
                        var classLevels = summary[classes[i]]["level"];
                        for (var j = 0; j < classLevels.length; j++) {
                            // console.log(levelToIndex[classLevels[j]] + "-" + (i + 1));
                            levelData[levelToIndex[classLevels[j]]][i + 1]++;
                        }
                    }

                    console.log(levelData);


                    setTimeout(function () {
                        chart = echarts.init(document.getElementById("fig6"));
                        option = {
                            legend: {},
                            tooltip: {
                                trigger: 'axis',
                                showContent: false
                            },
                            dataset: {
                                source: levelData
                            },
                            xAxis: {type: 'category'},
                            yAxis: {gridIndex: 0},
                            grid: {top: '55%'},
                            series: [
                                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                                {type: 'line', smooth: true, seriesLayoutBy: 'row'},
                                {
                                    type: 'pie',
                                    id: 'pie',
                                    radius: '30%',
                                    center: ['50%', '25%'],
                                    label: {
                                        formatter: "{b}: {@" + classes[0] + "} ({d}%)"
                                    },
                                    encode: {
                                        itemName: "class",
                                        value: classes[0],
                                        tooltip: classes[0]
                                    }
                                }
                            ]
                        };

                        chart.on('updateAxisPointer', function (event) {
                            var xAxisInfo = event.axesInfo[0];
                            if (xAxisInfo) {
                                var dimension = xAxisInfo.value + 1;
                                chart.setOption({
                                    series: {
                                        id: 'pie',
                                        label: {
                                            formatter: '{b}: {@[' + dimension + ']} ({d}%)'
                                        },
                                        encode: {
                                            value: dimension,
                                            tooltip: dimension
                                        }
                                    }
                                });
                            }
                        });

                        chart.setOption(option);

                    });
                }


            }
        });
    });

    $(document).on('click', '#yuketang', function () {
        $("#figContainer").empty();

        var obj = {};
        obj["token"] = layui.data('local').token;
        obj["courseId"] = document.getElementById("course").value;
        obj["gradeId"] = $("#gradeId").val();
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/ykt/summary",
            data: JSON.stringify(obj),
            success: function (result) {
                console.log(result);
                $("#figContainer").append(twoFig("课堂情况", "推送情况", "fig1", "fig2"));
                linePlot(result.presentRatio, "到课率", "%", "fig1");
                linePlot(
                    result.pushCorrectRatio,
                    result.pushReadingRatio,
                    "正确率", "阅读率", "%", "fig2");
                $("#figContainer").append(twoFig("推送阅读时长", "公告阅读情况", "fig3", "fig4"));
                linePlot(result.pushDuration, "阅读时长", "分钟", "fig3");
                linePlot(result.readingRatio, "阅读率", "%", "fig4");

                $("#figContainer").append(twoFig("学生正答率统计", "各班平均得分", "fig5", "fig6"));

                var total = [];
                var disqualify = [];
                for (var key in result.pushStudentCorrectRatio) {
                    var val = result.pushStudentCorrectRatio[key];
                    if (val === 100) val--;
                    total.push(val);
                }
                histPlot(total, "fig5");

                var classNames = [];
                var scores = [];
                for (var key in result.score) {
                    classNames.push(key);
                    scores.push(result.score[key]);
                }
                var chart = echarts.init(document.getElementById("fig6"));
                chart.setOption({
                    tooltip: {},
                    xAxis: {
                        type: 'category',
                        data: classNames
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: scores,
                        type: 'bar',
                        showBackground: true,
                        backgroundStyle: {
                            color: 'rgba(220, 220, 220, 0.8)'
                        }
                    }]
                });

                var disqualify = [];
                for (var key in result.studentPresentRatio) {
                    var val = result.studentPresentRatio[key];
                    if (val < 67) {
                        disqualify.push([key, val]);
                    }
                }
                console.log(disqualify);
                staticTable("取消考试资格名单", "#figContainer", ["姓名", "到课率(%)"], disqualify);
            }
        });
    });


    $(document).on('click', '#match', function () {
        $("#figContainer").empty();

        var obj = {};
        obj["token"] = layui.data('local').token;
        obj["courseId"] = document.getElementById("course").value;
        obj["gradeId"] = $("#gradeId").val();
        $.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            url: "/grade/match",
            data: JSON.stringify(obj),
            success: function (result) {
                console.log(result);
                $("#figContainer").append(oneFig("匹配度分析", "fig1"));
                var fig1 = document.getElementById("fig1");
                var chart = echarts.init(fig1);

                var schema = [
                    {name: '期末考试', index: 0},
                    {name: 'PTA', index: 1},
                    {name: '超星', index: 2},
                    {name: '雨课堂', index: 3},
                ];
                var fieldIndices = schema.reduce(function (obj, item) {
                    obj[item.name] = item.index;
                    return obj;
                }, {});

                var fieldNames = schema.map(function (item) {
                    return item.name;
                });
                var curXAxis = fieldNames[0];
                var curYAxis = fieldNames[1];

                chart.setOption(getOption(result.data, curXAxis, curYAxis));

                function getOption(origin, curXAxis, curYAxis) {
                    var data = origin.map(function (item) {
                        return [item[fieldIndices[curXAxis]], item[fieldIndices[curYAxis]]];
                    });

                    var reg = ecStat.regression('polynomial', data, 2);
                    reg.points.sort(function (a, b) {
                        return a[0] - b[0];
                    });

                    return {
                        tooltip: {},
                        xAxis: {
                            name: curXAxis,
                        },
                        yAxis: {
                            name: curYAxis,
                        },
                        series: [
                            {
                                zlevel: 1,
                                type: 'scatter',
                                data: data,
                                animationThreshold: 5000,
                                progressiveThreshold: 5000
                            },
                            {
                                name: 'line',
                                type: 'line',
                                smooth: true,
                                showSymbol: false,
                                data: reg.points,
                                markPoint: {
                                    itemStyle: {
                                        color: 'transparent'
                                    },
                                    label: {
                                        show: true,
                                        position: 'left',
                                        formatter: reg.expression,
                                        color: '#333',
                                        fontSize: 14
                                    },
                                    data: [{
                                        coord: reg.points[reg.points.length - 1]
                                    }]
                                }
                            }
                        ],
                        animationEasingUpdate: 'cubicInOut',
                        animationDurationUpdate: 2000
                    };
                }

                var gui = new dat.GUI({autoPlace: false});

                var guiText = function () {
                    this.xAxis = fieldNames[0];
                    this.yAxis = fieldNames[1];
                };
                var text = new guiText();
                var xAxisController = gui.add(text, "xAxis", fieldNames);
                var yAxisController = gui.add(text, "yAxis", fieldNames);
                fig1.appendChild(gui.domElement);


                xAxisController.onChange(function (value) {
                    curXAxis = value;
                    chart.setOption(getOption(result.data, curXAxis, curYAxis));
                });

                yAxisController.onChange(function (value) {
                    curYAxis = value;
                    chart.setOption(getOption(result.data, curXAxis, curYAxis));
                });
            }
        });
    });

});


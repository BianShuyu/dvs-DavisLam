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


function twoFig(title1, title2, id1, id2) {
    return "<div class=\"layui-row layui-col-space15\">\n" +
        "            <div class=\"layui-col-md6\">\n" +
        "                <div class=\"layui-card\">\n" +
        "                    <div class=\"layui-card-header\">" + title1 + "</div>\n" +
        "                    <div class=\"layui-card-body\"><div id=\"" + id1 + "\" style=\"width: 100%; height: 500px;\"></div></div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "            <div class=\"layui-col-md6\">\n" +
        "                <div class=\"layui-card\">\n" +
        "                    <div class=\"layui-card-header\">" + title2 + "</div>\n" +
        "                    <div class=\"layui-card-body\"><div id=\"" + id2 + "\" style=\"width: 100%; height: 500px;\"></div></div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>"
}

function oneFig(title, id) {
    return "<div class=\"layui-row layui-col-space15\">\n" +
        "            <div class=\"layui-col-md6\">\n" +
        "                <div class=\"layui-card\">\n" +
        "                    <div class=\"layui-card-header\">" + title + "</div>\n" +
        "                    <div class=\"layui-card-body\"><div id=\"" + id + "\" style=\"width: 100%; height: 500px;\"></div></div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>"
}


function final() {
    $("#figContainer").empty();
    $("#figContainer").append(twoFig("甜甜圈", "堆叠条形图", "fig1", "fig2"));
    $("#figContainer").append(oneFig("箱型图", "fig3"));

    var obj = {};
    obj["token"] = "123";
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

            var data = result.data.boxData;
            data = echarts.dataTool.prepareBoxplotData(data);

            option = {
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
                    data: classes,
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
                    name: '分数',
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
            chart = echarts.init(document.getElementById("fig3"));
            chart.setOption(option);


        }
    });

}

function pta() {

    var obj = {};
    obj["token"] = "123";
    obj["courseId"] = document.getElementById("course").value;
    obj["gradeId"] = $("#gradeId").val();
    $.ajax({
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        url: "/pta/summary",
        data: JSON.stringify(obj),
        success: function (result) {
            $("#figContainer").empty();
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
            var bins = ecStat.histogram(total).data.slice(1);
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
            var chart = echarts.init(document.getElementById("fig1"));
            chart.setOption(option);

            var boxData = [];
            for (var i = 0; i < classes.length; i++) {
                boxData.push(classData[classes[i]]);
            }
            console.log(boxData);
            var data = echarts.dataTool.prepareBoxplotData(boxData);

            option = {
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
                    data: classes,
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
                    name: '分数',
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
            chart = echarts.init(document.getElementById("fig2"));
            chart.setOption(option);


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
                                position: 'left'
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
}

function chaoxing() {

}

function yuketang() {

}
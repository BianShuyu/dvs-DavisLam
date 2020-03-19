layui.use(['element', 'layer', 'form', 'table'], function () {
    var element = layui.element,
        layer = layui.layer,
        form = layui.form,
        table = layui.table,
        $ = layui.jquery;


    var t = {
        elem: '#studentOverview',
        url: '/student/overview',
        method: 'post',
        page: true,
        where: {
            studentId:$("#studentId").val()
        },
        width: $(parent.window).width() / 2,
        cols: [[
            {title: '序号', width: '10%', align: 'left', type: 'numbers'},
            {field: 'name', title: '课程名', align: 'left', width: '70%'},
            {field: 'score', title: '得分', align: 'left', width: '20%'},
        ]]
    };

    table.render(t);

});

function twoFig(title1, title2, id1, id2) {
    return "<div class=\"layui-row layui-col-space15\">\n" +
        "            <div class=\"layui-col-md6\">\n" +
        "                <div class=\"layui-card\">\n" +
        "                    <div class=\"layui-card-header\">" + title1 + "</div>\n" +
        "                    <div class=\"layui-card-body\"><div id=\"" + id1 + "\" style=\"width: 100%; height: 400px;\"></div></div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "            <div class=\"layui-col-md6\">\n" +
        "                <div class=\"layui-card\">\n" +
        "                    <div class=\"layui-card-header\">" + title2 + "</div>\n" +
        "                    <div class=\"layui-card-body\"><div id=\"" + id2 + "\" style=\"width: 100%; height: 400px;\"></div></div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>"
}

function oneFig(title, id) {
    return "<div class=\"layui-row layui-col-space15\">\n" +
        "            <div class=\"layui-col-md6\">\n" +
        "                <div class=\"layui-card\">\n" +
        "                    <div class=\"layui-card-header\">" + title + "</div>\n" +
        "                    <div class=\"layui-card-body\"><div id=\"" + id + "\" style=\"width: 100%; height: 400px;\"></div></div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>"
}

function pta() {
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
        url: "/pta/subtotal",
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


}

function chaoxing() {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '超星',
            type: 2,
            area: ["1000px", "500px"],
            content: 'https://cn.bing.com'
        });
    });
}

function yuketang() {
    layui.use('layer', function () {
        var layer = layui.layer;
        layer.open({
            title: '雨课堂',
            type: 2,
            area: ["1000px", "500px"],
            content: 'https://weibo.com'
        });
    });
}
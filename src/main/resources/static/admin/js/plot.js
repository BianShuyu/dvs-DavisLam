function linePlot() {
    var nData = (arguments.length - 2) / 2;
    var figId = arguments[arguments.length - 1];
    var keys = [];
    for (var key in arguments[0]) {
        keys.push(key);
    }
    var legend = [];
    for (var i = nData; i < nData * 2; i++) {
        legend.push(arguments[i]);
    }
    var seriesData = [];


    for (var i = 0; i < nData; i++) {
        var cur = {};
        cur.name = legend[i];
        cur.type = "line";
        var curData = [];
        for (var key in arguments[i]) {
            curData.push(arguments[i][key]);
        }
        cur.data = curData;
        cur.markPoint = {
            data: [
                {type: 'max'},
                {type: 'min'}
            ]
        };
        cur.markLine = {
            data: [
                {type: 'average', name: '平均值'},
            ]
        };
        seriesData.push(cur);
    }
    var unit = arguments[arguments.length - 2];
    var chart = echarts.init(document.getElementById(figId));
    chart.setOption({
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: legend
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: keys
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: function(value) {
                    return value + " " + unit;
                }
            }
        },
        dataZoom: {   // 这个dataZoom组件，默认控制x轴。
            type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
            start: 30,      // 左边在 10% 的位置。
            end: 50         // 右边在 60% 的位置。
        },
        series: seriesData
    });


}

function twoFig(title1, title2, id1, id2) {
    return "<div class=\"layui-row layui-col-space15\">\n" +
        "       <div class=\"layui-col-md6\">\n" +
        "           <div class=\"layui-card\">\n" +
        "               <div class=\"layui-card-header\">" + title1 + "</div>\n" +
        "               <div class=\"layui-card-body\"><div id=\"" + id1 + "\" style=\"width: 100%; height: 400px;\"></div></div>\n" +
        "           </div>\n" +
        "       </div>\n" +
        "       <div class=\"layui-col-md6\">\n" +
        "           <div class=\"layui-card\">\n" +
        "               <div class=\"layui-card-header\">" + title2 + "</div>\n" +
        "               <div class=\"layui-card-body\"><div id=\"" + id2 + "\" style=\"width: 100%; height: 400px;\"></div></div>\n" +
        "           </div>\n" +
        "       </div>\n" +
        "   </div>"
}

function oneFig(title, id) {
    return "<div class=\"layui-row layui-col-space15\">\n" +
        "       <div class=\"layui-col-md6\">\n" +
        "           <div class=\"layui-card\">\n" +
        "               <div class=\"layui-card-header\">" + title + "</div>\n" +
        "               <div class=\"layui-card-body\"><div id=\"" + id + "\" style=\"width: 100%; height: 400px;\"></div></div>\n" +
        "           </div>\n" +
        "       </div>\n" +
        "   </div>"
}

function staticTable(title, containerId, head, data) {
    $(containerId).append(
        "<fieldset class=\"layui-elem-field layui-field-title\" style=\"margin-top: 20px;\">\n" +
        "  <legend>" + title + "</legend>\n" +
        "</fieldset>");
    var tableHtml =
        "<table class=\"layui-table\">\n" +
        "  <colgroup>\n" +
        "    <col>\n" +
        "    <col>\n" +
        "  </colgroup>\n" +
        "    <thead>\n" +
        "      <tr>\n";
    for (var i = 0; i < head.length; i++) {
        tableHtml += "        <th>" + head[i] + "</th>\n";
    }
    tableHtml +=
        "      </tr>\n" +
        "    </thead>\n" +
        "    <tbody>\n";
    for (var i = 0; i < data.length; i++) {
        tableHtml +=
            "<tr>\n";
        for (var j = 0; j < data[i].length; j++) {
            tableHtml +=
                "  <td>" + data[i][j] + "</td>\n";
        }
        tableHtml +=
            "</tr>"
    }
    tableHtml += "</tbody>\n</table>";
    $("#figContainer").append(tableHtml);
}
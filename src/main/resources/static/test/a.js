layui.data(
    "aaa",{
        key:"username",
        value:"阳菜"
    }
);
var d=layui.data("aaa");
$.post("/test.do",{
    "username":d.username
});
console.log(d.username);
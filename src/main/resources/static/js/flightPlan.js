$(function () {
    $("#input_beginTime, #input_endTime").datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd',
        minView: "month", //设置只显示到月份
        autoclose: true, //选中关闭
        todayBtn: 1
    });
});




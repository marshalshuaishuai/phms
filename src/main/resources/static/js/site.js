function showCities(parentId) {
    $.ajax({
        url: "/getCities",
        data: {parentId: parentId},
        success: function (data) {
            $("#city").html(data);
        }
    });
}

// $('#datetimepicker input').each(function () {
//     $(this).datepicker({
//         language: 'zh-CN', //语言
//         autoclose: true, //选择后自动关闭
//         clearBtn: true,//清除按钮
//         format: "yyyy/mm/dd"//日期格式
//     });
// });

$('#datetimepicker').datepicker({
    language: 'zh-CN', //语言
    autoclose: true, //选择后自动关闭
    clearBtn: true,//清除按钮
    format: "yyyy/mm/dd"//日期格式
});
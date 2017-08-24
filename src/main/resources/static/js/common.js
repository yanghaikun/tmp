/**
 * Created by yuelulan on 2017/6/2.
 */
/**
 * 全局插件和工具定义,通过Common调用避免污染全局变量.  ps:暂时这样写,以后引入模块化方式以后修改
 * @type {{versions: {trident, presto, webKit, gecko, mobile, ios, android, iPhone, iPad, webApp}, language: string}}
 */
/*对Date的扩展，方便解析时间 使用示例  new Date(row.createTime).format("yyyy-MM-dd") */
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
/*对Date的扩展结束*/

var Common = {}; //这样做是为了尽量避免全局变量冲突
Common.browser = {
    versions: function() {
        var u = navigator.userAgent,
            app = navigator.appVersion;
        return { //移动终端浏览器版本信息
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
            iPhone: u.indexOf('iPhone') > -1 || (u.indexOf('Mac') > -1 && u.indexOf('qq') > -1), //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
    }(),
    language: (navigator.browserLanguage || navigator.language).toLowerCase()
};

Common.arrayToText = function(array, name, spilt, slice) {
    var text = "";
    for (var i = 0; i < array.length; i++) {
        var Node = array[i];
        if (!name) {
            text += Node;
        } else if (slice) {
            text += Node[name].split(slice)[0];;
        } else {
            text += Node[name];
        }
        if (i != array.length - 1) {
            text += spilt
        }
    }
    return text;
};

Common.lang = {
    "sProcessing": "加载中...",
    "sLengthMenu": "每页 _MENU_ 项",
    "sZeroRecords": "没有匹配结果",
    "sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
    "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix": "",
    "sSearch": "搜索:",
    "sUrl": "",
    "sEmptyTable": "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands": ",",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上页",
        "sNext": "下页",
        "sLast": "末页",
        "sJump": "跳转"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
};

/**
 * targerDomId, myajaxurl, myparam, mycolumns, mycolumnDefs, mypageSetting
 * Get分页查询插件简单封装定义,便于使用.
 */
Common.createPageTable = function(targerDomId, myajaxurl, myparam, mycolumns, mycolumnDefs, mypageSetting) {

    if (myajaxurl == undefined || targerDomId == undefined || mycolumns == null) { //TODO: 待补充数据格式校验
        //
        layer.msg("初始化createPageTable对象失败,参数设置错误.");
        return null;
    }

    this.param = {}; //需要是json格式数据
    this.serverSource = myajaxurl;

    //初始化表格
    this.table = null;

    this.init = function() {
        if (this.table != null) {
            return this.table;
        }
        if (myparam != null) { //初始化参数
            $.extend(this.param, myparam);
        }
        var thisSelf = this;
        this.table =
            $("#" + targerDomId).dataTable({
                language: Common.lang, //提示信息
                autoWidth: false, //禁用自动调整列宽
                stripeClasses: ["odd", "even"], //为奇偶行加上样式，兼容不支持CSS伪类的场合
                processing: true, //隐藏加载提示,自行处理
                serverSide: true, //启用服务器端分页
                searching: false, //禁用原生搜索
                orderMulti: false, //启用多列排序
                retrieve: true,
                bRetrieve: true,
                renderer: "bootstrap", //渲染样式：Bootstrap和jquery-ui
                pagingType: "simple_numbers", //分页样式：simple,simple_numbers,full,full_numbers
                ordering: false,

                ajax: function(data, callback, settings) {
                    //封装请求参数
                    thisSelf.param.pageSize = data.length; //页面显示记录条数，在页面显示每页显示多少项的时候
                    thisSelf.param.start = data.start; //开始的记录序号
                    thisSelf.param.page = (data.start / data.length) + 1; //当前页码
                    //ajax请求数据
                    $.ajax({
                        type: "post",
                        url: thisSelf.serverSource,
                        cache: false, //禁用缓存
                        data: thisSelf.param, //传入组装的参数
                        dataType: "json",
                        traditional: true,
                        success: function(result) {
                            //封装返回数据
                            var returnData = {};
                            debugger
                            if (result.succeed) {
                                returnData.draw = data.draw; //这里直接自行返回了draw计数器,应该由后台返回
                                returnData.recordsTotal = result.totalRow; //返回数据全部记录
                                returnData.recordsFiltered = result.totalRow; //后台不实现过滤功能，每次查询均视作全部结果
                                if (result.data != null) {
                                    returnData.data = result.data; //返回的数据列表
                                } else {
                                    returnData.data = [];
                                }
                                //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                                //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                                callback(returnData);
                            } else {
                                layer.msg('查询失败.');
                                returnData.draw = data.draw;
                                returnData.recordsTotal = [];
                                returnData.recordsFiltered = [];
                                returnData.data = [];
                                callback(returnData);
                            }
                        }/*,
                         error: function() {
                         layer.msg('服务器出错或正忙,查询请求失败.');
                         }*/
                    });
                },
                //列表表头字段
                columns: mycolumns,
                columnDefs: mycolumnDefs,
            }).api();
        //此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象
    }
};
/**
 * 验证空
 */
Common.isBlank = function(str) {
    if (str == null || str == "") {
        return true;
    }
    if (str.replace(/(^s*)|(s*$)/g, "").length == 0) {
        return true;
    }
}

/**
 * 格式化字符串
 * @param args
 * @returns {String}
 */
String.prototype.format = function(args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if(args[key]!=undefined){
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    var reg= new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
}
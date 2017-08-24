$(function() {
    createFlightDataTable();
    createFlightPlanDataTable();
});

var flightTableComponent;
var flightPlanTableComponent;
function createFlightDataTable() {
    flightTableComponent = new Common.createPageTable("flightDataTable",
        serverPath + "/flight/list.do",
        null,
        [
            {"data": "id", defaultContent:""},
            {"data": "beginTime", defaultContent:""},
            {"data": "flightNumber", defaultContent:""},
            {"data": "airplane", defaultContent:""},
            {"data": "originCity", defaultContent:""},
            {"data": "destinationCity", defaultContent:""},
            {"data": "departureTime", defaultContent:""},
            {"data": "arrivalTime", defaultContent:""}
        ],
        [
            {   orderable: false,
                targets: 0
            },
            {
                orderable: false,
                targets: 1
            },
            { 	orderable: false,
                targets: 2
            },
            { 	orderable: false,
                targets: 3
            },
            {
                orderable: false,
                targets: 4
            },
            {
                orderable: false,
                targets: 5
            },
            {
                orderable: false,
                targets: 6
            },
            {
                orderable: false,
                targets: 7
            },
            {
                orderable: false,
                targets: 8,
                render: function(td, cellData, rowData) {
                    return '<div class="btn-group">' +
                        '<button id="editBtn" class="btn btn-info" onclick="editEntity(' + rowData.id +  ');">修改</button>' +
                        '<button id="deleteBtn" class="btn btn-danger" onclick="deleteEntity(' + rowData.id +  ');">删除</button>' +
                        '</div>';
                }
            }
        ]
    );
    flightTableComponent.init();
}

function createFlightPlanDataTable() {
    flightPlanTableComponent = new Common.createPageTable("flightPlanDataTable",
        serverPath + "/flightPlan/list.do",
        null,
        [
            {"data": "id", defaultContent:""},
            {"data": "beginTime", defaultContent:""},
            {"data": "endTime", defaultContent:""},
            {"data": "schedule", defaultContent:""},
            {"data": "flightNumber", defaultContent:""},
            {"data": "airplane", defaultContent:""},
            {"data": "originCity", defaultContent:""},
            {"data": "destinationCity", defaultContent:""},
            {"data": "departureTime", defaultContent:""},
            {"data": "arrivalTime", defaultContent:""}
        ],
        [
            {   orderable: false,
                targets: 0,
                render: function(td, cellData, rowData) {
                    return '<input type="checkbox" id="' + rowData.id + '">';
                }
            },
            {
                orderable: false,
                targets: 1
            },
            { 	orderable: false,
                targets: 2
            },
            { 	orderable: false,
                targets: 3
            },
            {
                orderable: false,
                targets: 4
            },
            {
                orderable: false,
                targets: 5
            },
            {
                orderable: false,
                targets: 6
            },
            {
                orderable: false,
                targets: 7
            },
            {
                orderable: false,
                targets: 8
            },
            {
                orderable: false,
                targets: 9
            }
        ]
    );
    flightPlanTableComponent.init();
}

//添加数据
$("#queryBtn").on( 'click', function() {
    flightTableComponent.param.name = $("#fieldName").val();
    flightTableComponent.table.page(0);
    flightTableComponent.table.draw(false);
});

//添加航线计划
$("#addFlightPlanBtn").on( 'click', function() {
    $("#flightPlanInfo").load(serverPath+"/flightPlan/editUI");
    $("#flightPlanModal").modal('show');
});

//查看所有航线
$("#queryFlightPlanBtn").on( 'click', function() {
    flightTableComponent.table.page(0);
    flightPlanTableComponent.table.draw(false);
    $("#flightPlanPublishModal").modal('show');
});

/**
 * 新增航线计划
 * @returns {boolean}
 */
$("#saveFlightPlanBtn").on( 'click', function() {
    /*var name = $("#input_name").val();
    var comment = $("#input_comment").val();
    var propertyName = $("#input_propertyName").val();
    var columnName = $("#input_columnName").val();
    var columnType = $("#input_columnType").val();

    if(Common.isBlank(name)){
        layer.alert("字段名称不能为空！");
        return false;
    }
    if(Common.isBlank(comment)){
        layer.alert("字段描述不能为空！");
        return false;
    }
    if(Common.isBlank(propertyName)){
        layer.alert("字段变量名不能为空！");
        return false;
    }
    if(Common.isBlank(columnName)){
        layer.alert("字段在数据库中列名不能为空！");
        return false;
    }
    if(Common.isBlank(columnType)){
        layer.alert("字段在数据库中类型不能为空！");
        return false;
    }*/
    //设置下班期
    var s = "";
    $("#flightPlanForm input[type=checkbox]").each(function () {
        if($(this).is(":checked")){
            s += $(this).attr("value") + ",";
        }
    })

    if(Common.isBlank(s)) {
        layer.alert("请选择班期！");
        return false;
    }
    //去掉最后的逗号
    s = s.substr(0, s.length - 1)

    $("#input_schedule").val(s);

    var data = $("#flightPlanForm").serialize();
    $.ajax({
        url:serverPath+"/flightPlan/save.do",
        type:"POST",
        dataType:"json",
        data:data,
        success: function(ret){
            debugger
            if(ret.succeed){
                layer.alert(ret.msg);
                $("#flightPlanModal").modal('hide');
                flightTableComponent.table.draw(false);
            }else {
                layer.alert(ret.msg);
            }
        }
    });
});

/**
 * 发布通知单
 */
$("#flightPlanPublishBtn").on( 'click', function() {
    //获取所有勾选的通知单
    var data = new Array();
    $("#flightPlanDataTable input[type=checkbox]").each(function () {
        if($(this).is(":checked")){
           data.push($(this).attr("id"));
        }
    });

    var d = {"data": JSON.stringify(data)};

    $.ajax({
        url: serverPath+"/flightPlan/publish",
        type: "POST",
        dataType: "json",
        data: d,
        success: function(ret){
            if(ret.succeed){
                layer.alert(ret.msg);
                $("#flightPlanPublishModal").modal('hide');
            }else {
                layer.alert(ret.msg);
            }
        },
        error:function(ret){

        }
    });
});

function deleteEntity(id) {
    layer.confirm('确定删除？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        $.ajax({
            url: serverPath+"/sys/config/archiveField/delete",
            type: "POST",
            dataType: "json",
            data: {id:id},
            success: function(ret){
                if(ret.succeed){
                    layer.alert(ret.msg);
                    flightTableComponent.table.draw(false);
                }else {
                    layer.alert(ret.msg);
                }
            },
            error:function(ret){

            }
        });
    }, function(){
        return ;
    });
}


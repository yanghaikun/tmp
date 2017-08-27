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
            {"data": "date", defaultContent:""},
            {"data": "flightNumber", defaultContent:""},
            {"data": "airplane", defaultContent:""},
            {"data": "originCity", defaultContent:""},
            {"data": "destinationCity", defaultContent:""},
            {"data": "departureTime", defaultContent:""},
            {"data": "arrivalTime", defaultContent:""},
        ],
        [
            {   orderable: false,
                targets: 0
            },
            {
                orderable: false,
                targets: 1,
                 render: function(data) {
                    return new Date(data).format("yyyy-MM-dd");
                 }
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
                        '<button id="editBtn" class="btn btn-info" onclick="editTimestamp(' + rowData.planId +  ');">修改时刻</button>' +
                         '<button id="editBtn" class="btn btn-info" onclick="editAirplane(' + rowData.planId +  ');">修改运力</button>' +
                         ((rowData.canceled) ?  '<button id="editBtn" class="btn btn-info" onclick="recoverFlight(' + rowData.planId +  ');">恢复</button>' : '<button id="editBtn" class="btn btn-info" onclick="cancelFlight(' + rowData.planId +  ');">取消</button>' )+
                        '<button id="deleteBtn" class="btn btn-danger" onclick="deleteFlight(' + rowData.planId +  ');">删除</button>' +
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
            {"data": "startDate", defaultContent:""},
            {"data": "endDate", defaultContent:""},
            {"data": "schedule", defaultContent:""},
            {"data": "flightNumber", defaultContent:""},
            {"data": "airplane", defaultContent:""},
            {"data": "originCity", defaultContent:""},
            {"data": "destinationCity", defaultContent:""},
            {"data": "departureTime", defaultContent:""},
            {"data": "arrivalTime", defaultContent:""},
            {"data": "description", defaultContent:""}
        ],
        [
            {   orderable: false,
                targets: 0,
                render: function(td, cellData, rowData) {
                if(rowData != undefined) {
                    return '<input type="checkbox" id="' + rowData.id + '">' + rowData.id;
                }
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
            },
            {
                orderable: false,
                targets: 10,
                 render: function(data) {
                    return '<font color="red">' + data + '</font>';
                 }
            },
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
    $("#flightPlanInfo").load(serverPath+"/flightPlan/addUI");
    $("#flightPlanModal").modal('show');
    $("#input_operation").val("ADD");
});

//查看所有航线
$("#queryFlightPlanBtn").on( 'click', function() {
    flightTableComponent.table.page(0);
    flightPlanTableComponent.table.draw(false);
    $("#flightPlanPublishModal").modal('show');
});

/**
 * 新增或修改航线计划
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
    var s = new Array();
    $("#flightPlanForm input[type=checkbox]").each(function () {
        if($(this).is(":checked")){
            s.push(parseInt($(this).attr("value")));
        }
    })

    if(s.length == 0) {
        layer.alert("请选择班期！");
        return false;
    }
    $("#input_schedule").val(JSON.stringify(s));


    //根据不同的操作发送不同的请求
    var operation = $("#input_operation").val();
    var url = "/flightPlan/update"
    var id = $("#input_id").val();
    var data = $("#flightPlanForm").serialize();
    //修改时刻
    if("ADD" == operation) {
        url = "/flightPlan/add";
    }
    //修改时刻
    if("EDIT_TIMESTAMP" == operation) {
        url = "/flightPlan/update";
    }
    //修改飞机
    if("EDIT_AIRPLANE" == operation) {
        url = "/flightPlan/update";
    }
    //取消航班
    if("CANCEL" == operation) {
        url = "/flightPlan/cancel";
    }
    //恢复航班
    if("RECOVER" == operation) {
        url = "/flightPlan/recover";
    }
    //删除航班
    if("DELETE" == operation) {
        url = "/flightPlan/delete";
    }

    $.ajax({
        url:serverPath + url,
        type:"POST",
        dataType:"json",
        data:data,
        success: function(ret){
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
                //刷新数据
                flightTableComponent.table.draw(false);
            }else {
                layer.alert(ret.msg);
            }
        },
        error:function(ret){

        }
    });
});

function editTimestamp(id){
    $("#flightPlanInfo").load(serverPath+"/flightPlan/updateUI", {"id" : id});
    $("#flightPlanModal").modal('show');
    $("#input_operation").val("EDIT_TIMESTAMP");
}

function editAirplane(id){
    $("#flightPlanInfo").load(serverPath+"/flightPlan/updateUI", {"id" : id});
    $("#flightPlanModal").modal('show');
    $("#input_operation").val("EDIT_AIRPLANE");
}

function recoverFlight(id){
    $("#flightPlanInfo").load(serverPath+"/flightPlan/updateUI", {"id" : id});
    $("#flightPlanModal").modal('show');
    $("#input_operation").val("RECOVER");
}

function cancelFlight(id){
    $("#flightPlanInfo").load(serverPath+"/flightPlan/updateUI", {"id" : id});
    $("#flightPlanModal").modal('show');
    $("#input_operation").val("CANCEL");
}

function deleteFlight(id){
    $("#flightPlanInfo").load(serverPath+"/flightPlan/updateUI", {"id" : id});
    $("#flightPlanModal").modal('show');
    $("#input_operation").val("DELETE");
}



function updateFlight(url, data) {
    layer.confirm('确定删除？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        $.ajax({
            url: serverPath + url,
            type: "POST",
            dataType: "json",
            data: data,
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


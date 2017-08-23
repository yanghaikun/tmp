$(function() {
    createDataTable();
});

var tableComponent;
function createDataTable() {
    tableComponent = new Common.createPageTable("dataTable",
        serverPath + "/sys/config/archiveField/list.do",
        null,
        [
            {"data": "name", defaultContent:""},
            {"data": "comment", defaultContent:""},
            {"data": "propertyName", defaultContent:""},
            {"data": "columnName", defaultContent:""},
            {"data": "columnType", defaultContent:""}
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
                targets: 5,
                render: function(td, cellData, rowData) {
                    return '<div class="btn-group">' +
                        '<button id="editBtn" class="btn btn-info" onclick="editEntity(' + rowData.id +  ');">修改</button>' +
                        '<button id="deleteBtn" class="btn btn-danger" onclick="deleteEntity(' + rowData.id +  ');">删除</button>' +
                        '</div>';
                }
            }
        ]
    );
    tableComponent.init();
}

//添加数据
$("#queryBtn").on( 'click', function() {
    tableComponent.param.name = $("#fieldName").val();
    tableComponent.table.page(0);
    tableComponent.table.draw(false);
});

//添加数据
$("#addBtn").on( 'click', function() {
    editEntity("");
});

function editEntity(id){
    $("#entityInfo").load(serverPath+"/sys/config/archiveField/edit",{id:id});
    $("#entityModal").modal('show');
}

/**
 * 提交插入或者修改
 * @returns {boolean}
 */
function submit() {
    var name = $("#input_name").val();
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
    }
    var data = $("#entityForm").serialize();
    $.ajax({
        url:serverPath+"/sys/config/archiveField/insertOrUpdate.do",
        type:"POST",
        dataType:"json",
        data:data,
        success: function(ret){
            debugger
            if(ret.succeed){
                layer.alert(ret.msg);
                $("#entityModal").modal('hide');
                tableComponent.table.draw(false);
            }else {
                layer.alert(ret.msg);
            }
        }
    });
}

function deleteEntity(id) {
    layer.confirm('确定删除？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        $.ajax({
            url:serverPath+"/sys/config/archiveField/delete",
            type:"POST",
            dataType:"json",
            data:{id:id},
            success: function(ret){
                if(ret.succeed){
                    layer.alert(ret.msg);
                    tableComponent.table.draw(false);
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


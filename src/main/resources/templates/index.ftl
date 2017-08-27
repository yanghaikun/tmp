<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>瑞丽航空档案管理系统</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="shortcut icon" href="${(serverPath)!}/static/css/images/favicon.ico"/>


    <!-- Theme style -->
    <link rel="stylesheet" href="/adminLTE/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <!--<link rel="stylesheet" href="dist/css/skins/_all-skins.min.css"> -->
    <link rel="stylesheet" href="/adminLTE/css/skins/skin-blue.min.css">


    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script>
        var serverPath = "${(serverPath)}";
    </script>

</head>
<body>
<div>


    <!-- Content Wrapper. Contains page content -->
    <div id="mainContent">


        <section class="content" xmlns="http://www.w3.org/1999/html">
            <div class="row">
            <#--  <div class="col-md-1">
              </div>-->
                <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <!-- Horizontal Form -->
                    <div class=" box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">航线计划（通知单）</h3>
                        </div>
                        <!-- /.box-header -->

                        <div class="box-body">
                            <div class="row">
                                <div class="col-lg-1 col-md-2 col-sm-4">
                                    <button id="addFlightPlanBtn" class="btn btn-info">新增航班</button>
                                </div>

                                <div class="col-lg-1 col-md-2 col-sm-4">
                                    <button id="queryFlightPlanBtn" class="btn btn-info">查看通知单</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /.box -->
                </div>
            </div><!-- /.row -->

            <div class="row">
                <div class="col-xs-12">
                    <div class="box-header with-border">
                        <h3 class="box-title">航班动态</h3>
                    </div>
                        <div class="box-body">
                            <table id="flightDataTable" class="display" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>日期</th>
                                    <th>航班号</th>
                                    <th>运力</th>
                                    <th>起飞站</th>
                                    <th>落地站</th>
                                    <th>起飞时刻</th>
                                    <th>落地时刻</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </section>

        <!-- Modal -->
        <input id="input_operation" name="operation" type="hidden"/>
        <div class="modal fade" id="flightPlanModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content" style="margin-top: 10%;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">新增航线计划</h4>
                    </div>
                    <div class="modal-body" id="flightPlanInfo">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
                        <button id = "saveFlightPlanBtn"  type="button" class="btn btn-info">确定</button>
                    </div>
                </div>
            </div>
        </div>

    <div class="modal fade" id="flightPlanPublishModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content" style="margin-top: 5%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">航线计划</h4>
                </div>
                <div class="modal-body" id="flightPlanPublishInfo">
                    <div class="box-body">
                        <table id="flightPlanDataTable" class="display" width="100%" cellspacing="0">
                            <thead>
                            <tr>
                                <th>勾选</th>
                                <th>开始日期</th>
                                <th>结束日期</th>
                                <th>班期</th>
                                <th>航班号</th>
                                <th>运力</th>
                                <th>起飞站</th>
                                <th>落地站</th>
                                <th>起飞时刻</th>
                                <th>落地时刻</th>
                                <th>变更</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
                    <button id="flightPlanPublishBtn" type="button" class="btn btn-info">发布</button>
                </div>
            </div>
        </div>
    </div>

    </div>
    <!-- /.content-wrapper -->


    <footer class="main-footer" style="text-align: center;">
        <strong>瑞丽航空版权所有 Ruili Airlines Copyright © <span id="year"></span> 滇ICP备14000625号<a
                href="https://www.rlair.net" target="_blank">www.rlair.net</a>
    </footer>

    <!-- /.control-sidebar -->
    <!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->

<!-- jQuery 3 -->
<script src="/jquery/3.2.1/jquery.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<script src="/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- Bootstrap 3.3.7 -->
<link rel="stylesheet" href="bootstrap/3.3.7/css/bootstrap.min.css">

<!-- AdminLTE App -->
<script src="/adminLTE/js/adminlte.min.js"></script>

<!--dataTable-->
<script src="/dataTables/js/jquery.dataTables.min.js"></script>
<!-- DataTables -->
<link rel="stylesheet" href="/dataTables/css/jquery.dataTables.min.css">

<!-- layer独立版 3.0.0 -->
<script src="/layer/layer.js"></script>
<!-- layer css 3.-->
<link rel="stylesheet" href="/layer/skin/default/layer.css">

<link rel="stylesheet" href="/datetimepicker/bootstrap-datetimepicker.min.css">
<script src="/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script src="/datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js"></script>

<!-- Font Awesome -->
<link rel="stylesheet" href="/font-awesome/css/font-awesome.min.css">


<script src="/js/common.js"></script>
<script src="/js/flight.js"></script>

<script>
</script>
</body>
</html>
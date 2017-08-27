<div xmlns="http://www.w3.org/1999/html" style="padding: 25px">
    <form id="flightPlanForm">

    <#if entity??>
        <input id="input_id" name="id" type="hidden" value="${entity.id?string("#")}"/>
    </#if>
        <div class="row">
            <div class="col-xs-6">
                <div class="input-group date">
                    <div class="input-group-addon">
                        <i class="fa fa-calendar"></i>
                    </div>
                    <input type="text" class="form-control pull-right" id="input_startDate" name="startDate"
                           placeholder="开始日期" <#if entity??>value="${entity.startDate?string("yyyy-MM-dd")}" </#if>>
                </div>
            </div>
            <div class="col-xs-6">
                <div class="input-group date">
                    <div class="input-group-addon">
                        <i class="fa fa-calendar"></i>
                    </div>
                    <input type="text" class="form-control pull-right" id="input_endDate" name="endDate"
                           placeholder="结束日期" <#if entity??>value="${entity.endDate?string("yyyy-MM-dd")}" </#if>>
                </div>
        </div>
        </div>
        <div class="row" style="margin-top: 15px;">
            <div class="col-xs-3">
                <input type="text" id="input_schedule" name="schedule" class="form-control" placeholder="班期"
                       <#if entity??>value="${entity.schedule}" </#if>>
            </div>
            <div class="col-xs-9">
                <input type="checkbox" id="1" value="1">一
                <input type="checkbox" id="2" value="2">二
                <input type="checkbox" id="3" value="3">三
                <input type="checkbox" id="4" value="4">四
                <input type="checkbox" id="5" value="5">五
                <input type="checkbox" id="6" value="6">六
                <input type="checkbox" id="7" value="7">日
            </div>
        </div>
        <div class="row" style="margin-top: 15px;">
            <div class="col-xs-6">
                <input type="text" id="input_airplane" name="airplane" class="form-control" placeholder="运力"
                       <#if entity??>value="${entity.airplane}" </#if>>
            </div>
            <div class="col-xs-6">
                <input type="text" id="input_flightNumber" name="flightNumber" class="form-control" placeholder="航班号"
                       <#if entity??>value="${entity.flightNumber}" </#if>>
            </div>
        </div>
        <div class="row" style="margin-top: 15px;">
            <div class="col-xs-6">
                <input type="text" id="input_originCity" name="originCity" class="form-control" placeholder="起飞站"
                       <#if entity??>value="${entity.originCity}" </#if>>
            </div>
            <div class="col-xs-6">
                <input type="text" id="input_destinationCity" name="destinationCity" class="form-control"
                       placeholder="落地站" <#if entity??>value="${entity.destinationCity}" </#if>>
            </div>
        </div>
        <div class="row" style="margin-top: 15px;">
            <div class="col-xs-6">
                <input type="text" id="input_departureTime" name="departureTime" class="form-control" placeholder="起飞时刻"
                       <#if entity??>value="${entity.departureTime}" </#if>>
            </div>
            <div class="col-xs-6">
                <input type="text" id="input_arrivalTime" name="arrivalTime" class="form-control" placeholder="落地时刻"
                       <#if entity??>value="${entity.arrivalTime}" </#if>>
            </div>
        </div>
    </form>
</div>

<script src="/js/flightPlan.js"></script>

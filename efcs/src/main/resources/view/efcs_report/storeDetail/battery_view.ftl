<#include "/begin.ftl">
<#assign style>
<link href="${contextPath}/core/style/index.css" rel="stylesheet">
<style>
    .timeline-item .date i.timeline-icon {
        width: 40px;
        line-height: 20px;
        color: #fff;
        font-size: 12px;
        font-weight: bold;
    }

    .timeline-item .date i.timeline-icon-BND {
        background-color: #28b294;
    }

    .timeline-item .date i.timeline-icon-HT {
        background-color: #2a88c4;
    }

    .timeline-item .date i.timeline-icon-FM {
        background-color: #32c5c7;
    }

    .timeline-item .date i.timeline-icon-SRT {
        background-color: #f7ab61;
    }

    .timeline-item .date i.timeline-icon-ST1 {
        background-color: #32c5c7;
    }

    .timeline-item .date i.timeline-icon-ocv1 {
        background-color: #28b294;
    }
</style>
</#assign>

<#assign content>
<div class="ibox-content inspinia-timeline">

    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="timeline-icon timeline-icon-BND">BND</i>
            </div>
            <div class="col-xs-7 content no-top-border">
                <p class="m-b-xs"><strong>Meeting</strong></p>

                <p>Conference on the sales results for the previous year. Monica please examine sales trends in
                    marketing and products. Below please find the current status of the
                    sale.</p>
            </div>
        </div>
    </div>
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="timeline-icon timeline-icon-HT">HT</i>
            </div>
            <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Send documents to Mike</strong></p>
                <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the
                    industry's standard dummy text ever since.</p>
            </div>
        </div>
    </div>
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="timeline-icon timeline-icon-FM">FM</i>
            </div>
            <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Coffee Break</strong></p>
                <p>
                    Go to shop and find some products.
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the
                    industry's.
                </p>
                <div class="time">2017-03-08 12:20:20</div>
            </div>
        </div>
    </div>
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="timeline-icon timeline-icon-SRT">SRT</i>
            </div>
            <div class="col-xs-7 content">
                <p class="m-b-xs">
                    <strong>Phone with Jeronimo</strong>
                </p>
                <p>111</p>
            </div>
        </div>
        <div class="timeline-item">
            <div class="row">
                <div class="col-xs-3 date">
                    <i class="timeline-icon timeline-icon-ST1">ST1</i>
                </div>
                <div class="col-xs-7 content">
                    <p class="m-b-xs"><strong>Go to the doctor dr Smith</strong></p>
                    <p>
                        Find some issue and go to doctor.
                    </p>
                </div>
            </div>
        </div>
        <div class="timeline-item">
            <div class="row">
                <div class="col-xs-3 date">
                    <i class="timeline-icon timeline-icon-ocv1">ocv1</i>
                </div>
                <div class="col-xs-7 content">
                    <p class="m-b-xs"><strong>Chat with Monica and Sandra</strong></p>
                    <p>
                        Web sites still in their infancy. Various versions have evolved over the years, sometimes by
                        accident, sometimes on purpose (injected humour and the like).
                    </p>
                </div>
            </div>
        </div>
    </div>
</#assign>

<#assign script>
</#assign>

<#include "/end.ftl">
<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/project/configure/js/configure.js"></script>
<div id="cc" class="easyui-layout" data-options="fit:true">
     <div data-options="region:'west',split:true,collapsed:true,
             hideExpandTool: true,
             expandMode: null,
             hideCollapsedContent: false,
             collapsedSize: 68,
             collapsedContent: function(){
                 return $('#titlebar');
             }
             " title="West" style="width:100px;"></div>
     <div data-options="region:'center'">
         <table id="configure_grid"></table>
     </div>
 </div>
 <div id="titlebar" style="padding:2px">
     <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100%" data-options="iconCls:'icon-large-setAdd',size:'large',iconAlign:'top'" onclick="addConfigure()">添加</a>
     <!-- <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100%" data-options="iconCls:'icon-large-shapes',size:'large',iconAlign:'top'">Shapes</a>
     <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100%" data-options="iconCls:'icon-large-smartart',size:'large',iconAlign:'top'">SmartArt</a>
     <a href="javascript:void(0)" class="easyui-linkbutton" style="width:100%" data-options="iconCls:'icon-large-chart',size:'large',iconAlign:'top'">Chart</a> -->
 </div>
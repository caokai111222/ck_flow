
<h4 align="center">基于若依开发的简易自定义流程+表单系统</h4>
<p align="center">
    <a href="https://gitee.com/y_project/RuoYi-Vue/stargazers"><img src="https://gitee.com/y_project/RuoYi-Vue/badge/star.svg?theme=dark"></a>
    <a href="https://gitee.com/y_project/RuoYi-Vue"><img src="https://img.shields.io/badge/RuoYi-v3.8.2-brightgreen.svg"></a>
    <a href="https://gitee.com/y_project/RuoYi-Vue/blob/master/LICENSE"><img src="https://img.shields.io/github/license/mashape/apistatus.svg"></a>
</p>

## 预览地址
<a href="http://pmxiaoxiao.top:8088">http://pmxiaoxiao.top:8088 </a>
- 帐密：flow/123456
## 平台简介

1. 本系统基于若依开发，若依介绍：  <a href="http://ruoyi.vip/">若依官网</a>。
2. 前端拖拽流程图基于<a href="http://logic-flow.org/">LogicFlow</a>实现。
3. 拖拽表单在<a href="http://qifeng.321zou.com/">简搭云可视化表单</a>的基础上进行了一些简单的修改，可以直接嵌入流程。
4. 本系统只是个人学习开发，并没有完全做完，只能说实现了自定义拖拽流程+表单的基本思路，可以非常粗糙的实现流程流转，如要使用需要针对我的开发基础进行后续的补全。比如流程管理，个人时间和精力问题，保存的地方没有做校验，人工节点和开始节点需要做表单关联，不然会出错，需要注意每个节点都配置下。
5. 本人：多年小项目经验的crud boy，技术不咋地，写的挺糟糕还望莫笑。
6. 流程没考虑过bmpn协议之类的。。。纯粹就是瞎搞玩玩。。mysql数据库基于8.0版本，改5.7的话个别json格式的字段改一下
## 功能介绍

1. 流程配置：可以实现简单的拖拽流程配置。
2. 表单管理：可以实现一些简单表单控件的拖拽配置。
3. 测试流程启动：针对配置好的流程可以直接发起一个新流程实例。
4. 流程节点查看：查看进行中未完结的流程的所有任务以及任务指定的操作人（为了方便测试，没有限制登录人权限，登录人可以看到和流转所有人的任务，后续如果拿我的思路进行开发，需要加上登录人id的限制）

## 演示图

<table>
    <tr>
        <td><img src="http://124.223.200.167:8888/down/tOoA2arKPJxe"/></td>
        <td><img src="http://124.223.200.167:8888/down/4qMOAyWTxSaD"/></td>
        <td><img src="http://124.223.200.167:8888/down/YSExOLSgHIGE"/></td>
        <td><img src="http://124.223.200.167:8888/down/1C2cWwGQD2xd"/></td>
    </tr>

</table>

## 设计详解
1. 流程
- 1.1 表结构 
<table>
<thead><th>表名</th><th>作用</th><th>备注</th></thead>
<tbody>
 <tr>
        <td>flow_set_main</td>
        <td>流程主表</td>
        <td>用于保存流程设置的基本信息</td>
    </tr>
 <tr>
        <td>flow_set_module</td>
        <td>流程节点表(模板节点表)</td>
        <td>保存每一个流程节点的信息</td>
    </tr>
 <tr>
        <td>flow_set_condition</td>
        <td>流程连线表(模板走向表)</td>
        <td>用于保存节点之间连线的信息，主要用于判定每一个流程节点之间触发条件的</td>
    </tr>
 <tr>
        <td>flow_work_order</td>
        <td>流程实例主表</td>
        <td>保存流程实例基本信息</td>
    </tr>
 <tr>
        <td>flow_nodes</td>
        <td>流程实例节点表</td>
        <td>关联流程节点表的id，作为每一个流程节点在对应流程流程实例对应的节点实例</td>
    </tr>
 <tr>
        <td>flow_user_task</td>
        <td>用户任务表</td>
        <td>挂载在flow_nodes下面，每一个节点实例关联的用户任务的表</td>
    </tr>
</tbody>
</table>
- 1.2 设计思路：
思路非常简单。日常简单的流程需求，就是一个个节点+状态机实现。所以我的思路是由模板节点表+模板走向表实现节点设置和走向设置。

具体来说，由模板节点表提供一个流程所需的所有节点，
流程流转的时候，去走向表里面获取源节点是对应节点的所有走向，
然后判断走向表的进入条件是否满足流程流转输入的条件（目前只支持传入条件=设置条件，可扩展），
如果满足，就找到这个走向的目标节点，根据目标节点的信息，新增实例节点吗，并且根据模板的设置查找到对应关联的用户，新增用户任务。完成流程流转。


2. 表单
- 2.1 表结构
<table>
<thead><th>表名</th><th>作用</th><th>备注</th></thead>
<tbody>
 <tr>
        <td>form_main</td>
        <td>表单主表</td>
        <td>用于保存表单设置的基本信息</td>
    </tr>
 <tr>
        <td>form_item</td>
        <td>表单内容表</td>
        <td>保存表单的每一个item，用pid关联包含信息</td>
    </tr>
 <tr>
        <td>flow_form</td>
        <td>表单数据表</td>
        <td>用于保存表单在每一个流程实例里面提交的数据</td>
    </tr>
</tbody>
</table>
- 1.2 设计思路：
表单这边没什么特别的设计思路，把页面拖拽的数据保存在数据库里面读写就行
/*
package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.constant.FlowConstant;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.dto.FlowInfoDto;
import com.ruoyi.workflow.enums.FlowExceptionEnum;
import com.ruoyi.workflow.exception.FlowException;
import com.ruoyi.workflow.service.IFlowService;
import com.ruoyi.workflow.domain.FlowNodes;
import com.ruoyi.workflow.domain.FlowSetCondition;
import com.ruoyi.workflow.domain.FlowSetModule;
import com.ruoyi.workflow.domain.FlowWorkOrder;
import com.ruoyi.workflow.domain.vo.FlowNodesPercent;
import com.ruoyi.workflow.mapper.FlowNodesMapper;
import com.ruoyi.workflow.mapper.FlowSetConditionMapper;
import com.ruoyi.workflow.mapper.FlowSetMainMapper;
import com.ruoyi.workflow.mapper.FlowSetModuleMapper;
import com.ruoyi.workflow.mapper.FlowWorkOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowServiceImpl2 {
    @Autowired
    private FlowSetMainMapper flowSetMainMapper;
    @Autowired
    private FlowSetConditionMapper flowSetConditionMapper;
    @Autowired
    private FlowSetModuleMapper flowSetModuleMapper;
    @Autowired
    private FlowNodesMapper flowNodesMapper;
    @Autowired
    private FlowWorkOrderMapper flowWorkOrderMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDeptService sysDeptService;


    @Transactional(rollbackFor = Exception.class)
    public Long startNewFlow(FlowInfoDto flowInfo) {
        FlowSetMain main = flowSetMainMapper.selectByKey(flowInfo.getFlowMainKey());
        if (main == null) {
            throw new FlowException(FlowExceptionEnum.NOT_HAS_MAIN_KEY);
        }
        // 获取起始节点
        FlowSetModule startModule = flowSetModuleMapper.selectOne(new LambdaQueryWrapper<FlowSetModule>()
                .eq(FlowSetModule::getFlowSetMainId, main.getId())
                .eq(FlowSetModule::getStartFlag, FlowConstant.NOW_FLAG)
        );
        // 起始节点的操作人是当前登录人/传入的用户
        startModule.setUserId(flowInfo.getUserId().toString());
        SysUser user = sysUserService.selectUserById(flowInfo.getUserId());

        //新增工单主表
        FlowWorkOrder order = new FlowWorkOrder();
        order.setBusinessId(flowInfo.getBusinessId());
        order.setBusinessKey(flowInfo.getBusinessKey());
        order.setWorkFlowCode(IdWorker.getIdStr());
        order.setEndFlag(FlowConstant.STATUS_NORMAL);
        order.setCloseFlag(FlowConstant.STATUS_NORMAL);
        order.setNowModuleIds(startModule.getId().toString());
        order.setNowModuleNames(startModule.getModuleName());
        order.setCreateBy(user.getUserName());
        order.setCreateTime(DateUtils.getNowDate());
        order.setDelFlag(Constants.NOT_DEL);
        order.setNowUserIds(user.getUserId().toString());
        order.setNowUserNames(user.getUserName());
        order.setTodoUserIds(user.getUserId().toString());
        order.setTodoUserNames(user.getUserName());
        order.setUpdateTime(DateUtils.getNowDate());
        flowWorkOrderMapper.insert(order);
        // 保存初始节点
        FlowNodes node = new FlowNodes();
        node.setBusinessKey(flowInfo.getBusinessKey());
        node.setBusinessId(flowInfo.getBusinessId());
        node.setUserId(user.getUserId());
        node.setNickName(user.getNickName());
        node.setNowStep(FlowConstant.NOW_FLAG);
        node.setApplySort(1);
        node.setFlowModuleId(startModule.getFlowSetMainId());
        node.setOrderId(order.getId());
        flowNodesMapper.insert(node);
        flowInfo.setFlowModuleKey(startModule.getModuleCode());
        flowInfo.setOrderId(order.getId());
        flowInfo.setApplyRes(FlowConstant.NORMAL_PASS);
        return this.flowToNext(flowInfo);
    }

    */
/**
     * 更新数据宽表信息
     *
     * @param id
     *//*

    private void updateForWorkOrder(Long id) {
        FlowWorkOrder order = flowWorkOrderMapper.selectById(id);
        List<FlowNodes> list = flowNodesMapper.selectList(new LambdaQueryWrapper<FlowNodes>()
                .eq(FlowNodes::getDelFlag, Constants.NOT_DEL)
                .eq(FlowNodes::getBusinessId, order.getBusinessId())
                .eq(FlowNodes::getBusinessKey, order.getBusinessKey())
        );
        List<FlowNodes> nowList = list.stream().filter(t -> FlowConstant.NOW_FLAG.equals(t.getNowStep())).collect(Collectors.toList());
        List<Long> ids = list.stream().map(FlowNodes::getUserId).distinct().collect(Collectors.toList());
        List<String> names = list.stream().map(FlowNodes::getNickName).distinct().collect(Collectors.toList());
        List<Long> nowIds = nowList.stream().map(FlowNodes::getUserId).distinct().collect(Collectors.toList());
        List<String> nowNames = nowList.stream().map(FlowNodes::getNickName).distinct().collect(Collectors.toList());
        List<Long> moduleIds = nowList.stream().map(FlowNodes::getFlowModuleId).distinct().collect(Collectors.toList());
        List<FlowSetModule> modules = flowSetModuleMapper.selectList(new LambdaQueryWrapper<FlowSetModule>().in(FlowSetModule::getId, moduleIds));
        List<String> moduleNames = modules.stream().map(FlowSetModule::getModuleName).distinct().collect(Collectors.toList());
        order.setNowUserIds(StringUtils.join(ids, Constants.SPLITE_BY));
        order.setNowUserNames(StringUtils.join(names, Constants.SPLITE_BY));
        order.setTodoUserIds(StringUtils.join(nowIds, Constants.SPLITE_BY));
        order.setTodoUserNames(StringUtils.join(nowNames, Constants.SPLITE_BY));
        order.setNowModuleIds(StringUtils.join(moduleIds, Constants.SPLITE_BY));
        order.setNowModuleNames(StringUtils.join(moduleNames, Constants.SPLITE_BY));
        order.setUpdateTime(DateUtils.getNowDate());
        flowWorkOrderMapper.updateById(order);
    }

    */
/**
     * 新增一个节点
     *
     * @param order 工单总表
     * @param info  参数
     * @param next  模板
     * @param old   上一节点数据
     *//*

    private List<FlowNodes> addNewNode(FlowWorkOrder order, FlowInfoDto info, FlowSetModule next, FlowNodes old) {
        List<SysUser> users = null;
        if (FlowConstant.REF_USER.equals(next.getRefType())) {
            // 如果是指定审核人的，直接用审核人的id取用户列表
            users = sysUserService.selectByIds(next.getUserId());
        } else {
            // 如果是按角色的，判断是否取指定部门角色的用户或者指定角色的用户
            Long deptId = null;
            String roleIds = next.getRoleId();
            if (FlowConstant.REF_DEPT_ROLE.equals(next.getRefType())) {
                deptId = info.getDeptId();
            }
            users = sysUserService.selectByDeptAndRole(deptId, roleIds);

        }
        List<FlowNodes> saveNodes = new ArrayList<>();
        // 遍历用户列表，新增用户审核节点
        for (SysUser user : users) {
            FlowNodes node = new FlowNodes();
            node.setId(IdWorker.getId());
            node.setOrderId(order.getId());
            node.setBusinessKey(info.getBusinessKey());
            node.setBusinessId(info.getBusinessId());
            node.setUserId(user.getUserId());
            node.setNickName(user.getNickName());
            node.setNowStep(FlowConstant.NOW_FLAG);
            node.setApplySort(old == null ? 1 : (old.getApplySort() + 1));
            node.setFlowModuleId(next.getFlowSetMainId());
            saveNodes.add(node);
        }
        //快速批量保存
        for (int i = 0; i < saveNodes.size(); i = i + Constants.BATCH_INSERT_MAX) {
            int endNum = i + Constants.BATCH_INSERT_MAX;
            if (saveNodes.size() <= endNum) {
                endNum = saveNodes.size();
            }
            List<FlowNodes> inserts = saveNodes.subList(i, endNum);
            if (!inserts.isEmpty()) {
                flowNodesMapper.insertBatch(inserts);
            }
        }
        return saveNodes;
    }

    @Transactional(rollbackFor = Exception.class)
    public Long flowToNext(FlowInfoDto flowInfo) {
        SysUser user = sysUserService.selectUserById(flowInfo.getUserId());
        FlowWorkOrder order = flowWorkOrderMapper.selectOne(new LambdaQueryWrapper<FlowWorkOrder>()
                .eq(FlowWorkOrder::getBusinessKey, flowInfo.getBusinessKey())
                .eq(FlowWorkOrder::getBusinessId, flowInfo.getBusinessId())
                .eq(FlowWorkOrder::getDelFlag, Constants.NOT_DEL)
        );
        // 先校验有没有工单，和工单是不是传入的节点
        if (order == null) {
            throw new FlowException(FlowExceptionEnum.NOT_HAS_ORDER);
        }
        FlowSetModule module = flowSetModuleMapper.selectOne(new LambdaQueryWrapper<FlowSetModule>()
                .eq(FlowSetModule::getDelFlag, Constants.NOT_DEL)
                .eq(FlowSetModule::getModuleCode, flowInfo.getFlowModuleKey())
                .in(FlowSetModule::getId, order.getNowModuleIds().split(Constants.SPLITE_BY))
        );
        if (module == null) {
            throw new FlowException(FlowExceptionEnum.ERROR_MODULE_ID);
        }
        List<FlowSetCondition> conditions = flowSetConditionMapper.selectList(new LambdaQueryWrapper<FlowSetCondition>()
                .eq(FlowSetCondition::getFlowModuleId, module.getId())
                .eq(FlowSetCondition::getFlowSetMainId, module.getFlowSetMainId())
                .eq(FlowSetCondition::getEdition, module.getEdition())
        );

        FlowNodes myNode = flowNodesMapper.selectOne(new LambdaQueryWrapper<FlowNodes>()
                .eq(FlowNodes::getOrderId, order.getId())
                .eq(FlowNodes::getUserId, flowInfo.getUserId())
                .eq(FlowNodes::getNowStep, FlowConstant.NOW_FLAG)
        );
        // 查询出传入的用户指定节点的待办任务，如果没有，丢出错误
        if (myNode == null) {
            throw new FlowException(FlowExceptionEnum.ERROR_MODULE_ID);
        }
        myNode.setApplyRemark(flowInfo.getApplyRemark());
        myNode.setApplyRes(flowInfo.getApplyRes());
        myNode.setUpdateTime(DateUtils.getNowDate());
        myNode.setUpdateBy(user.getUpdateBy());
        myNode.setFilePaths(flowInfo.getFilePaths());
        myNode.setNowStep(FlowConstant.NOT_NOW_FLAG);
        flowNodesMapper.updateById(myNode);
        List<FlowSetCondition> nextCondition = null;
        List<FlowNodes> allNodes = flowNodesMapper.selectList(new LambdaQueryWrapper<FlowNodes>()
                .eq(FlowNodes::getOrderId, order.getId())
                .eq(FlowNodes::getFlowModuleId, module.getId())
                .eq(FlowNodes::getApplySort, myNode.getApplySort())
        );
        switch (module.getApplyType()) {
            case FlowConstant.APPLY_SINGLE:
                // 单人通过模式，直接按当前人操作的审批结果进行走向
                nextCondition = getNextCondition(conditions, flowInfo.getApplyRes());
                break;
            case FlowConstant.APPLY_ALL:
                // 全部通过模式，查出所有人的审核结果
                long countNotSame = allNodes.stream().filter(t -> FlowConstant.NOT_NOW_FLAG.equals(t.getNowStep()) && !flowInfo.getApplyRes().equals(t.getApplyRes())).count();
                if (countNotSame > 0) {
                    //先查出操作结果里面和当前操作结果不一样的，如果有存在，那么直接匹配驳回结果
                    nextCondition = getNextCondition(conditions, FlowConstant.NOT_MATCHED);
                } else {
                    // 如果没有不一样的，查询是否全部审批过了。如果全部审批过了，就按当前操作结果匹配
                    long countEnd = allNodes.stream().filter(t -> FlowConstant.NOT_NOW_FLAG.equals(t.getNowStep())).count();
                    if (countEnd == Long.valueOf(allNodes.size())) {
                        nextCondition = getNextCondition(conditions, flowInfo.getApplyRes());
                    }
                }
                break;
            case FlowConstant.APPLY_SCLAE:
                //先查询所有结果的占比百分比
                List<FlowNodesPercent> nodesPercnets = flowNodesMapper.selectForPercent(flowInfo);
                if (!nodesPercnets.isEmpty()) {
                    Integer count = nodesPercnets.stream().map(x -> x.getApplyCount()).reduce(0, (a, b) -> a + b);
                    Integer maxCount = nodesPercnets.get(0).getApplyCount();
                    BigDecimal maxPercent = new BigDecimal(maxCount).divide(new BigDecimal(allNodes.size()));
                    if (maxPercent.compareTo(module.getApplyScale()) >= 0) {
                        //如果占比最大的结果，百分比达标了，直接通过
                        nextCondition = getNextCondition(conditions, nodesPercnets.get(0).getApplyRes());
                    } else {
                        Integer lastCount = allNodes.size() - count;
                        maxPercent = new BigDecimal(maxCount + lastCount).divide(new BigDecimal(allNodes.size()));
                        if (maxPercent.compareTo(module.getApplyScale()) < 0) {
                            //如果占比最大的结果+剩下未处理的结果之和都小于达标值，直接匹配驳回结果
                            nextCondition = getNextCondition(conditions, FlowConstant.NOT_MATCHED);
                        }
                    }
                }
                break;
            default:
        }
        if (nextCondition != null && !nextCondition.isEmpty()) {
            // 得出下一个节点的走向了，说明可以进入下一个节点
            toNext(nextCondition, order, module, flowInfo, myNode, user);
        }
        return order.getId();
    }

    */
/**
     * 往新节点流转
     *
     * @param nextCondition
     * @param order
     * @param module
     * @param flowInfo
     * @param myNode
     * @param user
     *//*

    private void toNext(List<FlowSetCondition> nextCondition, FlowWorkOrder order, FlowSetModule module, FlowInfoDto flowInfo, FlowNodes myNode, SysUser user) {
        //结束当前节点操作
        flowNodesMapper.updateForDid(order, module);
        for (FlowSetCondition condition : nextCondition) {
            FlowSetModule next = flowSetModuleMapper.selectById(condition.getNextFlowModuleId());
            switch (next.getNextDeptType()) {
                case FlowConstant.THIS_DEPT:
                    flowInfo.setDeptId(user.getDeptId());
                    break;
                case FlowConstant.PARENT_DEPT:
                    SysDept dept = sysDeptService.selectDeptById(user.getDeptId());
                    if (null == dept.getParentId()) {
                        flowInfo.setDeptId(user.getDeptId());
                    } else {
                        dept = sysDeptService.selectDeptById(dept.getParentId());
                        flowInfo.setDeptId(dept.getDeptId());
                    }
                    break;
                case FlowConstant.SPEC_DEPT:
                    flowInfo.setDeptId(next.getNextDeptId());
                    break;
                case FlowConstant.INPUT_DEPT:
                    break;
                default:
            }
            this.addNewNode(order, flowInfo, next, myNode);
        }
        updateForWorkOrder(order.getId());
    }

    private List<FlowSetCondition> getNextCondition(List<FlowSetCondition> conditions, String applyRes) {
        List<FlowSetCondition> condition = conditions.stream().filter(t ->
                (FlowConstant.SAME_VALUE.equals(t.getConditionType()) && t.getConditionKey().equals(applyRes)) ||
                        (FlowConstant.NOT_SAME_VALUE.equals(t.getConditionType()) && !t.getConditionKey().equals(applyRes)) ||
                        (FlowConstant.HAS_KEY.equals(t.getConditionType()) && ("," + t.getConditionKey() + ",").contains("," + applyRes + ","))
        ).collect(Collectors.toList());
        return condition;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean closeFlow(FlowInfoDto flowInfo) {

        return null;
    }
}
*/

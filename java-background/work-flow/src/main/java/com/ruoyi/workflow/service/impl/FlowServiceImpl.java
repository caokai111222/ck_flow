package com.ruoyi.workflow.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DistributedRedisLock;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.constant.FlowConstant;
import com.ruoyi.workflow.domain.FlowForm;
import com.ruoyi.workflow.domain.FlowNodes;
import com.ruoyi.workflow.domain.FlowSetCondition;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.FlowSetModule;
import com.ruoyi.workflow.domain.FlowUserTask;
import com.ruoyi.workflow.domain.FlowWorkOrder;
import com.ruoyi.workflow.domain.FormMain;
import com.ruoyi.workflow.domain.dto.FlowInfoDto;
import com.ruoyi.workflow.domain.dto.FlowNodeTaskDto;
import com.ruoyi.workflow.domain.vo.FlowMyTodoDto;
import com.ruoyi.workflow.enums.FlowApplyTypeEnum;
import com.ruoyi.workflow.enums.FlowDeptTypeEnum;
import com.ruoyi.workflow.enums.FlowExceptionEnum;
import com.ruoyi.workflow.enums.FlowNodeStatusEnum;
import com.ruoyi.workflow.enums.FlowNodeTypeEnum;
import com.ruoyi.workflow.enums.FlowRefTypeEnum;
import com.ruoyi.workflow.enums.FlowServiceDelayTimeUnitEnum;
import com.ruoyi.workflow.enums.FlowServicePassTypeEnum;
import com.ruoyi.workflow.exception.FlowException;
import com.ruoyi.workflow.mapper.FlowFormMapper;
import com.ruoyi.workflow.mapper.FlowNodesMapper;
import com.ruoyi.workflow.mapper.FlowUserTaskMapper;
import com.ruoyi.workflow.mapper.FlowWorkOrderMapper;
import com.ruoyi.workflow.service.IFlowService;
import com.ruoyi.workflow.service.IFlowSetMainService;
import com.ruoyi.workflow.service.IFormMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.PageUtils.startPage;

@Service
public class FlowServiceImpl extends ServiceImpl<FlowNodesMapper, FlowNodes> implements IFlowService {
    @Autowired
    private FlowNodesMapper flowNodesMapper;
    @Autowired
    private FlowUserTaskMapper flowUserTaskMapper;
    @Autowired
    private IFlowSetMainService flowSetMainService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private FlowWorkOrderMapper flowWorkOrderMapper;
    @Autowired
    private IFormMainService formMainService;
    @Autowired
    private FlowFormMapper flowFormMapper;
    /**
     * ???????????????????????????
     */
    @Autowired
    private ScheduledExecutorService scheduledExecutorService;
    @Autowired
    private DistributedRedisLock distributedRedisLock;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startNewFlow(FlowInfoDto flowInfo) {
        String key = FlowConstant.FLOW_LOCK_ADD_KEY + flowInfo.getBusinessKey() + Constants.UNDER_LINE + flowInfo.getBusinessId();
        try {
            if (distributedRedisLock.lock(key)) {
                FlowSetMain main = flowSetMainService.selectInfoByKey(flowInfo.getFlowMainKey());
                if (main == null) {
                    throw new FlowException(FlowExceptionEnum.NOT_HAS_MAIN_KEY);
                }
                FlowWorkOrder order = flowWorkOrderMapper.selectOne(new LambdaQueryWrapper<FlowWorkOrder>()
                        .eq(FlowWorkOrder::getBusinessKey, flowInfo.getBusinessKey())
                        .eq(FlowWorkOrder::getBusinessId, flowInfo.getBusinessId())
                        .eq(FlowWorkOrder::getCloseFlag, FlowConstant.NOT_NOW_FLAG)
                        .eq(FlowWorkOrder::getEndFlag, FlowConstant.NOT_NOW_FLAG)
                        .eq(FlowWorkOrder::getDelFlag, Constants.NOT_DEL)
                );
                if (order != null) {
                    throw new FlowException(FlowExceptionEnum.HAS_SAME_IN_FLOW);
                }
                FlowSetModule startModule = main.getStartNode();
                // ??????????????????????????????????????????/???????????????
                SysUser user = sysUserService.selectUserById(flowInfo.getUserId());
                //??????????????????
                order = this.getOrCreateOrder(flowInfo);
                order.setNowModuleIds(startModule.getId().toString());
                order.setNowModuleNames(startModule.getModuleName());
                flowWorkOrderMapper.updateById(order);
                // ??????????????????
                List<SysUser> users = new ArrayList<>();
                users.add(user);
                FlowNodes node = saveNode(startModule, order, null, users);
                flowInfo.setOrderId(order.getId());
                flowInfo.setFlowModuleId(startModule.getId());
                flowInfo.setApplyRes(FlowConstant.NORMAL_PASS);
                flowInfo.setNodeId(node.getId());
                FlowUserTask myTask = flowUserTaskMapper.selectByInfo(flowInfo);
                JSONObject object = JSON.parseObject(flowInfo.getDataJson());
                saveData(myTask, object, main);
                if (FlowConstant.NORMAL_PASS.equals(flowInfo.getAutoCommit())) {
                    this.flowToNext(flowInfo);
                }
            }
        } catch (Exception e) {
            log.error("???????????????", e);
        } finally {
            distributedRedisLock.unlock(key);
        }
    }

    private void saveData(FlowUserTask myTask, JSONObject object, FlowSetMain main) {
        FlowSetModule module = main.getCondition2ModuleMap().get(myTask.getFlowModuleId().toString());
        if (module.getFormMainId() != null) {
            FormMain formMain = formMainService.selectInfoById(module.getFormMainId());
            List<FlowForm> list = new ArrayList<>();
            formMain.getItems().forEach(item -> {
                if (StringUtils.isNotEmpty(item.getItemName()) && object.containsKey(item.getItemName())) {
                    FlowForm form = new FlowForm();
                    form.setId(IdWorker.getId());
                    form.setOrderId(myTask.getOrderId());
                    form.setModuleId(myTask.getFlowModuleId());
                    form.setNodeId(myTask.getNodeId());
                    form.setTaskId(myTask.getId());
                    form.setFormId(formMain.getId());
                    form.setFormItemId(item.getId());
                    form.setUserId(myTask.getUserId());
                    form.setItemName(item.getItemName());
                    form.setItemValue(object.getString(item.getItemName()));
                    form.setDelFlag(Constants.NOT_DEL);
                    form.setCreateTime(DateUtils.getNowDate());
                    list.add(form);
                }
            });
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i = i + Constants.BATCH_INSERT_MAX) {
                    int endNum = i + Constants.BATCH_INSERT_MAX;
                    if (list.size() <= endNum) {
                        endNum = list.size();
                    }
                    List<FlowForm> inserts = list.subList(i, endNum);
                    if (!inserts.isEmpty()) {
                        flowFormMapper.insertBatch(inserts);
                    }
                }
            }
        }

    }

    /**
     * ????????????
     *
     * @param module
     * @param order
     * @param lastNode
     * @param users
     * @return
     */
    private FlowNodes saveNode(FlowSetModule module, FlowWorkOrder order, FlowNodes lastNode, List<SysUser> users) {
        FlowNodes node = new FlowNodes();
        node.setBusinessKey(order.getBusinessKey());
        node.setBusinessId(order.getBusinessId());
        node.setApplySort(null != lastNode ? (lastNode.getApplySort() + 1) : 1);
        node.setFlowModuleId(module.getId());
        node.setOrderId(order.getId());
        node.setNodeStatus(FlowNodeStatusEnum.WAITING.getInfo());
        node.setDelFlag(Constants.NOT_DEL);
        node.setCreateTime(DateUtils.getNowDate());
        flowNodesMapper.insert(node);
        if (users != null && !users.isEmpty()) {
            List<FlowUserTask> tasks = new ArrayList<>();
            users.stream().forEach(u -> {
                FlowUserTask task = new FlowUserTask();
                task.setId(IdWorker.getId());
                task.setUserId(u.getUserId());
                task.setNickName(u.getNickName());
                task.setNodeId(node.getId());
                task.setOrderId(order.getId());
                task.setFlowModuleId(module.getId());
                task.setBusinessKey(order.getBusinessKey());
                task.setBusinessId(order.getBusinessId());
                task.setNodeStatus(FlowNodeStatusEnum.WAITING.getInfo());
                task.setCreateTime(node.getCreateTime());
                task.setDelFlag(Constants.NOT_DEL);
                tasks.add(task);
            });
            if (!tasks.isEmpty()) {
                batchSaveTask(tasks);
            }
        }
        if (FlowNodeTypeEnum.SERVICE_TASK.getInfo().equals(module.getType()) && FlowServicePassTypeEnum.PASS_DELAY.getInfo().equals(module.getAutoPassType())) {
            // ???????????????????????????
            FlowInfoDto dto = new FlowInfoDto();
            dto.setOrderId(order.getId());
            dto.setFlowModuleId(module.getId());
            dto.setApplyRes(FlowConstant.NORMAL_PASS);
            dto.setNodeId(node.getId());
            TimeUnit unit;
            switch (FlowServiceDelayTimeUnitEnum.getByInfo(module.getDelayTimeUnit())) {
                case MINUTE:
                    unit = TimeUnit.MINUTES;
                    break;
                case HOUR:
                    unit = TimeUnit.HOURS;
                    break;
                case DAY:
                    unit = TimeUnit.DAYS;
                    break;
                default:
                    unit = TimeUnit.SECONDS;
            }
            scheduledExecutorService.schedule(new FlowAutoPassScheduled(dto), module.getDelayTime(), unit);
        }
        return node;
    }

    class FlowAutoPassScheduled implements Runnable {
        @Autowired
        private IFlowService flowService;

        private FlowInfoDto flowInfoDto;

        public FlowAutoPassScheduled(FlowInfoDto dto) {
            this.flowInfoDto = dto;
        }

        @Override
        public void run() {
            flowToNext(flowInfoDto);
        }
    }

    /**
     * ????????????????????????
     *
     * @param tasks
     */
    private void batchSaveTask(List<FlowUserTask> tasks) {
        //??????????????????
        for (int i = 0; i < tasks.size(); i = i + Constants.BATCH_INSERT_MAX) {
            int endNum = i + Constants.BATCH_INSERT_MAX;
            if (tasks.size() <= endNum) {
                endNum = tasks.size();
            }
            List<FlowUserTask> inserts = tasks.subList(i, endNum);
            if (!inserts.isEmpty()) {
                flowUserTaskMapper.insertBatch(inserts);
            }
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param flowInfo
     * @return
     */
    private FlowWorkOrder getOrCreateOrder(FlowInfoDto flowInfo) {
        FlowSetMain main = flowSetMainService.selectInfoByKey(flowInfo.getFlowMainKey());
        FlowWorkOrder order = new FlowWorkOrder();
        order.setBusinessId(flowInfo.getBusinessId());
        order.setBusinessKey(flowInfo.getBusinessKey());
        order.setWorkFlowCode(IdWorker.getIdStr());
        order.setFlowMainId(main.getId());
        order.setEndFlag(FlowConstant.STATUS_NORMAL);
        order.setCloseFlag(FlowConstant.STATUS_NORMAL);
        order.setCreateTime(DateUtils.getNowDate());
        order.setDelFlag(Constants.NOT_DEL);
        order.setUpdateTime(DateUtils.getNowDate());
        if (flowInfo.getUserId() != null) {
            SysUser user = sysUserService.selectUserById(flowInfo.getUserId());
            if (user != null) {
                order.setCreateBy(user.getUserName());
                order.setNowUserIds(user.getUserId().toString());
                order.setNowUserNames(user.getUserName());
                order.setTodoUserIds(user.getUserId().toString());
                order.setTodoUserNames(user.getUserName());
            }
        }
        flowWorkOrderMapper.insert(order);
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void flowToNext(FlowInfoDto flowInfo) {
        String key = FlowConstant.FLOW_LOCK_NEXT_KEY + flowInfo.getOrderId();
        try {
            if (distributedRedisLock.lock(key)) {
                FlowNodes node = this.getById(flowInfo.getNodeId());
                // ???????????????????????????????????????????????????????????????
                if (node == null || !FlowNodeStatusEnum.WAITING.getInfo().equals(node.getNodeStatus())) {
                    throw new FlowException(FlowExceptionEnum.ERROR_NODE_STATUS);
                }
                FlowWorkOrder order = flowWorkOrderMapper.selectById(node.getOrderId());
                FlowSetMain main = flowSetMainService.selectInfoById(order.getFlowMainId());
                FlowSetModule module = main.getCondition2ModuleMap().get(node.getFlowModuleId().toString());
                Boolean toNext = false;
                if (FlowNodeTypeEnum.SERVICE_TASK.getInfo().equals(module.getType())) {
                    if (FlowServicePassTypeEnum.PASS_NOW.getInfo().equals(module.getAutoPassType())) {
                        // ??????????????????????????????
                        toNext = true;
                    } else {
                        // ????????????
                        Date dt = DateUtils.getNowDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(node.getCreateTime());
                        switch (FlowServiceDelayTimeUnitEnum.getByInfo(module.getDelayTimeUnit())) {
                            case MINUTE:
                                calendar.add(Calendar.MINUTE, module.getDelayTime());
                                break;
                            case HOUR:
                                calendar.add(Calendar.HOUR, module.getDelayTime());
                                break;
                            case DAY:
                                calendar.add(Calendar.DATE, module.getDelayTime());
                                break;
                            default:
                        }
                        if (dt.after(calendar.getTime())) {
                            toNext = true;
                        }
                    }

                } else if (FlowNodeTypeEnum.START_NODE.getInfo().equals(module.getType())) {
                    // ????????????????????????
                    toNext = true;
                    FlowUserTask myTask = flowUserTaskMapper.selectByInfo(flowInfo);
                    myTask.setApplyRemark(flowInfo.getApplyRemark());
                    myTask.setApplyRes(flowInfo.getApplyRes());
                    myTask.setFilePaths(flowInfo.getFilePaths());
                    myTask.setUpdateTime(DateUtils.getNowDate());
                    myTask.setNodeStatus(FlowNodeStatusEnum.PASS.getInfo());
                    flowUserTaskMapper.updateById(myTask);
                    JSONObject object = JSON.parseObject(flowInfo.getDataJson());
                    saveData(myTask, object, main);
                } else {
                    //????????????
                    FlowUserTask myTask = flowUserTaskMapper.selectByInfo(flowInfo);
                    // ???????????????????????????????????????????????????????????????
                    if (myTask == null || !FlowNodeStatusEnum.WAITING.getInfo().equals(myTask.getNodeStatus())) {
                        throw new FlowException(FlowExceptionEnum.ERROR_NODE_STATUS);
                    }
                    myTask.setApplyRemark(flowInfo.getApplyRemark());
                    myTask.setApplyRes(flowInfo.getApplyRes());
                    myTask.setFilePaths(flowInfo.getFilePaths());
                    myTask.setUpdateTime(DateUtils.getNowDate());
                    myTask.setNodeStatus(FlowNodeStatusEnum.PASS.getInfo());
                    flowUserTaskMapper.updateById(myTask);
                    JSONObject object = JSON.parseObject(flowInfo.getDataJson());
                    saveData(myTask, object, main);
                    //?????????????????????????????????????????????
                    if (FlowApplyTypeEnum.APPLY_SINGLE.getInfo().equals(module.getApplyType())) {
                        // ?????????????????????????????????
                        toNext = true;
                    } else {
                        FlowNodeTaskDto dto = flowUserTaskMapper.selectForCount(myTask.getNodeId(), myTask.getApplyRes());
                        // ???????????????100%
                        BigDecimal passPercent = FlowConstant.LONG_100;
                        if (FlowApplyTypeEnum.APPLY_SCLAE.getInfo().equals(module.getApplyType())) {
                            // ?????????????????????????????????????????????
                            passPercent = module.getApplyScale();
                        }
                        BigDecimal realPass = dto.getSameCount().multiply(FlowConstant.LONG_100).divide(dto.getAllCount(), RoundingMode.HALF_UP);
                        if (realPass.compareTo(passPercent) >= 0) {
                            toNext = true;
                        }
                    }
                }
                if (toNext) {
                    //????????????????????????????????????????????????????????????????????????
                    node.setNodeStatus(flowInfo.getApplyRes());
                    node.setNodeStatus(FlowNodeStatusEnum.PASS.getInfo());
                    node.setUpdateTime(DateUtils.getNowDate());
                    flowNodesMapper.updateById(node);
                    flowUserTaskMapper.clearForInWait(node.getId(), FlowNodeStatusEnum.AUTO_PASS.getInfo(), FlowNodeStatusEnum.WAITING.getInfo());
                    this.checkPassForModule(main, order, node, flowInfo, module);
                }
                this.updateForOrderInfo(order.getId());
            }
        } catch (Exception e) {
            log.error("???????????????", e);
        } finally {
            distributedRedisLock.unlock(key);
        }

    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param main
     * @param order
     * @param node
     * @param flowInfo
     * @param module
     */
    private void checkPassForModule(FlowSetMain main, FlowWorkOrder order, FlowNodes node, FlowInfoDto flowInfo, FlowSetModule module) {
        Boolean hasEnd = false;
        List<FlowSetCondition> conditions = main.getModule2ConditionMap().get(module.getId() + Constants.UNDER_LINE + flowInfo.getApplyRes());
        if (conditions == null || conditions.isEmpty()) {
            throw new FlowException(FlowExceptionEnum.NOT_NEXT_CONDITIONS);
        }
        for (FlowSetCondition condition : conditions) {
            FlowSetModule nextModules = main.getCondition2ModuleMap().get(condition.getNextFlowModuleId().toString());
            if (nextModules == null) {
                throw new FlowException(FlowExceptionEnum.NOT_NEXT_CONDITIONS);
            }
            Boolean isPass = false;
            if (FlowApplyTypeEnum.APPLY_SINGLE.getInfo().equals(nextModules.getFlowInType())) {
                // ??????????????????????????????
                isPass = true;
            } else {
                // ??????????????????????????????
                // ???????????????????????????????????????????????????
                String keyIn = module.getId() + Constants.UNDER_LINE_IN;
                List<FlowSetCondition> conditionList = main.getModule2ConditionMap().get(keyIn);
                List<Long> moduleIds = conditionList.stream().map(FlowSetCondition::getFlowModuleId).collect(Collectors.toList());
                String ids = StringUtils.join(moduleIds, Constants.SPLITE_BY);
                //??????????????????????????????
                List<FlowNodes> inWaitingNodes = flowNodesMapper.selectByOrderIdAndModuleIds(order.getId(), ids);
                isPass = true;
                for (FlowSetCondition cd : conditionList) {
                    Boolean isOk = false;
                    for (FlowNodes no : inWaitingNodes) {
                        if (no.getId().equals(cd.getFlowModuleId()) && cd.getConditionKey().equals(no.getNodeRes())) {
                            // ??????????????????
                            isOk = true;
                            break;
                        }
                    }
                    if (!isOk) {
                        //?????????????????????????????????????????????
                        isPass = false;
                        break;
                    }
                }
            }
            if (isPass) {
                //???????????????????????????????????????????????????
                //??????????????????????????????????????????
                flowNodesMapper.clearForInWait(nextModules, FlowNodeStatusEnum.DONE.getInfo(), order);
                List<SysUser> users = null;
                if (FlowNodeTypeEnum.USER_TASK.getInfo().equals(nextModules.getType())) {
                    if (FlowRefTypeEnum.REF_USER.getInfo().equals(nextModules.getRefType())) {
                        users = nextModules.getUserList();
                    } else if (FlowRefTypeEnum.INTPUT_USER.getInfo().equals(nextModules.getRefType())) {
                        if (StringUtils.isEmpty(flowInfo.getUserIds())) {
                            //???????????????????????????????????????
                            throw new FlowException(FlowExceptionEnum.NOT_NEXT_USERS);
                        }
                        // ????????????
                        users = sysUserService.selectByIds(flowInfo.getUserIds());
                    } else {
                        String deptId = null;
                        FlowSetModule startNode = main.getModuleList().stream().filter(t -> t.getEdition().equals(module.getEdition()) && FlowNodeTypeEnum.START_NODE.getInfo().equals(t.getType())).findFirst().orElse(null);
                        FlowUserTask startTask = flowUserTaskMapper.selectByModuleId(startNode.getId(), order.getId());
                        SysUser user = sysUserService.selectUserById(startTask.getUserId());
                        switch (FlowDeptTypeEnum.getByInfo(nextModules.getNextDeptType())) {
                            case THIS_DEPT:
                                deptId = user.getDeptId().toString();
                                break;
                            case PARENT_DEPT:
                                SysDept dept = sysDeptService.selectDeptById(user.getDeptId());
                                deptId = dept.getParentId().toString();
                                break;
                            case SPEC_DEPT:
                                deptId = nextModules.getNextDeptIds();
                                break;
                            case INPUT_DEPT:
                                deptId = flowInfo.getDeptId().toString();
                                break;
                            case SEPC_DEPT_TYPE:
                                SysDept mainDp = sysDeptService.selectDeptById(main.getDeptId());
                                List<SysDept> depts = sysDeptService.selectDeptByTypeAndMainId(mainDp.getFullPath(), nextModules.getDeptType());
                                if (depts.isEmpty()) {
                                    throw new FlowException(FlowExceptionEnum.NOT_NEXT_USERS);
                                }
                                deptId = StringUtils.join(depts.stream().map(SysDept::getDeptId).collect(Collectors.toList()), Constants.SPLITE_BY);
                                break;
                            default:
                        }
                        users = sysUserService.selectByDeptAndRole(deptId, nextModules.getRoleIds());
                    }
                    if (users.isEmpty()) {
                        throw new FlowException(FlowExceptionEnum.NOT_NEXT_USERS);
                    }
                }
                FlowNodes newNodes = this.saveNode(nextModules, order, node, users);
                if (FlowNodeTypeEnum.SERVICE_TASK.getInfo().equals(nextModules.getType()) && FlowServicePassTypeEnum.PASS_NOW.getInfo().equals(nextModules.getAutoPassType())) {
                    FlowInfoDto dto = new FlowInfoDto();
                    dto.setOrderId(order.getId());
                    dto.setFlowModuleId(nextModules.getId());
                    dto.setApplyRes(FlowConstant.NORMAL_PASS);
                    dto.setNodeId(newNodes.getId());
                    this.flowToNext(dto);
                }
                if (FlowNodeTypeEnum.END_NODE.getInfo().equals(nextModules.getType())) {
                    //?????????????????????????????????????????????????????????????????????
                    hasEnd = true;
                }
            }
        }
        if (hasEnd) {
            //????????????
            this.updateOrderForEnd(order);
        }
    }

    /**
     * ????????????
     *
     * @param order
     */
    private void updateOrderForEnd(FlowWorkOrder order) {
        order = flowWorkOrderMapper.selectById(order.getId());
        order.setTodoUserNames(null);
        order.setTodoUserIds(null);
        order.setNowUserNames(null);
        order.setNowUserIds(null);
        order.setNowModuleNames(null);
        order.setNowModuleIds(null);
        order.setEndDate(DateUtils.getNowDate());
        order.setEndFlag(FlowConstant.STATUS_CLOSE_OR_END);
        order.setUpdateTime(DateUtils.getNowDate());
        flowWorkOrderMapper.updateById(order);
        clearForInWaitAll(order);
    }

    /**
     * ??????????????????????????????
     *
     * @param order
     */
    private void clearForInWaitAll(FlowWorkOrder order) {
        flowNodesMapper.clearForInWaitAll(FlowNodeStatusEnum.DONE.getInfo(), FlowNodeStatusEnum.WAITING.getInfo(), order);
        flowUserTaskMapper.clearForInWaitAll(FlowNodeStatusEnum.DONE.getInfo(), FlowNodeStatusEnum.WAITING.getInfo(), order);
    }

    /**
     * ????????????????????????
     *
     * @param orderId
     */
    private void updateForOrderInfo(Long orderId) {
        flowWorkOrderMapper.updateForOrderInfo(orderId);
    }

    @Override
    public Boolean closeFlow(FlowInfoDto flowInfo) {
        FlowWorkOrder order = flowWorkOrderMapper.selectById(flowInfo.getOrderId());
        order.setTodoUserNames(null);
        order.setTodoUserIds(null);
        order.setNowUserNames(null);
        order.setNowUserIds(null);
        order.setNowModuleNames(null);
        order.setNowModuleIds(null);
        order.setCloseDate(DateUtils.getNowDate());
        order.setCloseFlag(FlowConstant.STATUS_CLOSE_OR_END);
        order.setUpdateTime(DateUtils.getNowDate());
        flowWorkOrderMapper.updateById(order);
        clearForInWaitAll(order);
        return true;
    }

    @Override
    public List<FlowMyTodoDto> getMyTodo(FlowSetMain flowSetMain) {
        /*flowSetMain.setUserId(SecurityUtils.getUserId());*/
        return flowUserTaskMapper.selectMyTodo(flowSetMain);
    }

    @Override
    public PageInfo queryFlowRecordByOrderId(FlowInfoDto flowInfo) {
        List<FlowMyTodoDto> list = flowUserTaskMapper.selectFlowRecordByOrderId(flowInfo);
        return new PageInfo(list);
    }

    @Override
    public List<FlowForm> getFormDataById(FlowInfoDto flowInfo) {
        return flowFormMapper.selectList(new LambdaQueryWrapper<FlowForm>().eq(FlowForm::getDelFlag, Constants.NOT_DEL).eq(FlowForm::getTaskId, flowInfo.getTaskId()));
    }
}

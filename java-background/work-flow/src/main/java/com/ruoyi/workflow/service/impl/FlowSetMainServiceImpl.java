package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.constant.FlowConstant;
import com.ruoyi.workflow.domain.FlowSetCondition;
import com.ruoyi.workflow.domain.FlowSetMain;
import com.ruoyi.workflow.domain.FlowSetModule;
import com.ruoyi.workflow.domain.FlowUserTask;
import com.ruoyi.workflow.domain.dto.FlowElementsDto;
import com.ruoyi.workflow.domain.dto.FlowSetDto;
import com.ruoyi.workflow.enums.FlowApplyTypeEnum;
import com.ruoyi.workflow.enums.FlowNodeTypeEnum;
import com.ruoyi.workflow.enums.FlowRefTypeEnum;
import com.ruoyi.workflow.mapper.FlowSetConditionMapper;
import com.ruoyi.workflow.mapper.FlowSetMainMapper;
import com.ruoyi.workflow.mapper.FlowSetModuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.workflow.service.IFlowSetMainService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowSetMainServiceImpl extends ServiceImpl<FlowSetMainMapper, FlowSetMain> implements IFlowSetMainService {
    @Autowired
    private FlowSetMainMapper flowSetMainMapper;
    @Autowired
    private FlowSetConditionMapper flowSetConditionMapper;
    @Autowired
    private FlowSetModuleMapper flowSetModuleMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisCache redisCache;

    @Override
    public FlowSetMain selectInfoById(Long id) {
        FlowSetMain flowSetMain = redisCache.getCacheMapValue(FlowConstant.FLOW_MAP_KEY, FlowConstant.FLOW_MAIN_ID + id);
        if (flowSetMain == null) {
            flowSetMain = flowSetMainMapper.selectById(id);
            flowSetMain = fullInfo(flowSetMain);
            redisCache.setCacheMapValue(FlowConstant.FLOW_MAP_KEY, FlowConstant.FLOW_MAIN_ID + id, flowSetMain);
        }
        return flowSetMain;
    }

    @Override
    public FlowSetMain selectInfoByKey(String key) {
        FlowSetMain flowSetMain = redisCache.getCacheMapValue(FlowConstant.FLOW_MAP_KEY, FlowConstant.FLOW_MAIN_KEY + key);
        if (flowSetMain == null) {
            flowSetMain = flowSetMainMapper.selectByKey(key);
            flowSetMain = fullInfo(flowSetMain);
            redisCache.setCacheMapValue(FlowConstant.FLOW_MAP_KEY, FlowConstant.FLOW_MAIN_KEY + key, flowSetMain);
        }
        return flowSetMain;
    }

    /**
     * 缓存补全数据
     *
     * @param flowSetMain
     * @return
     */
    private FlowSetMain fullInfo(FlowSetMain flowSetMain) {
        // 把节点和边都取出来，分别塞入list，并且生成map快速获取
        List<FlowSetModule> moduleList = flowSetModuleMapper.selectList(new LambdaQueryWrapper<FlowSetModule>()
                .eq(FlowSetModule::getFlowSetMainId, flowSetMain.getId())
        );
        List<FlowSetCondition> conditionList = flowSetConditionMapper.selectList(new LambdaQueryWrapper<FlowSetCondition>()
                .eq(FlowSetCondition::getFlowSetMainId, flowSetMain.getId()));
        Map<String, List<FlowSetCondition>> module2ConditionMap = new HashMap<>();
        Map<String, FlowSetModule> condition2ModuleMap = new HashMap<>();
        for (FlowSetModule module : moduleList) {
            if (FlowRefTypeEnum.REF_USER.getInfo().equals(module.getRefType())) {
                List<SysUser> userList = sysUserService.selectByIds(module.getUserIds());
                module.setUserList(userList);
            }
            for (FlowSetCondition condition : conditionList) {
                String keyIn = module.getId() + Constants.UNDER_LINE_IN;
                String keyOut = module.getId() + Constants.UNDER_LINE + condition.getConditionKey();
                if (condition.getFlowModuleId().equals(module.getId())) {
                    List<FlowSetCondition> ls;
                    if (!module2ConditionMap.containsKey(keyOut)) {
                        ls = new ArrayList<>();
                    } else {
                        ls = module2ConditionMap.get(keyOut);
                    }
                    ls.add(condition);
                    module2ConditionMap.put(keyOut, ls);
                }
                if (condition.getNextFlowModuleId().equals(module.getId())) {
                    List<FlowSetCondition> ls;
                    if (!module2ConditionMap.containsKey(keyIn)) {
                        ls = new ArrayList<>();
                    } else {
                        ls = module2ConditionMap.get(keyIn);
                    }
                    ls.add(condition);
                    module2ConditionMap.put(keyIn, ls);
                }
            }
            condition2ModuleMap.put(module.getId().toString(), module);
            if (FlowNodeTypeEnum.START_NODE.getInfo().equals(module.getType()) && flowSetMain.getNowEdition().equals(module.getEdition())) {
                flowSetMain.setStartNode(module);
            }
            if (FlowNodeTypeEnum.END_NODE.getInfo().equals(module.getType())) {
                flowSetMain.setEndNode(module);
            }
        }
        flowSetMain.setModuleList(moduleList);
        flowSetMain.setConditionList(conditionList);
        flowSetMain.setModule2ConditionMap(module2ConditionMap);
        flowSetMain.setCondition2ModuleMap(condition2ModuleMap);
        return flowSetMain;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFlowSet(FlowSetDto flowSetDto) {
        FlowSetMain flowSetMain;
        LoginUser user = SecurityUtils.getLoginUser();
        if (flowSetDto.getId() != null) {
            flowSetMain = selectInfoById(flowSetDto.getId());
            flowSetMain.setUpdateBy(user.getUsername());
            flowSetMain.setUpdateTime(DateUtils.getNowDate());
            flowSetModuleMapper.updateForNotNow(flowSetDto.getId(), FlowConstant.NOT_NOW_FLAG);
        } else {
            flowSetMain = new FlowSetMain();
            flowSetMain.setCreateBy(user.getUsername());
            flowSetMain.setCreateTime(DateUtils.getNowDate());
            flowSetMain.setDelFlag(Constants.NOT_DEL);
        }
        flowSetMain.setFlowName(flowSetDto.getFlowName());
        flowSetMain.setFlowKey(flowSetDto.getFlowKey());
        Integer nowEdition = flowSetMain.getNowEdition() != null ? (flowSetMain.getNowEdition() + 1) : 0;
        flowSetMain.setNowEdition(nowEdition);
        saveOrUpdate(flowSetMain);

        List<FlowElementsDto> nodes = flowSetDto.getNodes();
        List<FlowElementsDto> edges = flowSetDto.getEdges();
        List<FlowSetModule> moduleList = new ArrayList<>();
        List<FlowSetCondition> conditionList = new ArrayList<>();
        Map<String, Long> idChange = new HashMap<>();
        nodes.stream().forEach(t -> {
            FlowSetModule module = new FlowSetModule();
            BeanUtils.copyBeanProp(module, t);
            if (t.getRoleIds() != null &&!t.getRoleIds().isEmpty()) {
                module.setRoleIds(StringUtils.join(t.getRoleIds(), Constants.SPLITE_BY));
            } else {
                module.setRoleIds(null);
            }

            module.setId(IdWorker.getId());
            module.setFlowSetMainId(flowSetMain.getId());
            module.setEdition(nowEdition);
            module.setCurrentFlag(FlowConstant.NOW_FLAG);
            module.setModuleCode(IdWorker.get32UUID());
            if (StringUtils.isEmpty(t.getFlowInType())) {
                module.setFlowInType(FlowApplyTypeEnum.APPLY_SINGLE.getInfo());
            }
            if (StringUtils.isEmpty(t.getApplyType())) {
                module.setApplyType(FlowApplyTypeEnum.APPLY_SINGLE.getInfo());
            }
            moduleList.add(module);
            idChange.put(t.getId(), module.getId());
        });
        edges.stream().forEach(t -> {
            FlowSetCondition condition = new FlowSetCondition();
            BeanUtils.copyBeanProp(condition, t);
            condition.setId(IdWorker.getId());
            condition.setFlowSetMainId(flowSetMain.getId());
            condition.setEdition(nowEdition);
            condition.setFlowModuleId(idChange.get(t.getSourceNodeId()));
            condition.setNextFlowModuleId(idChange.get(t.getTargetNodeId()));
            conditionList.add(condition);
            idChange.put(t.getId(), condition.getId());
        });
        this.batchSaveModule(moduleList);
        this.batchSaveCondition(conditionList);
        redisCache.delCacheMapValue(FlowConstant.FLOW_MAP_KEY, FlowConstant.FLOW_MAIN_KEY + flowSetMain.getFlowKey());
        selectInfoByKey(flowSetMain.getFlowKey());
        redisCache.delCacheMapValue(FlowConstant.FLOW_MAP_KEY, FlowConstant.FLOW_MAIN_ID + flowSetMain.getId());
        selectInfoById(flowSetMain.getId());
    }

    /**
     * 批量保存节点
     *
     * @param moduleList
     */
    private void batchSaveModule(List<FlowSetModule> moduleList) {
        //快速批量保存
        for (int i = 0; i < moduleList.size(); i = i + Constants.BATCH_INSERT_MAX) {
            int endNum = i + Constants.BATCH_INSERT_MAX;
            if (moduleList.size() <= endNum) {
                endNum = moduleList.size();
            }
            List<FlowSetModule> inserts = moduleList.subList(i, endNum);
            if (!inserts.isEmpty()) {
                flowSetModuleMapper.insertBatch(inserts);
            }
        }
    }

    /**
     * 批量保存路径
     *
     * @param conditionList
     */
    private void batchSaveCondition(List<FlowSetCondition> conditionList) {
        //快速批量保存
        for (int i = 0; i < conditionList.size(); i = i + Constants.BATCH_INSERT_MAX) {
            int endNum = i + Constants.BATCH_INSERT_MAX;
            if (conditionList.size() <= endNum) {
                endNum = conditionList.size();
            }
            List<FlowSetCondition> inserts = conditionList.subList(i, endNum);
            if (!inserts.isEmpty()) {
                flowSetConditionMapper.insertBatch(inserts);
            }
        }
    }
}

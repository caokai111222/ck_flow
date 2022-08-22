package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.workflow.constant.FlowConstant;
import com.ruoyi.workflow.domain.FormItem;
import com.ruoyi.workflow.domain.FormMain;
import com.ruoyi.workflow.domain.dto.FormMainDto;
import com.ruoyi.workflow.mapper.FormItemMapper;
import com.ruoyi.workflow.mapper.FormMainMapper;
import com.ruoyi.workflow.service.IFormMainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FormMainServiceImpl extends ServiceImpl<FormMainMapper, FormMain> implements IFormMainService {
    @Autowired
    private FormMainMapper formMainMapper;
    @Autowired
    private FormItemMapper formItemMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public FormMain selectInfoById(Long id) {
        FormMain main = redisCache.getCacheMapValue(FlowConstant.FORM_MAP_KEY, FlowConstant.FORM_MAIN_ID + id);
        if (main == null) {
            main = formMainMapper.selectById(id);
            main = fullInfo(main);
            redisCache.setCacheMapValue(FlowConstant.FORM_MAP_KEY, FlowConstant.FORM_MAIN_ID + id, main);
        }
        return main;
    }

    /**
     * 补全数据
     *
     * @param main
     * @return
     */
    private FormMain fullInfo(FormMain main) {
        List<FormItem> items = formItemMapper.selectList(new LambdaQueryWrapper<FormItem>()
                .eq(FormItem::getFormId, main.getId())
                .eq(FormItem::getDelFlag, Constants.NOT_DEL));
        main.setItems(items);
        List<FormItem> itemList = new ArrayList<>();
        for (FormItem item : items) {
            if (null == item.getPid()) {
                recursionFn(item, items);
                itemList.add(item);
            }
        }
        main.setItemList(itemList);
        return main;
    }

    /**
     * 递归获取children
     *
     * @param item
     * @param items
     */
    private void recursionFn(FormItem item, List<FormItem> items) {
        List<FormItem> children = items.stream().filter(t -> item.getId().equals(t.getPid())).collect(Collectors.toList());
        if (!children.isEmpty()) {
            for (FormItem tChild : children) {
                recursionFn(tChild, items);
            }
        }
        item.setChildren(children);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFormMain(FormMainDto formMainDto) {
        FormMain old;
        LoginUser user = SecurityUtils.getLoginUser();
        if (formMainDto.getId() != null) {
            old = selectInfoById(formMainDto.getId());
            old.setNowFlag(FlowConstant.NOT_NOW_FLAG);
            old.setUpdateBy(user.getUsername());
            old.setUpdateTime(DateUtils.getNowDate());
            formMainMapper.updateById(old);
            formMainDto.setEdition(old.getEdition() + 1);
            formMainDto.setFormCode(old.getFormCode());
            formMainDto.setId(null);
        } else {
            formMainDto.setEdition(0);
            formMainDto.setFormCode(IdWorker.get32UUID());
        }
        FormMain main = new FormMain();
        formMainDto.setCreateBy(user.getUsername());
        formMainDto.setCreateTime(DateUtils.getNowDate());
        formMainDto.setDelFlag(Constants.NOT_DEL);
        BeanUtils.copyProperties(formMainDto, main);
        saveOrUpdate(main);
        List<FormItem> children = new ArrayList<>();
        Map<String, Long> idKeyMap = new HashMap();
        formMainDto.getItems().forEach(t -> {
            t.setFormId(main.getId());
            addItem(t, children, idKeyMap);
        });
        //快速批量保存
        for (int i = 0; i < children.size(); i = i + Constants.BATCH_INSERT_MAX) {
            int endNum = i + Constants.BATCH_INSERT_MAX;
            if (children.size() <= endNum) {
                endNum = children.size();
            }
            List<FormItem> inserts = children.subList(i, endNum);
            if (!inserts.isEmpty()) {
                formItemMapper.insertBatch(inserts);
            }
        }
        // 刷新缓存
        redisCache.delCacheMapValue(FlowConstant.FORM_MAP_KEY, FlowConstant.FORM_MAIN_ID + main.getId());
        selectInfoById(main.getId());
    }

    /**
     * 遍历子级添加
     *
     * @param t
     * @param children
     * @param idKeyMap
     */
    private void addItem(FormItem t, List<FormItem> children, Map<String, Long> idKeyMap) {
        if (null == t.getId()) {
            t.setId(IdWorker.getId());
            idKeyMap.put(t.getKey(), t.getId());
        }
        if (StringUtils.isNotEmpty(t.getPkey())) {
            t.setPid(idKeyMap.get(t.getPkey()));
        }
        children.add(t);
      /*  if (t.getChildren() != null && !t.getChildren().isEmpty()) {
            t.getChildren().stream().forEach(n -> {
                n.setPid(t.getId());
                n.setFormId(t.getFormId());
                addItem(n, children, idKeyMap);
            });
        }*/
    }
}

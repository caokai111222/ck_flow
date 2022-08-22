
import moment from 'moment'
import {getDicts} from "@/api/system/dict/data";

// 渲染数据处理
export function doWithFormData (t, dt) {
  // 数据来源加载
  if (['select', 'radio', 'checkbox'].indexOf(t.type) > -1 && 'dict' === t.options.sourceType) {
    getDicts(t.options.dictType).then(res => {
      const dictList = res.data || []
      const ops = []
      dictList.forEach(o => {
        ops.push({
          label: o.dictLabel,
          value: o.dictValue
        })
      })
      t.options.options = ops
    })
  }
  let res
  if ((t.type === 'select' && t.options.isMulty) || t.type === 'checkbox') {
    // 多选数据处理
    if (!dt) {
      res = []
    }
    if (typeof dt === "string") {
      res = dt.split(',')
    } else {
      res = dt
    }
  } else if ('date' === t.type) {
    // 日期时间格式处理
    if (typeof dt === "string") {
      switch (t.options.modeltype) {
        case  'week':res = moment(dt, 'yyyy 第 WW 周')
          break
        case  'range':
          const times = dt.split(',')
          const begin = moment(times[0])
          const end = moment(times[1])
          res = [begin, end]
          break
        default:
          res = dt ? moment(dt) : null
      }
    } else {
      res = dt ? moment(dt) : null
    }
  } else if ('timepicker' === t.type) {
    res = dt ? moment(dt, t.options.timeformat) : null
  } else if (['rate', 'inputnumber'].indexOf(t.type) > -1) {
    // 数字格式处理
    res = dt ? parseFloat(dt) : null
  } else if ('table' === t.type) {
    if (typeof dt === "string") {
      res = JSON.parse(dt)
    } else {
      res = dt
    }
    res.forEach(d => {
      t.list.forEach(l => {
        d[l.options.filed] = doWithFormData(l, d[l.options.filed])
      })
    })
  } else {
    // 其他正常字符串格式
    res = dt
  }
  return res
}
// 渲染数据和必填处理
export function doWithFormShow (list, formData, rules, saveData, dicts) {
  list.forEach(t => {
    let dt = t.options.defaultValue
    if ('table' === t.type) {
      dt = t.datas
    }
    if (saveData?.length) {
      dt =  null
      const sdt = saveData.find(n => { return n.itemName === t.options.filed })
      dt = sdt ? sdt.itemValue : null
    }
    if (t && t.options && t.options.required) {
      rules[t.options.filed] = [
        { required: true, message: `${t.options.labelTitle}不能为空`, trigger: "blur" }
      ]
    }
    formData[t.options.filed] = doWithFormData(t, dt)
  })
}
export function res (c) {
  const dt = c.configJson ? {...JSON.parse(c.configJson),
    id: c.id
  } : c
  if (dt.options.options) {
    dt.options.options.forEach(t=>{
      t.value = t.value.toString()
    })
  }
  if (dt.type === 'table') {
    const list = []
    dt.list.forEach(tt => {
      list.push(res(tt))
    })
    dt.list = list
  }
  return dt
}
// 把数据库的数据转成需要显示的数据
export function doWithFormSaveData (data) {
  const fromInfo = {
    formid: data.id,
    formName: data.formName,
    formCode: data.formCode,
    formDesc: data.formDesc,
    items: data.items || []
  }
  const layoutitemlist = []
  if (data.items?.length) {
    data.items.filter(t => { return ['gridPanel', 'tabs', 'collapse'].indexOf(t.itemType) > -1 }).forEach((t, idx) => {
      const layoutItem = JSON.parse(t.configJson)
      const children = data.items.filter(n => { return n.pid === t.id })
      const cls = []
      children.forEach(c => {
        cls.push(res(c))
      })
      if ('gridPanel' === t.itemType) {
        layoutItem.columns=[{
          span: 24,
          list: cls
        }]
      } else {
        cls.forEach(c => {
          const items = []
          const itemChildren = data.items.filter(n => { return n.pid === c.id })
          itemChildren.forEach(it => {
            items.push(res(it))
          })
          c.columns=[{
            span: 24,
            list: items
          }]
        })
        layoutItem.options.panelist = cls
      }
      layoutitemlist.push(layoutItem)
    })
  }
  fromInfo.layoutitemlist = [{widgetForm:{list:layoutitemlist}}]
  return fromInfo
}
// 数据处理
export function doWithForm (data, t) {
let res = data
  if ((t.type === 'select' && t.options.isMulty) || t.type === 'checkbox') {
    res = data.join(',')
  } else if ('date' === t.type) {
    switch (t.options.modeltype) {
      case  'date':res = moment(data).format(t.options.format)
        break
      case  'month':res = moment(data).format('yyyy-MM')
        break
      case  'week':res = moment(data).format('yyyy 第 WW 周')
        break
      case  'range':
        const begin = moment(data[0]).format(t.options.format)
        const end = moment(data[1]).format(t.options.format)
        res= [begin, end].join(',')
        break
    }
  } else if ('timepicker' === t.type) {
    res = moment(data).format(t.options.timeformat)
  }
  return res
}
// 把表单数据处理给后台保存
export function doWithFormForFlow (data, flowInfo) {
  const items = flowInfo.items || []
  const res = {...data}
  let isOk = true
  const msg = []
  items.forEach(item => {
    const t = JSON.parse(item.configJson)
    if (res[t.options.filed]) {
      res[t.options.filed] = doWithForm(res[t.options.filed], t)
      if ('table' === t.type) {
        res[t.options.filed].forEach((d, i) => {
          t.list.forEach(l => {
            d[l.options.filed] = doWithForm(d[l.options.filed], l)
            if (l.options.required && !(d[l.options.filed] || d[l.options.filed] === 0) ) {
              isOk = false
              msg.push(`<p>子表单${t.options.labelTitle}的第${(i+1)}行数据,${l.options.labelTitle}列不能为空</p>`)
            }
          })
        })
      }
    }
  })
  return {isOk, msg, data: res}
}

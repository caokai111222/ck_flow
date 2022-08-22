export const layoutComponents = [{
  type: 'gridPanel',
  name: '行控件',
  icon: 'el-icon-s-grid',
  isShowSpanSetting: true,
  columns: [
    {
      span: 24,
      list: []
    }
  ],
  options: {
    labelTitle: '',
    langId: '',
    idPrefix: 'pnl_',
    controlId: '',
    tagattr: ''
  }
},
{
  type: 'tabs',
  name: '标签页',
  icon: 'el-icon-collection-tag',
  layout: 'inline',
  isShowSpanSetting: true,
  options: {
    idPrefix: 'tabs_',
    describe: '',
    type: '',
    tabPosition: 'top',
    panelist: [
      {
        name: 'tab1',
        title: '用户管理',
        columns: [
          {
            span: 24,
            list: []
          }
        ]
      },
      {
        name: 'tab2',
        title: '用户管理2',
        columns: [
          {
            span: 24,
            list: []
          }
        ]
      }
    ],
    tagattr: ''
  }
},
{
  type: 'collapse',
  name: '折叠面板',
  icon: 'el-icon-s-fold',
  layout: 'inline',
  isShowSpanSetting: true,
  options: {
    describe: '',
    panelist: [
      {
        name: 'collapse1',
        title: '面板1',
        columns: [
          {
            span: 24,
            list: []
          }
        ]
      },
      {
        name: 'collapse2',
        title: '面板2',
        columns: [
          {
            span: 24,
            list: []
          }
        ]
      }
    ],
    tagattr: ''

  }
}
]

export const basicComponents = [{
  type: 'input',
  name: '单行文本',
  dragType: 'control',
  icon: 'el-icon-postcard',
  isShowCheck: true,
  isShowSpanSetting: true,
  layout: 'inline',
  eventlist: [],
  options: {
    labelTitle: '单行文本',
    defaultValue: '',
    placeholder: '',
    required: false,
    dataType: 'string',
    dataLength: '50',
    ctrlSpan: 12,
    filed: '',
    tagattr: ''
  }
},
{
  type: 'textarea',
  name: '多行文本',
  dragType: 'control',
  icon: 'el-icon-notebook-2',
  isShowCheck: true,
  isShowSpanSetting: true,
  eventlist: [],
  options: {
    labelTitle: '多行文本',
    defaultValue: '',
    placeholder: '',
    dataLength: '500',
    rows: 3,
    required: false,
    ctrlSpan: 24,
    filed: '',
    tagattr: ''
  }
},
{
  type: 'select',
  name: '下拉列表',
  dragType: 'control',
  icon: 'el-icon-finished',
  isShowCheck: true,
  isShowSpanSetting: true,
  layout: 'inline',
  eventlist: [],
  options: {
    labelTitle: '下拉列表',
    ctrlSpan: 12,
    sourceType: 'userInput',
    dictType: '',
    options: [
      {
        label: '选项1',
        value: '1'
      },
      {
        label: '选项2',
        value: '2'
      }
    ],
    isRemote: false,
    sourceApi: '',
    valueField: 'Value',
    textField: 'Text',
    defaultValue: '',
    isMulty: false,
    isSearch: false,
    required: false,
    edit: '',
    placeholder: '',
    filed: '',
    tagattr: ''
  }
},
{
  type: 'date',
  name: '日期控件',
  dragType: 'control',
  icon: 'el-icon-date',
  isShowCheck: true,
  isShowSpanSetting: true,
  layout: 'inline',
  eventlist: [],
  options: {
    labelTitle: '日期控件',
    langId: '',
    placeholder: '',
    dataType: 'date',
    format: 'yyyy-MM-DD',
    required: false,
    ctrlSpan: 12,
    modeltype: 'date',
   // isshowtime: false,
    disabled: false,
    filed: '',
    tagattr: ''
  }
},{
    type: 'timepicker',
    name: '时间选择',
    dragType: 'control',
    icon: 'el-icon-timer',
    layout: 'inline',
    isShowSpanSetting: true,
    eventlist: [],
    options: {
      labelTitle: '时间选择',
      ctrlSpan: 12,
      disabled: false,
      timeformat: 'HH:mm:ss',
      minute: 1,
      second: 1,
      required: false,
      filed: '',
      tagattr: ''

    }
  },
{
  type: 'radio',
  name: '单选控件',
  icon: 'el-icon-thumb',
  dragType: 'control',
  isShowCheck: true,
  isShowSpanSetting: true,
  layout: 'inline',
  eventlist: [],
  options: {
    labelTitle: '单选项',
    ctrlSpan: 12,
    sourceType: 'userInput',
    dictType: '',
    options: [
      {
        label: '选项1',
        value: '1'
      }
    ],
    defaultValue: '',
    filed: '',
    required: false,
    tagattr: ''
  }
},
{
  type: 'checkbox',
  name: '多选控件',
  icon: 'el-icon-circle-check',
  dragType: 'control',
  isShowCheck: true,
  isShowSpanSetting: true,
  layout: 'inline',
  eventlist: [],
  options: {
    labelTitle: '多选项',
    ctrlSpan: 12,
    sourceType: 'userInput',
    dictType: '',
    options: [
      {
        label: '选项1',
        value: '1'
      }
    ],
    defaultValue: [],
    filed: '',
    required: false,
    tagattr: ''
  }
},
{
  type: 'editor',
  name: '富文本',
  icon: 'el-icon-document',
  layout: 'inline',
  isShowSpanSetting: true,
  options: {
    labelTitle: '富文本',
    defaultValue: '',
    placeholder: '',
    required: false,
    ctrlSpan: 24,
    height: 350,
    filed: '',
    tagattr: ''
  }
},
{
  type: 'rate',
  name: 'Rate评分',
  icon: 'el-icon-star-off',
  dragType: 'control',
  layout: 'inline',
  isShowSpanSetting: true,
  eventlist: [],
  options: {
    ctrlSpan: 12,
    labelTitle: 'Rate评分',
    defaultValue: '',
    filed: '',
    required: false,
    tagattr: ''
  }
},
{
  type: 'inputnumber',
  name: '数字控件',
  icon: 'el-icon-circle-plus-outline',
  dragType: 'control',
  layout: 'inline',
  isShowSpanSetting: true,
  eventlist: [],
  options: {
    ctrlSpan: 12,
    labelTitle: '数字',
    defaultValue: '',
    filed: '',
    min: '-10000000000',
    max: '10000000000',
    precision: '0',
    step: '1',
    required: false,
    tagattr: ''
  }
},
{
  type: 'imgage',
  name: '图片控件',
  icon: 'el-icon-picture-outline',
  layout: 'inline',
  isShowSpanSetting: true,
  eventlist: [],
  options: {
    labelTitle: '图片',
    langId: '',
    idPrefix: 'img_',
    describe: '',
    filed: '',
    tagattr: '',
    limit: 1,
    fileSize: 10,
    fileType: '',
    isShowTip: true
  }
},
  {
    type: 'upFilesComp',
    name: '上传控件',
    icon: 'el-icon-upload2',
    layout: 'inline',
    isShowSpanSetting: true,
    options: {
      labelTitle: '上传',
      required: false,
      filed: '',
      tagattr: '',
      limit: 1,
      fileSize: 10,
      fileType: '',
      isShowTip: true
    }
  },]
export const platformComponents = [{
  type: 'user',
  name: '选人控件',
  icon: 'el-icon-user',
  dragType: 'control',
  isShowCheck: true,
  isShowSpanSetting: true,
  options: {
    labelTitle: '选人控件',
    langId: '',
    idPrefix: 'usr_',
    controlId: '',
    describe: '',
    required: false,
    ctrlSpan: 12,
    defaultValue: '',
    defaultText: '',
    multy: 'false',
    confirmFunName: '',
    confirm: '',
    edit: 'false',
    deptId: '',
    showUserid: 'false',
    placeholder: '',
    init: '',
    filed: '',
    tagattr: ''
  }
},
{
  type: 'department',
  name: '部门单选',
  icon: 'el-icon-school',
  isShowCheck: true,
  isShowSpanSetting: true,
  dragType: 'control',
  options: {
    labelTitle: '部门单选',
    langId: '',
    idPrefix: 'dep_',
    controlId: '',
    defaultValue: '',
    required: false,
    ctrlSpan: 12,
    filed: '',
    tagattr: ''
  }
},
  {
    type: 'departmentMutl',
    name: '部门多选',
    icon: 'el-icon-office-building',
    isShowCheck: true,
    isShowSpanSetting: true,
    dragType: 'control',
    layout: 'inline',
    options: {
      labelTitle: '部门多选',
      langId: '',
      idPrefix: 'dep_',
      controlId: '',
      defaultValue: '',
      required: false,
      ctrlSpan: 12,
      filed: '',
      tagattr: ''
    }
  },
/*{
  type: 'wmpDetail',
  name: '明细控件',
  icon: 'el-icon-s-grid',
  options: {
    labelTitle: '明细控件',
    langId: '',
    idPrefix: 'tbl_',
    controlId: 'wmpdetail1',
    keyField: 'FID',
    enableChoose: true,
    multy: false,
    showNo: true,
    columns: [
      {
        type: 'showno',
        name: '序号',
        width: '60',
        options: { labelTitle: '序号' }
      }
    ],
    tagattr: ''
  }
},
{
  type: 'listview',
  name: '列表控件',
  icon: 'el-icon-s-grid',
  isShowCheck: true,
  options: {
    labelTitle: '列表控件',
    langId: '',
    idPrefix: 'lst_',
    controlId: 'view1',
    sourceApi: '',
    enableChoose: false,
    multy: false,
    showNo: true,
    columns: [
    ],
    tagattr: ''
  }
},*/
  {
    type: 'table',
    name: '子表单',
    icon: 'el-icon-film',
    layout: 'inline',
    isShowSpanSetting: true,
    list: [],
    columns:[{
      span: 24,
      list: []
    }],
    datas:[{}],
    options: {
      describe: '',
      type: '',
      tabPosition: 'top',
      tagattr: '',
      filed: '',
      labelTitle: '子表单',
      required: false,
    }
  }]

export const controlComponents = [
  { controls: layoutComponents, title: '布局控件', draggablebind: { group: { name: 'people', pull: 'clone', put: false }, animation: 150, sort: false, ghostClass: 'ghost' } },
  { controls: basicComponents, title: '基础控件', draggablebind: { group: { name: 'people1', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' } },
  { controls: platformComponents, title: '高级控件', draggablebind: { group: { name: 'people1', pull: 'clone', put: false }, sort: false, ghostClass: 'ghost' } }
]

import request from '@/utils/request'

// 查询流程列表
export function listFlowMain(query) {
  return request({
    url: '/flowSet/list',
    method: 'get',
    params: query
  })
}
// 删除流程
export function delFlowMain(id) {
  return request({
    url: '/flowSet/' + id,
    method: 'delete'
  })
}
// 获取
export function getFlowMain(id) {
  return request({
    url: '/flowSet/' + id,
    method: 'get'
  })
}

// 新增
export function addFlowMain(data) {
  return request({
    url: '/flowSet',
    method: 'post',
    data: data
  })
}

// 修改
export function updateFlowMain(data) {
  return request({
    url: '/flowSet',
    method: 'put',
    data: data
  })
}
// 发起
export function flowAddStart(data) {
  return request({
    url: '/flow/add',
    method: 'post',
    data: data
  })
}
// 发起
export function flowToNext(data) {
  return request({
    url: '/flow/flowToNext',
    method: 'post',
    data: data
  })
}
// 查询待办
export function myTodo(query) {
  return request({
    url: '/flow/myTodo',
    method: 'get',
    params: query
  })
}
// 查询审批记录
export function flowRecord(query) {
  return request({
    url: '/flow/flowRecord',
    method: 'get',
    params: query
  })
}
// 查询审批明细
export function getFormDataById(query) {
  return request({
    url: '/flow/getFormDataById',
    method: 'get',
    params: query
  })
}

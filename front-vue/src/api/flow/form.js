import request from '@/utils/request'
// 查询列表
export function listFormMain(query) {
  return request({
    url: '/formMain/list',
    method: 'get',
    params: query
  })
}
// 新增
export function addFormMain(data) {
  return request({
    url: '/formMain',
    method: 'post',
    data: data
  })
}

// 修改
export function updateFormMain(data) {
  return request({
    url: '/formMain',
    method: 'put',
    data: data
  })
}
// 获取
export function getFormMain(id) {
  return request({
    url: '/formMain/' + id,
    method: 'get'
  })
}

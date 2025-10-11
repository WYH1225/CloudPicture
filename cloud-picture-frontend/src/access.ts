import router from '@/router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'

// 是否为首次获取登录用户
let firstFetchLoginUser = true

/**
 * 全局权限校验，每次切换页面时都会执行
 */
router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser
  // 确保页面刷新时，首次加载时，能等待后端返回用户信息后再校验权限
  if (firstFetchLoginUser) {
    await loginUserStore.fetchLoginUser()
    loginUser = loginUserStore.loginUser
    firstFetchLoginUser = false
  }
  const toUrl = to.fullPath
  // 自定义权限校验规则
  if (toUrl.startsWith('/admin')) {
    if (!loginUser || loginUser.userRole !== 'admin') {
      message.error('无权限访问')
      next(`/user/login?redirect=${to.fullPath}`)
      return
    }
  }
  next()
})

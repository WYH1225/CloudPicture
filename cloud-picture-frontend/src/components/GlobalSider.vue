<template>
  <div id="globalSider">
    <a-layout-sider
      v-if="loginUserStore.loginUser.id"
      breakpoint="lg"
      width="200"
      collapsed-width="0"
      :zeroWidthTriggerStyle="{ display: 'none' }"
    >
      <a-menu
        v-model:selectedKeys="current"
        mode="inline"
        :items="menuItems"
        @click="doMenuClick"
      />
    </a-layout-sider>
  </div>
</template>
<script lang="ts" setup>
import { computed, h, ref, watch, watchEffect } from 'vue'
import { PictureOutlined, UserOutlined, TeamOutlined } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { SPACE_TYPE_ENUM } from '@/constants/space.ts'
import { listMyTeamSpaceUsingPost } from '@/api/spaceUserController.ts'
import { message } from 'ant-design-vue'

const loginUserStore = useLoginUserStore()

// 固定的菜单项
const fixedMenuItems = [
  {
    key: '/',
    label: '公共图库',
    icon: () => h(PictureOutlined),
  },
  {
    key: '/my_space',
    label: '我的空间',
    icon: () => h(UserOutlined),
  },
  {
    key: `/add_space?type=${SPACE_TYPE_ENUM.TEAM}`,
    label: '创建团队',
    icon: () => h(TeamOutlined),
  },
]

const teamSpaceList = ref<API.SpaceUserVO[]>([])
const menuItems = computed(() => {
  // 如果没有团队空间，则只展示固定菜单项
  if (teamSpaceList.value.length < 1) {
    return fixedMenuItems
  }
  // 如果用户有团队空间，则展示固定菜单项和团队空间菜单项
  const teamSpaceSubMenus = teamSpaceList.value.map((spaceUser) => {
    const space = spaceUser.space
    return {
      key: '/space/' + spaceUser.spaceId,
      label: space?.spaceName,
    }
  })
  const teamSpaceMenuGroup = {
    type: 'group',
    label: '我的团队',
    key: 'teamSpace',
    children: teamSpaceSubMenus,
  }
  return [...fixedMenuItems, teamSpaceMenuGroup]
})

// 加载我的空间团队列表
const fetchTeamSpaceList = async () => {
  const res = await listMyTeamSpaceUsingPost()
  if (res.data.code === 0 && res.data.data) {
    teamSpaceList.value = res.data.data
  } else {
    message.error('加载我的团队空间列表失败: ' + res.data.message)
  }
}

// 监听变量，改变时触发数据的重新加载
watchEffect(() => {
  // 登录才加载
  if (loginUserStore.loginUser.id) {
    fetchTeamSpaceList()
  }
})

const router = useRouter()
// 当前要高亮的菜单项
const current = ref<string[]>([])
// 当前路由变化，更新高亮菜单项
router.afterEach((to, from, next) => {
  if (to.path.startsWith('/space/')) {
    // 检查来源页面是否是 /my_space，如果是则高亮"我的空间"
    if (from.path === '/my_space' || from.name === '我的空间') {
      current.value = ['/my_space']
    } else {
      // 否则高亮具体的团队空间项
      current.value = [to.path]
    }
  } else if (to.path === '/add_space') {
    // 检查查询参数来确定应该高亮哪个菜单项
    if (to.query.type && Number(to.query.type) === SPACE_TYPE_ENUM.TEAM) {
      current.value = [`/add_space?type=${SPACE_TYPE_ENUM.TEAM}`]
    } else {
      current.value = ['/my_space']
    }
  } else if (to.path === '/' || to.name === 'home') {
    // 主页高亮"公共图库"
    current.value = ['/']
  } else {
    current.value = [to.path]
  }
})

// 路由跳转事件
const doMenuClick = ({ key }) => {
  router.push(key)
}
</script>

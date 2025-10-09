<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <BasicLayout />
    </div>
  </a-config-provider>
  
</template>

<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue';
import { healthUsingGet } from './api/mainController';
import { useLoginUserStore } from './stores/useLoginUserStore';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
dayjs.locale('zh-cn');
const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser()

healthUsingGet().then((res) => {
  console.log(res);
});
</script>

<style scoped>

</style>

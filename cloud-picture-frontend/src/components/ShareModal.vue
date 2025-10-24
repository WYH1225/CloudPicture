<template>
  <div>
    <a-modal v-model:open="visible" :title="props.title" :footer="false" @cancel="closeModal">
      <h4>复制分享链接</h4>
      <a-typography-link copyable>
        {{ props.link }}
      </a-typography-link>
      <div style="margin-bottom: 16px" />
      <h4>手机扫码查看</h4>
      <a-qrcode :value="props.link" />
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue';

interface Props {
  title?: string,
  link?: string,
}

const props = withDefaults(defineProps<Props>(), {
  title: '分享图片',
  link: 'https://baidu.com',
})

const visible = ref<boolean>(false);

const openModal = () => {
  visible.value = true;
};

const closeModal = () => {
  visible.value = false
};

// 暴露方法给父组件调用
defineExpose({
  openModal,
})
</script>


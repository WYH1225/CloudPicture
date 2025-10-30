<template>
  <div class="space-usage-analyze">
    <a-flex gap="middle">
      <a-card title="存储空间" style="width: 50%">
        <div style="height: 320px; text-align: center">
          <h3>
            {{ formatSize(data.usedSize) }} /
            {{ data.maxSize ? formatSize(data.maxSize) : '无限制' }}
          </h3>
          <a-progress type="dashboard" :percent="data.sizeUsageRatio ?? 0" />
        </div>
      </a-card>
      <a-card title="图片数量" style="width: 50%">
        <div style="height: 320px; text-align: center">
          <h3>{{ data.usedCount }} / {{ data.maxCount ?? '无限制' }}</h3>
          <a-progress type="dashboard" :percent="data.countUsageRatio ?? 0" />
        </div>
      </a-card>
    </a-flex>
  </div>
</template>

<script lang="ts" setup>
import { ref, watchEffect } from 'vue'
import { getSpaceUsageAnalyzeUsingPost } from '@/api/spaceAnalyzeController.ts'
import { message } from 'ant-design-vue'
import { formatSize } from '@/utils'

interface Props {
  spaceId?: number
  queryPublic?: boolean
  queryAll?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  queryPublic: false,
  queryAll: false,
})

const loading = ref<boolean>(true)

// 图表数据
const data = ref<API.SpaceUsageAnalyzeResponse>({})

// 获取数据
const fetchData = async () => {
  loading.value = true
  const res = await getSpaceUsageAnalyzeUsingPost({
    spaceId: props.spaceId,
    queryPublic: props.queryPublic,
    queryAll: props.queryAll,
  })
  if (res.data.code === 0 && res.data.data) {
    data.value = res.data.data
  } else {
    message.error('获取数据失败: ' + res.data.message)
  }
  loading.value = false
}

/**
 * 监听变量，参数变化时触发数据的重新加载
 */
watchEffect(() => {
  fetchData()
})
</script>

<style scoped></style>

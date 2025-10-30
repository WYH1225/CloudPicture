<template>
  <div class="space-rank-analyze">
    <a-card title="空间使用排行分析">
      <v-chart :option="options" style="height: 320px; max-width: 100%" :loading="loading" />
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import VChart from 'vue-echarts'
import 'echarts'
import { computed, ref, watchEffect } from 'vue'
import { getSpaceRankAnalyzeUsingPost } from '@/api/spaceAnalyzeController.ts'
import { message } from 'ant-design-vue'

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
const dataList = ref<API.Space[]>([])

// 获取数据
const fetchData = async () => {
  loading.value = true
  const res = await getSpaceRankAnalyzeUsingPost({
    spaceId: props.spaceId,
    queryPublic: props.queryPublic,
    queryAll: props.queryAll,
    topN: 10, // 后端默认是 10
  })
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data ?? []
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

// 图表选项
const options = computed(() => {
  const spaceName = dataList.value.map((item) => item.spaceName)
  const usageData = dataList.value.map((item) => (item.totalSize / (1024 * 1024)).toFixed(2))

  return {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: spaceName },
    yAxis: { type: 'value', name: '空间使用量（MB）' },
    series: [
      {
        name: '空间使用量（MB）',
        type: 'bar',
        data: usageData,
        itemStyle: {
          color: '#5470C6',
        },
      },
    ],
  }
})
</script>

<style scoped></style>

<template>
  <div class="space-category-analyze">
    <a-card title="空间图片分类分析">
      <v-chart :option="options" style="height: 320px; max-width: 100%" :loading="loading" />
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import VChart from 'vue-echarts'
import 'echarts'
import { computed, ref, watchEffect } from 'vue'
import { getSpaceCategoryAnalyzeUsingPost } from '@/api/spaceAnalyzeController.ts'
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
const dataList = ref<API.SpaceCategoryAnalyzeResponse[]>([])

// 获取数据
const fetchData = async () => {
  loading.value = true
  const res = await getSpaceCategoryAnalyzeUsingPost({
    spaceId: props.spaceId,
    queryPublic: props.queryPublic,
    queryAll: props.queryAll,
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
  const categories = dataList.value.map((item) => item.category)
  const countData = dataList.value.map((item) => item.count)
  const sizeData = dataList.value.map((item) => (item.totalSize / (1024 * 1024)).toFixed(2))
  return {
    tooltip: { trigger: 'axis' },
    legend: { data: ['图片数量', '图片总大小'], top: 'bottom' },
    xAxis: { type: 'category', data: categories },
    yAxis: [
      {
        type: 'value',
        name: '图片数量',
        axisLine: { show: true, lineStyle: { color: '#5470C6' } },
      },
      {
        type: 'value',
        name: '图片总大小（MB）',
        position: 'right',
        axisLine: { show: true, lineStyle: { color: '#91CC75' } },
        splitLine: {
          lineStyle: {
            color: '#91CC75',
            type: 'dashed',
          },
        },
      },
    ],
    series: [
      { name: '图片数量', type: 'bar', data: countData, yAxisIndex: 0 },
      { name: '图片总大小', type: 'bar', data: sizeData, yAxisIndex: 1 },
    ],
  }
})
</script>

<style scoped></style>

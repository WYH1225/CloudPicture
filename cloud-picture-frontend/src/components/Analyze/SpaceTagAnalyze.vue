<template>
  <div class="space-tag-analyze">
    <a-card title="空间图片标签分析">
      <v-chart :option="options" style="height: 320px; max-width: 100%" :loading="loading" />
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import VChart from 'vue-echarts'
import 'echarts'
import 'echarts-wordcloud'
import { computed, ref, watchEffect } from 'vue'
import { getSpaceTagAnalyzeUsingPost } from '@/api/spaceAnalyzeController.ts'
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
const dataList = ref<API.SpaceTagAnalyzeResponse[]>([])

// 获取数据
const fetchData = async () => {
  loading.value = true
  const res = await getSpaceTagAnalyzeUsingPost({
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
  const tagData = dataList.value.map((item) => ({
    name: item.tag,
    value: item.count,
  }))

  return {
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => `${params.name}: ${params.value} 次`,
    },
    series: [
      {
        type: 'wordCloud',
        gridSize: 10,
        sizeRange: [12, 50],
        rotationRange: [-90, 90],
        shape: 'circle',
        textStyle: {
          fontWeight: 'bold',
          color: () =>
            `rgb(${Math.round(Math.random() * 255)},
            ${Math.round(Math.random() * 255)},
            ${Math.round(Math.random() * 255)})`
        },
        data: tagData,
      },
    ],
  }
})
</script>

<style scoped></style>

<template>
  <div class="space-size-analyze">
    <a-card title="空间图片大小分析">
      <v-chart :option="options" style="height: 320px; max-width: 100%" :loading="loading" />
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import VChart from 'vue-echarts'
import 'echarts'
import { computed, ref, watchEffect } from 'vue'
import {
  getSpaceSizeAnalyzeUsingPost,
} from '@/api/spaceAnalyzeController.ts'
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
const dataList = ref<API.SpaceSizeAnalyzeResponse[]>([])

// 获取数据
const fetchData = async () => {
  loading.value = true
  const res = await getSpaceSizeAnalyzeUsingPost({
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
  const pieData = dataList.value.map((item) => ({
    name: item.sizeRange,
    value: item.count,
  }))

  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a}: {b}<br/>{c} 张 ({d}%)',
    },
    legend: {
      top: 'bottom',
    },
    series: [
      {
        name: '图片大小',
        type: 'pie',
        radius: ['40%', '70%'],
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 18,
            fontWeight: 'bold'
          }
        },
        data: pieData,
      }
    ]
  }
})
</script>

<style scoped></style>

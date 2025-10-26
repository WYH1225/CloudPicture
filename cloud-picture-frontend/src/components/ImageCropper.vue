<template>
  <a-modal
    class="image-cropper"
    v-model:open="visible"
    title="编辑图片"
    :footer="false"
    @cancel="closeModal"
  >
    <!-- 图片裁切组件 -->
    <vue-cropper
      ref="cropperRef"
      :img="imageUrl"
      output-type="png"
      :info="true"
      :can-move="false"
      :can-move-box="true"
      :fixed-box="false"
      :auto-crop="true"
      :center-box="true"
      :full="true"
      :infoTrue="true"
    />
    <!-- 图片操作 -->
    <div class="image-cropper-actions">
      <a-space class="button-group">
        <a-button @click="rotateLeft">向左旋转</a-button>
        <a-button @click="rotateRight">向右旋转</a-button>
        <a-button @click="changeScale(1)">放大</a-button>
        <a-button @click="changeScale(-1)">缩小</a-button>
        <a-button type="primary" :loading="loading" @click="handleConfirm">确认</a-button>
      </a-space>
    </div>
  </a-modal>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { uploadPictureUsingPost } from '@/api/pictureController.ts'
import { message } from 'ant-design-vue'

interface Props {
  imageUrl?: string
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()

const visible = ref<boolean>(false)

const openModal = () => {
  visible.value = true
}

const closeModal = () => {
  visible.value = false
}

// 暴露方法给父组件调用
defineExpose({
  openModal,
})

// 获取图片裁切器的引用
const cropperRef = ref()

// 缩放比例
const changeScale = (num: number) => {
  cropperRef.value?.changeScale(num)
}

// 向左旋转
const rotateLeft = () => {
  cropperRef.value?.rotateLeft()
}

// 向右旋转
const rotateRight = () => {
  cropperRef.value?.rotateRight()
}

// 确认裁切
const handleConfirm = () => {
  cropperRef.value.getCropBlob((blob: Blob) => {
    // blob 为已经裁切好的图片
    const fileName = (props.picture?.name || 'image') + '.png'
    const file = new File([blob], fileName, { type: blob.type })
    // 上传图片
    handleUpload({ file })
  })
}

const loading = ref<boolean>(false)

/**
 * 上传图片
 * @param file
 */
const handleUpload = async ({ file }: any) => {
  loading.value = true
  try {
    const params: API.PictureUploadRequest = props.picture ? { id: props.picture.id } : {}
    params.spaceId = props.spaceId
    const res = await uploadPictureUsingPost(params, {}, file)
    if (res.data.code === 0 && res.data.data) {
      message.success('图片上传成功')
      // 将新图片数据返回给父组件
      props.onSuccess?.(res.data.data)
      closeModal()
    } else {
      message.error('图片上传失败, ' + res.data.message)
    }
  } catch (error) {
    console.error(error)
    message.error('图片上传失败, ' + error.message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.image-cropper {
}

.image-cropper .vue-cropper {
  height: 400px;
  margin-bottom: 16px;
}

.button-group {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

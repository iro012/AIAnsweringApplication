<script setup lang="ts">
import ShareModal from "@/components/ShareModal.vue";
import { IconShareInternal } from "@arco-design/web-vue/es/icon";
import { ref } from "vue";
import { useRouter } from "vue-router";

interface Props {
  app: API.AppVO,
}

const props = defineProps<Props>();

const router = useRouter();

// 分享弹窗的引用
const shareModalRef = ref();
// 分享链接
const shareLink = `${window.location.protocol}//${window.location.host}/app/detail/${props.app.id}`;


const doCardClick = () => {
  router.push(`/app/detail/${props.app.id}`);
};

const doShare = (e: Event) => {
  if (shareModalRef.value) {
    shareModalRef.value.openModal();
  }
  // 阻止冒泡
  e.stopPropagation();
};
</script>

<template>
  <a-card hoverable :style="{ width: '100%' }" @click="doCardClick()">
    <template #actions>
      <span class="icon-hover" @click="doShare">
        <IconShareInternal />
      </span>
    </template>
    <template #cover>
      <div
        :style="{
          height: '204px',
          overflow: 'hidden',
        }"
      >
        <img
          :style="{ width: '100%', transform: 'translateY(-20px)' }"
          alt="dessert"
          :src="app.appIcon"
        />
      </div>
    </template>
    <a-card-meta>
      <template #description>
        <div style="text-align: left">
          <h2>{{ app.appName }}</h2>
          <p>{{ app.appDesc }}</p>
          <a-space>
            <a-avatar>
              <img alt="头像" :src="app.user?.userAvatar" />
            </a-avatar>
            <span>{{ app.user?.userName }}</span>
          </a-space>
        </div>
        <ShareModal :link="shareLink" ref="shareModalRef" />
      </template>
    </a-card-meta>
  </a-card>
</template>

<style scoped></style>

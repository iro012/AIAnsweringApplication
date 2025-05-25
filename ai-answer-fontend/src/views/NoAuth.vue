<script setup lang="ts">
import message from "@arco-design/web-vue/es/message";
import { onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

onMounted(() => {
  message.error("无权限，三秒后跳回到首页");
  setTimeout(() => {
    router.push("/");
  }, 3000);
});
</script>

<template>
  <div id="no-auth">无权限</div>
</template>

<style scoped></style>

<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <!-- 如果路由以 /user 开头，则渲染 UserLayout -->
      <router-view />
    </template>
    <template v-else>
      <!-- 否则渲染 BasicLayout -->
      <BasicLayout />
    </template>
  </div>
</template>

<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { useRoute } from "vue-router";
import { getLoginUserUsingGet } from "@/api/userController";

const route = useRoute();

getLoginUserUsingGet().then((res) => {
  console.log(res);
});
</script>

<style>
#app {
}
</style>

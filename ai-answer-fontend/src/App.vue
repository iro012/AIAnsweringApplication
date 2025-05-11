<template>
 <div id="app">
  <template v-if="route.path.startsWith('/user')">
    <!-- 如果路由以 /user 开头，则渲染 UserLayout -->
    <UserLayout />
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

const route = useRoute();

</script>

<style>
#app {

}

</style>

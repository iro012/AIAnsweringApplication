<script setup lang="ts">
import { routes } from "@/router/routes";

</script>

<template>
    <a-row id="global-header" align="center" :wrap="false">
      <a-col flex="200px">
        <router-link to="/">
          <div class="title-bar">
            <img class="image" src="../assets/logo.png" alt="logo图片">
            <span class="title">ai答题应用平台</span>
          </div>
        </router-link>
      </a-col>
      <a-col flex="auto" >
      <!--导航菜单-->
        <a-menu class="menu" mode="horizontal" :default-selected-keys="['/']">
          <a-menu-item class="menu-item" v-for="item in routes" :key="routes.path">
            <router-link :to="item.path">{{ item.name }}</router-link>
          </a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="200px">
        <a-button type="primary">登录</a-button>
      </a-col>
    </a-row>
</template>

<style scoped>
#global-header {
  background-color: var(--color-neutral-2);

  .title-bar {
    display: flex;
    align-items: center;
    text-decoration: none;

    .image {
      width: 50px;
      height: 50px;
    }

    .title {
      color: #000;

    }
  }

  .menu {
    display: flex;
    justify-content: flex-start;
    box-sizing: border-box;
    width: 100%;
    padding: 10px;
    background-color: var(--color-neutral-2);
    
    .menu-item {

    }
  }
}

</style>

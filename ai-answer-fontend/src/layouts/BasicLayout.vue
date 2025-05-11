<script setup lang="ts">

import GlobalHeader from "@/components/GlobalHeader.vue";
</script>

<template>
  <div id="basic-layout">
      <a-layout>
        <a-layout-header class="header">
          <GlobalHeader/>
        </a-layout-header>
        <a-layout-content class="content">
          <router-view></router-view>
        </a-layout-content>
        <a-layout-footer class="footer">联系管理员QQ：962230485，作者：林浩铨</a-layout-footer>
      </a-layout>
  </div>
</template>

<style scoped>



#basic-layout :deep(.arco-layout-header),
#basic-layout :deep(.arco-layout-footer),
#basic-layout :deep(.arco-layout-sider-children),
#basic-layout :deep(.arco-layout-content) {
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 16px;
  font-stretch: condensed;
  text-align: center;
}

#basic-layout .header {
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
}


</style>
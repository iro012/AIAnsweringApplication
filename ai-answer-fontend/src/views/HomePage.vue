<script lang="ts" setup>
import { IconShareInternal } from "@arco-design/web-vue/es/icon";
import { listAppVoByPageUsingPost } from "@/api/appController";
import { ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import ShareModal from "@/components/ShareModal.vue";
import AppCard from "@/components/AppCard.vue";

const router = useRouter();

// 初始化搜索条件，不做修改
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

const dataList = ref<API.AppVO>([]);
const total = ref<number>();

// 搜索条件
const searchParams = ref<API.AppQueryRequest>({
  ...initSearchParams,
});

/**
 * 搜索按钮
 * @param value
 */
const search = (value: string) => {
  searchParams.value.searchText = value;
};

const onPageChange = (page: number) => {
  searchParams.value.current = page;
};

const loadData = async () => {
  const res = await listAppVoByPageUsingPost(searchParams.value);
  if (res.data.code === 0) {
    dataList.value = res.data.data?.records || [];
    total.value = res.data.data?.total || 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

watchEffect(() => {
  loadData();
});
</script>

<template>
  <div class="home">
    <!-- 搜索框 -->
    <a-space direction="vertical" size="large">
      <a-input-search
        :style="{ width: '320px' }"
        placeholder="快速发现答题应用"
        button-text="搜索"
        search-button
        @search="search"
      />
      <a-list
        :grid-props="{ gutter: [20, 20], sm: 24, md: 12, lg: 8, xl: 6 }"
        :data="dataList"
        :bordered="false"
        :pagination-props="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total,
        }"
        @page-change="onPageChange"
      >
        <template #item="{ item }">
          <AppCard :app="item" />
        </template>
      </a-list>
    </a-space>
  </div>
</template>

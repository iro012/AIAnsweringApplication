<script setup lang="ts">
import { reactive, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import dayjs from "dayjs";
import { listAppByPageUsingPost } from "@/api/appController";
import {
  APP_SCORING_STRATEGY_MAP,
  APP_TYPE_MAP,
  REVIEW_STATUS_MAP,
} from "../../constant/app";

const formSearchParams = reactive<API.AppQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

// 搜索条件
const searchParams = ref<API.AppQueryRequest>({
  ...initSearchParams,
});

// 数据列表
const dataList = ref<API.App[]>([]);
// 总条数
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listAppByPageUsingPost(searchParams.value);
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records || [];
    total.value = res.data.data.total;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

/**
 * 执行搜索
 */
const doSearch = () => {
  searchParams.value = {
    ...initSearchParams,
    ...formSearchParams,
  };
};

/**
 * 当分页变化时，改变搜索条件，触发数据加载
 * @param page
 */
const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};
/**
 * 组件挂载时加载数据
 */
watchEffect(() => {
  loadData();
});

const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "应用名",
    dataIndex: "appName",
  },
  {
    title: "应用描述",
    dataIndex: "appDesc",
  },
  {
    title: "应用图标",
    dataIndex: "appIcon",
  },
  {
    title: "应用类型",
    dataIndex: "appType",
    slotName: "appType",
  },
  {
    title: "评分策略",
    dataIndex: "scoringStrategy",
    slotName: "scoringStrategy",
  },
  {
    title: "审核状态",
    dataIndex: "reviewStatus",
    slotName: "reviewStatus",
  },
  {
    title: "审核信息",
    dataIndex: "reviewMessage",
  },
  {
    title: "审核人 id",
    dataIndex: "reviewerId",
  },
  {
    title: "审核时间",
    dataIndex: "reviewTime",
  },
  {
    title: "创建用户 id",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
    slotName: "updateTime",
  },
];
</script>

<template>
  <div id="admin-app">
    <a-form
      :model="formSearchParams"
      :style="{ marginBottom: '20px' }"
      layout="inline"
      @submit="doSearch"
    >
      <a-form-item field="appName" label="应用名">
        <a-input
          v-model="formSearchParams.appName"
          placeholder="请输入应用名"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="appDesc" label="应用描述">
        <a-input
          v-model="formSearchParams.appDesc"
          placeholder="请输入应用描述"
          allow-clear
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100px">
          搜索
        </a-button>
      </a-form-item>
    </a-form>
    <a-table
      :columns="columns"
      :data="dataList"
      :pagination="{
        showTotal: true,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        total,
      }"
      @page-change="onPageChange"
    >
      <template #appType="{ record }">
        {{ APP_TYPE_MAP[record.appType] }}
      </template>
      <template #scoringStrategy="{ record }">
        {{ APP_SCORING_STRATEGY_MAP[record.scoringStrategy] }}
      </template>
      <template #reviewStatus="{ record }">
        {{ REVIEW_STATUS_MAP[record.reviewStatus] }}
      </template>
      <template #createTime="{ record }">
        {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #updateTime="{ record }">
        {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
    </a-table>
  </div>
</template>

<style scoped></style>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import { listUserByPageUsingPost } from "@/api/userController";
import message from "@arco-design/web-vue/es/message";
import dayjs from "dayjs";
import { deleteUserAnswerUsingPost, listUserAnswerByPageUsingPost } from "@/api/userAnswerController";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "@/constant/app";

const formSearchParams = reactive<API.UserAnswerQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

// 搜索条件
const searchParams = ref<API.UserAnswerQueryRequest>({
  ...initSearchParams,
});

// 数据列表
const dataList = ref<API.UserAnswer[]>([]);
// 总条数
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listUserAnswerByPageUsingPost(searchParams.value);
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
 * 删除
 * @param record
 */
const doDelete = async (record: API.UserAnswer) => {
  if (!record.id) {
    return;
  }

  const res = await deleteUserAnswerUsingPost({
    id: record.id,
  });
  if (res.data.code === 0) {
    loadData();
  } else {
    message.error("删除失败，" + res.data.message);
  }
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
    title: "应用 id",
    dataIndex: "appId",
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
    title: "用户答案（JSON 数组）",
    dataIndex: "choices",
  },
  {
    title: "评分结果 id",
    dataIndex: "resultId",
  },
  {
    title: "结果名称",
    dataIndex: "resultName",
  },
  {
    title: "结果描述",
    dataIndex: "resultDesc",
  },
  {
    title: "结果图标",
    dataIndex: "resultPicture",
    slotName: "resultPicture",
  },
  {
    title: "得分",
    dataIndex: "resultScore",
  },
  {
    title: "用户 id",
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
  {
    title: "操作",
    slotName: "optional",
  },
];
</script>

<template>
  <div id="admin-user">
    <a-form
      :model="formSearchParams"
      :style="{ marginBottom: '20px' }"
      layout="inline"
      @submit="doSearch"
    >
      <a-form-item field="resultName" label="结果名称">
        <a-input
          v-model="formSearchParams.resultName"
          placeholder="请输入结果名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="resultDesc" label="结果描述">
        <a-input
          v-model="formSearchParams.resultDesc"
          placeholder="请输入结果描述"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="appId" label="应用 id">
        <a-input
          v-model="formSearchParams.appId"
          placeholder="请输入应用 id"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="userId" label="用户 id">
        <a-input
          v-model="formSearchParams.userId"
          placeholder="请输入用户 id"
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
      <template #resultPicture="{ record }">
        <a-image :src="record.resultPicture" width="50px" />
      </template>
      <template #createTime="{ record }">
        {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #updateTime="{ record }">
        {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button :href="`/answer/result/${record.id}`">查看结果</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<style scoped></style>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from "vue";
import { listUserByPageUsingPost } from "@/api/userController";
import message from "@arco-design/web-vue/es/message";
import dayjs from "dayjs";
import { listQuestionByPageUsingPost } from "@/api/questionController";

const formSearchParams = reactive<API.QuestionQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
};

// 搜索条件
const searchParams = ref<API.QuestionQueryRequest>({
  ...initSearchParams,
});

// 数据列表
const dataList = ref<API.Question[]>([]);
// 总条数
const total = ref<number>(0);

/**
 * 加载数据
 */
const loadData = async () => {
  const res = await listQuestionByPageUsingPost(searchParams.value);
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
    title: "题目内容（json格式）",
    dataIndex: "questionContent",
    slotName: "questionContent",
  },
  {
    title: "应用 id",
    dataIndex: "appId",
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
  <div id="admin-question">
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
      <template #questionContent="{ record }">
        <div
          v-for="question in JSON.parse(record.questionContent)"
          :key="question.title"
        >
          {{ question }}
        </div>
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

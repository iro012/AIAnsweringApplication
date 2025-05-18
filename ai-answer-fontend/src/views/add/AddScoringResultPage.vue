<script setup lang="ts">
// 表单
import { reactive, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import dayjs from "dayjs";
import {
  addScoringResultUsingPost,
  deleteScoringResultUsingPost,
  editScoringResultUsingPost,
  listMyScoringResultVoByPageUsingPost,
} from "@/api/scoringResultController";
import { useLoginUserStore } from "@/stores/userStore";

interface Props {
  appId: string;
}

const props = defineProps<Props>();
const router = useRouter();

const form = ref<API.ScoringResultAddRequest>({
  appId: props.appId,
  resultDesc: "",
  resultName: "",
  resultPicture: "",
  resultProp: [],
  resultScoreRange: 0,
});

const oldScoringResult = ref<API.ScoringResultVO>();

/**
 * 提交表单
 * @param data
 */
const handleSubmit = async (data) => {
  let res = null;
  if (oldScoringResult.value?.id) {
    res = await editScoringResultUsingPost({
      id: oldScoringResult.value.id,
      ...form.value,
    });
  } else {
    res = await addScoringResultUsingPost(form.value);
  }

  if (res.data.code === 0) {
    message.success("操作成功");
    router.go();
  } else {
    message.error("操作失败失败，" + res.data?.message);
  }
};

const formSearchParams = reactive<API.ScoringResultQueryRequest>({});

// 初始化搜索条件（不应该被修改）
const initSearchParams = {
  current: 1,
  pageSize: 10,
  sortField: "createTime",
  sortOrder: "descend",
};

// 搜索条件
const searchParams = ref<API.ScoringResultQueryRequest>({
  ...initSearchParams,
});

// 数据列表
const dataList = ref<API.ScoringResult[]>([]);
// 总条数
const total = ref<number>(0);

const { loginUser } = useLoginUserStore();

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
 * 加载数据
 */
const loadData = async () => {
  const params = ref({
    appId: props.appId,
    ...searchParams.value,
  });
  const res = await listMyScoringResultVoByPageUsingPost(params.value);
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records || [];
    total.value = res.data.data.total;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
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
const doDelete = async (record: API.ScoringResultVO) => {
  if (!record.id) {
    return;
  }

  const res = await deleteScoringResultUsingPost({
    id: record.id,
  });
  if (res.data.code === 0) {
    loadData();
  } else {
    message.error("删除失败，" + res.data.message);
  }
};

/**
 * 修改
 * @param record
 */
const doUpdate = (record: API.ScoringResultVO) => {
  form.value = {
    ...record,
  };
  oldScoringResult.value = record;
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
    title: "结果名称",
    dataIndex: "resultName",
  },
  {
    title: "结果描述",
    dataIndex: "resultDesc",
  },
  {
    title: "结果图片",
    dataIndex: "resultPicture",
    slotName: "resultPicture",
  },
  {
    title: "结果属性集合",
    dataIndex: "resultProp",
  },
  {
    title: "结果得分范围",
    dataIndex: "resultScoreRange",
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
  {
    title: "操作",
    slotName: "optional",
  },
];
</script>

<template>
  <div id="add-result">
    <h2 style="text-align: left; margin-left: 16px">创建评分结果</h2>
    <a-form :model="form" :style="{ width: '600px' }" @submit="handleSubmit">
      <a-form-item label="应用id">
        {{ appId }}
      </a-form-item>
      <a-form-item field="resultName" label="结果名称">
        <a-input v-model="form.resultName" placeholder="请输入结果名称" />
      </a-form-item>
      <a-form-item field="resultDesc" label="结果描述">
        <a-input v-model="form.resultDesc" placeholder="请输入结果描述" />
      </a-form-item>
      <a-form-item field="resultPicture" label="结果图片">
        <a-input v-model="form.resultPicture" placeholder="请输入结果图片" />
      </a-form-item>
      <a-form-item field="resultProp" label="结果集" style="text-align: left">
        <a-input-tag
          v-model="form.resultProp"
          placeholder="请输出结果集，按回车确认"
          allow-clear
        />
      </a-form-item>
      <a-form-item field="resultScoreRange" label="结果得分范围">
        <a-input-number
          v-model="form.resultScoreRange"
          placeholder="请输入结果得分范围"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100px">
          提交
        </a-button>
      </a-form-item>
    </a-form>
    <h2 style="text-align: left; margin-left: 16px">评分管理页面</h2>
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
      <template #resultPicture="{ record }">
        <a-image width="50px" :src="record?.resultPicture"></a-image>
      </template>
      <template #createTime="{ record }">
        {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #updateTime="{ record }">
        {{ dayjs(record.updateTime).format("YYYY-MM-DD HH:mm:ss") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button status="warning" @click="doUpdate(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<style scoped></style>

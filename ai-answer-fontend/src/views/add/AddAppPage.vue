<script setup lang="ts">
// 表单
import { onMounted, ref } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import { APP_SCORING_STRATEGY_MAP, APP_TYPE_MAP } from "@/constant/app";
import {
  addAppUsingPost,
  getAppVoByIdUsingGet,
  updateAppUsingPost,
} from "@/api/appController";

interface Props {
  id: string;
}

const props = defineProps<Props>();

const router = useRouter();

const form = ref<API.AppAddRequest>({
  appDesc: "",
  appIcon: "",
  appName: "",
  appType: 0,
  scoringStrategy: 0,
});

const oldApp = ref<API.AppVO>();

/**
 * 提交表单
 * @param data
 */
const handleSubmit = async (data) => {
  let res = null;
  if (props.id) {
    res = await updateAppUsingPost(form.value);
  } else {
    res = await addAppUsingPost(form.value);
  }

  if (res.data.code === 0) {
    setTimeout(() => {
      router.push({
        path: `/app/detail/${props.id || res.data.data}`,
        replace: true,
      })
    }, 1000);
    message.success("操作成功，即将跳转到应用详情页");
  } else {
    message.error("操作失败失败，" + res.data?.message);
  }
};

/**
 * 加载应用数据
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({ id: props.id });
  if (res.data.code === 0) {
    form.value = res.data.data;
    oldApp.value = res.data.data;
  } else {
    message.error("获取应用信息失败，" + res.data?.message);
  }
};

onMounted(() => {
  loadData();
});
</script>

<template>
  <div id="add-app">
    <a-form :model="form" :style="{ width: '600px' }" @submit="handleSubmit">
      <a-form-item field="appName" label="应用名称">
        <a-input v-model="form.appName" placeholder="请输入应用名称" />
      </a-form-item>
      <a-form-item field="appDesc" label="应用描述">
        <a-input v-model="form.appDesc" placeholder="请输入应用描述" />
      </a-form-item>
      <a-form-item field="appIcon" label="应用图标">
        <a-input v-model="form.appIcon" placeholder="请输入应用图标" />
      </a-form-item>
      <a-form-item field="应用类型" label="应用类型">
        <a-select v-model="form.appType" allow-clear>
          <a-option
            v-for="(value, key) in APP_TYPE_MAP"
            :value="Number(key)"
            :label="value"
          />
        </a-select>
      </a-form-item>
      <a-form-item field="scoringStrategy" label="评分策略">
        <a-select v-model="form.scoringStrategy" allow-clear>
          <a-option
            v-for="(value, key) in APP_SCORING_STRATEGY_MAP"
            :value="Number(key)"
            :label="value"
          />
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100px">
          提交
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped>
#add-app {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>

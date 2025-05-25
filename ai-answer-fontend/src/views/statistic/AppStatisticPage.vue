<script setup lang="ts">
import VChart from "vue-echarts";
import {
  getAnswerCountUsingGet,
  getAnswerResultCountUsingGet,
} from "@/api/appStatisticController";
import { computed, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import { log } from "echarts/types/src/util/log";

const appAnswerCountList = ref<API.AppAnswerCountDTO[]>([]);
const appAnswerResultList = ref<API.AppAnswerResultCountDTO[]>([]);

const loadAppAnswerCountData = async () => {
  const res = await getAnswerCountUsingGet();
  if (res.data.code === 0 && res.data.data) {
    appAnswerCountList.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

watchEffect(() => {
  loadAppAnswerCountData();
});

const loadAppAnswerResultData = async (appId: string) => {
  if (!appId) {
    return;
  }
  const res = await getAnswerResultCountUsingGet({
    appId: appId,
  });
  if (res.data.code === 0 && res.data.data) {
    appAnswerResultList.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

watchEffect(() => {
  loadAppAnswerResultData("");
});

const appAnswerCountOptions = computed(() => {
  return {
    xAxis: {
      type: "category",
      data: appAnswerCountList.value.map((item) => item.appId),
      name: "应用id",
    },
    yAxis: {
      type: "value",
      name: "用户答案数量",
    },
    series: [
      {
        data: appAnswerCountList.value.map((item) => item.count),
        type: "bar",
      },
    ],
  };
});

const appAnswerResultCountOptions = computed(() => {
  return {
    xAxis: {
      type: "category",
      data: appAnswerResultList.value.map((item) => item.resultName),
      name: "答案名称",
    },
    yAxis: {
      type: "value",
      name: "答案数量",
    },
    series: [
      {
        data: appAnswerResultList.value.map((item) => item.count),
        type: "bar",
      },
    ],
  };
});
</script>

<template>
  <div id="app-statistic">statistic</div>
  <h2>热门应用统计</h2>
  <v-chart :option="appAnswerCountOptions" style="height: 300px" />
  <h2>应用结果统计</h2>
  <a-input-search
    @search="(value) => loadAppAnswerResultData(value)"
    :style="{ width: '250px' }"
    placeholder="请输入应用 id"
  />
  <v-chart :option="appAnswerResultCountOptions" style="height: 300px" />
</template>

<style scoped></style>

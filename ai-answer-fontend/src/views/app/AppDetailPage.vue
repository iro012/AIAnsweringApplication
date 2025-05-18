<script setup lang="ts">
import { getAppVoByIdUsingGet } from "@/api/appController";
import message from "@arco-design/web-vue/es/message";
import { onMounted, ref } from "vue";
import {
  APP_SCORING_STRATEGY_ENUM,
  APP_SCORING_STRATEGY_MAP,
} from "../../constant/app";
import dayjs from "dayjs";

interface Props {
  id: string;
}

const props = defineProps<Props>();

const app = ref<API.AppVO>();

/**
 * 获取app信息
 */
const loadData = async () => {
  if (!props.id) {
    return;
  }
  const res = await getAppVoByIdUsingGet({ id: props.id });
  if (res.data.code === 0) {
    app.value = res.data.data;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
};

onMounted(() => {
  loadData();
});
</script>

<template>
  <div id="app-detail">
    <a-card>
      <a-row>
        <a-col flex="auto" class="content-wrapper">
          <h2>{{ app?.appName }}</h2>
          <p>{{ app?.appDesc }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[app?.scoringStrategy] }}</p>
          <a-space
            >作者：
            <a-avatar>
              <img alt="avatar" :src="app?.user?.userAvatar" />
            </a-avatar>
            {{ app?.user?.userName }}
          </a-space>
          <p>创建时间：{{dayjs(app?.createTime).format("YYYY-MM-DD HH:mm:ss")}}</p>
          <a-space>
            <a-button type="primary" :href="`/answer/do/${app?.id}`">开始答题</a-button>
            <a-button>分享应用</a-button>
            <a-button :href="`/add/question/${app?.id}`">设置题目</a-button>
            <a-button :href="`/add/scoring_result/${app?.id}`">设置评分</a-button>
            <a-button :href="`/add/app/${app?.id}`">修改应用</a-button>
          </a-space>
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="app?.appIcon" />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<style scoped>

#app-detail {
  padding: 24px;
}

#app-detail .content-wrapper {
  text-align: left;
}

#app-detail .content-wrapper > * {
  margin-bottom: 24px;
}

</style>

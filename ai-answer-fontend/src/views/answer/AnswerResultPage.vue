<script setup lang="ts">
import { getUserAnswerVoByIdUsingGet } from "@/api/userAnswerController";
import { ref, watchEffect } from "vue";
import { APP_SCORING_STRATEGY_MAP } from "@/constant/app";
import dayjs from "dayjs";
import message from "@arco-design/web-vue/es/message";

interface Props {
  id: string;
}

const props = defineProps<Props>();

const answerResult = ref<API.UserAnswerVO>();

const loadData = async () => {
  const res = await getUserAnswerVoByIdUsingGet({ id: props.id });
  if (res.data.code === 0) {
    answerResult.value = res.data.data;
  } else {
    message.error("获取答案数据失败，" + res.data.message);
  }
};

watchEffect(() => {
  loadData();
});
</script>

<template>
  <div id="answer-result">
    <a-card>
      <a-row>
        <a-col flex="auto" class="content-wrapper">
          <h2>{{ answerResult?.resultName }}</h2>
          <p>结果描述：{{ answerResult?.resultDesc }}</p>
          <p>结果id：{{ answerResult?.id }}</p>
          <p>我的答案：{{ answerResult?.choices }}</p>
          <p>应用id：{{ answerResult?.appId }}</p>
          <p>应用类型：{{ APP_SCORING_STRATEGY_MAP[answerResult?.appType] }}</p>
          <p>评分策略：{{ APP_SCORING_STRATEGY_MAP[answerResult?.scoringStrategy] }}</p>
          <a-space
            >答题人：
            <a-avatar>
              <img alt="avatar" :src="answerResult?.user?.userAvatar" />
            </a-avatar>
            {{ answerResult?.user?.userName }}
          </a-space>
          <p>
            答题时间：{{
              dayjs(answerResult?.updateTime).format("YYYY-MM-DD HH:mm:ss")
            }}
          </p>
          <a-button type="primary" :href="`/answer/do/${answerResult?.appId}`"
            >去答题</a-button
          >
        </a-col>
        <a-col flex="320px">
          <a-image width="100%" :src="answerResult?.resultPicture" />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<style scoped>
#answer-result {
  padding: 24px;
}

#answer-result .content-wrapper {
  text-align: left;
}

#answer-result .content-wrapper > * {
  margin-bottom: 24px;
}
</style>
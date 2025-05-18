<script setup lang="ts">
import { getAppVoByIdUsingGet } from "@/api/appController";
import { computed, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import { listQuestionVoByPageUsingPost } from "@/api/questionController";
import { addUserAnswerUsingPost } from "@/api/userAnswerController";
import { useRouter } from "vue-router";

interface Props {
  appId: string;
}

const props = defineProps<Props>();

const router = useRouter();

const app = ref<API.AppVo>();
const questionContent = ref<API.QuestionContentDTO[]>();

// 当前题目序号
const current = ref(1);
// 当前题目
const currentQuesiton = ref<API.QuestionContentDTO>();

// 当前题目选项
const questionOptions = computed(() => {
  return currentQuesiton.value?.options?.map((option) => {
    return {
      label: `${option.key}. ${option.value}`,
      value: option.key
    };
  });
});

// 当前题目答案
const currentAnswer = ref<string>();
// 题目答案列表
const answerList = ref<string[]>([]);

const loadData = async () => {
  if (!props.appId) {
    return;
  }

  let res = await getAppVoByIdUsingGet({
    id: props.appId
  });

  if (res.data.code === 0) {
    app.value = res.data.data;
  } else {
    message.error("获取应用信息失败，" + res.data.message);
  }

  res = await listQuestionVoByPageUsingPost({
    appId: props.appId
  });
  if (res.data.code === 0 && res.data.data) {
    questionContent.value = res.data.data.records[0].questionContent || [];
  } else {
    message.error("加载题目失败，" + res.data.message);
  }
};
// 获取旧数据
watchEffect(() => {
  loadData();
});

/**
 * 题目答案改变时，更新答案列表
 * @param value
 */
const doRedisChange = (value: string) => {
  answerList.value[current.value - 1] = value;
};

/**
 * 提交答案
 */
const doSubmit = async () => {
  if (!props.appId || answerList.value.length != questionContent.value.length) {
    return;
  }
  const res = await addUserAnswerUsingPost({
    appId: props.appId,
    choices: answerList.value,
  });
  if (res.data.code === 0 && res.data.data) {
    message.success("提交成功");
    router.push(`/answer/result/${res.data.data}`);
  } else {
    message.error("提交失败，" + res.data.message);
  }
};

// 改变 current 题号时，会自动更新当前题目和答案
watchEffect(() => {
  currentQuesiton.value = questionContent.value?.[current.value - 1];
  currentAnswer.value = answerList.value?.[current.value - 1];
});
</script>

<template>
  <div id="do-answer">
    <a-card style="text-align: left">
      <h1>{{ app?.appName }}</h1>
      <p>{{ app?.appDesc }}</p>
      <h2>{{ currentQuesiton?.title }}</h2>
      <a-radio-group
        direction="vertical"
        v-model="currentAnswer"
        :options="questionOptions"
        @change="doRedisChange"
      />
      <div style="margin-top: 20px">
        <a-space>
          <a-button
            v-if="answerList.length == 10"
            type="primary"
            @click="doSubmit"
            >提交
          </a-button>
          <a-button
            v-if="current < 10"
            :disabled="!currentAnswer"
            type="primary"
            @click="current = current + 1"
            >下一题
          </a-button>
          <a-button v-if="current > 1" @click="current = current - 1"
            >上一题
          </a-button>
        </a-space>
      </div>
    </a-card>
  </div>
</template>

<style scoped></style>

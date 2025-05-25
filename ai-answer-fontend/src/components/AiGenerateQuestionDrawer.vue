<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import {
  aiGenerateQuestionSseUsingGet,
  aiGenerateQuestionUsingPost,
} from "@/api/questionController";
import message from "@arco-design/web-vue/es/message";

interface Props {
  appId: string;
  onSuccess?: (result: API.QuestionContentDTO[]) => void;
  onSSESuccess?: (result: API.QuestionContentDTO) => void;
  onSSEStart?: (event: any) => void;
  onSSEClose?: (event: any) => void;
}

const props = defineProps<Props>();

const router = useRouter();

const form = ref<API.AiGenerateQuestionRequest>({
  questionNumber: 10,
  optionNumber: 2,
});

const submitting = ref(false);

const sseSubmitting = ref(false);

/**
 * 一键生成
 * @param data
 */
const handleSubmit = async () => {
  submitting.value = true;
  if (!props.appId) {
    return;
  }
  const res = await aiGenerateQuestionUsingPost({
    ...form.value,
    appId: props.appId,
  });
  if (res.data.code === 0 && res.data.data) {
    props.onSuccess(res.data.data);
    visible.value = false;
  } else {
    message.error("ai生成题目失败，" + res.data.message);
  }
  submitting.value = false;
};

/**
 * 实时生成
 * @param data
 */
const doSSESubmit = async () => {
  sseSubmitting.value = true;
  if (!props.appId) {
    return;
  }
  const eventSource = new EventSource(
    // 填写后端地址和参数
    "http://localhost:8080/api/question/ai_generate/sse" +
      `?appId=${props.appId}&optionNumber=${form.value.optionNumber}&questionNumber=${form.value.questionNumber}`
  );
  let first = true;
  // 接收消息
  eventSource.onmessage = function(event) {
    if (first) {
      props.onSSEStart?.(event);
      handleCancel();
      first = !first;
    }
    props.onSSESuccess?.(JSON.parse(event.data));
  };
  // 报错或连接关闭时触发
  eventSource.onerror = function(event) {
    if (event.eventPhase == EventSource.CLOSED) {
      console.log("关闭连接");
      props.onSSEClose?.(event);
      eventSource.close();
    } else {
      eventSource.close();
    }
  };
  sseSubmitting.value = false;
};

const visible = ref(false);

const handleClick = () => {
  visible.value = true;
};
const handleOk = () => {
  visible.value = false;
};
const handleCancel = () => {
  visible.value = false;
};


</script>

<template>
  <a-button type="outline" @click="handleClick">AI 生成题目</a-button>
  <a-drawer
    :width="340"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    unmountOnClose
  >
    <template #title>AI 生成题目</template>
    <a-form :model="form" @submit="handleSubmit">
      <a-form-item label="应用id">
        {{ props.appId }}
      </a-form-item>
      <a-form-item field="questionNumber" label="题目数量">
        <a-input-number
          :max="12"
          :min="6"
          v-model="form.questionNumber"
          placeholder="请输入题目数量"
        />
      </a-form-item>
      <a-form-item field="optionNumber" label="选项数量">
        <a-input-number
          :max="6"
          :min="2"
          v-model="form.optionNumber"
          placeholder="请输入选项数量"
        />
      </a-form-item>
      <a-form-item>
        <a-space>
          <a-button
            type="primary"
            html-type="submit"
            style="width: 100px"
            :loading="submitting"
            :disabled="sseSubmitting"
          >
            {{ submitting ? "生成中" : "一键生成" }}
          </a-button>
          <a-button
            style="width: 100px"
            :loading="sseSubmitting"
            :disabled="submitting"
            @click="doSSESubmit"
          >
            {{ sseSubmitting ? "生成中" : "实时生成" }}
          </a-button>
        </a-space>
      </a-form-item>
    </a-form>
  </a-drawer>
</template>

<style scoped></style>

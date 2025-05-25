<script setup lang="ts">
// 表单
import { ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";
import {
  addQuestionUsingPost,
  listQuestionVoByPageUsingPost,
} from "@/api/questionController";
import AiGenerateQuestionDrawer from "@/components/AiGenerateQuestionDrawer.vue";

interface Props {
  appId: string;
}

const props = defineProps<Props>();

const router = useRouter();

const questionContent = ref<API.QuestionContentDTO[]>([]);

const oldQuestion = ref<API.QuestionVO>();

const addQuesiton = () => {
  questionContent.value.push({
    title: "",
    options: [],
  });
};
const deleteQuesiton = (index) => {
  questionContent.value.splice(index, 1);
};

const addQuesitonOption = (question: API.QuestionContentDTO) => {
  question.options?.push({
    key: "",
    value: "",
    result: "",
    score: 0,
  });
};

const deleteQuesitonOption = (
  question: API.QuestionContentDTO,
  index: number
) => {
  question.options?.splice(index, 1);
};

/**
 * 加载题目数据
 */
const loadData = async () => {
  if (!props.appId) {
    return;
  }
  const res = await listQuestionVoByPageUsingPost({
    appId: props.appId,
  });
  if (res.data.code === 0 && res.data.data) {
    oldQuestion.value = res.data.data.records[0] || {};
    questionContent.value = res.data.data.records[0].questionContent || [];
  } else {
    message.error("加载题目失败，" + res.data.message);
  }
};

watchEffect(() => {
  loadData();
});



/**
 * 提交表单
 * @param data
 */
const handleSubmit = async (data) => {
  if (!questionContent.value || !props.appId) {
    return;
  }
  const res = await addQuestionUsingPost({
    questionContent: questionContent.value,
    appId: props.appId,
  });
  if (res.data.code === 0) {
    message.success("添加成功");
    router.push(`/app/detail/${props.appId}`);
  } else {
    message.error("添加失败，" + res.data.message);
  }
};

/**
 * AI 生成题目成功后执行
 * @param result
 */
const onAiGenerateSuccess = (result: API.QuestionContentDTO[]) => {
  questionContent.value = [...questionContent.value, ...result];
  message.success(`AI 生成题目成功，已新增 ${result.length} 道题目`);
};

/**
 * AI 生成题目成功后执行（SSE）
 */
const onAiGenerateSuccessSSE = (result: API.QuestionContentDTO) => {
  questionContent.value = [...questionContent.value, result];
};

/**
 * SSE 开始生成
 * @param event
 */
const onSSEStart = (event: any) => {
  message.success("开始生成");
};

/**
 * SSE 生成完毕
 * @param event
 */
const onSSEClose = (event: any) => {
  message.success("生成完毕");
};

</script>

<template>
  <div id="add-question">
    <a-form
      :model="questionContent"
      :style="{ width: '600px' }"
      @submit="handleSubmit"
    >
      <a-form-item label="应用id">
        {{ props.appId }}
      </a-form-item>
      <a-form-item label="应用列表">
        <a-space>
          <a-button @click="addQuesiton">底部添加题目</a-button>
          <AiGenerateQuestionDrawer
            :app-id="appId"
            :onSuccess="onAiGenerateSuccess"
            :onSSESuccess="onAiGenerateSuccessSSE"
            :onSSEClose="onSSEClose"
            :onSSEStart="onSSEStart"
          />
        </a-space>
      </a-form-item>
      <div v-for="(question, index) of questionContent" :key="index">
        <a-space>
          <h3>题目{{ index + 1 }}</h3>
          <a-button @click="addQuesiton">添加题目</a-button>
          <a-button status="danger" @click="deleteQuesiton(index)"
            >删除题目
          </a-button>
        </a-space>
        <a-form-item
          :field="`question[${index}].title`"
          :label="`题目 ${index + 1} 标题`"
          :key="index"
        >
          <a-input v-model="question.title" />
        </a-form-item>
        <a-space>
          <h4>题目 {{ index + 1 }} 选项列表</h4>
          <a-button @click="addQuesitonOption(question)">底部添加选项</a-button>
        </a-space>
        <a-form-item
          v-for="(option, optionIndex) in question.options"
          :key="optionIndex"
          :label="`选项${optionIndex + 1}`"
          :content-flex="false"
        >
          <a-form-item field="key" label="选项key">
            <a-input v-model="option.key" />
          </a-form-item>
          <a-form-item field="value" label="选项值">
            <a-input v-model="option.value" />
          </a-form-item>
          <a-form-item field="result" label="选项结果">
            <a-input v-model="option.result" />
          </a-form-item>
          <a-form-item field="score" label="选项得分">
            <a-input v-model="option.score" />
          </a-form-item>
          <a-space>
            <a-button @click="addQuesitonOption(question)">添加题目</a-button>
            <a-button
              status="danger"
              @click="deleteQuesitonOption(question, optionIndex)"
              >删除题目
            </a-button>
          </a-space>
        </a-form-item>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100px">
          提交
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped>
#add-question {
}
</style>

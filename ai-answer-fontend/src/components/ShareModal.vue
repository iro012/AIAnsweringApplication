<script setup lang="ts">
import QRCode from "qrcode";
import { ref } from "vue";

interface Props {
  link: string;
}

const props = withDefaults(defineProps<Props>(), {
  link: () => "https://laoyujianli.com/share/yupi",
});

const codeImg = ref();

const visible = ref(false);

const openModal = () => {
  visible.value = true;
};
const closeModal = () => {
  visible.value = false;
};

// With promises
QRCode.toDataURL(props.link)
  .then((url) => {
    console.log(url);
    codeImg.value = url;
  })
  .catch((err) => {
    console.error(err);
  });

defineExpose({
  openModal,
});


</script>

<template>
  <a-modal v-model:visible="visible" @ok="openModal" @cancel="closeModal">
    <template #title>应用分享</template>
    <h4>复制分享链接</h4>
    <a-typography-paragraph copyable>
      {{ link }}
    </a-typography-paragraph>
    <h4>手机扫描查看</h4>
    <img :src="codeImg" />
  </a-modal>
</template>

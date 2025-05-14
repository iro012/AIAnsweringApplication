<script lang="ts" setup>
import { reactive } from "vue";
import { userLoginUsingPost } from "@/api/userController";
import { useLoginUserStore } from "@/stores/userStore";
import { useRouter } from "vue-router";
import message from "@arco-design/web-vue/es/message";

const loginUserStore = useLoginUserStore();

const router = useRouter();

// 表单
const form = reactive<API.UserLoginRequest>({
  userAccount: "",
  userPassword: "",
});
const handleSubmit = async (data) => {
  const res = await userLoginUsingPost(data.values);
  if (res.data.code === 0) {
    loginUserStore.setLoginUser(res.data.data as API.UserVO);
    router.push({
      path: "/",
      replace: true,
    });
    message.success("登录成功");
  } else {
    message.error("登录失败，" + res.data?.message);
  }
};
</script>

<template>
  <div id="user-login">
    <a-form :model="form" :style="{ width: '600px' }" @submit="handleSubmit">
      <a-form-item field="userAccount" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入用户账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入用户密码"
        />
      </a-form-item>
      <router-link
        to="/user/register"
        style="color: #000; display: flex; justify-content: flex-end"
        >注册账号
      </router-link>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="margin: 0 auto"
          >登录</a-button
        >
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped></style>

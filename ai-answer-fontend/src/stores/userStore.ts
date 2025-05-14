import { defineStore } from "pinia";
import { ref } from "vue";
import { getLoginUserUsingGet } from "@/api/userController";
import AccessEnum from "@/access/accessEnum";

export const useLoginUserStore = defineStore("loginUser", () => {
  const loginUser = ref<API.UserVO>({
    userName: "未登录",
  });

  function setLoginUser(newLoginUser: API.UserVO) {
    loginUser.value = newLoginUser;
  }

  async function fetchLoginUser() {
    const res = await getLoginUserUsingGet();
    if (res.data.code === 0) {
      loginUser.value = res.data.data as API.UserVO;
    } else {
      loginUser.value = { userRole: AccessEnum.NOT_LOGIN };
    }
  }

  return {
    loginUser,
    setLoginUser,
    fetchLoginUser,
  };
});

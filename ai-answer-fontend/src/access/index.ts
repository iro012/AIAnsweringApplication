import { useLoginUserStore } from "@/stores/userStore";
import AccessEnum from "@/access/accessEnum";
import checkAccess from "@/access/checkAccess";
import router from "@/router";

// 是否为首次获取登录用户
let firstFetchLoginUser = true;

router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore();
  let loginUser = loginUserStore.loginUser;

  // 确保页面刷新时，首次加载时，能等待后端返回用户信息后再校验权限
  if (firstFetchLoginUser) {
    await loginUserStore.fetchLoginUser();
    loginUser = loginUserStore.loginUser;
    firstFetchLoginUser = false;
  }

  const needAccess = to.meta.access ?? AccessEnum.NOT_LOGIN;
  if (needAccess != AccessEnum.NOT_LOGIN) {
    // 没有登录，跳转到登录页面
    if (
      !loginUser ||
      !loginUser.userRole ||
      loginUser.userRole === AccessEnum.NOT_LOGIN
    ) {
      next(`/user/login?redirect=${to.fullPath}`);
    }
  }
  // 如果没有权限，跳转到无权限页面
  if (!checkAccess(loginUser, needAccess as string)) {
    next("/noAuth");
    return;
  }
  next();
});

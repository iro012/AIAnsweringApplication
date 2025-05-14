import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import UserLayout from "@/layouts/UserLayout.vue";
import AccessEnum from "@/access/accessEnum";
import NoAuth from "@/views/NoAuth.vue";
import UserLogin from "@/views/user/UserLoginPage.vue";
import UserRegister from "@/views/user/UserRegisterPage.vue";
import AdminUserPage from "@/views/admin/AdminUserPage.vue";
import AdminAppPage from "@/views/admin/AdminAppPage.vue";
import AdminQuestionPage from "@/views/admin/AdminQuestionPage.vue";
import AdminScoringResultPage from "@/views/admin/AdminScoringResultPage.vue";
import AdminUserAnswerPage from "@/views/admin/AdminUserAnswerPage.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/noAuth",
    name: "无权限页面",
    component: NoAuth,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/admin/user",
    name: "用户管理页面",
    component: AdminUserPage,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  {
    path: "/admin/app",
    name: "应用管理页面",
    component: AdminAppPage,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  {
    path: "/admin/question",
    name: "问题管理页面",
    component: AdminQuestionPage,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  {
    path: "/admin/scoring_result",
    name: "评分策略管理页面",
    component: AdminScoringResultPage,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  {
    path: "/admin/user_answer",
    name: "用户答案管理页面",
    component: AdminUserAnswerPage,
    meta: {
      access: AccessEnum.ADMIN,
    },
  },
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    children: [
      {
        path: "login",
        name: "登录",
        component: UserLogin,
      },
      {
        path: "register",
        name: "注册",
        component: UserRegister,
      },
    ],
    meta: {
      hideInMenu: true,
    },
  },
];

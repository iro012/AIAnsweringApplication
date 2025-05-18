import { RouteRecordRaw } from "vue-router";
import UserLayout from "@/layouts/UserLayout.vue";
import AccessEnum from "@/access/accessEnum";
import NoAuth from "@/views/NoAuth.vue";
import HomePage from "@/views/HomePage.vue";
import UserLogin from "@/views/user/UserLoginPage.vue";
import UserRegister from "@/views/user/UserRegisterPage.vue";
import AdminUserPage from "@/views/admin/AdminUserPage.vue";
import AdminAppPage from "@/views/admin/AdminAppPage.vue";
import AdminQuestionPage from "@/views/admin/AdminQuestionPage.vue";
import AdminScoringResultPage from "@/views/admin/AdminScoringResultPage.vue";
import AdminUserAnswerPage from "@/views/admin/AdminUserAnswerPage.vue";
import AppDetailPage from "@/views/app/AppDetailPage.vue";
import AddAppPage from "@/views/add/AddAppPage.vue";
import AddScoringResultPage from "@/views/add/AddScoringResultPage.vue";
import AddQuestionPage from "@/views/add/AddQuestionPage.vue";
import DoAnswerPage from "@/views/answer/DoAnswerPage.vue";
import AnswerResultPage from "@/views/answer/AnswerResultPage.vue";
import MyAnswerPage from "@/views/answer/MyAnswerPage.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "主页",
    component: HomePage,
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
    path: "/answer/do/:appId",
    name: "应用答题页面",
    component: DoAnswerPage,
    props: true,
    meta: {
      access: AccessEnum.USER,
    },
  },
  {
    path: "/answer/result/:id",
    name: "答案结果页面",
    component: AnswerResultPage,
    props: true,
    meta: {
      hideInMenu: true,
      access: AccessEnum.USER,
    },
  },
  {
    path: "/answer/my",
    name: "我的答案",
    component: MyAnswerPage,
    props: true,
    meta: {
      access: AccessEnum.USER,
    },
  },
  {
    path: "/add/app",
    name: "创建应用",
    component: AddAppPage,
  },
  {
    path: "/add/app/:id",
    name: "修改应用",
    props: true,
    component: AddAppPage,
    meta: {
      hideInMenu: true,
      access: AccessEnum.USER,
    },
  },
  {
    path: "/add/scoring_result/:appId",
    name: "创建评分",
    props: true,
    component: AddScoringResultPage,
    meta: {
      hideInMenu: true,
      access: AccessEnum.USER,
    },
  },
  {
    path: "/add/question/:appId",
    name: "创建问题",
    props: true,
    component: AddQuestionPage,
    meta: {
      hideInMenu: true,
      access: AccessEnum.USER,
    },
  },
  {
    path: "/app/detail/:id",
    name: "应用详情页面",
    component: AppDetailPage,
    props: true,
    meta: {
      access: AccessEnum.USER,
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

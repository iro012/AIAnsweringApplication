import { RouteRecordRaw } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import UserLayout from "@/layouts/UserLayout.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    children: [
      {
        path: "login",
        name: "登录",
        component: () => import("@/views/LoginView.vue"),
      },
      {
        path: "register",
        name: "注册",
        component: () => import("@/views/RegisterView.vue"),
      }
    ]
  },
];

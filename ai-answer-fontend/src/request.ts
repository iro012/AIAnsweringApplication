import axios from "axios";

const myAxios = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 20000,
  withCredentials: true, // 允许跨域携带cookie
});

// 添加请求拦截器
myAxios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    return config;
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 添加响应拦截器
myAxios.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    console.log(response);
    const { data } = response;
    if (data.code === 40100) {
      // 不是登录接口、注册接口和获取用户信息接口，则跳转到登录页面
      if (
        !response.request.baseURL?.include("user/get/login") &&
        !window.location.pathname?.includes("user/login") &&
        !window.location.pathname?.includes("user/register") &&
        !window.location.pathname?.includes("/")
      ) {
        window.location.href = `/user/login?redirect=${window.location.href}`;
        console.log("request: 进入登录界面");
      }
    }
    return response;
  },
  function (error) {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error);
  }
);

export default myAxios;

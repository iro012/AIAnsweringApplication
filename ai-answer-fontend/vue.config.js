const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    client: {
      overlay: false, // 禁用全屏错误覆盖层
    },
    port: 8010,
  },
  lintOnSave: "warning",
});

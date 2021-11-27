module.exports = {
  css: {
    loaderOptions: {
      sass: {
        additionalData: `@import "@/css/style.scss";`,
      },
    },
  },
  transpileDependencies: ["vuetify"],
  devServer: {
    overlay: false,
    port: 8081,
    proxy: {
      "^/api": {
        target: "https://127.0.0.1:8443/api",
        pathRewrite: { "^/api": "" },
        changeOrigin: true,
        logLevel: "debug",
      },
    },
  },
  runtimeCompiler: true,
};

module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    overlay: false,
    port: 8081,
    proxy: {
      // 이부분 추가
      "^/api": {
        target: "https://127.0.0.1:8443/api", // 요청할 서버 주소
        pathRewrite: { "^/api": "" },
        changeOrigin: true,
        logLevel: "debug", // 터미널에 proxy 로그가 찍힌다.
      },
    },
  },
  runtimeCompiler: true,
};

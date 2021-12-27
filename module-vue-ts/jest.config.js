module.exports = {
  preset: "ts-jest",
  testEnvironment: "node",
  setupFiles: ["dotenv/config"],
  transform: {
    "\\.[jt]sx?$": "babel-jest"
  },
};

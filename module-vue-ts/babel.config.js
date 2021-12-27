module.exports = {
  // presets: ["@vue/cli-plugin-babel/preset"],
  presets: [
    ['@babel/preset-env', {targets: {node: 'current'}}],
    '@babel/preset-typescript'
  ],
};

const path = require('path');

module.exports = {
    outputDir: path.resolve(__dirname, "../module-api/src/main/resources/static"),

    configureWebpack: {
        resolve: {
            alias: {
                '@': path.join(__dirname, 'src/')
            }
        }
    },

    devServer: {
        proxy: {
            '/api':{
                target: 'http://localhost:8090',
                ws: true,
                changeOrigin: true
            },
        }
    }
}
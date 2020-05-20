const path = require('path');
const HtmlWebPackPlugin = require("html-webpack-plugin");

module.exports = {
    mode: 'development',
    devServer: {
        historyApiFallback: true,
        contentBase: './public',
        compress: true,
        port: 9000
    },
    entry: __dirname + '/src/index.js',
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js',
        publicPath: "/"
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                },
            },
            {
                test: /\.css$/,
                use: ["style-loader", "css-loader"]
            }
        ]
    },
    resolve: {
        alias: {
            App: path.resolve(__dirname, '/Users/kunats-ay/IdeaProjects/go-together/frontend/src/App'),
            forms: path.resolve(__dirname, '/Users/kunats-ay/IdeaProjects/go-together/frontend/src/forms')
        }
    },
    plugins: [
        new HtmlWebPackPlugin({
            template: "./public/index.html",
            filename: "./index.html",
            inject: "body"
        })
    ]
}
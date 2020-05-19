const path = require('path');

module.exports = {
    mode: 'development',
    entry: __dirname + '/src/index.js',
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.js$/,
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
    }
}
var webpack = require('webpack');
var path = require('path');

module.exports = {
  entry: {
    app: './src/main/vuejs/index.js'
  },
  output: {
    path: path.resolve(__dirname, '../src/main/resources/static/dist/'),
    filename: 'app-bundle.js'
  },
  resolve: {
    extensions: [ '', '.js', '.vue' ],
    alias: {
      'src': path.resolve(__dirname, '../src')
    }
  },
  module: {
    loaders: [
      {
        test: /\.vue$/,
        loader: 'vue'
      },
      {
        test: /\.js$/,
        loader: 'babel',
        exclude: /node_modules/
      },
      {
        test: /\.(png|jpg|gif|svg)/,
        loader: 'url',
        query: {
          limit: 10000,
          name: '[name].[ext]?[hash]'
        }
      },
      {
        test: /\.(woff|eot|ttf|woff(2)?|otf)/i,
        loader: 'file-loader?[name].[ext]?[hash]'
      }
    ]
  },
  vue: {
    loaders: {
      js: 'babel-loader'
    },
    autoprefixer: {
      browsers: ['last 2 versions']
    }
  },
  eslint: {
    formatter: require('eslint-friendly-formatter')
  }
};
var packageJSON = require('./package.json');
var path = require('path');
var webpack = require('webpack');

const
PATHS = {
  build : path.join(__dirname, 'target', 'classes', 'META-INF', 'resources', 'webjars', packageJSON.name, packageJSON.version)
};

module.exports = {
  entry : './src/main/resources/static/js/index.js',

  output : {
    path : PATHS.build,
    publicPath : '/assets/',
    filename : 'app-bundle.js'
  },

  module : {
    rules : [ {
      test : /\.vue$/,
      loader : 'vue-loader',
      options : {
        html : {
          root : path.resolve(__dirname, './src/main/resources/')
        }
      }
    }, {
      test : /\.js$/,
      loader : 'babel-loader',
      exclude : /node_modules/,
      options : {
        presets : [ 'es2015' ],
        plugins : [ 'transform-runtime' ]
      }
    }, {
      test : /\.css$/,
      include: /node_modules/,
      loader : "style-loader!css-loader"
    }, {
      test : /\.(png|jpg|gif|svg)$/,
      loader : 'file-loader',
      options : {
        name : '[name].[ext]?[hash]'
      }
    } ]
  },
  devServer : {
    historyApiFallback : true,
    noInfo : true
  },
  performance : {
    hints : false
  },
  devtool : '#eval-source-map'
}

if (process.env.NODE_ENV === 'production') {
  module.exports.devtool = '#source-map'
  // http://vue-loader.vuejs.org/en/workflow/production.html
  module.exports.plugins = (module.exports.plugins || []).concat([ new webpack.DefinePlugin({
    'process.env' : {
      NODE_ENV : '"production"'
    }
  }), new webpack.optimize.UglifyJsPlugin({
    sourceMap : true,
    compress : {
      warnings : false
    }
  }), new webpack.LoaderOptionsPlugin({
    minimize : true
  }) ])
}
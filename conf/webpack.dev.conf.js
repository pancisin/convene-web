var config = require('./webpack.base.conf');

config.devtool = 'eval-source-map';

config.output.publicPath = 'http://localhost:3000/dist/';

config.devServer = {
  host: '0.0.0.0',
  port: 3000,
  historyApiFallback: true,
  noInfo: true,
  contentBase: 'src/main/resources/static/',
  proxy: {
    '/*': 'http://localhost:8180/'
  }
};

module.exports = config;

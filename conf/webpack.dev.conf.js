var config = require('./webpack.base.conf');

config.devtool = 'eval-source-map';

config.output.publicPath = '/dist/';

config.devServer = {
  host: '0.0.0.0',
  port: 3000,
  historyApiFallback: {
    index: '/index.html'
  },
  noInfo: true,
  contentBase: 'src/main/resources/static/',
  proxy: {
    '/api/**': {
      target: 'http://localhost:8180',
      secure: false
    },
    '/public/**': {
      target: 'http://localhost:8180',
      secure: false
    },
    '/stomp/**': {
      target: 'http://localhost:8180',
      secure: false
    }
  }
};

module.exports = config;

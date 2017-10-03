import ArticlesListApi from 'api/articles-list.api';

export default class ArticlesListInjector {

  constructor (articlesList_id) {
    this.articlesList_id = articlesList_id;
  }

  postArticle (article, success) {
    ArticlesListApi.postArticle(this.articlesList_id, article, success);
  }

  getArticles (success) {
    ArticlesListApi.getArticles(this.articlesList_id, success);
  }

  getArticlesList (success) {
    ArticlesListApi.getArticlesList(this.articlesList_id, success);
  }

  getBots (success) {
    ArticlesListApi.getBots(this.articlesList_id, success);
  }

  postBot (articleBot, success) {
    ArticlesListApi.postBot(this.articlesList_id, articleBot, success);
  }
};

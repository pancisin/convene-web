import ArticlesListApi from 'api/articles-list.api';

export default class ArticlesListInjector {

  get instance () {
    return this.inst;
  }

  constructor (articlesList_id) {
    this.articlesList_id = articlesList_id;
    this.inst = null;
  }

  getArticlesLists (success) {
    ArticlesListApi.getArticlesLists(success);
  }

  postArticlesList (articlesList, success) {
    ArticlesListApi.postArticlesList(articlesList, success);
  }

  putArticlesList (articlesList, success) {
    ArticlesListApi.putArticlesList(this.articlesList_id, articlesList, success);
  }

  getArticlesList (success) {
    ArticlesListApi.getArticlesList(this.articlesList_id, articlesList => {
      this.inst = articlesList;
      success(articlesList);
    });
  }

  postArticle (article, success) {
    ArticlesListApi.postArticle(this.articlesList_id, article, success);
  }

  getArticles (page, size, success) {
    ArticlesListApi.getArticles(this.articlesList_id, page, size, success);
  }

  getBots (success) {
    ArticlesListApi.getBots(this.articlesList_id, success);
  }

  postBot (articleBot, success) {
    ArticlesListApi.postBot(this.articlesList_id, articleBot, success);
  }
};

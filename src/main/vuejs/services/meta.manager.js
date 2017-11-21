
const MetaManager = {};
MetaManager.install = (Vue, options) => {
  Vue.myGlobalMethod = function () {
    // something logic ...
  };

  // 3. inject some component options
  Vue.mixin({
    created () {
      // something logic ...
    }
  });

  Vue.prototype.$updateMeta = (meta) => {
    console.warn(meta);
    document.title = `${meta.title} | Convene`;
  };
};

export default MetaManager;

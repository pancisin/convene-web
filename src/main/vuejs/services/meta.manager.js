import router from './router.js';
const MetaManager = {};
const metaElements = [
  'description'
];
var customElements = [];

MetaManager.install = (Vue, options) => {
  let headAvailable = false;

  const updateMeta = (metas) => {

    for (var key in metas) {
      if (metas.hasOwnProperty(key)) {
        const element = document.head.querySelector(`meta[name="${key}"]`);

        if (element != null) {
          element.setAttribute('content', metas[key]);
        } else {
          customElements.push(key);
          const meta_node = document.createElement('meta');
          meta_node.setAttribute('name', key);
          meta_node.setAttribute('content', metas[key]);
          document.head.appendChild(meta_node);
        };
      }
    }
  };

  const initializeMeta = () => {
    metaElements.forEach(m => {
      let meta_node = document.createElement('meta');
      meta_node.setAttribute('name', m);
      document.head.appendChild(meta_node);
    });
  };

  router.afterEach(() => {
    headAvailable = false;

    metaElements.forEach(m => {
      document.head.querySelector(`meta[name="${m}"]`).setAttribute('content', '');
    });

    customElements.forEach(m => {
      const element = document.head.querySelector(`meta[name="${m}"]`);
      if (element) {
        element.remove();
      }
    });
  });

  Vue.mixin({
    created () {
      const head = this.$options.head;
      if (head) {
        this.$watch(head.title, newVal => {
          document.title = `${newVal ? newVal + ' |' : ''}  Convene`;
        });

        this.$watch(head.meta, newVal => {
          updateMeta(newVal);
        }, {
          deep: true
        });

        headAvailable = true;
      }

      if (!headAvailable) {
        const route_title = this.$route ? this.$route.meta.title : null;
        document.title = `${route_title ? route_title + ' |' : ''}  Convene`;
      }
    }
  });

  initializeMeta();
};

export default MetaManager;

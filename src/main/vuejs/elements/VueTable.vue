<script>
export default {
  props: {
    func: Function,
    data: {
      type: Array,
      default () {
        return [];
      }
    }
  },
  render (h) {
    const constructEl = (options) => {
      return h(options.el, {
        class: options.class,
        on: {
          click: options.onClick
        }
      }, options.content);
    };

    return h('table', {
      class: 'table'
    }, [
      h('thead', [ h('tr', this.columns.map(col => h('th', col))) ]),
      h('tbody', this.data.map(item => {
        return h('tr', Object.values(this.func(item)).map(value => {
          if (typeof value === 'function') {
            const res = value();

            if (typeof res === 'object') {
              return constructEl(res);
            } else {
              return h('td', res);
            }
          } else if (typeof value === 'object') {
            return h('td', [ constructEl(value) ]);
          } else {
            return h('td', value);
          }
        }));
      }))
    ]);
  },
  computed: {
    columns () {
      if (this.data.length > 0) {
        const def = this.func(this.data[0]);
        console.log(Object.keys(def));

        return Object.keys(def);
      } else return [];
    }
  },
  methods: {
  }
};
</script>

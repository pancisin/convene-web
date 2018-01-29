<script>
export default {
  props: {
    func: Function,
    data: {
      type: Array,
      default () {
        return [];
      }
    },
    contextmenu: {
      type: Function,
      default () {
        return () => {};
      }
    }
  },
  data () {
    return {
      sort: null
    };
  },
  render (h) {
    const constructEl = (options) => {
      const opt = {
        class: options.class,
        style: options.style,
        attrs: {
          ...options.attrs
        }
      };

      if (options.onClick) {
        opt.on = {
          click: options.onClick
        };
      }

      return h(options.el, opt, options.content);
    };

    const constructContextMenuItem = (name, onClick) => {
      return h('li', [
        h('a', {
          on: {
            click () {
              onClick(menu.componentInstance.data);
            }
          }
        }, name)
      ]);
    };

    const menu = h('context-menu', [ h('ul', this.contextmenu(constructContextMenuItem))]);

    if (this.sort == null) {
      this.sort = {
        column: this.columns[0],
        order: true
      };
    }

    const data = [ ...this.items ];
    data.sort((a, b) => {
      if (this.sort.order) {
        return a[this.sort.column] < b[this.sort.column];
      } else {
        return a[this.sort.column] >= b[this.sort.column];
      }
    });

    const table = h('table', {
      class: 'table vue-table'
    }, [
      h('thead', [ h('tr', this.columns.map(col => {
        const column = h('th', [
          h('a', {
            on: {
              click: () => {
                if (this.sort.column === col) {
                  this.sort.order = !this.sort.order;
                } else {
                  this.sort = {
                    column: col,
                    order: true
                  };
                }
              }
            }
          }, col)
        ]);

        if (this.sort.column === col) {
          column.children.push(h('i', {
            class: `fa ${ this.sort.order ? 'fa-angle-down' : 'fa-angle-up' }`,
            style: {
              'margin-left': '10px',
              'font-weight': 'bold'
            }
          }));
        }

        return column;
      })) ]),
      h('tbody', data.map((item, index) => {
        const opt = {};
        if (this.contextmenu.length > 0) {
          opt.on = {
            contextmenu: (event) => {
              event.preventDefault();
              menu.componentInstance.open(event, item.ref);
            }
          };
        }
        return h('tr', opt, Object.values(this.columns).map(col => {
          const value = item[col];
          if (typeof value === 'function') {
            const res = value();

            if (typeof res === 'object') {
              return h('td', [ constructEl(res) ]);
            } else {
              return h('td', res);
            }
          } else if (typeof value === 'object') {
            return h('td', { class: value.colClass }, [ constructEl(value) ]);
          } else {
            return h('td', value);
          }
        }));
      }))
    ]);

    return h('div', [
      menu,
      table
    ]);
  },
  computed: {
    columns () {
      if (this.data.length > 0) {
        const def = this.func(this.data[0]);
        return Object.keys(def);
      } else return [];
    },
    items () {
      return this.data.map((item, index) => {
        return {
          ...this.func(item, index),
          ref: item
        };
      });
    }
  }
};
</script>

<style lang="less">
.vue-table {
  thead {
    th {
      a {
        color: #fff;
      }
    }
  }
}
</style>

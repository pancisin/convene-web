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

    const table = h('table', {
      class: 'table'
    }, [
      h('thead', [ h('tr', this.columns.map(col => h('th', col))) ]),
      h('tbody', this.data.map((item, index) => {
        const opt = {};
        if (this.contextmenu.length > 0) {
          opt.on = {
            contextmenu: (event) => {
              event.preventDefault();
              menu.componentInstance.open(event, item);
            }
          };
        }
        return h('tr', opt, Object.values(this.func(item, index)).map(value => {
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
    }
  }
};
</script>

<template>
  <div style="height: 200px" v-loading="loading">

  </div>
</template>

<script>
export default {
  name: 'data-parser-editor',
  props: {
    value: String
  },
  data () {
    return {
      loading: false
    };
  },
  mounted () {
    this.loadEditor(() => {
      monaco.languages.register({ id: 'dataParserLang' });
      monaco.languages.setMonarchTokensProvider('dataParserLang', {
        tokenizer: {
          root: [
            [/&.*:/, 'object-property'],
            [/\$/, 'parser-root'],
            [/\.[a-zA-Z]+(\[([0-9]+|\*)\])?/, 'parser-node']
          ]
        }
      });

      monaco.editor.defineTheme('dataParserLangTheme', {
        base: 'vs',
        inherit: false,
        rules: [
          { token: 'object-property', foreground: '1FAB89', fontStyle: 'bold' },
          { token: 'parser-root', foreground: '808080' },
          { token: 'parser-node', foreground: '808080' }
        ]
      });

      var editor = monaco.editor.create(this.$el, {
      	value: this.value,
        language: 'dataParserLang',
        theme: 'dataParserLangTheme',
        scrollBeyondLastLine: false,
        minimap: {
          enabled: false
        }
      });

      editor.onKeyUp(() => {
        this.$emit('input', editor.getValue());
      });

      Object.defineProperty(this, 'editor', {
        value: editor,
        writable: false
      });
    });
  },
  watch: {
    value (newVal) {
      if (this.editor.getValue() === '') {
        this.editor.setValue(newVal);
      }
    }
  },
  methods: {
    loadEditor (callback) {
      this.loading = true;
      if (window.monaco) {
        callback();
        this.loading = false;
        return;
      }
      const config = {
        paths: {
          vs: 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.10.1/min/vs'
        }
      };
      const loaderUrl = `${config.paths.vs}/loader.js`;
      const onGotAmdLoader = () => {

        if (window.LOADER_PENDING) {
          window.require.config(config);
        }

        window.require(['vs/editor/editor.main'], () => {
          callback();
          this.loading = false;
        });

        if (window.LOADER_PENDING) {
          window.LOADER_PENDING = false;
          const loaderCallbacks = window.LOADER_CALLBACKS;
          if (loaderCallbacks && loaderCallbacks.length) {
            let currentCallback = loaderCallbacks.shift();
            while (currentCallback) {
              currentCallback.fn.call(currentCallback.window);
              currentCallback = loaderCallbacks.shift();
            }
          }
        }
      };

      if (window.LOADER_PENDING) {
        window.LOADER_CALLBACKS = window.LOADER_CALLBACKS || [];
        window.LOADER_CALLBACKS.push({
          window: this,
          fn: onGotAmdLoader
        });
      } else {
        if (typeof window.require === 'undefined') {
          const loaderScript = window.document.createElement('script');
          loaderScript.type = 'text/javascript';
          loaderScript.src = loaderUrl;
          loaderScript.addEventListener('load', onGotAmdLoader);
          window.document.body.appendChild(loaderScript);
          window.LOADER_PENDING = true;
        } else {
          onGotAmdLoader();
        }
      }
    }
  }
};
</script>

<style>

</style>

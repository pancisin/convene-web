<template>
  <div class="row">
    <div class="col-md-4">
      <panel type="primary">
        <span slot="title">Search & filter</span> 
        <div class="form-group">
          <label>Keyword</label>
          <input 
            type="text" 
            class="form-control" 
            v-model="filters.q" 
            v-stream:change="searchQuery$">
        </div>
        <div class="form-group">
          <label>Search places</label>
          <place-picker v-stream:input="search$"></place-picker>
        </div>
        <div class="form-group">
          <label>Radius (meters)</label>
          <input 
            type="number" 
            class="form-control" 
            v-model="filters.radius">
        </div>

        <div class="form-group">
          <label>Import state</label>
          <div 
            class="radio radio-primary" 
            v-for="(show, index) in showFilters" 
            :key="index">
            
            <input 
              :id="`radio-${show}`" 
              type="radio" 
              v-model="filters.show" 
              :value="show">
            
            <label :for="`radio-${show}`">
              {{ $t(`filter.${show}`) }} 
            </label>
          </div>
        </div>
        <a class="btn btn-block btn-primary" @click="searchPlaces(null)">Search</a>
      </panel>
    </div>
    <div class="col-md-8">
      <panel type="table" v-loading="loading">
        <span slot="title">Results</span>
        <table class="table">
          <thead>
            <tr>
              <th>Name</th>
              <th class="text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(place, index) in paginator$.content" :key="index">
              <td>
                {{ place.name }}
              </td>
              <td class="text-center">
                <span v-if="place.importing">
                  Importing in progress...
                </span> 
                <a 
                  v-else-if="!place.imported" 
                  class="btn btn-default btn-xs"
                  @click="importPage(place.id)">
                  Import
                </a> 
                <a 
                  v-else
                  class="btn btn-inverse btn-xs" 
                  @click="updatePage(place.pageImportId)">
                  Update
                </a> 
              </td>
            </tr>
          </tbody>
        </table>

            <!-- @click="searchPlaces(paginator.nextCursor)" -->
        <div class="text-center" v-if="paginator$.nextCursor != ''">
          <button
            type="button" 
            class="btn btn-rounded btn-default" 
            v-stream:click="loadMore$">
            Load more
          </button>
        </div>
      </panel>
    </div>
  </div>
</template>

<script>
import ImporterApi from 'api/importer.api';
import { mapActions } from 'vuex';
import { PlacePicker } from 'elements';
import { Observable, Subject } from 'rxjs';

export default {
  name: 'page-import',
  data () {
    return {
      subscription: null,
      loading: false,
      filters: {
        radius: 1000,
        limit: 10,
        show: 'all'
      }
    };
  },
  components: {
    PlacePicker
  },
  computed: {
    showFilters () {
      return [
        'all', 'imported', 'notimported'
      ];
    }
  },
  watch: {
    '$route': 'initialize'
  },
  beforeDestroy () {
    this.subscription.unsubscribe();
  },
  subscriptions () {
    this.search$ = new Subject();
    this.loadMore$ = new Subject();
    this.searchQuery$ = new Subject();

    const websocket$ = Observable
      .fromPromise(this.connectWM('/stomp'))
      .flatMap(() => Observable.create(ob => {
        this.subscription = this.$stompClient.subscribe('/user/queue/page.import', response => {
          ob.next(JSON.parse(response.body));
        });
      }));

    const searchResult$ = Observable
      .combineLatest(this.search$, this.loadMore$.map(() => this.paginator$.nextCursor).startWith(false), (a, after) => {
        return {
          ...a.event.msg,
          filters: {
            after: after || ''
          }
        };
      })
      .do(() => { this.loading = true; })
      .flatMap(req => Observable.create(ob => {
        ImporterApi.searchPlace(req.lat, req.lng, paginator => {
          ob.next({ paginator, append: true });
        }, {
          ...this.filters,
          ...req.filters
        });
      }))
      .scan((acc, cur) => {
        if (cur.append) {
          const items = acc.content || [];
          return {
            ...cur.paginator,
            content: [ ...items ].concat(cur.paginator.content)
          };
        }

        return cur.paginator;
      }, {})
      .combineLatest(websocket$.startWith({}), (paginator, page_import) => {
        if (page_import.state) {
          paginator.content = paginator.content.map(x => {
            if (x.id === page_import.sourceId) {
              if (page_import.state.prop === 'SUCCESS') {
                this.initializePages();
              }

              return {
                ...x,
                imported: page_import.state.prop === 'SUCCESS',
                importing: page_import.state.prop === 'RUNNING',
                pageImportId: page_import.id
              };
            }

            return x;
          });
        }

        return paginator;
      })
      .do(() => { this.loading = false; })
      .startWith({});

    return {
      paginator$: searchResult$
    };
  },
  methods: {
    ...mapActions(['initializePages', 'updatePages']),
    importPage (place_id) {
      this.sendWM('/app/page-import', JSON.stringify({
        facebook_id: place_id
      }));
    },
    updatePage (importId) {
      this.sendWM('/app/import-replay', JSON.stringify({
        id: importId
      }));
    }
  }
};
</script>

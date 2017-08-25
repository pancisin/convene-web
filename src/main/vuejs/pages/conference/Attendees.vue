<template>
  <panel type="table">
    <span slot="title">Attendees</span>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>
            Name
          </th>
          <th>
            Email
          </th>
          <th v-for="field in metaFields" :key="field.id">
            {{ field.name }}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td v-text="user.displayName"></td>
          <td>
            {{ user.email }}
          </td>
          <td v-for="field in metaFields" :key="field.id">
            {{ getUserMetaValue(user.id, field.id) }}
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
export default {
  name: 'conference-attendees-new',
  inject: ['provider'],
  data () {
    return {
      attendees: [],
      metaFields: []
    };
  },
  created () {
    this.initialize();
  },
  computed: {
    users () {
      return this.attendees.map(x => {
        return x.user;
      });
    },
    api () {
      return this.provider.api;
    }
  },
  methods: {
    initialize () {
      this.api.getAttendees(attendees => {
        this.attendees = attendees;
      });

      this.api.getMetaFields(metaFields => {
        this.metaFields = metaFields;
      });
    },
    getUserMetaValue (user_id, field_id) {
      var user = this.attendees.filter(x => {
        return x.user.id === user_id;
      });

      if (user.length > 0) {
        user = user[0];
      }

      var meta = user.meta.filter(x => {
        return x.field === field_id;
      });

      if (meta.length > 0) {
        return meta[0].value;
      } else null;
    }
  }
};
</script>

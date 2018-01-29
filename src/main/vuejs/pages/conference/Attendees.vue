<template>
  <panel type="table">
    <span slot="title">Attendees</span>

    <!-- <vue-table :func="tableRender" :data="members" /> -->

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
        <tr v-for="member in members" :key="member.user.id">
          <td>{{ member.user.displayName }}</td>
          <td>
            {{ member.user.email }}
          </td>
          <td v-for="field in metaFields" :key="field.id">
            {{ getUserMetaValue(member.submission, field.id) }}
          </td>
        </tr>
      </tbody>
    </table>
  </panel>
</template>

<script>
import { VueTable } from 'elements';
export default {
  name: 'conference-attendees-new',
  inject: ['provider'],
  props: ['conference'],
  data () {
    return {
      members: []
    };
  },
  created () {
    this.initialize();
  },
  components: {
    VueTable
  },
  computed: {
    api () {
      return this.provider.api;
    },
    metaFields () {
      if (this.conference && this.conference.registrationForm != null) {
        return this.conference.registrationForm.formFields;
      } else return [];
    }
  },
  methods: {
    tableRender () {
      return {

      };
    },
    initialize () {
      this.api.getAttendees(members => {
        this.members = members;
      });
    },
    getUserMetaValue (submission, field_id) {
      const fields = submission.values.filter(e => e.field === field_id);

      if (fields.length > 0) {
        return fields[0].value;
      }
    }
  }
};
</script>

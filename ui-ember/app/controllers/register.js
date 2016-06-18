import Ember from 'ember';

export default Ember.Controller.extend({
  name: "",
  actions: {
    registerUser() {
      alert(this.get('name'));
    }
  }
});

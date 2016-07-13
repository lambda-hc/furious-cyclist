import Ember from 'ember';
import config from './config/environment';

const Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {
    this.route('login');
    this.route('register');
    this.route('registerEntry');
    this.route('home');
    this.route('viewDB');
});

export default Router;

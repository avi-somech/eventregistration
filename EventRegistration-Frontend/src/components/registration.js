import axios from 'axios'
var config = require('../../config')

var frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
var backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

var AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

function PersonDto (name) {
    this.name = name
    this.events = []
  }
  
  function EventDto (name, date, start, end) {
    this.name = name
    this.eventDate = date
    this.startTime = start
    this.endTime = end
  }

  export default {
    name: 'eventregistration',
    data () {
      return {
        persons: [],
        newPerson: '',
        errorPerson: '',
        response: []
      }
    },

    created: function () {
    // Initializing persons from backend
        AXIOS.get('/persons')
    .then(response => {
          // JSON responses are automatically parsed.
          this.persons = response.data
        })
        .catch(e => {
          this.errorPerson = e
        })
                AXIOS.get('/events')
        .then(response => {
          this.events = response.data
        })
        .catch(e => {
          this.errorEvent = e
          // this.errors.push(e)
        })
},

   // created: function () {
        // Test data
        // const p1 = new PersonDto('John')
        // const p2 = new PersonDto('Jill')
        // // Sample initial content
        // this.persons = [p1, p2]
        
      //},

      methods: {
        // createPerson: function (personName) {
        //   // Create a new person and add it to the list of people
        //   var p = new PersonDto(personName)
        //   this.persons.push(p)
        //   // Reset the name field for new people
        //   this.newPerson = ''
        // }

        createPerson: function (personName) {
            AXIOS.post('/persons/'.concat(personName), {}, {})
              .then(response => {
              // JSON responses are automatically parsed.
                this.persons.push(response.data)
                this.errorPerson = ''
                this.newPerson = ''
              })
              .catch(e => {
                var errorMsg = e.response.data.message
                console.log(errorMsg)
                this.errorPerson = errorMsg
              })
          }
      }, 

      
    //...
  }
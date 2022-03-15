const fs = require('fs'); // import fs module and assign to fs variable

class WordFrequencyFramework{
  load_event_handlers = []; // create load event list
  dowork_event_handlers = []; // do work event list
  end_event_handlers =[]; // do event handler list


  register_for_load_event(handler){
    this.load_event_handlers.push(handler); // push --> add to an array
  }

  register_for_dowork_event(handler){
    this.dowork_event_handlers.push(handler); // push --> add to an array
  }

  register_for_end_event(handler){
    this.end_event_handlers.push(handler); // push --> add to an array
  }

  run(path_to_file){
    // for(let i = 0; i < this.load_event_handlers.length; i++){
    //   this.load_event_handlers[i](path_to_file);
    // }
    for(const h of this.load_event_handlers){
      h(path_to_file);
    }
    for(const h of this.dowork_event_handlers){
      h(); // h is function, so for "function in event" --> execute the function
    }
    for(const h of this.end_event_handlers){
      h();
    }
  }
}

class DataStorage{
  constructor(wfapp, stop_word_filter){
    this.data = '';
    this._stop_word_filter = stop_word_filter;
    this._word_event_handlers = [];

    wfapp.register_for_load_event(this._load.bind(this)); // bind this object to the load event
    wfapp.register_for_dowork_event(this._produce_words.bind(this)); // bind this object to the load event
  }

  _load(path_to_file){
    this.data = fs.readFileSync(path_to_file, 'utf8').split(/[^A-Za-z0-9]/).filter((str) => {
      return str !== '' }).map((str) => {return str.toLowerCase();});
  }

  _produce_words(){
    for(const w of this.data){
      if(!this._stop_word_filter.is_stop_word(w)){
        for(const h of this._word_event_handlers){
          h(w)
        }
      }
    }
  }

  register_for_word_event(handler){ // this gets called in WordFrequncyCounter class
    this._word_event_handlers.push(handler);
  }
}

class StopWordFilter{
  constructor(wfapp){
    this._stop_words = [];
    wfapp.register_for_load_event(this._load.bind(this))
  }

  _load(ignore){
    this._stop_words = fs.readFileSync('stop_words.txt', 'utf8').split(',').filter((str) => {return str != '';}) // filter returns an array of words
    .map((str) => {return str.toLowerCase();}); // map the array of words to lower case
  }

  is_stop_word(word){
    return this._stop_words.includes(word);
  }
}

class WordFrequncyCounter{
  constructor(wfapp, data_storage){
    this._word_freqs = new Map(); // same as hash map in java
    data_storage.register_for_word_event(this._increment_count.bind(this));
    wfapp.register_for_end_event(this._print_freqs.bind(this));
  }

  _increment_count(word){
    if(word.length > 1){
      if(this._word_freqs.has(word))
        this._word_freqs.set(word, this._word_freqs.get(word) + 1); // if word exists replace its value with itself + 1
      else
        this._word_freqs.set(word, 1); // add word for the first time
    }
  }

  _print_freqs(){
    // spread operator separtes the hash map and sorts them individually
    const sortedMap = new Map([...this._word_freqs.entries()].sort((a,b) => b[1] - a[1])); // sort hash map
    let i = 1; // let allows "i" to update its values
  
    console.log("\n--------------");
    console.log("Frequency List");
    console.log("--------------");
    for (const [key, value] of sortedMap.entries()) { // get [key, value] from sortedMap one by one
      console.log(key + " - " + value);
      if (i === 25) { // === strictly checks both sides of the operator as "equal value and equal type", == checks equal to WITHOUT checking the type
        break;
      }
      i++;
    } 
    console.log();
  }
}


class zFrequency{
  z = 0;
  constructor(wfapp, data_storage){
    data_storage.register_for_word_event(this._increment_Zcount.bind(this)); // register word to count for 'z'
    wfapp.register_for_end_event(this._print_Zfreqs.bind(this)); // when it is an end event print 'z' count
  }

  _increment_Zcount(word){
    if(word.includes('z')){ // if the word has a 'z' included then increment
      this.z++; // this is needed here because 'z' is a property of the zFrequency class
    }
  }

  _print_Zfreqs(){
    console.log("Total \"z\" Count: " + this.z);
    console.log();
  }
}

// Main Class Calls
const wfapp = new WordFrequencyFramework();
const stop_word_filter = new StopWordFilter(wfapp);
const data_storage = new DataStorage(wfapp, stop_word_filter);
const word_freq_counter = new WordFrequncyCounter(wfapp, data_storage);

// Part 15.2 Counting the Words with the Letter "Z"
const zCounter = new zFrequency(wfapp, data_storage); // assigning class to a variable
wfapp.run(process.argv.slice(2).toString()); // slice at index 2 because first one is "node" and the second index is "Fifteen.js"
// console.log(process.argv);
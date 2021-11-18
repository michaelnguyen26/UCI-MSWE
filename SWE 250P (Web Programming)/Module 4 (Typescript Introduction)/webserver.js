const express = require('express'); // express helps bundle all html files together since html cannot recognize all files
const app = express();

app.use(express.static(__dirname + '/static')); // get current directory and append static folder where all the files that are 
                                                //related to html are located

app.listen(80, () => { // listen on port 80
    console.log("\nListening for a Request on Port 80...\n");
    console.log("Enter http://127.0.0.1/ to view the webpage")
});
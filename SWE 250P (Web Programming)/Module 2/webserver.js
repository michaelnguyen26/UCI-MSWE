const requestModule = require("request");
const fs = require("fs");  // import the filesystem module to read files from computer

console.log("\nListening for a Request on Port 80...\n");
console.log("Enter http://127.0.0.1/ to view the webpage")

require("http").createServer((inRequest, inResponse) => { // get request when user enters web server
    // response begins 
    if(inRequest.url === "/"){ // strictly equals
        fs.readFile("./M1 - Michael Nguyen.html", function(err, html){
            if(err) {
                console.error(err);
                return;
              }
            inResponse.end(html);
            console.log("Received a Request, Webpage is Now Visible!");
        })
    }
    // end response
}).listen(80); // listen on port 80
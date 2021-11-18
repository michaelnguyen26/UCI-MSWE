import * as module from "./mymodule" // import module that I created
import "./style.css" // put .css file in index.js to bundle together

module.start(); //from module get class navbar and assign it to NavBar
module.alertMsg("Developed in Webpack with Modules!");

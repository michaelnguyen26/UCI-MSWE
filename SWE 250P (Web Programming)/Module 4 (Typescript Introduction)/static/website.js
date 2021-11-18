"use strict";
// npm install @types/node@16.11.7 is needed to avoid some error handling with "Sets"
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
exports.__esModule = true;
var React = require("react"); // import all from react and use module AS "React"
var ReactDOM = require("react-dom");
function start() {
    var NavBar = /** @class */ (function (_super) {
        __extends(NavBar, _super);
        function NavBar() {
            return _super !== null && _super.apply(this, arguments) || this;
        }
        NavBar.prototype.render = function () {
            return (React.createElement("li", {}, React.createElement("a", { href: this.props.href }, this.props.description)));
        };
        return NavBar;
    }(React.Component));
    var rootElement = React.createElement("header", {}, // parent tag encapsulates all tags underneath
    React.createElement("h1", {}), // child tag of parent
    React.createElement("nav", {}, // second child tag
    React.createElement("ul", {}, //unordered list
    React.createElement(NavBar, {
        href: "",
        description: "Home"
    }), React.createElement(NavBar, {
        href: "https://www.tripadvisor.com/Attractions-g190486-Activities-c47-Arendal_Aust_Agder_Southern_Norway.html",
        description: "Landmarks"
    }), React.createElement(NavBar, {
        href: "https://www.visitnorway.com/places-to-go/southern-norway/arendal/listings-arendal/restaurant-kitchen-%26-table/10295/",
        description: "Unique Dining Experiences"
    }), React.createElement(NavBar, {
        href: "https://www.visitnorway.com/places-to-go/southern-norway/arendal/",
        description: "About"
    }), React.createElement(NavBar, {
        href: "https://www.visitnorway.com/places-to-go/southern-norway/arendal/listings-arendal/arendal-tourist-information/3725/",
        description: "Contact"
    }))));
    var sayHi = function (greeting) {
        alert("" + greeting); // template literal with  ${}
    };
    sayHi("Welcome to my Website!\n\nDeveloped by Michael Nguyen");
    ReactDOM.render(rootElement, document.getElementById("mainContainer"));
}

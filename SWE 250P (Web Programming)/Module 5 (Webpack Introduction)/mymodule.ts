// npm install @types/node@16.11.7 is needed to avoid some error handling with "Sets"

import * as React from 'react';  // import all from react and use module AS "React"
import * as ReactDOM from 'react-dom';

export const alertMsg = (msg: String) => { // allow for import in another file 
    alert(`${msg}`)
}

export function start() {  // allow for import in another file

    interface Prop{
        href: String;
        description: String;
    }
    
    class NavBar extends React.Component<Prop> { // extend React.Component to contain interface Prop that gets defined below
        render() {
            return (
                React.createElement("li", {},
                    React.createElement("a",
                        { href: this.props.href },
                        this.props.description
                    )
                )
            );
        }
    }

    const rootElement =
        React.createElement("header", {}, // parent tag encapsulates all tags underneath
            React.createElement("h1", {},), // child tag of parent
            React.createElement("nav", {}, // second child tag
                React.createElement("ul", {}, //unordered list
                    React.createElement(
                        NavBar, {
                        href: "",  //second parameter is props
                        description: "Home"
                    }
                    ), React.createElement(
                        NavBar, {
                        href: "https://www.tripadvisor.com/Attractions-g190486-Activities-c47-Arendal_Aust_Agder_Southern_Norway.html",
                        description:
                            "Landmarks"
                    }
                    ), React.createElement(
                        NavBar, {
                        href: "https://www.visitnorway.com/places-to-go/southern-norway/arendal/listings-arendal/restaurant-kitchen-%26-table/10295/",
                        description:
                            "Unique Dining Experiences"
                    }
                    ), React.createElement(
                        NavBar, {
                        href: "https://www.visitnorway.com/places-to-go/southern-norway/arendal/",
                        description:
                            "About"
                    }
                    ), React.createElement(
                        NavBar, {
                        href: "https://www.visitnorway.com/places-to-go/southern-norway/arendal/listings-arendal/arendal-tourist-information/3725/",
                        description:
                            "Contact"
                    }
                    )
                )
            )
        );

    const sayHi = (greeting: String): void =>{ // arrow notation
        alert(`${greeting}`); // template literal with  ${}
    }
    sayHi("Welcome to my Website!\n\nDeveloped by Michael Nguyen");

    ReactDOM.render(rootElement,
        document.getElementById("mainContainer")
    );
}
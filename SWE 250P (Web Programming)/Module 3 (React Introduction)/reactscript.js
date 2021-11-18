function start() {
    class NavBar extends React.Component {
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
                        href: "",  // second parameter is props
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
            ));
    ReactDOM.render(rootElement,
        document.getElementById("mainContainer")
    );
}
import React, {Component} from "react";
import Toolbar from "./Components/Toolbar/Toolbar";
import Board from "./Components/Screens/Board";
import BoardHistory from "./Components/Screens/BoardHistory";
import Actions from "./Components/Screens/Actions.js";
import Home from "./Components/Screens/Home.js";
import "react-dom";
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import AddBoard from "./Components/Screens/AddBoard.js";

// import queryString from 'query-string'

class App extends Component {

    render() {
        return (
            <Router>
                <div className="App">
                    <Toolbar/>
                    <Switch>
                        <Route path="/" exact component={Home}/>
                        <Route path="/addBoard" exact component={AddBoard}/>
                        <Route path="/actions" component={Actions}/>
                        <Route path="/board" component={Board}/>
                        <Route path="/boardhistory" component={BoardHistory}/>
                    </Switch>
                </div>
            </Router>
        );
    }
}


export default App;

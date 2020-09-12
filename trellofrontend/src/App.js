import React, {Component} from 'react';
import './App.css';

import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Home from "./pages/Home.js";
import AddBoard from "./pages/AddBoard.js";

class App extends Component {

    render() {
        return (
            <Router>
                <div className="App">
                    <Switch>
                        <Route path="/" exact component={Home}/>
                        <Route path="/addBoard" exact component={AddBoard}/>
                    </Switch>
                </div>
            </Router>
        );
    }
}


export default App;

import React, {Component} from 'react';
import './App.css';

import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Home from "./pages/Home.js";
import PickBoard from "./pages/PickBoard.js";
import ViewBoard from "./pages/ViewBoard.js";
import BoardHistory from "./pages/BoardHistory.js";
import GoogleAuthProvider from "./components/GoogleAuthProvider.js";
import AddBoard from "./pages/AddBoard.js";

class App extends Component {

    render() {
        return (
            <GoogleAuthProvider>
                <Router>
                    <div className="App">
                        <Switch>
                            <Route path="/" exact component={Home}/>
                            <Route path="/pickBoard" exact component={PickBoard}/>
                            <Route path="/addBoard" exact component={AddBoard}/>
                            <Route path="/viewBoard" exact component={ViewBoard}/>
                            <Route path="/viewHistory" exact component={BoardHistory}/>
                        </Switch>
                    </div>
                </Router>
            </GoogleAuthProvider>
        );
    }
}


export default App;

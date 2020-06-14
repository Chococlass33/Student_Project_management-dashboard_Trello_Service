import React, { Component } from "react";
import Toolbar from "./Components/Toolbar/Toolbar";
import Board from "./Components/Screens/Board";
import BoardHistory from "./Components/Screens/BoardHistory";
import Activity from "./Components/Screens/Activity";
import Home from "./Components/Screens/Home.js";
import "react-dom";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import AddBoard from "./Components/Screens/AddBoard.js";

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Toolbar />
          <Switch>
            <Route path="/" exact component={Home} />
            <Route path="/addBoard" exact component={AddBoard} />
            <Route path="/activity" component={Activity} />
            <Route path="/board" component={Board} />
            <Route path="/boardhistory" component={BoardHistory} />
          </Switch>
        </div>
      </Router>
    );
  }
}


export default App;

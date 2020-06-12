import React, { Component } from "react";
import Toolbar from "./Components/Toolbar/Toolbar";
import Board from "./Components/Screens/Board";
import BoardHistory from "./Components/Screens/BoardHistory";
import Activity from "./Components/Screens/Activity";
import "react-dom";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Toolbar />
          <Switch>
            <Route path="/" exact component={Home} />
            <Route path="/activity" component={Activity} />
            <Route path="/board" component={Board} />
            <Route path="/boardhistory" component={BoardHistory} />
          </Switch>
        </div>
      </Router>
    );
  }
}

const Home = () => (
  <div>
    <h1 style={{ marginTop: "64px" }}>Home Page</h1>
    <p>Student ID</p>
  </div>
);

export default App;

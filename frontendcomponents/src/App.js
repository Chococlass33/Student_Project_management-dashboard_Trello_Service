import React, { Component } from "react";
import Toolbar from "./Components/Toolbar/Toolbar";
import Board from "./Components/Board";
import BoardHistory from "./Components/BoardHistory";
import Activity from "./Components/Activity";
import "react-dom";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
          <Toolbar />
          <Route path="/activity" component={Activity} />
          <Route path="/board" component={Board} />
          <Route path="/boardhistory" component={BoardHistory} />
          <main style={{ marginTop: "64px" }}>
            <p>Student details</p>
          </main>
        </div>
      </Router>
    );
  }
}

export default App;

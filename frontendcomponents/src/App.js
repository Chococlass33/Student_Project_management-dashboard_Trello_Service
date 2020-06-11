import React, { Component } from "react";
import Toolbar from "./Components/Toolbar/Toolbar";
import { render } from "fusioncharts";

class App extends Component {
  render() {
    return (
      <div className="App">
        <Toolbar />
        <main style={{ marginTop: "64px" }}>
          <p>This is the space underneath the toolbar.</p>
        </main>
      </div>
    );
  }
}

export default App;

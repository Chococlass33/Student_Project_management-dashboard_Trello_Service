import React from "react";
import "./Toolbar.css";

const toolbar = (props) => (
  <header className="toolbar">
    <nav className="toolbar_navigation">
      <div></div>
      <div className="toolbar_name">
        <a href="/">Student Project Dashboard- Trello</a>
      </div>
      <div className="spacer" />
      <div className="toolbar_navigation-items">
        <ul>
          <li>
            <a href="/">Activity</a>
          </li>
          <li>
            <a href="/">Board</a>
          </li>
          <li>
            <a href="/">Board History</a>
          </li>
        </ul>
      </div>
    </nav>
  </header>
);

export default toolbar;

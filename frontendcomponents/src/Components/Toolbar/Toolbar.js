import React from "react";
import "./Toolbar.css";

const toolbar = (props) => (
  <header className="toolbar">
    <nav className="toolbar_navigation">
      <div></div>
      <div className="toolbar_name">
        <a href="/">Student Project Dashboard- Trello</a>
      </div>
      <div className="toolbar_navigation-items">
        <ul>
          <li>
            <a href="/">Student Details</a>
          </li>
          <li>
            <a href="/">Project Details</a>
          </li>
          <li>
            <a href="/">Boards</a>
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

import React from "react";
import "./Toolbar.css";
import { Link } from "react-router-dom";

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
          <Link to="/Activity">
            <li>
              <a href="/">Activity</a>
            </li>
          </Link>
          <Link to="/Board">
            <li>
              <a href="/">Board</a>
            </li>
          </Link>
          <Link to="/BoardHistory">
            <li>
              <a href="/">BoardHistory</a>
            </li>
          </Link>
        </ul>
      </div>
    </nav>
  </header>
);

export default toolbar;

import React, { useEffect } from "react";
import logo from "./logo.svg";
import "./App.scss";

export const App = (): JSX.Element => {
  useEffect(() => {
    fetch("/login")
      .then(res => {
        return res.json();
      })
      .then(json => {
        console.log(json["msg"]);
      })
      .catch(e => {
        console.error(e);
      })
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.tsx</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
};

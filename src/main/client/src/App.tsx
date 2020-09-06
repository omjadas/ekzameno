import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { Exam } from "./components/exam/exam";
import { Header } from "./components/header/header";
import { Subject } from "./components/subject/subject";

export const App = (): JSX.Element => {
  return (
    <div className="App">
      <BrowserRouter>
        <Header />
        <Switch>
          <Route exact path="/subjects/:slug">
            <Subject />
          </Route>
          <Route exact path="/exams/:slug">
            <Exam />
          </Route>
        </Switch>
      </BrowserRouter>
    </div>
  );
};

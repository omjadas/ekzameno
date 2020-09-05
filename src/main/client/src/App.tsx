import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import { Exam } from "./components/exam/exam";
import { Subject } from "./components/subject/subject";

export const App = (): JSX.Element => {
  return (
    <div className="App">
      <BrowserRouter>
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

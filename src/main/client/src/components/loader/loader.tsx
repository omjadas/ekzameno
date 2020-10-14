import React from "react";
import { Spinner } from "react-bootstrap";
import styles from "./loader.module.scss";

export const Loader = (): JSX.Element => {
  return (
    <Spinner animation="border" role="status" className={`d-flex mx-auto ${styles.loader}`}>
      <span className="sr-only">Loading...</span>
    </Spinner>
  );
};

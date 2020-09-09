import React from "react";
import { Modal, Tab } from "react-bootstrap";

export interface AuthProps {
  show: boolean,
  onHide: () => any,
}

export const ViewAllExamModal = (props: AuthProps): JSX.Element => {
  return (
    <div>
      <Modal show={props.show} onHide={props.onHide} centered>
      </Modal>
    </div>
  );
};

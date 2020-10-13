import React from "react";
import { Modal, Tab } from "react-bootstrap";
import { SignIn } from "./signIn/signIn";

export interface AuthProps {
  show: boolean,
  onHide: () => any,
}

export const AuthModal = (props: AuthProps): JSX.Element => {
  return (
    <div>
      <Modal show={props.show} onHide={props.onHide} centered>
        <Tab.Container id="auth" defaultActiveKey="signIn">
          <Modal.Header closeButton>
            <Modal.Title>
              Sign In
            </Modal.Title>
          </Modal.Header>
          <Tab.Content>
            <SignIn onHide={props.onHide} />
          </Tab.Content>
        </Tab.Container>
      </Modal>
    </div>
  );
};

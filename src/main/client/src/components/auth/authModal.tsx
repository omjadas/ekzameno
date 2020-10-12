import React from "react";
import { Modal, Spinner, Tab } from "react-bootstrap";
import { useSelector } from "react-redux";
import { selectUsersStatus } from "../../redux/slices/usersSlice";
import { SignIn } from "./signIn/signIn";

export interface AuthProps {
  show: boolean,
  onHide: () => any,
}

export const AuthModal = (props: AuthProps): JSX.Element => {
  const usersStatus = useSelector(selectUsersStatus);

  if (usersStatus === "loading") {
    return (
      <Spinner animation="border" role="status">
        <span className="sr-only">Loading...</span>
      </Spinner>
    );
  }

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

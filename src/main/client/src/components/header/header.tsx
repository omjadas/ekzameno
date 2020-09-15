import React, { useState } from "react";
import { Button, Container, Dropdown, DropdownButton, Form, Nav, Navbar } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link, useHistory } from "react-router-dom";
import { selectMe, signOut } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";
import { AuthModal } from "../auth/authModal";
import { SubjectModal } from "../subject/subjectModal";
import { UserModal } from "../user/userModal";

export const Header = (): JSX.Element => {
  const [authModalShow, setAuthModalShow] = useState(false);
  const [subjectModalShow, setSubjectModalShow] = useState(false);
  const [userModalShow, setUserModalShow] = useState(false);
  const dispatch = useAppDispatch();
  const history = useHistory();
  const me = useSelector(selectMe);

  const onSignOut = (): void => {
    dispatch(signOut())
      .then(() => dispatch({ type: "RESET" }))
      .then(() => history.push("/"));
  };

  return (
    <Navbar bg="dark" variant="dark" fixed="top">
      <Container>
        <Link className="navbar-brand" to="/">Ekzameno</Link>
        <Nav>
          {
            me !== undefined &&
              <Link className="nav-link" to="/subjects">
                Subjects
              </Link>
          }
        </Nav>
        <SubjectModal show={subjectModalShow} onHide={() => setSubjectModalShow(false)} />
        <Form className="ml-auto" inline>
          {
            me === undefined ?
              <Button
                variant="outline-info"
                onClick={() => setAuthModalShow(true)}>
                Sign In
              </Button>
              :
              <>
                {
                  me.type === "ADMINISTRATOR" &&
                    <DropdownButton className="mr-2" title="Create">
                      <Dropdown.Item onClick={() => setSubjectModalShow(true)}>Subject</Dropdown.Item>
                      <Dropdown.Item onClick={() => setUserModalShow(true)}>User</Dropdown.Item>
                    </DropdownButton>
                }
                <Button
                  variant="outline-info"
                  onClick={onSignOut}>
                  Sign Out
                </Button>
              </>
          }
        </Form>
        <AuthModal show={authModalShow} onHide={() => setAuthModalShow(false)} />
        <UserModal show={userModalShow} onHide={() => setUserModalShow(false)} />
      </Container>
    </Navbar>
  );
};

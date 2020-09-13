import React, { useState } from "react";
import { Button, Container, Form, Nav, Navbar } from "react-bootstrap";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import { selectMe, signOut } from "../../redux/slices/usersSlice";
import { useAppDispatch } from "../../redux/store";
import { AuthModal } from "../auth/authModal";
import { CreateSubjectModal } from "../subject/subjectModal";

export const Header = (): JSX.Element => {
  const [authModalShow, setAuthModalShow] = useState(false);
  const [createSubjectShow, setCreateSubjectShow] = useState(false);
  const dispatch = useAppDispatch();
  const me = useSelector(selectMe);

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
        <CreateSubjectModal show={createSubjectShow} onHide={() => setCreateSubjectShow(false)} />
        <Form className="ml-auto" inline>
          <Button className="mr-2" onClick={() => setCreateSubjectShow(true)}>
            Create Subject
          </Button>
          {
            me === undefined ?
              <Button
                variant="outline-info"
                onClick={() => setAuthModalShow(true)}>
                Sign In
              </Button>
              :
              <Button
                variant="outline-info"
                onClick={() => dispatch(signOut())}>
                Sign Out
              </Button>
          }
        </Form>
        <AuthModal show={authModalShow} onHide={() => setAuthModalShow(false)} />
      </Container>
    </Navbar>
  );
};

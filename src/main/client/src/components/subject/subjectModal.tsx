import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useEffect } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import * as yup from "yup";
import { addSubject, fetchSubjects, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";

export interface SubjectModalProps {
  show: boolean,
  onHide: () => any,
}

interface UpdateSubjectProps extends SubjectModalProps {
  name: string,
  slug: string,
  instructors: string[],
  students: string[],
  description: string,
}

interface FormValues {
  name: string,
  instructors: string,
  students: string,
  description: string,
}

const FormSchema = yup.object().shape({
  name: yup.string().required(),
  instructors: yup.string(),
  students: yup.string(),
  description: yup.string(),
});

export const CreateSubjectModal = (props: UpdateSubjectProps | SubjectModalProps): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useDispatch();
  const subjectStatus = useSelector(selectSubjectsStatus);

  useEffect(() => {
    if (subjectStatus === "idle") {
      dispatch(fetchSubjects());
    }
  }, [slug, dispatch, subjectStatus]);

  const onSubmit = (values: FormValues): void => {
    dispatch(addSubject({
      name: values.name,
      description: values.description,
      instructors: [values.instructors],
      students: [values.students],
    }));
  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title> Create Subject </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          name: (props as any).name ?? "",
          instructors: (props as any).instructors ?? "",
          students: (props as any).students ?? "",
          description: (props as any).description ?? "",
        }}
        validationSchema={FormSchema}
        onSubmit={onSubmit}
      >
        {
          ({
            handleSubmit,
            isSubmitting,
          }) => (
            <Form id="createSubject" onSubmit={handleSubmit as any}>
              <Modal.Body>
                <FormikControl
                  type="text"
                  label="Name"
                  name="name" />
                <FormikControl
                  as="textarea"
                  label="Description"
                  name="description" />
                <FormikControl
                  type="text"
                  label="Instructors"
                  name="instructors" />
                <FormikControl
                  type="text"
                  label="Students"
                  name="students" />
              </Modal.Body>
              <Modal.Footer>
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  Create Subject
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

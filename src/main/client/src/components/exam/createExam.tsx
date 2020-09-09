import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { fetchExams, selectExamsStatus } from "../../redux/slices/examsSlice";
import { Form, Button, Modal } from "react-bootstrap";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import * as yup from "yup";
//import { useMutation } from "@apollo/client";

export interface ExamModalProps {
  show: boolean,
  onHide: () => any,
}
interface UpdateExamProps extends ExamModalProps {
  id: string,
  name: string,
  startTime: string,
  finishTime: string,
  description: string,
}

interface FormValues {
  name: string,
  startTime: string,
  finishTime: string,
  description: string,
}
const FormSchema = yup.object().shape({
  name: yup.string().required(),
  startTime: yup.date().required(),
  finishTime: yup.date().min(yup.ref("startTime")).required(),
  description: yup.string(),
});

export const CreateExamModel = (props:UpdateExamProps | ExamModalProps): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useDispatch();
  const examsStatus = useSelector(selectExamsStatus);

  useEffect(() => {
    if (examsStatus === "idle") {
      dispatch(fetchExams(slug));
    }
  }, [slug, dispatch, examsStatus]);
  

  const onSubmit = (values: FormValues) => {
  // return createExam({
  // variables: {
  //   name: values.name,
  //   startTime: new Date(values.startTime).toISOString(),
  //   finishTime: new Date(values.finishTime).toISOString(),
  //   description: values.description,
  // }})
  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title> 
          Creating Exam for "SubjectID"
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          name: (props as any).name ?? "",
          startTime: (props as any).startTime === undefined ? "" : new Date(new Date((props as any).startTime).getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16),
          finishTime: (props as any).finishTime === undefined ? "" : new Date(new Date((props as any).finishTime).getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16),
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
            <Form id="createExam" onSubmit={handleSubmit as any}>
              <Modal.Body>
                <FormikControl
                  type="text"
                  label="Name"
                  name="name" />
                <FormikControl
                  type="datetime-local"
                  label="Start Time"
                  name="startTime" />
                <FormikControl
                  type="datetime-local"
                  label="Finish Time"
                  name="finishTime" />
                <FormikControl
                  as="textarea"
                  label="Description"
                  name="description" />
              </Modal.Body>
              <Modal.Footer>
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  Create Exam
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
  </Modal>
  );

};
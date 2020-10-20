import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useState } from "react";
import { Alert, Button, Form, Modal } from "react-bootstrap";
import * as yup from "yup";
import { updateExam, addExam } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";

export interface ExamModalProps {
  show: boolean,
  onHide: () => any,
}

interface CreateExamModalProps extends ExamModalProps {
  subjectId: string,
}

interface UpdateExamModalProps extends ExamModalProps {
  id: string,
  name: string,
  startTime: string,
  finishTime: string,
  description: string,
  eTag: string,
}

interface FormValues {
  name: string,
  description: string,
  startTime: string,
  finishTime: string,
}

const FormSchema = yup.object().shape({
  name: yup.string().required("Name is a required field."),
  startTime: yup.date().required("Start time is a required field."),
  finishTime: yup.date().min(yup.ref("startTime"), "Finish time must be greater than Start time").required("End time is a required field."),
  description: yup.string(),
});

export const ExamModal = (props: UpdateExamModalProps | CreateExamModalProps): JSX.Element => {
  const dispatch = useAppDispatch();
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  const handleHide = (): void => {
    setErrorMessage(null);
    props.onHide();
  };

  const handleSubmit = (values: FormValues): Promise<void> => {
    if ("id" in props) {
      return dispatch(updateExam({
        id: props.id,
        exam: {
          ...values,
          startTime: new Date(values.startTime).toISOString(),
          finishTime: new Date(values.finishTime).toISOString(),
        },
        eTag: props.eTag,
      }))
        .then(unwrapResult)
        .then(() => {
          handleHide();
        })
        .catch((e: Error) => {
          setErrorMessage("Failed to update exam");
          console.error(e);
        });
    } else {
      return dispatch(addExam({
        subjectId: props.subjectId,
        exam: {
          ...values,
          startTime: new Date(values.startTime).toISOString(),
          finishTime: new Date(values.finishTime).toISOString(),
        },
      }))
        .then(unwrapResult)
        .then(() => {
          handleHide();
        })
        .catch((e: Error) => {
          setErrorMessage("Failed to create exam");
          console.error(e);
        });
    }
  };

  return (
    <Modal show={props.show} onHide={handleHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            "id" in props
              ? "Update Exam"
              : "Create Exam"
          }
        </Modal.Title>
      </Modal.Header>
      {
        errorMessage !== null &&
          <Alert variant="danger">
            {errorMessage}
          </Alert>
      }
      <Formik
        initialValues={{
          name: (props as any).name ?? "",
          description: (props as any).description ?? "",
          startTime: (props as any).startTime === undefined ? "" : new Date(new Date((props as any).startTime).getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16),
          finishTime: (props as any).finishTime === undefined ? "" : new Date(new Date((props as any).finishTime).getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16),
        }}
        validationSchema={FormSchema}
        onSubmit={handleSubmit}
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
                  as="textarea"
                  label="Description"
                  name="description" />
                <FormikControl
                  type="datetime-local"
                  label="Start Time"
                  name="startTime" />
                <FormikControl
                  type="datetime-local"
                  label="Finish Time"
                  name="finishTime" />
              </Modal.Body>
              <Modal.Footer>
                <Button type="submit" variant="success" disabled={isSubmitting}>
                  {
                    "id" in props
                      ? "Update Exam"
                      : "Create Exam"
                  }
                </Button>
              </Modal.Footer>
            </Form>
          )
        }
      </Formik>
    </Modal>
  );
};

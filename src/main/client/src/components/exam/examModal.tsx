import { unwrapResult } from "@reduxjs/toolkit";
import { Formik } from "formik";
import { FormikControl } from "formik-react-bootstrap";
import React, { useEffect } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import * as yup from "yup";
import { updateExam, addExam, fetchExams, selectExamsStatus } from "../../redux/slices/examsSlice";
import { useAppDispatch } from "../../redux/store";

export interface ExamModalProps {
  subjectId: string,
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
  description: string,
  startTime: string,
  finishTime: string,
}

const FormSchema = yup.object().shape({
  name: yup.string().required(),
  startTime: yup.date().required(),
  finishTime: yup.date().min(yup.ref("startTime")).required(),
  description: yup.string(),
});

export const ExamModal = (props: UpdateExamProps | ExamModalProps): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useAppDispatch();
  const examsStatus = useSelector(selectExamsStatus);

  useEffect(() => {
    if (examsStatus === "idle") {
      dispatch(fetchExams(slug));
    }
  }, [slug, dispatch, examsStatus]);

  const onSubmit = (values: FormValues): void => {
    if ("id" in props) {
      dispatch(updateExam({
        id: props.id,
        subjectId: props.subjectId,
        exam: values,
      }))
        .then(unwrapResult)
        .then(() => {
          props.onHide();
        })
        .catch(e => {
          console.error(e);
        });
    } else {
      dispatch(addExam({
        subjectId: props.subjectId,
        exam: values,
      }))
        .then(unwrapResult)
        .then(() => {
          props.onHide();
        })
        .catch(e => {
          console.error(e);
        });
    }
  };

  return (
    <Modal show={props.show} onHide={props.onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {
            "id" in props ?
              "Update Exam"
              :
              "Create Exam"
          }
        </Modal.Title>
      </Modal.Header>
      <Formik
        initialValues={{
          name: (props as any).name ?? "",
          description: (props as any).description ?? "",
          startTime: (props as any).startTime === undefined ? "" : new Date(new Date((props as any).startTime).getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16),
          finishTime: (props as any).finishTime === undefined ? "" : new Date(new Date((props as any).finishTime).getTime() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16),
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
                    "id" in props ?
                      "Update Election"
                      :
                      "Create Election"
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

import React from "react";
import { useParams } from "react-router-dom";

export const Exam = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  return <></>;
};

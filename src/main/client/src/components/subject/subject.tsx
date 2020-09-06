import React, { useEffect } from "react";
import { useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchSubjects, selectSubjectsStatus } from "../../redux/slices/subjectsSlice";

export const Subject = (): JSX.Element => {
  const { slug } = useParams<{ slug: string }>();
  const dispatch = useDispatch();
  const subjectsStatus = useSelector(selectSubjectsStatus);

  useEffect(() => {
    if (subjectsStatus === "idle") {
      dispatch(fetchSubjects());
    }
  }, [slug, dispatch, subjectsStatus]);

  return <></>;
};

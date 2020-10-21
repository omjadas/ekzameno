import React from "react";
import { Container, Image } from "react-bootstrap";
import ekzamenoLogo from "/app/src/assets/ekzamenoLogo.jpg";
import styles from "./home.module.scss";
import { useSelector } from "react-redux";
import { selectMe } from "../../redux/slices/usersSlice";

export const Home = (): JSX.Element => {
  const me = useSelector(selectMe);

  return (
    <Container className={styles.totalArea}>
      <div className={styles.align}>
        <Image src={ekzamenoLogo} alt="Logo" className={styles.image}/>
        <h1> Welcome to Ekzameno </h1>
        <br/>
        <h4> This tool will allow you to browse through the subjects </h4>
        <h4> you are enroled in and see past and upcoming exams </h4>
        <h4> as well as view your grades. </h4>
        {
          me === undefined &&
            <>
              <br/>
              <br/>
              <h3> Sign in to start your exams.</h3>
            </>
        }
      </div>
    </Container>
  );
};

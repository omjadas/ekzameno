CREATE TABLE users (
    id uuid PRIMARY KEY,
    email text NOT NULL UNIQUE,
    name text NOT NULL,
    password_hash text NOT NULL,
    type text NOT NULL,
);

CREATE TABLE subjects (
    id uuid PRIMARY KEY,
    name text NOT NULL,
);

CREATE TABLE exams (
    id uuid PRIMARY KEY,
    name text NOT NULL,
    subject_id uuid NOT NULL REFERENCES subjects ON DELETE CASCADE,
    publish_date timestamp,
    close_date timestamp,
);

CREATE TABLE questions (
    id uuid PRIMARY KEY,
    question text NOT NULL,
    type text NOT NULL,
    marks integer NOT NULL,
    exam_id uuid NOT NULL REFERENCES exams ON DELETE CASCADE,
);

CREATE TABLE answers (
    id uuid PRIMARY KEY,
    answer text NOT NULL,
    correct boolean NOT NULL,
    question_id uuid NOT NULL REFERENCES questions ON DELETE CASCADE,
);

CREATE TABLE exam_submissions (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL REFERENCES users ON DELETE CASCADE,
    exam_id uuid NOT NULL REFERENCES exams ON DELETE CASCADE,
    marks integer,
);

CREATE TABLE question_submissions (
    id uuid PRIMARY KEY,
    answer text,
    exam_submission_id uuid NOT NULL REFERENCES exam_submissions ON DELETE CASCADE,
    question_id uuid NOT NULL REFERENCES questions ON DELETE CASCADE,
);

CREATE TABLE instructor_subjects (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL REFERENCES users ON DELETE CASCADE,
    subject_id uuid NOT NULL REFERENCES subjects ON DELETE CASCADE,
);

CREATE TABLE student_subjects (
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL REFERENCES users ON DELETE CASCADE,
    subject_id uuid NOT NULL REFERENCES subjects ON DELETE CASCADE,
);
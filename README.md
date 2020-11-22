# ekzameno

![Build](https://github.com/omjadas/swen90007-group1/workflows/Build/badge.svg?branch=master)
![Lint](https://github.com/omjadas/swen90007-group1/workflows/Lint/badge.svg?branch=master)

## Table of Contents

- [ekzameno](#ekzameno)
  - [Table of Contents](#table-of-contents)
  - [Contributors](#contributors)
  - [Development Information](#development-information)
    - [Running Ekzameno](#running-ekzameno)
    - [Environment Variables](#environment-variables)
    - [Git Workflow](#git-workflow)

## Contributors

| Name                         | Student ID | Email                               |
|------------------------------|------------|-------------------------------------|
| Mohammed Muzamil Kallarakodi | 1033777    | mkallarakodi@student.unimelb.edu.au |
| Omja Das                     | 835780     | odas@student.unimelb.edu.au         |
| Joao Pereira                 | 1016983    | jfpereira@student.unimelb.edu.au    |

## Development Information

### Running Ekzameno

To start Ekzameno using [Docker](https://www.docker.com/get-started) Compose run
the following command:

```bash
docker-compose up --build
```

To stop Ekzameno you can hit <kbd>ctrl</kbd> + <kbd>c</kbd>

If you wish to delete the containers, network, and volumes that were created,
the following command can be run:

```bash
docker-compose down -v
```

### Environment Variables

- `JDBC_DATABASE_URL`: URL to the database
- `JWT_SECRET`: secret to sign JWTs with

### Git Workflow

For the development workflow, the team has decided to follow a feature branch
strategy.

Each time an addition needs to be made to the code in the master branch, a new
branch is to be created with a meaningful name that incorporates the nature of
the change and the name and ID of the corresponding issue in Jira, for example:

- EKZ-11/feat/login (this task is a feature, its Jira issue ID is EKZ-11, and it
  implements the login feature)
- docs/meeting-notes-sep-2 (this is a documentation update for the meeting notes
  from September second)
- EKZ-24/fix/login (this is a bug fix for the login feature, with a Jira issue
  ID of EKZ-24)

For each commit, the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)
standard must be followed.

package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ekzameno.ekzameno.models.Administrator;
import com.ekzameno.ekzameno.models.Instructor;
import com.ekzameno.ekzameno.models.Student;
import com.ekzameno.ekzameno.models.User;

public class UserMapper extends AbstractUserMapper<User> {
    public void insert(User obj) throws SQLException {
        if (obj instanceof Administrator) {
            Mapper<Administrator> mapper = new AdministratorMapper();
            mapper.insert((Administrator) obj);
        } else if (obj instanceof Instructor) {
            Mapper<Instructor> mapper = new InstructorMapper();
            mapper.insert((Instructor) obj);
        } else if (obj instanceof Student) {
            Mapper<Student> mapper = new StudentMapper();
            mapper.insert((Student) obj);
        }
    }

    protected User load(ResultSet rs) throws SQLException {
        String type = rs.getString("type");

        Mapper<?> mapper;

        if (type == Student.TYPE) {
            mapper = new StudentMapper();
        } else if (type == Instructor.TYPE) {
            mapper = new InstructorMapper();
        } else if (type == Administrator.TYPE) {
            mapper = new AdministratorMapper();
        } else {
            throw new RuntimeException();
        }

        return (User) mapper.load(rs);
    }

    protected String getType() {
        return "";
    }
}

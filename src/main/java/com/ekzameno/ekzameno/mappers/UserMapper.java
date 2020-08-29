package com.ekzameno.ekzameno.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ekzameno.ekzameno.models.Administrator;
import com.ekzameno.ekzameno.models.Instructor;
import com.ekzameno.ekzameno.models.Student;
import com.ekzameno.ekzameno.models.User;

public class UserMapper extends AbstractUserMapper<User> {
    public void insert(User obj) throws SQLException {
        Mapper<User> mapper = Mapper.getMapper(obj.getClass());
        mapper.insert(obj);
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

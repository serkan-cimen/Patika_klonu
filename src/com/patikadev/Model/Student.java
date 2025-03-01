package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student extends User{

    public static Student getFetch(int id) {
        Student obj = null;
        String query = "SELECT * FROM user WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new Student();
                obj.setId((rs.getInt("id")));
                obj.setName(rs.getString("name"));
                obj.setUsername(rs.getString("uname"));
                obj.setPassword(rs.getString("pass"));
                obj.setType(rs.getString("type"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}

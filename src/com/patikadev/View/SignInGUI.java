package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class SignInGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_e_mail;
    private JTextField fld_uname;
    private JTextField fld_firstname;
    private JTextField fld_surname;
    private JTextField fld_pass;
    private JTextField fld_pass_again;
    private JButton btn_signin;


    public SignInGUI(Student student) {
        add(wrapper);
        setSize(600, 500);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_signin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = fld_e_mail.getText();
                String uname = fld_uname.getText();
                String firstname = fld_firstname.getText();
                String surname = fld_surname.getText();
                String pass = fld_pass.getText();
                String pass_again = fld_pass_again.getText();

                String name = firstname + " " + surname;
                if (Helper.isFieldEmpty(fld_e_mail) || Helper.isFieldEmpty(fld_uname) || Helper.isFieldEmpty(fld_firstname) || Helper.isFieldEmpty(fld_surname) || Helper.isFieldEmpty(fld_pass) || Helper.isFieldEmpty(fld_pass_again)) {
                    Helper.showMsg("fill");
                } else {
                    if (isEMailValid(email) && isPasswordsMatch(pass, pass_again)) {
                        addNewStudent(name, uname, pass);
                        LoginGUI loginGUI = new LoginGUI();
                        dispose();
                    }
                }
            }
        });
    }

    private boolean isEMailValid(String mail) {
        if (mail.contains("@gmail.com") || mail.contains("@hotmail.com") || mail.contains("@icloud.com") || mail.contains("@yahoo.com") || mail.contains("@yandex.com") || mail.contains("@outlook.com")) {
            return true;
        } else {
            Helper.showMsg("Lütfen geçerli bir e-posta adresi girin!");
            return false;
        }
    }

    private boolean isPasswordsMatch(String pass, String pass_again) {
        if (!(pass.equals(pass_again))) {
            Helper.showMsg("Şifre aynı değil!");
            return false;
        }
        return true;
    }

    private boolean addNewStudent(String name, String uname, String pass) {
        String query = "INSERT INTO user (name, uname, password, type) VALUES (?,?,?,?)";
        User findUser = User.getFetch(uname);
        if (findUser != null) {
            Helper.showMsg("Bu kullanıcı adı mevcut. Lütfen farklı bir kullanıcı adı giriniz!");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, uname);
            pr.setString(3, pass);
            pr.setObject(4, "student", Types.OTHER);

            return pr.executeUpdate() != -1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}

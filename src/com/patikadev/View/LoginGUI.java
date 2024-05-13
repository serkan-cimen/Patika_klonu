package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;

public class LoginGUI extends JFrame{
    private JPanel wrapper;
    private JPanel wtop;
    private JPanel wbottom;
    private JTextField fld_user_uname;
    private JPasswordField fld_user_password;
    private JButton btn_login;

    public LoginGUI() {
        add(wrapper);
        setSize(600, 500);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_password)) {
                Helper.showMsg("fill");
            } else {
                User user = User.getFetch(fld_user_uname.getText(), fld_user_password.getText());
                if (user == null) {
                    Helper.showMsg("Kullanıcı Bulunamadı!");
                } else {
                    switch (user.getType()) {
                        case "operator":

                            OperatorGUI opGUI = new OperatorGUI(Operator.getFetch(user.getId()));
                            break;
                        case "educator":
                            EducatorGUI edGUI = new EducatorGUI(Educator.getFetch(user.getId()));

                            break;
                        case "student":
                            StudentGUI stGUI = new StudentGUI(user);
                            break;
                    }
                    dispose();
                }
            }
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI login = new LoginGUI();
    }
}

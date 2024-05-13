package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.CourseContent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentContentGUI extends JFrame{
    private JPanel wrapper;
    private JPanel pnl_quiz;
    private JTextArea text_quiz;
    private JPanel pnl_description;
    private JTextArea text_description;
    private JPanel pnl_youtubeLink;
    private JFormattedTextField textField_youtubeLink;
    private JPanel pnl_comment;
    private JPanel lbl_studentContent_title;
    private JButton btn_studentContent_back;
    private String title;
    private CourseContent content;

    public StudentContentGUI(String title) {
        this.title = title;
        add(wrapper);
        setSize(800, 900);
        int x = Helper.screenCenterPoint("x", getSize());
        int y = Helper.screenCenterPoint("y", getSize());
        setLocation(x, y);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        loadContentModel(title);

        setLocationRelativeTo(null);
        setVisible(true);
        lbl_studentContent_title.setToolTipText(title);


        btn_studentContent_back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int courseID = CourseContent.getFetch(title).getCourse_id();
                StudentCourseGUI studentCourseGUI = new StudentCourseGUI(getCourseNameByCourseId(courseID));
                dispose();
            }
        });
    }

    private void loadContentModel(String title) {
        content = CourseContent.getFetch(title);
        textField_youtubeLink.setText(content.getYoutubeLink());
        textField_youtubeLink.setEditable(false);
        text_description.setText(content.getDescription());
        text_description.setLineWrap(true);
        text_description.setWrapStyleWord(true);
        text_description.setEditable(false);
        text_quiz.setText(content.getQuizQuestion());
        text_quiz.setLineWrap(true);
        text_quiz.setWrapStyleWord(true);
        text_quiz.setEditable(false);
    }

    public static String getCourseNameByCourseId(int id) {
        String query = "SELECT name FROM course WHERE id = " + id;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Course;
import com.patikadev.Model.CourseContent;
import com.patikadev.Model.Educator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ContentGUI extends JFrame{
    private JPanel wrapper;
    private JLabel lbl_course_name;
    private JTable tbl_content;
    private JTextField fld_content_srch;
    private JButton btn_content_search;
    private JButton btn_content_delete;
    private JButton btn_content_add;
    private JTextField pane_content_title;
    private JTextField pane_content_discription;
    private JTextField pane_content_youtubeLink;
    private JButton btn_content_back;
    private Course course;
    private String courseName;
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;

public ContentGUI(String courseName) {
    this.courseName = courseName;
    this.course = getSelectedCourse(this.courseName);
    add(wrapper);
    setSize(1000, 750);
    setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setTitle(Config.PROJECT_TITLE);
    setVisible(true);
    lbl_course_name.setText(this.course.getName());

    mdl_content_list = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return false;
            }
            return super.isCellEditable(row, column);
        }
    };

    Object[] col_content_list = {"ID", "Başlık", "Açıklama", "Youtube Link", "Quiz Soruları", "course_id"};
    mdl_content_list.setColumnIdentifiers(col_content_list);
    row_content_list =new Object[col_content_list.length];
    tbl_content.setModel(mdl_content_list);
    tbl_content.getTableHeader().setReorderingAllowed(false);
    tbl_content.getColumnModel().getColumn(0).setMaxWidth(70);
    loadContentModel();


    btn_content_add.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Helper.isFieldEmpty(pane_content_title) || Helper.isFieldEmpty(pane_content_discription) || Helper.isFieldEmpty(pane_content_youtubeLink)) {
                Helper.showMsg("fill");
            } else {
                String title = pane_content_title.getText();
                String discription = pane_content_discription.getText();
                String youtubeLink = pane_content_youtubeLink.getText();
                int course_id = course.getId();
                if (CourseContent.add(title, discription, youtubeLink, course_id)) {
                    Helper.showMsg("done");
                    loadContentModel();
                    pane_content_discription.setText(null);
                    pane_content_title.setText(null);
                    pane_content_youtubeLink.setText(null);
                }
            }
        }
    });


    btn_content_back.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Educator educator = Educator.getFetch(course.getUser_id());
            EducatorGUI educatorGUI = new EducatorGUI(educator);
            dispose();
        }
    });


    btn_content_delete.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (Helper.confirm("sure")) {
                int course_id = (int) tbl_content.getValueAt(tbl_content.getSelectedRow(), 0);
                if (CourseContent.delete(course_id)) {
                    Helper.showMsg("done");
                    loadContentModel();
                } else {
                    Helper.showMsg("error");
                }
            }
        }
    });


    fld_content_srch.addKeyListener(new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String searchBoxText = fld_content_srch.getText();
                String title = courseName;
                int id = CourseContent.getCourseID(title);
                System.out.println("course id" + id);
                if (!searchBoxText.equals("")) {
                    ArrayList<CourseContent> searchTitleList = CourseContent.searchContentForTitle(searchBoxText, id);
                    loadContentModel(searchTitleList);
                    fld_content_srch.setText(null);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    });


    btn_content_search.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = fld_content_srch.getText();
            int id = CourseContent.getCourseID(title);
            if (!title.equals("")) {
                ArrayList<CourseContent> searchTitleList = CourseContent.searchContentForTitle(title, id);
                loadContentModel(searchTitleList);
                fld_content_srch.setText(null);
            }
        }
    });
}

public static Course getSelectedCourse(String course_name) {
    //Tablodan ilgili course name' e eşit olan satırı getirir.
    String query = "SELECT * FROM course WHERE name =?";
    Course obj = null;
    try {
        PreparedStatement ps = DBConnector.getInstance().prepareStatement(query);
        ps.setString(1, course_name);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            int user_id = rs.getInt("user_id");
            int patika_id = rs.getInt("patika_id");
            String name = rs.getString("name");
            String language = rs.getString("language");
            obj = new Course(id, user_id, patika_id, name, language);
        }
        return obj;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

public void loadContentModel() {
    DefaultTableModel clearModel = (DefaultTableModel) tbl_content.getModel();
    clearModel.setRowCount(0);
    int i;
    int id = this.course.getId();
    for (CourseContent obj : CourseContent.getList(id)) {
        i = 0;
        row_content_list[i++] = obj.getId();
        row_content_list[i++] = obj.getTitle();
        row_content_list[i++] = obj.getDescription();
        row_content_list[i++] = obj.getYoutubeLink();
        row_content_list[i++] = obj.getQuizQuestion();
        row_content_list[i++] = obj.getCourse_id();
        mdl_content_list.addRow(row_content_list);
    }
}

public void loadContentModel(ArrayList<CourseContent> list) {
    DefaultTableModel clearModel = (DefaultTableModel) tbl_content.getModel();
    clearModel.setRowCount(0);
    int i;
    for (CourseContent obj : list) {
        i = 0;

        row_content_list[i++] = obj.getId();
        row_content_list[i++] = obj.getTitle();
        row_content_list[i++] = obj.getDescription();
        row_content_list[i++] = obj.getYoutubeLink();
        row_content_list[i++] = obj.getQuizQuestion();
        row_content_list[i++] = obj.getCourse_id();
        mdl_content_list.addRow(row_content_list);
    }
}

}

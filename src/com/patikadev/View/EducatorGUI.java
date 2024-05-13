package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Course;
import com.patikadev.Model.CourseContent;
import com.patikadev.Model.Educator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class EducatorGUI extends JFrame{
    private JPanel wrapper;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JComboBox cmb_content_course;
    private JComboBox cmb_content_title;
    private JEditorPane pane_content_quiz;
    private JButton btn_content_add;
    private JButton btn_educator_logout;
    private final Educator educator;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private int course_id;


    public EducatorGUI(Educator educator) {
        this.educator = educator;
        add(wrapper);
        setSize(600, 500);
        setLocation(Helper.screenCenterPoint("x", getSize()), Helper.screenCenterPoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        mdl_course_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_course_list = {"Ders Adı", "Patika"};
        mdl_course_list.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        loadEducatorModel();
        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadCondentCourseCombo();

        loadContentTitleCombo();


        tbl_course_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Point point = e.getPoint();
                    int selected_row = tbl_course_list.rowAtPoint(point);
                    int selected_column = tbl_course_list.columnAtPoint(point);
                    tbl_course_list.setRowSelectionInterval(selected_column, selected_row);
                    dispose();
                    ContentGUI countentGUI = new ContentGUI((String) tbl_course_list.getValueAt(selected_row, selected_column));
                } catch (IllegalArgumentException exception) {
                    exception.getStackTrace();
                }
            }
        });

        btn_educator_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginGUI loginGUI = new LoginGUI();
            }
        });

        btn_content_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = cmb_content_title.getSelectedItem().toString();
                String quizText = pane_content_quiz.getText();
                if (CourseContent.addQuiz(title, quizText)) {
                    Helper.showMsg("done");
                    pane_content_quiz.setText(null);
                } else {
                    Helper.showMsg("error");
                    pane_content_quiz.setText(null);
                }
            }
        });

        cmb_content_course.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                loadContentTitleCombo();
            }

        });

    }

    private void loadContentTitleCombo() {
        cmb_content_title.removeAllItems();
        String title = cmb_content_course.getSelectedItem().toString();
        int id = CourseContent.getCourseID(title);
        for (CourseContent obj : CourseContent.getList(id)) {
            cmb_content_title.addItem(new Item(obj.getId(), obj.getTitle()));
        }
    }

    private void loadCondentCourseCombo() {
        cmb_content_course.removeAllItems();
        for (Course obj : Course.getList()) {
            if (obj.getEducator().getName().equals(educator.getName())) {
                cmb_content_course.addItem(new Item(obj.getId(), obj.getName()));
            }
        }
    }

    private void loadEducatorModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Course obj : Course.getList()) {
            i = 0;
            if (obj.getEducator().getName().equals(educator.getName())) {
                row_course_list[i++] = obj.getName();
                row_course_list[i++] = obj.getPatika().getName();
                mdl_course_list.addRow(row_course_list);
            }
        }
    }

}

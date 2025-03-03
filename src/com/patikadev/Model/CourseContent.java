package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.patikadev.Model.Patika.getFetch;

public class CourseContent {
    private int id;
    private String title;
    private String description;
    private String youtubeLink;
    private String quizQuestion;
    private int course_id;
    private Course course;

    public CourseContent(int id,String title, String description, String youtubeLink, String quizQuestion, int course_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.youtubeLink = youtubeLink;
        this.quizQuestion = quizQuestion;
        this.course_id = course_id;
    }

    public static ArrayList<CourseContent> getList(int courseId) {
        ArrayList<CourseContent> cotentList = new ArrayList<>();
        CourseContent obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM content WHERE course_id = " + courseId);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String youtubeLink = rs.getString("youtube_link");
                String quiz_question = rs.getString("quiz_question");
                int course_id = rs.getInt("course_id");
                obj = new CourseContent(id, title, description, youtubeLink, quiz_question, course_id);
                cotentList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cotentList;
    }

    public static ArrayList<CourseContent> getList(String contentTitle) {
        ArrayList<CourseContent> contentList = new ArrayList<>();
        CourseContent obj;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement("SELECT * FROM content WHERE title = ?");
            pr.setString(1, contentTitle);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String youtubeLink = rs.getString("youtube_link");
                String quiz_question = rs.getString("quiz_question");
                int course_id = rs.getInt("course_id");
                obj = new CourseContent(id, title, description, youtubeLink, quiz_question, course_id);
                contentList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contentList;
    }

    public static boolean add(String title, String description, String youtubeLink, int course_id) {
        String query = "INSERT INTO content (title, description, youtube_link, course_id) VALUES (?,?,?,?)";
        CourseContent findContent = getFetch(title);
        if (findContent != null) {
            Helper.showMsg("Aynı konu başlığı mevcut!");
            return false;
        } else {
            try {
                PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
                pr.setString(1, title);
                pr.setString(2, description);
                pr.setString(3, youtubeLink);
                pr.setInt(4, course_id);
                return pr.executeUpdate() != -1;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CourseContent getFetch(String title) {
        CourseContent obj = null;
        String query = "SELECT * FROM content WHERE title = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, title);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new CourseContent(rs.getInt("id"), rs.getString("title"), rs.getString("description"), rs.getString("youtube_link"), rs.getString("quiz_question"), rs.getInt("course_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static boolean delete(int content_id) {
        String query = "DELETE FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, content_id);
            return pr.executeUpdate() != 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<CourseContent> searchContentForTitle(String title, int id) {
        String query = "SELECT * FROM content WHERE title ILIKE '%{{title}}%' AND course_id = " + id;
        query = query.replace("{{title}}", title);
        ArrayList<CourseContent> contentList = new ArrayList<>();
        CourseContent obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new CourseContent(rs.getInt("id"), rs.getNString("title"), rs.getString("description"), rs.getString("youtube_link"), rs.getString("quiz_question"), rs.getInt("course_id"));
                contentList.add(obj);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contentList;
    }

    public static int getCourseID(String name) {
        String query = "SELECT id FROM course WHERE name = ?";
        int id = 0;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            return id;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addQuiz(String title, String quizQuestion) {
        String query = "UPDATE content SET quiz_question=? WHERE title=?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, quizQuestion);
            pr.setString(2, title);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(String quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

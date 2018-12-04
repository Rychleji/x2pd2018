/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datovavrstva;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.image.Image;

/**
 *
 * @author Radim
 */
public interface ISkolniDB {

    public Connection connectToDB(String server, int port, String SID, String login, String pass) throws SQLException;

    public boolean commit();
    
    public boolean rollback();
    
    public Connection getConnect();

    public void setConnect(Connection connect);

    public void editTeacher(int origId, String name, String lastname, String titles, String titlesAfter, int phone, int mobilePhone, String email, String department, FileInputStream image, int role, int rights, String username, String password) throws SQLException, IOException;

    public void addPicture(InputStream image, int id_zamestnanec) throws SQLException;
    
    public void addTeacher(String name, String lastname, String titles, String titlesAfter, int phone, int mobilePhone, String email, String department, int role, int rights, String username, String password) throws SQLException, IOException;

    public void deleteTeacher(int id) throws SQLException;

    public void editDepartment(String origShort, String newShort, String name, String faculty) throws SQLException;

    public void addDepartment(String newShort, String name, String faculty) throws SQLException;

    public void deleteDepartment(String shortName) throws SQLException;

    public void editFaculty(String origShort, String newShort, String name) throws SQLException;

    public void addFaculty(String newShort, String name) throws SQLException;

    public void deleteFaculty(String shortname) throws SQLException;

    public void editSpecialization(String origShort, String newShort, String name, String faculty) throws SQLException;

    public void addSpecialization(String newShort, String name, String faculty) throws SQLException;

    public void deleteSpecialization(String shortname) throws SQLException;

    public void editSubject(String origShort, String newShort, String name, int year, int semester, int end, int form) throws SQLException;

    public void addSubject(String newShort, String name, int year, int semester, int end, int form) throws SQLException;

    public void deleteSubject(String shortname) throws SQLException;
    
    public void editSpecializationSubject(String origShortSpec, String origShortSubj, String newShortSpec, String newShortSubj, String category) throws SQLException;

    public void addSpecializationSubject(String shortSpec, String shortSubj, String category) throws SQLException;

    public void deleteSpecializationSubject(String shortSpec, String shortSubj) throws SQLException;

    public void editSchedule(int id, int numberOfStudents, float startsAt, float span, String subjectShort, int type, String teacherRole, int teacherId, int roomId) throws SQLException;

    public void addSchedule(int numberOfStudents, float startsAt, float span, String subjectShort, int type, String teacherRole, int teacherId, int roomId) throws SQLException;

    public void deleteSchedule(int id) throws SQLException;
    
    public void editClassroom(int id, String name, int capacity) throws SQLException;
    
    public void addClassroom(String name, int capacity) throws SQLException;
    
    public void deleteClassroom(int id) throws SQLException;
    
    public ResultSet selectClassromm() throws SQLException; 
    
    public ResultSet selectTeacher(String id) throws SQLException;

    public ResultSet selectTeachers() throws SQLException;

    public ResultSet selectTeachers(String name, String prijmeni) throws SQLException;
    
    public ResultSet selectTeachers(String department) throws SQLException;

    public ResultSet selectTeachers(String name, String prijmeni, String zkratkaPredmetu) throws SQLException;

    public ResultSet selectDepartment(String shortname) throws SQLException;

    public ResultSet selectDepartments() throws SQLException;

    public ResultSet selectDepartments(String facultyName) throws SQLException;

    public ResultSet selectFaculty(String shortname) throws SQLException;

    public ResultSet selectFaculties() throws SQLException;

    public ResultSet selectSpecialization(String origShort) throws SQLException;

    public ResultSet selectSpecializations() throws SQLException;

    public ResultSet selectSpecializations(String faculty) throws SQLException;
    
    public ResultSet selectSpecializationSubjects(String specId) throws SQLException;
    
    public ResultSet selectSpecializationSubjects_bySubj(String subjId) throws SQLException;

    public ResultSet selectSubject(String shortname) throws SQLException;

    public ResultSet selectSubjects_semester(int semester) throws SQLException;
    
    public ResultSet selectSubjects(String teacherId) throws SQLException;

    public ResultSet selectSubjects_end(int end) throws SQLException;

    public ResultSet selectSubjects_form(int form) throws SQLException;

    public ResultSet selectSubjects_relatedToSpecialization(String shornameSpecialization) throws SQLException;

    public ResultSet selectSubjects() throws SQLException;

    public ResultSet selectSchedule(int id) throws SQLException;

    public ResultSet selectSchedules(String teacherName, String teacherSurname) throws SQLException;
    
    public ResultSet selectSchedules_byTeacherId(String teacherID) throws SQLException;

    public ResultSet selectSchedules(String subjectShort) throws SQLException;

    public ResultSet selectSchedules_type(String type) throws SQLException;

    public ResultSet selectSchedules() throws SQLException;
    
    public ResultSet selectWorkSpaces() throws SQLException;
    
    public ResultSet selectEmployees() throws SQLException;
    
    public ResultSet selectSchedules_byClassroom(int classroomId) throws SQLException;
}

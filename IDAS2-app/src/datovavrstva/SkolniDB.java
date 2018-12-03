package datovavrstva;

import OracleConnector.OracleConnector;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SkolniDB implements ISkolniDB {

    private Connection connect = null;

    @Override
    public Connection connectToDB(String server, int port, String SID, String login, String pass) throws SQLException {
        try {
            OracleConnector.setUpConnection(server, port, SID, login, pass);
            connect = OracleConnector.getConnection();
        } catch (SQLException ex) {
            connect = null;
            throw new SQLException(ex.getMessage(), ex.getSQLState(), ex.getCause());
        }
        return connect;
    }

    @Override
    public boolean commit() {
        try {
            connect.commit();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    @Override
    public boolean rollback() {
        try {
            connect.rollback();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    @Override
    public Connection getConnect() {
        return connect;
    }

    @Override
    public void setConnect(Connection connect) {
        this.connect = connect;
    }

    @Override
    public void editTeacher(int origId, String name, String lastname, String titles, String titlesAfter, int phone, int mobilePhone, String email, String department, FileInputStream image, int role, int rights, String username, String password) throws SQLException, IOException {
        PreparedStatement ps=connect.prepareStatement("exec UPRAVZAMESTNANCE(?,?,?)");  
        
        ps.setString(1, String.format("%d, %s, %s, %s, %s, %s, %s, %d, %d, %d, %d", origId, name, lastname, titles, titlesAfter, email, department, rights, role, mobilePhone, phone));
        
        if(image == null)
            ps.setString(2, "NULL");
        else
            ps.setBinaryStream(2, image, image.available());
        
        ps.setString(3, String.format("%s, %s", username, password));

        ps.execute();
    }

    @Override
    public void addTeacher(String name, String lastname, String titles, String titlesAfter, int phone, int mobilePhone, String email, String department, FileInputStream image, int role, int rights, String username, String password) throws SQLException, IOException {
        PreparedStatement ps=connect.prepareStatement("exec VLOZZAMESTNANCE(?,?,?)");  
        
        ps.setString(1, String.format("%s, %s, %s, %s, %s, %s, %d, %d, %d, %d",name, lastname, titles, titlesAfter, email, department, rights, role, mobilePhone, phone));
        
        if(image == null)
            ps.setString(2, "NULL");
        else
            ps.setBinaryStream(2, image, image.available());
        
        ps.setString(3, String.format("%s, %s", username, password));

        ps.execute();
    }

    @Override
    public void deleteTeacher(int id) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute("EXEC SMAZZAMESTNANCE(%d)", id);
    }

    @Override
    public void editDepartment(String origShort, String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec UPRAVKATEDRU(%s, %s, %s, %s)", origShort, newShort, name, faculty));
    }

    @Override
    public void addDepartment(String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec VLOZKATEDRU(%s, %s, %s)", newShort, name, faculty));
    }

    @Override
    public void deleteDepartment(String shortName) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec SMAZKATEDRU(%s)", shortName));
    }

    @Override
    public void editFaculty(String origShort, String newShort, String name) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec UPRAVFAKULTU(%s, %s, %s)", origShort, newShort, name));
    }

    @Override
    public void addFaculty(String newShort, String name) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec VLOZFAKULTU(%s, %s)", newShort, name));
    }

    @Override
    public void deleteFaculty(String shortname) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec SMAZFAKULTU(%s)", shortname));
    }

    @Override
    public void editSpecialization(String origShort, String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec UPRAVOBOR(%s, %s, %s, %s)", origShort, newShort, name, faculty));
    }

    @Override
    public void addSpecialization(String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec VLOZOBOR(%s, %s, %s)", newShort, name, faculty));
    }

    @Override
    public void deleteSpecialization(String shortname) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec SMAZOBOR(%s)", shortname));
    }

    @Override
    public void editSubject(String origShort, String newShort, String name, int year, int semester, int end, int form) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec UPRAVPREDMET(%s, %s, %s, %d, %d, %d, %d)", origShort, newShort, name, year, semester, end, form));
    }

    @Override
    public void addSubject(String newShort, String name, int year, int semester, int end, int form) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec VLOZPREDMET(%s, %s, %d, %d, %d, %d)", newShort, name, year, semester, end, form));
    }

    @Override
    public void deleteSubject(String shortname) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec SMAZPREDMET(%s)", shortname));
    }

    @Override
    public void editSpecializationSubject(String origShortSpec, String origShortSubj, String newShortSpec, String newShortSubj, String category) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec UPRAVOBORPREDMET(%s, %s, %s, %s, %s)", origShortSpec, origShortSubj, newShortSpec, newShortSubj, category));
    }

    @Override
    public void addSpecializationSubject(String shortSpec, String shortSubj, String category) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec VLOZOBORPREDMET(%s, %s, %s)", shortSpec, shortSubj, category));
    }

    @Override
    public void deleteSpecializationSubject(String shortSpec, String shortSubj) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec SMAZOBORPREDMET(%s, %s)", shortSpec, shortSubj));
    }

    @Override
    public void editSchedule(int id, int numberOfStudents, float startsAt, int span, String subjectShort, int type, String teacherRole, int teacherId, int roomId) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec UPRAVROZVRHOVOUAKCI(%d, %d, %d, %f, %s, %d, %s, %d, %d)", id, numberOfStudents, span, startsAt, subjectShort, type, teacherRole, teacherId, roomId));
    }

    @Override
    public void addSchedule(int numberOfStudents, float startsAt, int span, String subjectShort, int type, String teacherRole, int teacherId, int roomId) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec VLOZROZVRHOVOUAKCI(%d, %d, %f, %s, %d, %s, %d, %d)", numberOfStudents, span, startsAt, subjectShort, type, teacherRole, teacherId, roomId));
    }

    @Override
    public void deleteSchedule(int id) throws SQLException {
        Statement stmt = connect.createStatement();

        stmt.execute(String.format("exec SMAZROZVRHOVOUAKCI(%d)", id));
    }

    @Override
    public ResultSet selectTeacher(String id) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from VYUC_VIEW"
                + " left join KATEDRA on VYUC_VIEW.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY"
                + " left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY"
                + " where ID_VYUCUJICIHO = " + id);
    }

    @Override
    public ResultSet selectTeachers() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from VYUC_VIEW"
                + " left join KATEDRA on VYUC_VIEW.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY"
                + " left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY");
    }

    @Override
    public ResultSet selectTeachers(String name, String prijmeni) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from VYUC_VIEW"
                + " left join KATEDRA on VYUC_VIEW.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY"
                + " left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY"
                + " where JMENO like %" + name + "% and PRIJMENI like %" + prijmeni + "%");
    }

    @Override
    public ResultSet selectTeachers(String department) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from VYUC_VIEW"
                + " left join KATEDRA on VYUC_VIEW.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY"
                + " left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY"
                + " where KATEDRA_ZKRATKA_KATEDRY = '" + department + "'");
    }

    @Override
    public ResultSet selectTeachers(String name, String prijmeni, String zkratkaPredmetu) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from rozvrhova_akce"
                + " inner join ZAMESTNANEC on ROZHVRHOVA_AKCE.ID_ZAMESTNANEC = ZAMESTNANEC.ID_ZAMESTNANEC"
                + " left join KATEDRA on ZAMESTNANEC.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY"
                + " left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY"
                + " inner join PREDMET on ROZVRHOVA_AKCE.PREDMET_ZKRATKA_PREDMETU = PREDMET.ZKRATKA_PREDMETU"
                + " where JMENO like %" + name + "% and PRIJMENI like %" + prijmeni + "% and ZKRATKA_PREDMETU like %" + zkratkaPredmetu + "%");
    }

    @Override
    public ResultSet selectDepartment(String shortname) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PRACOVISTE"
                + " where ZKRATKA_KATEDRY = '" + shortname + "'");
    }

    @Override
    public ResultSet selectDepartments() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PRACOVISTE");
    }

    @Override
    public ResultSet selectDepartments(String facultyName) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PRACOVISTE"
                + "where NAZEV_FAKULTY like %" + facultyName + "%");
    }

    @Override
    public ResultSet selectFaculty(String shortname) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from FAKULTA"
                + "where ZKRATKA_FAKULTY = '" + shortname + "'");
    }

    @Override
    public ResultSet selectFaculties() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from FAKULTA");
    }

    @Override
    public ResultSet selectSpecialization(String origShort) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from OBOR"
                + " left join FAKULTA on OBOR.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY"
                + " where ZKRATKA_OBORU = '" + origShort + "'");
    }

    @Override
    public ResultSet selectSpecializations() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from OBOR"
                + " left join FAKULTA on OBOR.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY");
    }

    @Override
    public ResultSet selectSpecializations(String faculty) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from OBOR"
                + " left join FAKULTA on OBOR.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY"
                + " where NAZEV_FAKULTY = '" + faculty + "'");
    }

    @Override
    public ResultSet selectSubject(String shortname) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PREDMET_EXT_VIEW where ZKRATKA_PREDMETU = '" + shortname + "'");
    }

    @Override
    public ResultSet selectSubjects_semester(int semester) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PREDMET_EXT_VIEW where SEMESTR_ID_SEMESTER = " + semester);
    }

    @Override
    public ResultSet selectSubjects(String teacherId) throws SQLException{
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PREDMET_EXT_VIEW left join ROZVRHOVE_AKCE_EXT_VIEW "
                + "on PREDMET_EXT_VIEW.ZKRATKA_PREDMETU = ROZVRHOVE_AKCE_EXT_VIEW.ZKRATKA_PREDMETU "
                + "where ROZVRHOVE_AKCE_EXT_VIEW.ID_VYUCUJICIHO = '"+teacherId+"'");
    }
    
    @Override
    public ResultSet selectSubjects_end(int end) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PREDMET_EXT_VIEW where ZPUSOB_ZAKONCENI_ID_ZZ = " + end);
    }

    @Override
    public ResultSet selectSubjects_form(int form) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PREDMET_EXT_VIEW where FORMA_VYUKY_ID_FV = " + form);
    }

    @Override
    public ResultSet selectSubjects_relatedToSpecialization(String shornameSpecialization) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from SPOJENI_OBORU_PREDMETU_VIEW where"
                + " ZKRATKA_OBORU = '" + shornameSpecialization + "'");
    }

    @Override
    public ResultSet selectSubjects() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PREDMET_EXT_VIEW");
    }

    @Override
    public ResultSet selectSchedule(int id) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW where ID_ROZVRHOVE_AKCE = " + id);
    }

    @Override
    public ResultSet selectSchedules(String teacherName, String teacherSurname) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW"
                + " where JMENO_VYUCUJICIHO = '" + teacherName + "' and PRIJMENI_VYUCUJICIHO = '" + teacherSurname + "'");
    }

    @Override
    public ResultSet selectSchedules_byTeacherId(String teacherID) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW"
                + " where ID_VYUCUJICIHO = '" + teacherID + "'");
    }

    @Override
    public ResultSet selectSchedules(String subjectShort) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW"
                + " where ZKRATKA_PREDMETU = '" + subjectShort + "'");
    }

    @Override
    public ResultSet selectSchedules_type(String type) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW"
                + " where ZPUSOB = '" + type + "'");
    }

    @Override
    public ResultSet selectSchedules() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW");
    }

    @Override
    public ResultSet selectSpecializationSubjects(String specId) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from SPOJENI_OBORU_PREDMETU_VIEW where ZKRATKA_OBORU = '" + specId + "'");
    }

    @Override
    public ResultSet selectSpecializationSubjects_bySubj(String subjId) throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from SPOJENI_OBORU_PREDMETU_VIEW where ZKRATKA_PREDMETU = '" + subjId + "'");
    }

    @Override
    public ResultSet selectWorkSpaces() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from PRACOVISTE");
    }
    
    @Override
    public ResultSet selectClassromm() throws SQLException{
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from UCEBNA");
    }

    @Override
    public void editClassroom(int id, String name, int capacity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addClassroom(String name, int capacity) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteClassroom(int id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ResultSet selectEmployees() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ZAM_VIEW"
                + " left join KATEDRA on ZAM_VIEW.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY"
                + " left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY");
    }
}

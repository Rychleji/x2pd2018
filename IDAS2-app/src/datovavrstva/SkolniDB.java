package datovavrstva;

import OracleConnector.OracleConnector;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
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
    public void editTeacher(int origId, String name, String lastname, String titles, String titlesAfter, int phone, int mobilePhone, String email, String department, int role, int rights, String username, String password) throws SQLException{
        CallableStatement stmt = connect.prepareCall("{call UPRAVZAMESTNANCE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        stmt.setInt(1, origId);
        stmt.setString(2, name);
        stmt.setString(3, lastname);
        stmt.setString(4, titles);
        stmt.setString(5, titlesAfter);
        stmt.setString(6, email);
        stmt.setString(7, department);
        stmt.setInt(8, rights);
        stmt.setInt(9, role);
        if(mobilePhone!=0)
            stmt.setInt(10, mobilePhone);
        else
            stmt.setNull(10, java.sql.Types.INTEGER);
        if(phone!=0)
            stmt.setInt(11, phone);
        else
            stmt.setNull(11, java.sql.Types.INTEGER);
        if(username.equals("") ||password.equals("")){
            stmt.setNull(12, java.sql.Types.VARCHAR);
            stmt.setNull(13, java.sql.Types.VARCHAR);
        }else{
            stmt.setString(12, username);
            stmt.setString(13, password);
        }
        
        stmt.executeUpdate();
    }

    @Override
    public void deletePicture(int id) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZFOTKU(?)}");
        
        stmt.setInt(1, id);
        
        stmt.executeUpdate();
    }
    
    @Override
    public void addPicture(InputStream image, int id_zamestnanec) throws SQLException{
        CallableStatement stmt = connect.prepareCall("{call VLOZOBRAZEK(?, ?)}");
        
        stmt.setBinaryStream(1, image);
        stmt.setInt(2, id_zamestnanec);
        stmt.executeUpdate();
    }
    
    @Override
    public void addTeacher(String name, String lastname, String titles, String titlesAfter, int phone, int mobilePhone, String email, String department, int role, int rights, String username, String password) throws SQLException{
        CallableStatement stmt = connect.prepareCall("{call VLOZZAMESTNANCE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        stmt.setString(1, name);
        stmt.setString(2, lastname);
        stmt.setString(3, titles);
        stmt.setString(4, titlesAfter);
        stmt.setString(5, email);
        stmt.setString(6, department);
        stmt.setInt(7, rights);
        stmt.setInt(8, role);
        if(mobilePhone!=0)
            stmt.setInt(9, mobilePhone);
        else
            stmt.setNull(9, java.sql.Types.INTEGER);
        if(phone!=0)
            stmt.setInt(10, phone);
        else
            stmt.setNull(10, java.sql.Types.INTEGER);
        if(username.equals("") ||password.equals("")){
            stmt.setNull(11, java.sql.Types.VARCHAR);
            stmt.setNull(12, java.sql.Types.VARCHAR);
        }else{
            stmt.setString(11, username);
            stmt.setString(12, password);
        }
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteTeacher(int id) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZZAMESTNANCE(?)}");
        stmt.setInt(1, id);
        
        stmt.executeUpdate();
    }

    @Override
    public void editDepartment(String origShort, String newShort, String name, String faculty) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call UPRAVKATEDRU(?, ?, ?, ?)}");
        stmt.setString(1, origShort);
        stmt.setString(2, newShort);
        stmt.setString(3, name);
        stmt.setString(4, faculty);
        
        stmt.executeUpdate();
    }

    @Override
    public void addDepartment(String newShort, String name, String faculty) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call VLOZKATEDRU(?, ?, ?)}");
        stmt.setString(1, newShort);
        stmt.setString(2, name);
        stmt.setString(3, faculty);
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteDepartment(String shortName) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZKATEDRU(?)}");
        stmt.setString(1, shortName);
        
        stmt.executeUpdate();
    }

    @Override
    public void editFaculty(String origShort, String newShort, String name) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call UPRAVFAKULTU(?, ?, ?)}");
        stmt.setString(1, origShort);
        stmt.setString(2, newShort);
        stmt.setString(3, name);
        
        stmt.executeUpdate();
    }

    @Override
    public void addFaculty(String newShort, String name) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call VLOZFAKULTU(?, ?)}");
        stmt.setString(1, newShort);
        stmt.setString(2, name);
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteFaculty(String shortname) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZFAKULTU(?)}");
        stmt.setString(1, shortname);
        
        stmt.executeUpdate();
    }

    @Override
    public void editSpecialization(String origShort, String newShort, String name, String faculty) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call UPRAVOBOR(?, ?, ?, ?)}");
        stmt.setString(1, origShort);
        stmt.setString(2, newShort);
        stmt.setString(3, name);
        stmt.setString(4, faculty);
        
        stmt.executeUpdate();
    }

    @Override
    public void addSpecialization(String newShort, String name, String faculty) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call VLOZOBOR(?, ?, ?)}");
        stmt.setString(1, newShort);
        stmt.setString(2, name);
        stmt.setString(3, faculty);
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteSpecialization(String shortname) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZOBOR(?)}");
        stmt.setString(1, shortname);
        
        stmt.executeUpdate();
    }

    @Override
    public void editSubject(String origShort, String newShort, String name, int year, int semester, int end, int form) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call UPRAVPREDMET(?, ?, ?, ?, ?, ?, ?)}");
        stmt.setString(1, origShort);
        stmt.setString(2, newShort);
        stmt.setString(3, name);
        stmt.setInt(4, year);
        stmt.setInt(5, semester);
        stmt.setInt(6, end);
        stmt.setInt(7, form);
        
        stmt.executeUpdate();
    }

    @Override
    public void addSubject(String newShort, String name, int year, int semester, int end, int form) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call VLOZPREDMET(?, ?, ?, ?, ?, ?)}");
        stmt.setString(1, newShort);
        stmt.setString(2, name);
        stmt.setInt(3, year);
        stmt.setInt(4, semester);
        stmt.setInt(5, end);
        stmt.setInt(6, form);
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteSubject(String shortname) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZPREDMET(?)}");
        stmt.setString(1, shortname);
        
        stmt.executeUpdate();
    }

    @Override
    public void editSpecializationSubject(String origShortSpec, String origShortSubj, String newShortSpec, String newShortSubj, String category) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call UPRAVOBORPREDMET(?, ?, ?, ?, ?)}");
        stmt.setString(1, origShortSpec);
        stmt.setString(2, origShortSubj);
        stmt.setString(3, newShortSpec);
        stmt.setString(4, newShortSubj);
        stmt.setString(5, category);
        
        stmt.executeUpdate();
    }

    @Override
    public void addSpecializationSubject(String shortSpec, String shortSubj, String category) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call VLOZOBORPREDMET(?, ?, ?)}");
        stmt.setString(1, shortSpec);
        stmt.setString(2, shortSubj);
        stmt.setString(3, category);
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteSpecializationSubject(String shortSpec, String shortSubj) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZOBORPREDMET(?, ?)}");
        stmt.setString(1, shortSpec);
        stmt.setString(2, shortSubj);
        
        stmt.executeUpdate();
    }

    @Override
    public void editSchedule(int id, int numberOfStudents, float startsAt, float span, String subjectShort, int type, String teacherRole, int teacherId, int roomId, String day) throws SQLException {
        //stmt.execute(String.format("exec UPRAVROZVRHOVOUAKCI(%d, %d, %f, %f, %s, %d, %s, %d, %d)", id, numberOfStudents, span, startsAt, subjectShort, type, teacherRole, teacherId, roomId));
        
        CallableStatement stmt = connect.prepareCall("{call UPRAVROZVRHOVOUAKCI(?,?,?,?,?,?,?,?,?,?,?)}");
        stmt.setInt(1, id);
        stmt.setInt(2, numberOfStudents);
        stmt.setFloat(3, span);
        stmt.setFloat(4, startsAt);
        stmt.setString(5, subjectShort);
        stmt.setInt(6, type);
        stmt.setString(7, teacherRole);
        stmt.setInt(8, teacherId);
        stmt.setInt(9, roomId);
        stmt.setInt(10, 0);
        stmt.setString(11, day);
        
        stmt.executeUpdate();
    }

    @Override
    public void addSchedule(int numberOfStudents, float startsAt, float span, String subjectShort, int type, String teacherRole, int teacherId, int roomId, String day) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call VLOZROZVRHOVOUAKCI(?,?,?,?,?,?,?,?,?)}");
        stmt.setInt(1, numberOfStudents);
        stmt.setFloat(2, span);
        stmt.setFloat(3, startsAt);
        stmt.setString(4, subjectShort);
        stmt.setInt(5, type);
        stmt.setString(6, teacherRole);
        stmt.setInt(7, teacherId);
        stmt.setInt(8, roomId);
        stmt.setString(9, day);
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteSchedule(int id) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZROZVRHOVOUAKCI(?)}");
        stmt.setInt(1, id);
        
        stmt.executeUpdate();
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
    public InputStream selectPictureToTeacher(int id) throws SQLException {
        
        PreparedStatement stmt =  connect.prepareStatement("select OBRAZEK FROM DATA WHERE ID_ZAMESTNANEC = " + id);
        InputStream is = null;
        ResultSet rset = stmt.executeQuery();
        while (rset.next()) {
            Blob blob = connect.createBlob();
            blob = rset.getBlob("obrazek");
            if (blob == null) {
                break;
            }
            is = blob.getBinaryStream();
        }

        return is;
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
        CallableStatement stmt = connect.prepareCall("{call UPRAVUCEBNU(?,?,?)}");
        stmt.setInt(1, id);
        stmt.setString(2, name);
        stmt.setInt(3, capacity);
        
        stmt.executeUpdate();
    }

    @Override
    public void addClassroom(String name, int capacity) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call VLOZUCEBNU(?,?)}");
        stmt.setString(1, name);
        stmt.setInt(2, capacity);
        
        stmt.executeUpdate();
    }

    @Override
    public void deleteClassroom(int id) throws SQLException {
        CallableStatement stmt = connect.prepareCall("{call SMAZUCEBNU(?)}");
        stmt.setInt(1, id);
        
        int a = stmt.executeUpdate();
   }

    @Override
    public ResultSet selectEmployees() throws SQLException {
        Statement statement = connect.createStatement();

        return statement.executeQuery("select * from ZAM_VIEW"
                + " left join KATEDRA on ZAM_VIEW.KATEDRA_ZKRATKA_KATEDRY = KATEDRA.ZKRATKA_KATEDRY"
                + " left join FAKULTA on KATEDRA.FAKULTA_ZKRATKA_FAKULTY = FAKULTA.ZKRATKA_FAKULTY");
    }

    @Override
    public ResultSet selectSchedules_byClassroom(int classroomId) throws SQLException {
        Statement statement = connect.createStatement();
        
        return statement.executeQuery("select * from ROZVRHOVE_AKCE_EXT_VIEW where ID_UCEBNY = "+classroomId);
    }
}

package datovavrstva;

import OracleConnector.OracleConnector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SkolniDB implements ISkolniDB {

    private Connection connect = null;
    private final String updateMask = "UPDATE %s SET %s WHERE %s";
    private final String addMask = "INSERT INTO %s %s VALUES (%s)";
    private final String deleteMask = "DELETE FROM %s WHERE %s";

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
    public void editTeacher(String origId, String newId, String name, String lastname, String titles, String titlesAfter, String phone, String mobilePhone, String email, String department) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "VYUCUJICI";
        String set = String.format("ID_VYUCUJICIHO = '%s', JMENO = '%s', PRIJMENI = '%s', TITUL_PRED = '%s', "
                + "TITUL_ZA = '%s', TELEFON = '%s', MOBIL = '%s', EMAIL = '%s', KATEDRA_ZKRATKA_KATEDRY = '%s'",
                newId, name, lastname, titles, titlesAfter, phone, mobilePhone, email, department);
        String condition = "ID_VYUCUJICIHO = '" + origId + "'";

        stmt.execute(String.format(updateMask, table, set, condition));
    }

    @Override
    public void addTeacher(String newId, String name, String lastname, String titles, String titlesAfter, String phone, String mobilePhone, String email, String department) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "VYUCUJICI";
        String columns = "(ID_VYUCUJICIHO, JMENO, PRIJMENI, TITUL_PRED, TITUL_ZA, TELEFON, MOBIL, EMAIL, KATEDRA_ZKRATKA_KATEDRY)";
        String set = String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                newId, name, lastname, titles, titlesAfter, phone, mobilePhone, email, department);

        stmt.execute(String.format(addMask, table, columns, set));
    }

    @Override
    public void deleteTeacher(String id) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "VYUCUJICI";
        String condition = "ID_VYUCUJICIHO = '" + id + "'";

        stmt.execute(String.format(deleteMask, table, condition));
    }

    @Override
    public void editDepartment(String origShort, String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "KATEDRA";
        String set = String.format("ZKRATKA_KATEDRY = '%s', NAZEV_KATEDRY = '%s',"
                + " FAKULTA_ZKRATKA_FAKULTY = '%s'",
                newShort, name, faculty);
        String condition = "ZKRATKA_KATEDRY = '" + origShort + "'";

        stmt.execute(String.format(updateMask, table, set, condition));
    }

    @Override
    public void addDepartment(String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "KATEDRA";
        String set = String.format("(ZKRATKA_KATEDRY, NAZEV_KATEDRY,"
                + " FAKULTA_ZKRATKA_FAKULTY)");
        String values = String.format("'%s','%s', '%s'", newShort, name, faculty);

        stmt.execute(String.format(addMask, table, set, values));
    }

    @Override
    public void deleteDepartment(String shortName) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "KATEDRA";
        String condition = "ZKRATKA_KATEDRY = '" + shortName + "'";

        stmt.execute(String.format(deleteMask, table, condition));
    }

    @Override
    public void editFaculty(String origShort, String newShort, String name) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "FAKULTA";
        String set = String.format("ZKRATKA_FAKULTY = '%s', NAZEV_FAKULTY = '%s'",
                newShort, name);
        String condition = "ZKRATKA_FAKULTY = '" + origShort + "'";

        stmt.execute(String.format(updateMask, table, set, condition));
    }

    @Override
    public void addFaculty(String newShort, String name) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "FAKULTA";
        String set = "";
        String values = "'" + newShort + "', '" + name + "'";

        stmt.execute(String.format(addMask, table, set, values));
    }

    @Override
    public void deleteFaculty(String shortname) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "FAKULTA";
        String condition = "ZKRATKA_FAKULTY = '" + shortname + "'";

        stmt.execute(String.format(deleteMask, table, condition));
    }

    @Override
    public void editSpecialization(String origShort, String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "OBOR";
        String set = String.format("ZKRATKA_OBORU = '%s', NAZEV_OBORU = '%s',"
                + " FAKULTA_ZKRATKA_FAKULTY = '%s'",
                newShort, name, faculty);
        String condition = "ZKRATKA_OBORU = '" + origShort + "'";

        stmt.execute(String.format(updateMask, table, set, condition));
    }

    @Override
    public void addSpecialization(String newShort, String name, String faculty) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "OBOR";
        String set = "";
        String values = "'" + newShort + "', '" + name + "', '" + faculty + "'";

        stmt.execute(String.format(addMask, table, set, values));
    }

    @Override
    public void deleteSpecialization(String shortname) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "OBOR";
        String condition = "ZKRATKA_OBORU = '" + shortname + "'";

        stmt.execute(String.format(deleteMask, table, condition));
    }

    @Override
    public void editSubject(String origShort, String newShort, String name, int year, int semester, int end, int form) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "PREDMET";
        String set = String.format("ZKRATKA_PREDMETU = '%s', NAZEV_PREDMETU = '%s',"
                + " DOPORUCENY_ROCNIK = %d, SEMESTR_ID_SEMESTR = %d, ZPUSOB_ZAKONCENI_ID_ZZ = %d,"
                + " FORMA_VYUKY_ID_FV = %d",
                newShort, name, year, semester, end, form);
        String condition = "ZKRATKA_PREDMETU = '" + origShort + "'";

        stmt.execute(String.format(updateMask, table, set, condition));
    }

    @Override
    public void addSubject(String newShort, String name, int year, int semester, int end, int form) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "PREDMET";
        String set = "(ZKRATKA_PREDMETU, NAZEV_PREDMETU,"
                + " DOPORUCENY_ROCNIK, SEMESTR_ID_SEMESTR, ZPUSOB_ZAKONCENI_ID_ZZ, "
                + "FORMA_VYUKY_ID_FV, ROZSAH_HODIN)";
        String values = String.format("'%s', '%s', %d, %d, %d, %d, 0",
                newShort, name, year, semester, end, form);

        stmt.execute(String.format(addMask, table, set, values));
    }

    @Override
    public void deleteSubject(String shortname) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "PREDMET";
        String condition = "ZKRATKA_PREDMETU = '" + shortname + "'";

        stmt.execute(String.format(deleteMask, table, condition));
    }

    @Override
    public void editSpecializationSubject(String origShortSpec, String origShortSubj, String newShortSpec, String newShortSubj, String category) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "OBOR_PREDMET";
        String set = String.format("OBOR_ZKRATKA_OBORU = '%s', PREDMET_ZKRATKA_PREDMETU = '%s',"
                + " KATEGORIE_PREDMETU_KATEGORIE = '%s'", newShortSpec, newShortSubj, category);
        String condition = "where OBOR_ZKRATKA_OBORU = '"+origShortSpec+"' "
                + "and PREDMET_ZKRATKA_PREDMETU = '"+origShortSubj+"'";          

        stmt.execute(String.format(updateMask, table, set, condition));
    }

    @Override
    public void addSpecializationSubject(String shortSpec, String shortSubj, String category) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "OBOR_PREDMET";
        String set = "(OBOR_ZKRATKA_OBORU, PREDMET_ZKRATKA_PREDMETU, KATEGORIE_PREDMETU_KATEGORIE)";
        String values = String.format("'%s', '%s', '%s'",
                shortSpec, shortSubj, category);

        stmt.execute(String.format(addMask, table, set, values));
    }

    @Override
    public void deleteSpecializationSubject(String shortSpec, String shortSubj) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "OBOR_PREDMET";
        String condition = "OBOR_ZKRATKA_OBORU = '" + shortSpec+" and PREDMET_ZKRATKA_PREDMETU = '"+shortSubj+"'";

        stmt.execute(String.format(deleteMask, table, condition));
    }

    @Override
    public void editSchedule(int id, int numberOfStudents, float startsAt, int span, String subjectShort, int type, String teacherRole, String teacherId) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "ROZVRHOVA_AKCE";
        String set = String.format("POCET_STUDENTU = %d, MAHODIN = %d, ZACINAV = %.2f, "
                + "PREDMET_ZKRATKA_PREDMETU = '%s', ZPUSOB_VYUKY_ID_ZV = %d, "
                + "ROLE_VYUCUJICIHO_ROLE = '%s', ID_ZAMESTNANEC = '%s'",
                numberOfStudents, span, startsAt, subjectShort, type, teacherRole, teacherId);
        String condition = "ID_RA = " + id;

        stmt.execute(String.format(updateMask, table, set, condition));
    }

    @Override
    public void addSchedule(int numberOfStudents, float startsAt, int span, String subjectShort, int type, String teacherRole, String teacherId) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "ROZVRHOVA_AKCE";
        String columns = "(POCET_STUDENTU, MAHODIN, ZACINAV, PREDMET_ZKRATKA_PREDMETU, ZPUSOB_VYUKY_ID_ZV, ROLE_VYUCUJICIHO_ROLE, ID_ZAMESTNANEC)";
        String set = String.format("%d, %d, %s, '%s', %d, '%s', '%s'",
                numberOfStudents, span, String.valueOf(startsAt).replace(',', '.'), subjectShort, type, teacherRole, teacherId);

        stmt.execute(String.format(addMask, table, columns, set));
    }

    @Override
    public void deleteSchedule(int id) throws SQLException {
        Statement stmt = connect.createStatement();

        String table = "ROZVRHOVA_AKCE";
        String condition = "ID_RA = " + id;

        stmt.execute(String.format(deleteMask, table, condition));
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
}

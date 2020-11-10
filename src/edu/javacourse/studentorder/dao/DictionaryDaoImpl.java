package edu.javacourse.studentorder.dao;

import edu.javacourse.studentorder.domain.Street;
import edu.javacourse.studentorder.exception.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DictionaryDaoImpl implements DictionaryDao
{
    private static final String GET_STREET = "SELECT street_code, street_name " +
            "FROM jc_street WHERE UPPER(street_name) LIKE UPPER(?)";

    private Connection getConnection() throws SQLException {
        // подгружаем наш класс в систему JDBC
        // Class.forName("org.postgresql.Driver"); уже можно это не делать
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/jc_student",
                "postgres", "postgre"); // or password "postgre"!!!
        return con;
    }

    public List<Street> findStreet(String pattern) throws DaoException {
        List<Street> result = new LinkedList<>();
        try (Connection con = getConnection();
            PreparedStatement stmt = con.prepareStatement(GET_STREET)) { // подключаемся к базе и она потом сама отключится при выходе из try
            // создаем запрос
            // выполняем этот запрос, который позволяет получить ответ
            // чтобы получить ответ надо ResultSet
            stmt.setString(1, "%" + pattern + "%");
            ResultSet rs = stmt.executeQuery();
            // получаем некоторое множество и чтобы по нему двигаться rs.next
            // этот метод передвигает в данном множестве на следующую строчку
            // пока строчек не останется. Если строчка есть то он на нее передвинется
            // и мы можем общаться с этой строчкой через объект ResultSet
            while (rs.next()) {
                Street str = new Street(rs.getLong("street_code"),
                        rs.getString("street_name"));
                result.add(str);
            }
        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }
}

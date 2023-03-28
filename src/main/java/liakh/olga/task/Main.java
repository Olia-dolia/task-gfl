package liakh.olga.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //private static final String CREATE_TABLE = "CREATE TABLE equations(id serial PRIMARY KEY, equation VARCHAR(255))";
    private static final String INSERT_QUERY = "INSERT INTO equations (equation) VALUES (?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM equations";

    /*private static void setCreateTable(DBConnector dbConnector) throws SQLException {
        dbConnector.getConnection().prepareStatement(CREATE_TABLE);
    }*/

    private static void insertRecord(DBConnector dbConnector, String input) throws SQLException {
        PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, input);
        int affectedRows = preparedStatement.executeUpdate();
        int id = 0;
        if (affectedRows > 0) {
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private static List<Equation> selectAllRecords(DBConnector dbConnector) throws SQLException {
        List<Equation> records = new ArrayList<>();
        Equation equation;
        PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(SELECT_ALL_QUERY);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            equation = new Equation();
            equation.setId(rs.getInt("id"));
            equation.setEquation(rs.getString("equation"));
            records.add(equation);
        }
        return records;
    }

    public static void main(String[] args) throws SQLException {
        DBConnector dbConnector = new DBConnector();
        Equation equation = new Equation();
        Scanner sc = new Scanner(System.in);

        String equationStr = equation.getEquationFromScanner(sc);
        if (!equationStr.isEmpty()) {
            System.out.println("Amount of numbers = " + equation.amountOfNumbers(equationStr));
            insertRecord(dbConnector, equationStr);
        }

        List<Equation> allRecords = selectAllRecords(dbConnector);

        for (Equation e : allRecords) {
            System.out.println(e.toString());
        }
        dbConnector.getConnection().close();

    }
}

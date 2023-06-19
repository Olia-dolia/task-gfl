package liakh.olga.task;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    //private static final String CREATE_TABLE = "CREATE TABLE equations(id serial PRIMARY KEY, equation VARCHAR(255), root REAL)";
    private static final String INSERT_QUERY = "INSERT INTO equations (equation, root) VALUES (?,?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM equations";
    private static final String SELECT_ALL_EQU_WITH_ROOT = "SELECT * FROM equations WHERE root=";

    /*private static void setCreateTable(DBConnector dbConnector) throws SQLException {
        dbConnector.getConnection().prepareStatement(CREATE_TABLE);
    }*/

    private static void insertRecord(DBConnector dbConnector, String inputE, double inputR) throws SQLException {
        PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, inputE);
        preparedStatement.setDouble(2, inputR);
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
            equation.setRoot(rs.getDouble("root"));
            records.add(equation);
        }
        return records;
    }

    private static List<Equation> selectAllRecordsWithRoot(DBConnector dbConnector, double root) throws SQLException {
        List<Equation> records = new ArrayList<>();
        Equation equation;
        PreparedStatement preparedStatement = dbConnector.getConnection().prepareStatement(SELECT_ALL_EQU_WITH_ROOT + root);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            equation = new Equation();
            equation.setId(rs.getInt("id"));
            equation.setEquation(rs.getString("equation"));
            equation.setRoot(rs.getDouble("root"));
            records.add(equation);
        }
        return records;
    }

    private static Double isRoot(double root, String equationStr) {
        Pattern p = Pattern.compile(".*(?==)|(?<==).*");
        Matcher m = p.matcher(equationStr);
        List<String> partsOfEquation = new ArrayList<>();
        while (m.find()) {
            partsOfEquation.add(m.group());
        }
        partsOfEquation.remove(1);
        double res;
        if (partsOfEquation.get(0).contains("x")) {
            Expression expression = new ExpressionBuilder(partsOfEquation.get(0))
                    .variable("x").build().setVariable("x", root);
            res = expression.evaluate();
            System.out.println(res);
            if (res == Double.parseDouble(partsOfEquation.get(1))) {
                return root;
            }
        } else {
            Expression expression = new ExpressionBuilder(partsOfEquation.get(1)).variable("x").build().setVariable("x", root);
            res = expression.evaluate();
            System.out.println(res);
            if (res == Double.parseDouble(partsOfEquation.get(0))) {
                return root;
            }
        }
        return 0.0;
    }

    public static void main(String[] args) throws SQLException {
        DBConnector dbConnector = new DBConnector();
        Equation equation = new Equation();
        Scanner sc = new Scanner(System.in);

        String equationStr = equation.getEquationFromScanner(sc);
        if (!equationStr.isEmpty()) {
            System.out.println("Amount of numbers = " + equation.amountOfNumbers(equationStr));
            System.out.println("Please enter a root of equation: ");
            sc = new Scanner(System.in);
            insertRecord(dbConnector, equationStr, isRoot(sc.nextDouble(), equationStr));
        }

        List<Equation> allRecords = selectAllRecords(dbConnector);

        for (Equation e : allRecords) {
            System.out.println(e.toString());
        }

        List<Equation> allRecordsWithRoot = selectAllRecordsWithRoot(dbConnector, 6.0);

        for (Equation e : allRecordsWithRoot) {
            System.out.println(e.toString());
        }
        dbConnector.getConnection().close();
    }
}

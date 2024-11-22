package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.EmployeeDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {

    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("select * from employee");

        ArrayList<EmployeeDTO> employeeDTOS = new ArrayList<>();

        while (rst.next()) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)


            );
            employeeDTOS.add(employeeDTO);
        }
        return employeeDTOS;

    }

    public String getNextEmployeeId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select emp_id from employee order by emp_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("E%03d", newIdIndex);
        }
        return "E001";
    }

    public boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into employee values (?,?,?,?,?,?,?)",
                employeeDTO.getEmpId(),
                employeeDTO.getEmpName(),
                employeeDTO.getEmpRole(),
                employeeDTO.getEmpSalary(),
                employeeDTO.getEmpAddress(),
                employeeDTO.getEmpNum(),
                employeeDTO.getEmpMail()

        );
    }

    public boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException {
        return CrudUtil.execute(
                "update employee set emp_name=?, emp_role=?, emp_salary=?, emp_add=?, emp_num=?, emp_mail=? where emp_id=?",
                employeeDTO.getEmpName(),
                employeeDTO.getEmpRole(),
                employeeDTO.getEmpSalary(),
                employeeDTO.getEmpAddress(),
                employeeDTO.getEmpNum(),
                employeeDTO.getEmpMail(),
                employeeDTO.getEmpId()
        );

    }

    public boolean deleteEmployee(String employeeId) throws SQLException {
        return CrudUtil.execute("delete from employee where emp_id=?", employeeId);
    }

    public EmployeeDTO findEmpById(String employeeId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from employee where emp_id=?", employeeId);

        if (rst.next()) {
            return new EmployeeDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        }
        return null;
    }

    public ArrayList<String> getAllEmployIds() throws SQLException {

        ResultSet rst = CrudUtil.execute("select emp_id from employee"), selectedEmpId;
        ArrayList<String> employIds = new ArrayList<>();

        while (rst.next()) {
            employIds.add(rst.getString(1));
        }
        return employIds;
    }
}

package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.EmployeePayrollDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeePayrollModel {

    public ArrayList<EmployeePayrollDTO> getAllEmployeePayrolls() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from employee_payroll");

        ArrayList<EmployeePayrollDTO> employeePayrollDTOS = new ArrayList<>();

        while (rst.next()) {
            EmployeePayrollDTO employeePayrollDTO = new EmployeePayrollDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDate(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
                    );
            employeePayrollDTOS.add(employeePayrollDTO);
        }
        return employeePayrollDTOS;
    }

    public String getNextEmployeePayrollId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select payroll_id from employee_payroll order by payroll_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(4);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("PR%03d", newIdIndex);
        }
        return "PR001";
    }


    public boolean saveEmployeePayroll(EmployeePayrollDTO employeePayrollDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into employee_payroll (payroll_id, emp_id, payroll_date, basic_salary, deductions, bonuses) values (?, ?, ?, ?, ?, ?)",
                employeePayrollDTO.getPayrollId(),
                employeePayrollDTO.getPayrollEmpId(),
                employeePayrollDTO.getPayrollDate(),
                employeePayrollDTO.getBaseSalary(),
                employeePayrollDTO.getDeductions(),
                employeePayrollDTO.getBonuses()
        );
    }

    public boolean updateEmployeePayroll(String payrollId, String deductions, String bonuses) throws SQLException {
        return CrudUtil.execute(
                "update employee_payroll set deductions = ?, bonuses = ? where payroll_id = ?",
                deductions,
                bonuses,
                payrollId
        );
    }


    public boolean deleteEmployeePayroll(String payrollId) throws SQLException {
        return CrudUtil.execute(
                "delete from employee_payroll where payroll_id = ?",
                payrollId
        );
    }




}

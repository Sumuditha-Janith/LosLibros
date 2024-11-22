package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.dto.EmployeeLeaveDTO;
import lk.ijse.gdse.loslibros.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeLeaveModel {

    public ArrayList<EmployeeLeaveDTO> getAllEmployeeLeaves() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from employee_leave");

        ArrayList<EmployeeLeaveDTO> employeeLeaveDTOs = new ArrayList<>();

        while (rst.next()) {
            EmployeeLeaveDTO employeeLeaveDTO = new EmployeeLeaveDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDate(4),
                    rst.getDate(5),
                    rst.getString(6)

                    );
            employeeLeaveDTOs.add(employeeLeaveDTO);
        }
        return employeeLeaveDTOs;
    }

    public String getNextEmployeeLeaveId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select leave_id from employee_leave order by leave_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("L%03d", newIdIndex);
        }
        return "L001";
    }

    public boolean saveEmployeeLeave(EmployeeLeaveDTO employeeLeaveDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into employee_leave values(?,?,?,?,?,?)",
                employeeLeaveDTO.getLeaveId(),
                employeeLeaveDTO.getLeaveEmpId(),
                employeeLeaveDTO.getLeaveType(),
                employeeLeaveDTO.getLeaveStartDate(),
                employeeLeaveDTO.getLeaveEndDate(),
                employeeLeaveDTO.getLeaveStatus()
        );
    }


    public boolean updateEmployeeLeave(EmployeeLeaveDTO employeeLeaveDTO) throws SQLException {
        return CrudUtil.execute(
                "update employee_leave set leave_type = ?, start_date = ?, end_date = ?, status = ? where leave_id = ?",
                employeeLeaveDTO.getLeaveType(),
                employeeLeaveDTO.getLeaveStartDate(),
                employeeLeaveDTO.getLeaveEndDate(),
                employeeLeaveDTO.getLeaveStatus(),
                employeeLeaveDTO.getLeaveId()
        );
    }


    public boolean deleteEmployeeLeave(String leaveId) throws SQLException {
        return CrudUtil.execute(
                "delete from employee_leave where leave_id=?",leaveId
        );
    }

}

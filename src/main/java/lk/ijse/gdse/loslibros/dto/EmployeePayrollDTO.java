package lk.ijse.gdse.loslibros.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EmployeePayrollDTO {

    private String payrollId;
    private String payrollEmpId;
    private Date payrollDate;
    private String baseSalary;
    private String deductions;
    private String bonuses;
    private String netSalary;

}

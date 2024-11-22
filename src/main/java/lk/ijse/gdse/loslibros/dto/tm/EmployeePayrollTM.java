package lk.ijse.gdse.loslibros.dto.tm;

import java.sql.Date;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class EmployeePayrollTM {

    private String payrollId;
    private String payrollEmpId;
    private Date payrollDate;
    private String baseSalary;
    private String deductions;
    private String bonuses;
    private String netSalary;

}

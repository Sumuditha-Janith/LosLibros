package lk.ijse.gdse.loslibros.model;

import lk.ijse.gdse.loslibros.util.CrudUtil;
import lk.ijse.gdse.loslibros.dto.SupplierDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {

    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("select * from supplier");

        ArrayList<SupplierDTO> supplierDTOS = new ArrayList<>();

        while (rst.next()) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
            supplierDTOS.add(supplierDTO);
        }
        return supplierDTOS;

    }

    public String getNextSupplierId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select sup_id from supplier order by sup_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("S%03d", newIdIndex);
        }
        return "S001";
    }

    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "insert into supplier values (?,?)",
                supplierDTO.getSupplierId(),
                supplierDTO.getSupplierName()
        );
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {
        return CrudUtil.execute(
                "update supplier set sup_name=? where sup_id=?",
                supplierDTO.getSupplierName(),
                supplierDTO.getSupplierId()
        );

    }

    public boolean deleteSupplier(String supplierId) throws SQLException {
        return CrudUtil.execute("delete from supplier where sup_id=?", supplierId);
    }

    public ArrayList<String> getAllSupplierIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select sup_id from supplier");

        ArrayList<String> supplierIds = new ArrayList<>();

        while (rst.next()) {
            supplierIds.add(rst.getString(1));
        }

        return supplierIds;
    }

    public SupplierDTO findSupplierById(String selectedSupplierId) throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from supplier where sup_id=?", selectedSupplierId);

        if (rst.next()) {
            return new SupplierDTO(
                    rst.getString(1),
                    rst.getString(2)

            );
        }
        return null;
    }

}

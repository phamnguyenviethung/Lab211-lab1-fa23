package controllers;

import models.Warehouse;
import java.util.List;
import models.Product;

public interface IWarehouseController {

    boolean addReceipt(Warehouse receipt);

    int getSize();

    List<Warehouse> getReceiptList(String productCode);

    List<Product> getProductList(String productCode);

    boolean isProductExist(String productCode);

    boolean loadFromFile();

    boolean saveToFile();

}

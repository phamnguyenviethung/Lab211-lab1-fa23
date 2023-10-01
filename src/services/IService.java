package services;

import models.Product;
import utils.Trade;

public interface IService {

    // Product
    void showAllProduct();

    Product addProduct();

    void updateProduct();

    void deleteProduct();

    // Warehouse
    void addReceipt(Trade tradeType);

    // Report 
    void showExpiredProducts();

    void showSellingProducts();

    void showOutOfStockProducts();

    void showReceipt();

    // File 
    boolean loadProductsFromFile();

    boolean saveProductsToFile();

    boolean loadWarehouseFromFile();

    boolean saveWarehouseToFile();
}

package services;

import controllers.ControllerFactory;
import controllers.IControllerFactory;
import controllers.IProductController;
import controllers.IWarehouseController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import models.Product;
import models.ProductType;
import models.Warehouse;
import utils.IValidation;
import utils.Status;
import utils.Trade;

public class Service implements IService {

    private static final IControllerFactory factory = new ControllerFactory();
    static final IProductController ProductController = factory.productController();
    static final IWarehouseController WarehouseController = factory.warehouseController();
    static final IValidation Validator = factory.validation();

    boolean isNameChanged, isPriceChanged, isTypeChanged, isManuDateChanged, isExpDateChanged;

    // Product
    @Override
    public Product addProduct() {
        String productCode = "";
        while (true) {
            productCode = Validator.checkString("Input product code:", Status.NORMAL);
            if (!ProductController.isProductCodeExist(productCode)) {
                break;
            } else {
                System.err.println("Code is dulplicated! Please enter again.");
            }
        }
        String productName = Validator.checkString("Input product name:", Status.NORMAL);
        int quantity = Validator.checkInt("Input quantity:", 0, Integer.MAX_VALUE, Status.NORMAL);
        double price = Validator.checkDouble("Input price:", 0, Double.MAX_VALUE, Status.NORMAL);
        ProductType productType = Validator.checkProductType("Input product type (DAILY/LONG):", Status.NORMAL);
        Date manufacturingDate = Validator.checkBeforeDate("Input manufacturing date (dd/MM/yyyy):", Status.NORMAL);
        Date expirationDate = Validator.checkAfterDate("Input expiration date (dd/MM/yyyy):", manufacturingDate, Status.NORMAL);
        Product addedProduct = new Product(productCode, productName, quantity, price, productType, manufacturingDate, expirationDate);

        if (ProductController.addProduct(addedProduct)) {
            System.out.println("Successfully add product: " + addedProduct);
        }
        return addedProduct;
    }

    @Override
    public void updateProduct() {
        if (ProductController.isProductListEmpty()) {
            System.out.println("The product list is empty");
            System.out.println("=======");

        } else {
            String pattern = "dd/MM/yyyy";
            DateFormat sdf = new SimpleDateFormat(pattern);
            String productCode = "";

            while (true) {
                productCode = Validator.checkString("Input product code:", Status.NORMAL);
                if (ProductController.isProductCodeExist(productCode)) {
                    break;
                } else {
                    System.err.println("Product does not exist! Please enter again.");
                }
            }

            Product updatedProduct = ProductController.getProduct(productCode);

            String productName = Validator.checkString("Input product name:", Status.UPDATE);
            isNameChanged = updatedProduct.setName(productName);

            if (isNameChanged) {
                System.out.println("Successfully changed to: " + productName);
            }

            int quantity = Validator.checkInt("Input quantity:", 0, Integer.MAX_VALUE, Status.UPDATE);
            if (updatedProduct.setQuantity(quantity)) {
                System.out.println("Successfully changed to: " + quantity);
            }

            double price = Validator.checkDouble("Input price:", 0, Double.MAX_VALUE, Status.UPDATE);
            isPriceChanged = updatedProduct.setPrice(price);
            if (isPriceChanged) {
                System.out.println("Successfully changed to: " + price);
            }

            ProductType productType = Validator.checkProductType("Input product type (DAILY/LONG):", Status.UPDATE);
            isTypeChanged = updatedProduct.setType(productType);
            if (isTypeChanged) {
                System.out.println("Successfully changed to: " + productType);
            }

            Date manufacturingDate = Validator.checkBeforeDate("Input manufacturing date (dd/MM/yyyy):", Status.UPDATE);
            isManuDateChanged = updatedProduct.setManufacturingDate(manufacturingDate);
            if (isManuDateChanged) {
                System.out.println("Successfully changed to: " + sdf.format(manufacturingDate));
            }

            Date expirationDate = Validator.checkAfterDate("Input expiration date (dd/MM/yyyy):", updatedProduct.getManufacturingDate(), Status.UPDATE);
            isExpDateChanged = updatedProduct.setExpirationDate(expirationDate);
            if (isExpDateChanged) {
                System.out.println("Successfully changed to: " + sdf.format(expirationDate));
            }
            for (Product product : WarehouseController.getProductList(productCode)) {
                if (isNameChanged) {
                    product.setName(productName);
                }
                if (isPriceChanged) {
                    product.setPrice(price);
                }
                if (isTypeChanged) {
                    product.setType(productType);
                }
                if (isManuDateChanged) {
                    product.setManufacturingDate(manufacturingDate);
                }
                if (isExpDateChanged) {
                    product.setExpirationDate(expirationDate);
                }
            }
        }
    }

    @Override
    public void deleteProduct() {
        String productCode = "";
        while (true) {
            productCode = Validator.checkString("Input product code:", Status.NORMAL);
            if (ProductController.isProductCodeExist(productCode)) {
                break;
            } else {
                System.err.println("Product does not exist! Please enter again.");
            }
        }
        if (Validator.checkYesOrNo("Confirm delete (Y/N)?")) {
            Product deletedProduct = ProductController.getProduct(productCode);
            if (!WarehouseController.isProductExist(productCode)) {
                ProductController.deleteProduct(deletedProduct);
                System.out.println("Successfully delete: " + deletedProduct);
            } else {
                System.err.println("Fail to delete: " + deletedProduct);
            }
        }
    }

    @Override
    public void showAllProduct() {
        System.out.println("-----List of all products in store-----");
        for (Product product : ProductController.getAllProducts()) {
            System.out.println(product);
        }
    }

    @Override
    public boolean loadProductsFromFile() {
        return ProductController.loadFromFile();
    }

    @Override
    public boolean saveProductsToFile() {
        System.out.println("...Saving the list product to file...");
        return ProductController.saveToFile();
    }

    // Warehouse
    @Override
    public boolean loadWarehouseFromFile() {
        return WarehouseController.loadFromFile();
    }

    @Override
    public boolean saveWarehouseToFile() {
        System.out.println("...Saving the list warehouse information to file...");
        return WarehouseController.saveToFile();
    }

    @Override
    public void addReceipt(Trade tradeType) {
        List<Product> productList = new ArrayList<>();

        do {
            Product importProduct;
            String productCode = Validator.checkString("Input product code:", Status.NORMAL);
            // Case 1: ko ton tai
            if (!ProductController.isProductCodeExist(productCode)) {
                if (tradeType == Trade.EXPORT) {
                    System.err.println("Product does not exist! Please enter again.");
                    continue;
                } else {
                    String productName = Validator.checkString("Input product name:", Status.NORMAL);
                    int quantity = Validator.checkInt("Input quantity:", 1, Integer.MAX_VALUE, Status.NORMAL);
                    double price = Validator.checkDouble("Input price:", 0, Double.MAX_VALUE, Status.NORMAL);
                    ProductType productType = Validator.checkProductType("Input product type (DAILY/LONG):", Status.NORMAL);
                    Date manufacturingDate = Validator.checkBeforeDate("Input manufacturing date (dd/MM/yyyy):", Status.NORMAL);
                    Date expirationDate = Validator.checkAfterDate("Input expiration date (dd/MM/yyyy):", manufacturingDate, Status.NORMAL);
                    importProduct = new Product(productCode, productName, quantity, price, productType, manufacturingDate, expirationDate);

                    if (ProductController.addProduct(importProduct)) {
                        System.out.println("Successfully add product!");
                    }
                }
            } // Case 2: ton tai
            else {
                Product addedProduct = ProductController.getProduct(productCode);
                int newQuantity;
                if (tradeType == Trade.EXPORT) {

                    while (true) {
                        newQuantity = Validator.checkInt("Input quantity:", 1, Integer.MAX_VALUE, Status.NORMAL);

                        if (addedProduct.getQuantity() - newQuantity >= 0) {
                            break;
                        } else {
                            System.err.println("Not enough quantity to export! Please enter again.");
                        }
                    }
                } else {
                    newQuantity = Validator.checkInt("Input quantity:", 1, Integer.MAX_VALUE, Status.NORMAL);
                }

                String productName = addedProduct.getName();
                double price = addedProduct.getPrice();
                ProductType productType = addedProduct.getType();
                Date manufacturingDate = addedProduct.getManufacturingDate();
                Date expirationDate = addedProduct.getExpirationDate();
                importProduct = new Product(productCode, productName, newQuantity, price, productType, manufacturingDate, expirationDate);

                if (tradeType == Trade.EXPORT) {
                    addedProduct.setQuantity(addedProduct.getQuantity() - newQuantity);
                } else {
                    addedProduct.setQuantity(addedProduct.getQuantity() + newQuantity);
                }
            }

            if (tradeType == Trade.EXPORT && importProduct.isExpried()) {

                System.out.println("== Create export receipt failed : The product is expried !!  ==");

            } else {

                productList.add(importProduct);

            }

        } while (Validator.checkYesOrNo("Continue to add product (Y/N)?"));

        if (!productList.isEmpty()) {
            int code = 1000001 + WarehouseController.getSize();
            Date now = new Date();
            Warehouse warehouse = new Warehouse(code, tradeType, now, productList);

            if (WarehouseController.addReceipt(warehouse)) {
                System.out.println("Successfully add " + tradeType + " receipt with information:");
                System.out.println(warehouse);
            }

        }
    }

    // Report
    @Override
    public void showExpiredProducts() {
        System.out.println("-----Products that have expired-----");
        for (Product expProduct : ProductController.getExpiredProducts()) {
            System.out.println(expProduct.toReportString());
        }
    }

    @Override
    public void showSellingProducts() {
        System.out.println("-----Products that the store is selling-----");
        for (Product sellingProduct : ProductController.getSellingProducts()) {
            System.out.println(sellingProduct.toReportString());
        }
    }

    @Override
    public void showOutOfStockProducts() {
        System.out.println("-----Products that are running out of stock-----");
        List<Product> list = ProductController.getOutOfStockProducts();
        Collections.sort(list, Product.compareQuantity);
        for (Product outOfStockProduct : list) {
            System.out.println(outOfStockProduct.toReportString());
        }
    }

    @Override
    public void showReceipt() {
        String productCode = "";
        while (true) {
            productCode = Validator.checkString("Input product code:", Status.NORMAL);
            if (ProductController.isProductCodeExist(productCode)) {
                break;
            } else {
                System.err.println("Product does not exist! Please enter again.");
            }
        }

        for (Warehouse warehouse : WarehouseController.getReceiptList(productCode)) {
            String listItems = "";
            for (Product product : warehouse.getItems()) {
                if (product.getProductCode().equalsIgnoreCase(productCode)) {
                    listItems += product.toString() + "\n";
                }
            }
            System.out.println(warehouse.toReportString() + "," + "\n-----List of products-----\n" + listItems + "}");
        }
    }

}

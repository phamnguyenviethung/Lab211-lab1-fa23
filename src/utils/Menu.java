package utils;

import services.IService;
import services.Service;

public class Menu {

    private static final IService service = new Service();

    private static final IValidation validator = new Validation();

    public static void mainMenu() {
        int choice;
        service.loadProductsFromFile();
        service.loadWarehouseFromFile();
        do {

            System.out.println("---MAIN MENU---\n"
                    + "1. Manage products\n"
                    + "2. Manage Warehouse\n"
                    + "3. Report\n"
                    + "4. Store data to files\n"
                    + "5. Close the application");
            choice = validator.checkInt("Input the choice:", 1, 5, Status.NORMAL);
            switch (choice) {
                case 1:
                    subMenu_manageProducts();
                    break;
                case 2:
                    subMenu_manageWarehouse();
                    break;
                case 3:
                    subMenu_report();
                    break;
                case 4:
                    if (service.saveProductsToFile()) {
                        System.out.println("-----Successfully save the list product to product.dat!-----");
                    } else {
                        System.err.println("-----Fail to save the list product to product.dat!-----");
                    }
                    if (service.saveWarehouseToFile()) {
                        System.out.println("-----Successfully save the list warehouse information to warehouse.dat!-----");
                    } else {
                        System.err.println("-----Fail to save the list warehouse information to warehouse.dat!-----");
                    }
            }
        } while (choice != 5);
    }

    public static void subMenu_manageProducts() {
        int choice;
        do {
            System.out.println("----MANAGE PRODUCTS----\n"
                    + "1. Add a product\n"
                    + "2. Update product information\n"
                    + "3. Delete product\n"
                    + "4. Show all product\n"
                    + "5. Back to main menu");
            choice = validator.checkInt("Input the choice:", 1, 5, Status.NORMAL);
            switch (choice) {
                case 1:
                    do {
                        service.addProduct();
                    } while (validator.checkYesOrNo("Continue to add product (Y/N)?"));
                    break;
                case 2:
                    service.updateProduct();
                    break;
                case 3:
                    service.deleteProduct();
                    break;
                case 4:
                    service.showAllProduct();
            }
        } while (choice != 5);
    }

    public static void subMenu_manageWarehouse() {
        int choice;
        do {
            System.out.println("----MANAGE WAREHOUSE----\n"
                    + "1. Create an import receipt\n"
                    + "2. Create an export receipt\n"
                    + "3. Back to main menu");
            choice = validator.checkInt("Input the choice:", 1, 3, Status.NORMAL);
            switch (choice) {
                case 1:
                    service.addReceipt(Trade.IMPORT);
                    break;
                case 2:
                    service.addReceipt(Trade.EXPORT);
            }
        } while (choice != 3);
    }

    public static void subMenu_report() {
        int choice;
        do {
            System.out.println("----REPORT----\n"
                    + "1. Products that have expired\n"
                    + "2. The products that the store is selling\n"
                    + "3. Products that are running out of stock (sorted in ascending order)\n"
                    + "4. Import/export receipt of a product\n"
                    + "5. Back to main menu");
            choice = validator.checkInt("Input the choice:", 1, 5, Status.NORMAL);
            switch (choice) {
                case 1:
                    service.showExpiredProducts();
                    break;
                case 2:
                    service.showSellingProducts();
                    break;
                case 3:
                    service.showOutOfStockProducts();
                    break;
                case 4:
                    service.showReceipt();
            }
        } while (choice != 5);
    }
}

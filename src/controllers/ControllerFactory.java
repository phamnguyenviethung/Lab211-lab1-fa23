
package controllers;

import utils.IValidation;
import utils.Validation;

public class ControllerFactory implements IControllerFactory{

    @Override
    public IProductController productController() {
        return new ProductController();
    }

    @Override
    public IWarehouseController warehouseController() {
        return new WarehouseController();
    }

    @Override
    public IValidation validation() {
       return new Validation();
    }
    
}

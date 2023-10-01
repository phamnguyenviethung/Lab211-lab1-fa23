
package controllers;

import utils.IValidation;


public interface IControllerFactory {
    IProductController productController();
    IWarehouseController warehouseController();
    IValidation validation();
}

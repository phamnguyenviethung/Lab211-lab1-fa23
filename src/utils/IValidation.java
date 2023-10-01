
package utils;


import models.ProductType;
import java.util.Date;
import utils.Status;

public interface IValidation {
    
    String checkString(String msg, Status status);
    
    int checkInt(String msg, int min, int max, Status status);

    double checkDouble(String msg, double min, double max, Status status);

    boolean checkYesOrNo(String msg);
    
    Date checkBeforeDate(String msg, Status status);

    Date checkAfterDate(String msg, Date productionDate, Status status);
    
    ProductType checkProductType(String msg, Status status);
    
    Trade checkTradeType(String msg, Status status);

}

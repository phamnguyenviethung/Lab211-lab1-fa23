
package models;

import utils.Trade;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Warehouse implements Serializable{
    private int code;
    private Trade tradeType;
    private Date timeStamp;
    private List<Product> listItem;

    public Warehouse() {
        listItem = new ArrayList<>();
    }

    public Warehouse(int code, Trade tradeType, Date timeStamp, List<Product> listItems) {
        this.code = code;
        this.tradeType = tradeType;
        this.timeStamp = timeStamp;
        this.listItem = listItems;
    }

    public int getCode() {
        return code;
    }

    public Trade getTradeType() {
        return tradeType;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public List<Product> getItems() {
        return listItem;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setTradeType(Trade tradeType) {
        this.tradeType = tradeType;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setItems(List<Product> items) {
        this.listItem = items;
    }
    
    public void addItem(Product item) {
        listItem.add(item);
    }

    @Override
    public String toString() {
        String pattern = "dd/MM/yyyy HH:mm:ss a";
        DateFormat sdf = new SimpleDateFormat(pattern);
        String listItems = "";
        for(Product product: listItem){
            listItems += product.toString() + "\n";
        }
        return "Warehouse{" + "code=" + code + ", tradeType=" + tradeType + ", timeStamp=" + sdf.format(timeStamp) + "," + "\n-----List of products-----\n" + listItems + "}";
    }
    
    public String toReportString() {
        String pattern = "dd/MM/yyyy HH:mm:ss a";
        DateFormat sdf = new SimpleDateFormat(pattern);
        return "Warehouse{" + "code=" + code + ", tradeType=" + tradeType + ", timeStamp=" + sdf.format(timeStamp);
    }
}

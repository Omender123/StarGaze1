package star.example.stargaze.models;

public class AllOrderModelData {

    String EventName,ArtistName,StartDate,Price,Status,Order_Number,Payment,Imageurl;

    public AllOrderModelData(String eventName, String artistName, String startDate, String price, String status, String order_Number, String payment, String imageurl) {
        EventName = eventName;
        ArtistName = artistName;
        StartDate = startDate;
        Price = price;
        Status = status;
        Order_Number = order_Number;
        Payment = payment;
        Imageurl = imageurl;
    }

    public AllOrderModelData() {

    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrder_Number() {
        return Order_Number;
    }

    public void setOrder_Number(String order_Number) {
        Order_Number = order_Number;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getImageurl() {
        return Imageurl;
    }

    public void setImageurl(String imageurl) {
        Imageurl = imageurl;
    }
}

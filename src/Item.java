import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Item {
    private String barcode;
    private String category;
    private String name;
    private String kr;
    private String ore;
    private String limit;
    private String discountKR;
    private String discountORE;
    private int count = 0;
    private DecimalFormat df;

    public Item(String barcode, String category, String name, String kr, String ore){
        this.barcode = barcode;
        this.category = category;
        this.name = name;
        this.kr = kr;
        this.ore = ore;

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.00", otherSymbols);

        //We do not set discounts here because they are possible null
    }

    public void incrementItemCount(){
        count++;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getOre() {
        return ore;
    }

    public String getKr() {
        return kr;
    }

    public String getDiscountKR() {
        return discountKR;
    }

    public String getLimit() {
        return limit;
    }

    public String getDiscountORE() {
        return discountORE;
    }

    public int getCount() {
        return count;
    }

    public void setDiscountKR(String discountKR) {
        this.discountKR = discountKR;
    }

    public void setDiscountORE(String discountORE) {
        this.discountORE = discountORE;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public Double savings(){
        int numberofRabats = (count>=Integer.parseInt(limit)) ? count : 0;
        Double discountedPrice = Double.parseDouble(discountKR+"."+discountORE);
        return (Double.parseDouble(getKr()+"."+getOre())-discountedPrice)*numberofRabats;
    }

    public Double regularPrice(){
        return (double)getCount()*(Double.parseDouble(getKr()+"."+getOre()));
    }

    @Override
    public String toString() {
        String myString = "";
        double total = regularPrice();
        if (getCount()<2){
            int rightPad = (getKr().length()<2) ? 34-myString.length() : 33-myString.length();
            myString+=String.format("%-"+rightPad+"s",getName());
            myString+=getKr()+","+getOre()+"\n";
        }
        else{
            myString+=getName()+"\n  "+getCount()+" x "+getKr()+","+getOre();
            int rightPad = (total<10) ? 27-getKr().length()-getOre().length() : 26-getKr().length()-getOre().length();
            myString = String.format("%-"+(myString.length()+rightPad)+"s",myString);
            myString+=df.format(total)+"\n";
        }
        if (getLimit()!=null && getCount()>=Integer.parseInt(getLimit())){
            int rightPad = (savings()<10) ? 34 : 33;
            myString+=String.format("%-"+(rightPad)+"s","RABAT");
            myString+=df.format(savings())+"-\n";
        }
        return myString;
    }
}

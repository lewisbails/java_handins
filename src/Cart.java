import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Cart {
    private HashSet<Item> items;
    private List<String> categories;
    private HashMap<String,ArrayList<String>> categoryMap;
    private double subtotal;
    private double rabat;
    private double total;
    private DecimalFormat df;


    public Cart(){
        items = new HashSet<>();
        categories = new ArrayList<>();
        categoryMap = new HashMap<>();


        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.00", otherSymbols);
    }

    public void addItem(Item item){
        items.add(item);
    }

    public HashSet<Item> getItems() {
        return items;
    }

    public void updateCategories(){
        //creates a sorted list of the categories in the purchase (hashmap keys)
        this.categories = new ArrayList<String>(categoryMap.keySet());
        Collections.sort(this.categories);
    }

    public List<String> getCategories(){
        return this.categories;
    }

    public double calculateSubTotal(){
        subtotal = 0;
        for (Item item : items){
            subtotal+=item.regularPrice();
        }
        return subtotal;
    }

    public double calculateRabat(){
        rabat = 0;
        for (Item item : items){
            if (item.getLimit()!=null && item.getCount()>=Integer.parseInt(item.getLimit())) {
                rabat += item.savings();
            }
        }
        return rabat;
    }

    public double calculateTotal(){
        total = subtotal - rabat;
        return total;
    }

    public int calcualteMarkers(){
        return (int)total/50;
    }

    public double calculateTax(){
        return total*0.20;
    }

    public String toString(){
        String myString = "";
        for (String category : getCategories()){
            myString+="\n";
            int length = 19+(category.length()+4)/2;
            myString+=String.format("%-"+(38-length)+"s",String.format("%"+length+"s","* "+category+" *"))+"\n";
            //
            for (String name : categoryMap.get(category)){
                myString+=findItem(name,category).toString();
            }
        }
        myString+="\n--------------------------------------\n\n";

        myString+=String.format("%-"+(38-df.format(calculateSubTotal()).length())+"s","SUBTOT")+df.format(subtotal)+"\n\n";
        if (calculateRabat()!=0){
            myString+=String.format("%-"+(38-df.format(rabat).length())+"s","RABAT")+df.format(rabat)+"\n\n";
        }
        myString+=String.format("%-"+(38-df.format(calculateTotal()).length())+"s","TOTAL")+df.format(total)+"\n\n";
        myString+="KØBET HAR UDLØST "+calcualteMarkers()+" MÆRKER\n\n";
        myString+=String.format("%-"+(38-df.format(calculateTax()).length())+"s","MOMS UDGØR")+df.format(calculateTax())+"\n\n";
        return myString;
    }

    public void category2names(){
        for (Item item : items){
            if (categoryMap.containsKey(item.getCategory())){
                ArrayList<String> currentList = categoryMap.get(item.getCategory());
                currentList.add(item.getName());
                categoryMap.put(item.getCategory(),currentList);
            }
            else {
                ArrayList<String> newList = new ArrayList<>();
                newList.add(item.getName());
                categoryMap.put(item.getCategory(),newList);
            }
        }
        //alphabetically sorts the name lists (values) to the categories (keys)
        for (String category :categoryMap.keySet()){
            ArrayList<String> currentList = categoryMap.get(category);
            Collections.sort(currentList);
            categoryMap.put(category,currentList);
        }

        updateCategories();
    }

    private Item findItem(String name, String category){
        for (Item item : items){
            if (item.getName().equals(name) && item.getCategory().equals(category)){
                return item;
            }
        }
        return null;
    }

}

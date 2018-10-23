import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class CashRegister {
    private HashSet<String> categories;
    private Cart cart;
    private HashMap<String,Item> products;


    public CashRegister(String priceFilename, String discountFilename){
        products = new HashMap<>();
        categories = new HashSet<>();
        cart = new Cart();

        try {
            Scanner priceScanner = new Scanner(Paths.get(priceFilename));
            while (priceScanner.hasNextLine()) {
                String[] priceString = priceScanner.nextLine().split(",");
                if (!products.containsKey(priceString[0])) {
                    products.put(priceString[0], new Item(priceString[0], priceString[1], priceString[2], priceString[3], priceString[4]));
                    categories.add(priceString[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot find file "+priceFilename);
        }

        try {
            Scanner discountScanner = new Scanner(Paths.get(discountFilename));
            while (discountScanner.hasNextLine()){
                String[] discountString = discountScanner.nextLine().split(",");
                products.get(discountString[0]).setLimit(discountString[1]);
                products.get(discountString[0]).setDiscountKR(discountString[2]);
                products.get(discountString[0]).setDiscountORE(discountString[3]);
            }
        } catch (IOException e){
            System.out.println("Cannot find file "+discountFilename);
        }

    }


    public void printReceipt(String barcodeFilename){

        //increases count for items on receipt
        scanBarcode(barcodeFilename);

        //extracts items with counts greater than zero and adds them to the cart
        populateCart(this.cart);
        this.cart.category2names();

        //prints the contents of the cart
        System.out.println(cart);

    }


    private void scanBarcode(String barcodeFilename){
        try {
            Scanner barcodeScanner = new Scanner(Paths.get(barcodeFilename));
            while (barcodeScanner.hasNextLine()){
                products.get(barcodeScanner.nextLine()).incrementItemCount();
            }
        } catch (IOException e){
            System.out.println("Cannot find file "+barcodeFilename);
        }
    }

    private void populateCart(Cart cart){
        for (String barcode : products.keySet()){
            if (products.get(barcode).getCount()!=0){
                cart.addItem(products.get(barcode));
            }
        }
    }





































//    private void sortCategories(){
//        sortedCategories = new ArrayList<>(categories);
//        Collections.sort(sortedCategories);
//    }
//
//    private void sortNames(){
//        sortedNames = new HashMap<>();
//        for (Item product : products.values()){
//            ArrayList<String> currentList = sortedNames.get(product.getCategory());
//            currentList.add(product.getName());
//            sortedNames.put(product.getCategory(),currentList);
//        }
//
//        for (String category : sortedNames.keySet()){
//            ArrayList<String> currentList = sortedNames.get(category);
//            Collections.sort(currentList);
//            sortedNames.put(category,currentList);
//        }
//    }
//
//    private void sortBarcodes(){
//        sortedBarcodes = new ArrayList<>();
//        for (String category : sortedNames.keySet()){
//            for (String name : sortedNames.get(category)){
//                sortedBarcodes.add(findMatch(name,category));
//            }
//        }
//    }
//
//    private String findMatch(String name,String category){
//        for (String barcode : products.keySet()){
//            if (products.get(barcode).getName().equals(name) && products.get(barcode).getCategory().equals(category)){
//                return barcode;
//            }
//        }
//        return "";
//    }


}

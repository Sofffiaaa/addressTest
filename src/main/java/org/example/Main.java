package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            AddressQuantity stats = new AddressQuantity();
            stats.processAddressFile("address.xml");
            stats.printStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
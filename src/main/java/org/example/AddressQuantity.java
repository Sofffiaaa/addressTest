package org.example;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AddressQuantity {
    private Map<Building, Integer> buildingCounts = new ConcurrentHashMap<>();
    private Map<String, Map<Integer, Integer>> cityFloorCounts = new ConcurrentHashMap<>();

    public void processAddressFile(String filename) throws ParserConfigurationException, SAXException, IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
        if (inputStream == null) {
            throw new IOException("address.xml не найден");
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        AddressHandler handler = new AddressHandler();
        saxParser.parse(inputStream, handler);
    }

    private class AddressHandler extends DefaultHandler {
        private Building currentBuilding;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("item")) {
                String city = attributes.getValue("city");
                String street = attributes.getValue("street");
                String house = attributes.getValue("house");
                String floor = attributes.getValue("floor");

                try {
                    currentBuilding = new Building(city, street, house, floor);
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Invalid house or floor number in XML: " + e.getMessage());
                    currentBuilding = null;
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            if (qName.equals("item")) {
                buildingCounts.merge(currentBuilding, 1, Integer::sum);
                cityFloorCounts.computeIfAbsent(currentBuilding.city, k -> new HashMap<>())
                        .merge(Integer.valueOf(currentBuilding.floor), 1, Integer::sum);
            }
        }
    }
    public void printStatistics() {
        System.out.println("Повторяющиеся записи:");
        if (buildingCounts.isEmpty()) {
            System.out.println("Нет повторяющихся записей.");
        } else {
            buildingCounts.entrySet().stream()
                    .filter(e -> e.getValue() > 1)
                    .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " повторений"));
        }

        System.out.println("");
        System.out.println("Сколько в каждом городе 1,2,3,4,5-этажных зданий:");
        cityFloorCounts.forEach((city, floorCounts) -> {
            System.out.println("Город: " + city);
            floorCounts.forEach((floor, count) -> {
                System.out.println("  Этажей " + floor + ": " + count);
            });
        });
    }
}

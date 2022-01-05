package AddressParser;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UtilClass {
    private static ArrayList<Address> addresses = new ArrayList<>();

    public static ArrayList<Address> parsingXML(String filePath) {
        try {
            addresses.clear();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            File file = new File(filePath);
            Reader reader = new InputStreamReader(new FileInputStream(file));
            InputSource source = new InputSource();
            source.setCharacterStream(reader);
            parser.parse(source, handler);
        } catch (Exception e) {
            System.out.println("Ошибка данных");
        }
        return addresses;
    }

    public static ArrayList<Address> parsingCSV(String filePath) {
        addresses.clear();
        final String DELIMITER = ";";
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine(); //пропускаем заголовок
            line = reader.readLine();
            while (line != null) {
                String[] columns = line.split(DELIMITER);
                Address address = new Address(columns[0].replace("\"", ""), columns[1].replace("\"", ""), columns[2], Integer.parseInt(columns[3]));
                addresses.add(address);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {
            System.out.println("Ошибка формата файла");
        }
        return addresses;
    }

    public static Map<Integer, Address> searchDuplicates(ArrayList<Address> addresses) {
        Map<Integer, Address> duplicates = addresses.stream()
                .collect(Collectors.groupingBy(Function.identity()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toMap(e -> e.getValue().size(), e -> e.getKey()));
        return duplicates;
    }

    public static void searchCountHous(ArrayList<Address> addresses) {
        Set<Address> singlAddresses = new HashSet<>();
        singlAddresses.addAll(addresses);
        Map<String, List<Address>> citys = singlAddresses.stream()
                .collect(Collectors.groupingBy(Address::getCity));
        Map<String, List<Address>> sortedCitys = new TreeMap<>(citys);

        for (Map.Entry<String, List<Address>> entry : sortedCitys.entrySet()) {
            System.out.println(entry.getKey());
            List<Address> house = entry.getValue();
            Map<Integer, Integer> countHouse = house.stream()
                    .collect(Collectors.groupingBy(Address::getFloor))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().size()));
            Map<Integer, Integer> sortedCountHouse = new TreeMap<>(countHouse);

            for (Map.Entry<Integer, Integer> entry1 : sortedCountHouse.entrySet()) {
                System.out.println(entry1.getKey() + " этажей - " + entry1.getValue());
            }
        }
    }

    public static void printDuplicates(Map<Integer, Address> duplicates) {
        System.out.println("Дублирующиеся записи:");
        for (Map.Entry<Integer, Address> entry : duplicates.entrySet()) {
            Address address = entry.getValue();
            String city = address.getCity();
            String street = address.getStreet();
            String house = address.getHouse();
            int floor = address.getFloor();
            int count = entry.getKey();
            System.out.println("Адрес: " + city + ", " + street + ", " + house + ", " + floor + "; колличество повторений " + count);
        }
    }

    public static String readFilePath() {
        Scanner input = new Scanner(System.in);
        System.out.print("Введите полный путь до файла-справочника, либо exit для выхода");
        return input.nextLine();
    }

    public static boolean checkFilePath(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }

    public static boolean checkFileXML(String filePath) {
        String extension = filePath.substring((filePath.lastIndexOf('.') + 1), filePath.length());
        if (extension.equalsIgnoreCase("xml")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkFileCSV(String filePath) {
        String extension = filePath.substring((filePath.indexOf('.') + 1), filePath.length());
        if (extension.equalsIgnoreCase("csv.xls")) {
            return true;
        } else {
            return false;
        }
    }

    private static class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {

            if (qName.equals("item")) {
                String city = attributes.getValue("city");
                String street = attributes.getValue("street");
                String house = attributes.getValue("house");
                int floor = Integer.parseInt(attributes.getValue("floor"));
                addresses.add(new Address(city, street, house, floor));
            }
        }
    }
}

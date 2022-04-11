import com.google.gson.*;
import com.google.gson.reflect.*;
import com.opencsv.*;
import com.opencsv.bean.*;
import com.opencsv.exceptions.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "C:\\Users\\Fox\\IdeaProjects\\CsvJsonParserHomework\\target\\data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        System.out.println(list);
        String json = listToJson(list);
        System.out.println(json);
        writeString(json);
        String xmlFileName = "data.xml";
        List<Employee> someone = parseXML(xmlFileName);
        String xmlToJson = listToJson(someone);
        writeString(xmlToJson);


    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        String[] nextLine;
        List<Employee> staff = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            while ((nextLine = reader.readNext()) != null) {
                ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
                strategy.setType(Employee.class);
                strategy.setColumnMapping(columnMapping);
                CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader).
                        withMappingStrategy(strategy).
                        build();
                staff = csv.parse();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static String listToJson(List<Employee> listToJson) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(listToJson, listType);
    }

    public static void writeString(String jsonToNewJsonFile) {
        try (FileWriter writer = new FileWriter("EmployeeList")) {
            writer.write(jsonToNewJsonFile);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> someEmployees = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        doc.getDocumentElement();
        NodeList nodeList = doc.getElementsByTagName("employee");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if(Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                Employee employee = new Employee(Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent()),
                        element.getElementsByTagName("firstName").item(0).getTextContent(),
                        element.getElementsByTagName("lastName").item(0).getTextContent(),
                        element.getElementsByTagName("country").item(0).getTextContent(),
                        Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent()));
                System.out.println(employee);
                    someEmployees.add(employee);
                }
            }
        return someEmployees;
        }
}
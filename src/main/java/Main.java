import com.google.gson.*;
import com.google.gson.reflect.*;
import com.opencsv.*;
import com.opencsv.bean.*;
import com.opencsv.exceptions.*;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "C:\\Users\\Fox\\IdeaProjects\\CsvJsonParserHomework\\target\\data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        System.out.println(list);
        String json = listToJson(list);
        System.out.println(json);
        writeString(json);
    }

        public static List<Employee> parseCSV (String[]columnMapping, String fileName) {
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

        public static String listToJson (List <Employee> listToJson) {
            Type listType = new TypeToken<List<Employee>>() {}.getType();
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            return gson.toJson(listToJson, listType);
        }
        public static void writeString(String jsonToNewJsonFile) {
            try(FileWriter writer = new FileWriter("EmployeeList")) {
                writer.write(jsonToNewJsonFile);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
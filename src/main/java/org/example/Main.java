package org.example;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class Main {
    static Map<String, String> parameters = new HashMap<String, String>() {{
        put("warehouseCode", "value");
        put("warehouseName", "value");
        put("active", "true");
        put("updatedBy", "null");
        put("createdBy", "null");
        put("legalEntityCode", "value");
    }};

    public static void main(String[] args) throws IOException {
        String pathSqlFile = "C:\\Users\\barkhatov_r\\IdeaProjects\\JavaTestFiels\\CreateWarehouse.sql";
        String pathNewSqlFile = "C:\\Users\\barkhatov_r\\IdeaProjects\\JavaTestFiels\\CreateWarehouseEdited.sql";
        String SqlFile = new String(Files.readAllBytes(Paths.get(pathSqlFile)));

        try{
            writeFile(pathNewSqlFile,prepareSqlQuery(parameters,SqlFile));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private static String prepareSqlQuery(Map<String, String> parameters, String SqlText) throws IOException {

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int startIndex = SqlText.indexOf("'${" + key + ":-");
            int endIndex = SqlText.indexOf("}'", startIndex);
            //Полное значение ключа + значения в SQL файле
            String placeholder = SqlText.substring(startIndex, endIndex + 2);

            // Обрабатываем значения null и "null"
            if (value == null || value.equalsIgnoreCase("null")) {
                //Значение атрибута в SQL файле
                String placeholderValue = placeholder.substring(placeholder.indexOf("-") + 1, placeholder.indexOf("}"));
                SqlText = SqlText.replace(placeholder, placeholderValue);
            } else {
                SqlText = SqlText.replace(placeholder, value);
            }

        }
        return SqlText;
    }
    public static void writeFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content.getBytes());
    }
}




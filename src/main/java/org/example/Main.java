package org.example;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.example.connectionDb.ConnectForDB;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
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

        String pathSqlFile = "C:\\Users\\barkhatov_r\\IdeaProjects\\JavaTestFiels\\resources\\CreateWarehouse.sql";
        String pathNewSqlFile = "C:\\Users\\barkhatov_r\\IdeaProjects\\JavaTestFiels\\resources\\CreateWarehouseEdited.sql";
        String str = prepareSqlQuery(parameters, pathSqlFile);

        //Создание нового файла CreateWarehouseEdited.sql на основе изменений из мапы
        try{
            writeSqlFile(pathNewSqlFile,prepareSqlQuery(parameters,pathSqlFile));
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //Реализация метода по изменению файла CreateWarehouse.sql
        rewriteSqlFile(pathSqlFile,prepareSqlQuery(parameters,pathSqlFile));


    }
   // @Step("Подготовим sql запрос")
    private static String prepareSqlQuery(Map<String, String> parameters, String sqlQueryPath) throws IOException {

        String SqlQuery = new String(Files.readAllBytes(Paths.get(sqlQueryPath)));

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            int startIndex = SqlQuery.indexOf("'${" + key + ":-");
            int endIndex = SqlQuery.indexOf("}'", startIndex);
            //Полное значение ключа + значения в SQL файле
            String placeholder = SqlQuery.substring(startIndex, endIndex + 2);

            // Обрабатываем значения null и "null"
            if (value == null || value.equalsIgnoreCase("null")) {
                //Значение атрибута в SQL файле
                String placeholderValue = placeholder.substring(placeholder.indexOf("-") + 1, placeholder.indexOf("}"));
                SqlQuery = SqlQuery.replace(placeholder, placeholderValue);
            } else {
                SqlQuery = SqlQuery.replace(placeholder, value);
            }

        }
        return SqlQuery;
    }

    @Attachment(value = "query", fileExtension = ".sql", type = "text/plain")
    @Step("Выполним sql запрос из файла с подстановкой параметров")
    public static void executeSqlQuery(java.util.Map<String, String> parametersSqlQuery ,String sqlQueryPath) throws IOException, SQLException {
    String sqlPreparedQuery = prepareSqlQuery(parametersSqlQuery, sqlQueryPath);

    try (Connection connection = ConnectForDB.getConnection();
             Statement statement = connection.createStatement()) {
            // Выполняем SQL запрос
            statement.execute(sqlPreparedQuery);
        }
    }

    public static void rewriteSqlFile(String sqlQueryPath, String sqlFileContent) throws IOException {
        Files.write(Paths.get(sqlQueryPath), sqlFileContent.getBytes());
    }

    public static void writeSqlFile(String filePath, String content) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, content.getBytes());
    }
}




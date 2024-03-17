package net.jsf.working;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class Reader {
    static ArrayList<String[]> data = null;
    static Path file = Paths.get("src/main/db/accounts.txt");

    public Reader() {
        data = new ArrayList<>();
        List<String> fileLines = null;
        
        try {
            fileLines = Files.readAllLines(file);
        }

        catch (IOException error) {
            error.printStackTrace();
        }

        finally {
            for (String fileLine: fileLines) {
                data.add(fileLine.split("  "));
            }
        }
    }

    public ArrayList<String[]> read() {
        return data;
    }

    public void update(String[] data1, String[] data2) {
        StringBuilder builder = new StringBuilder();

        for (String[] dataArray: Reader.data) {

            String stringDataArray = String.format("%s  %s  %s", dataArray[0], dataArray[1], dataArray[2]);
            String stringData1 = String.format("%s  %s  %s", data1[0], data1[1], data1[2]);
            String stringData2 = String.format("%s  %s  %s", data2[0], data2[1], data2[2]);

            if (stringDataArray.equals(stringData1)) {
                data.set(data.indexOf(dataArray), data2);

                builder.append(stringData2 + "\n");
                continue;
            }

            if (Reader.data.indexOf(dataArray) == Reader.data.size() - 1) {
                builder.append(stringDataArray);
            }
            else {
                builder.append(stringDataArray + "\n");
            }
        }

        try {
            Files.writeString(file, builder.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        }
        catch (IOException error) {
            error.printStackTrace();
        }
    }

    public static void append(String[] newData) {
        String stringNewData = String.format("%s  %s  %s", newData[0], newData[1], newData[2]);

        try {
            FileReader reader = new FileReader(file.toFile());
            int lastChar = -1;

            reader.skip(file.toFile().length() - 1);
            lastChar = reader.read();
            reader.close();

            
            if (lastChar != '\n' && lastChar != -1) {
                Files.writeString(file, "\n", StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            }
            Files.writeString(file, stringNewData, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }

        catch (IOException error) {
            error.printStackTrace();
        }
    }
}
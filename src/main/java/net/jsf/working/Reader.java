package net.jsf.working;

import java.util.ArrayList;
import java.io.*;

public class Reader {
    static ArrayList<String[]> data = null;
    static File file = new File("src/main/resources/accounts.txt");

    public Reader() {
        data = new ArrayList<>();

        try (BufferedReader buffRead = new BufferedReader(new FileReader(Reader.file))) {
            String fileLine = null;

            while ((fileLine = buffRead.readLine()) != null) {
                data.add(fileLine.split("  "));
            }
        }

        catch (Exception error) {
            error.printStackTrace();
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
            
            builder.append(stringDataArray + "\n");

            if (Reader.data.indexOf(dataArray) == Reader.data.size() - 1) {
                builder.delete(builder.length() - 1, builder.length());
            }
        }

        try (FileWriter writer = new FileWriter(Reader.file, false)) {
            writer.write(builder.toString());
            writer.close();
        }

        catch (Exception error) {
            error.printStackTrace();
        }
    }

    public static void append(String[] newData) {
        String stringNewData = String.format("%s  %s  %s", newData[0], newData[1], newData[2]);

        try (FileWriter writer = new FileWriter(Reader.file, true)) {
            writer.write("\n" + stringNewData);
            writer.close();
        }

        catch (Exception error) {
            error.printStackTrace();
        }
    }
}

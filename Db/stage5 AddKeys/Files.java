/* class to read table from a text file
   and to write table to a text file
 */

import java.util.*;
import java.io.*;

public class Files{
    boolean readFromFile(String filename,Table table){
        try {
            File file = new File(filename);
            Scanner read = new Scanner(file);
            String tempword = read.nextLine();

            String[] header = tempword.split(",");
            table.createTable(filename.split("\\.")[0],header);
            while(read.hasNextLine()){
                tempword = read.nextLine();
                String[] records = tempword.split(",");
                table.insertRecord(records);
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    boolean writeToFile(String filename,Table table){
        Map<String,List<String>> temptable = table.getTable();
        try {
            File file = new File (filename);
            FileWriter filewriter = new FileWriter(file);
            for(int k=0;k<table.getAttributesNum();k++){
                if(k==table.getAttributesNum()-1){
                    filewriter.write(table.getAllAttributesNames().get(k));
                }
                else{
                    filewriter.write(table.getAllAttributesNames().get(k));
                    filewriter.write(",");
                }
            }
            filewriter.write("\n");
            for(int i=0; i<temptable.size();i++){
                for (int j=0;j<temptable.get(table.getKeys().get(i)).size();j++) {
                    if(j==temptable.get(table.getKeys().get(i)).size()-1){
                        filewriter.write(temptable.get(table.getKeys().get(i)).get(j));
                    }
                    else {
                        filewriter.write(temptable.get(table.getKeys().get(i)).get(j));
                        filewriter.write(",");
                    }
                }
                filewriter.write("\n");
            }
            filewriter.flush();
            filewriter.close();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    // tests 1-7, check test 7 by opening the destination text file
    private void testFiles(){
        Table testTable = new Table();
        readFromFile("People.txt", testTable);
        claim(testTable.gettableName().equals("People"));
        claim(testTable.getAllAttributesNames().get(1).equals("Name"));
        claim(testTable.selectRecord("ab123").get(0).equals("ab123"));
        claim(testTable.selectRecord("ab123").get(1).equals("Jo"));
        claim(testTable.selectRecord("gh012").get(0).equals("gh012"));
        claim(testTable.selectRecord("gh012").get(1).equals("Pete"));
        writeToFile("writePeopletoFile.txt",testTable);
    }

    void claim(boolean b){
        if(!b) throw new Error("Files Test"+testNumber+"fails");
        testNumber++;
    }

    private int testNumber = 1;

    // testing
    private void run(){
        testFiles();
        System.out.println("All Files Tests Passed!");
    }

    public static void main (String[] args) {
        Files f = new Files();
        f.run();
    }
}
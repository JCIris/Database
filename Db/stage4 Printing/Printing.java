/* class to print a table in a neat format
   each individual record occupies a line
   the width of column depends on the longest element in this column
*/

import java.util.*;

public class Printing {
    private List<Integer> maxValueSizes = new ArrayList<Integer>();

    void setmaxValueSizes(Table table) {
        Integer[] temp = new Integer[table.getAttributesNum()];
        for (int i = 0; i < table.getAttributesNum(); i++) {
            temp[i] = table.getAttributeName(i).length();
            for (int j = 0; j < table.gettableSize(); j++) {
                if (temp[i]<table.selectRecord(j).get(i).length()) {
                    temp[i] = table.selectRecord(j).get(i).length();
                }
            }
            maxValueSizes.add(i,temp[i]);
        }
    }

    List<Integer> getMaxValueSizes(){
        return maxValueSizes;
    }

    void printSpace(int spaceNum){
        for(int i=0;i<spaceNum;i++){
            System.out.print(" ");
        }
    }

    void printDash(int dashNum){
        for(int i=0;i<dashNum;i++){
            System.out.print("-");
        }
    }

    int countDash(Table table){
        int dashnum = 0;
        for(int i=0;i<table.getAttributesNum();i++){
            dashnum = dashnum + getMaxValueSizes().get(i);
        }
        dashnum = dashnum + table.getAttributesNum()+1;
        return dashnum;
    }

    void printLine(int dashNum){
        printDash(dashNum);
        System.out.println();
    }

    void printTable(Table table) {
        System.out.println("Table: "+table.gettableName());
        setmaxValueSizes(table);
        List<Integer> maxes = getMaxValueSizes();
        printLine(countDash(table));
        for(int i=0;i<table.getAttributesNum();i++){
            System.out.print("|");
            System.out.print(table.getAttributeName(i));
            printSpace(maxes.get(i)-table.getAttributeName(i).length());
        }
        System.out.print("|");
        System.out.println();
        printLine(countDash(table));
        for (int j = 0; j < table.gettableSize(); j++) {
            for (int k = 0; k < table.getAttributesNum(); k++) {
                System.out.print("|");
                System.out.print(table.selectRecord(j).get(k));
                printSpace(maxes.get(k)-table.selectRecord(j).get(k).length());
            }
            System.out.print("|");
            System.out.println();
            printLine(countDash(table));
        }
    }

    // testing
    private void run(){
        Table testtable = new Table();
        testtable.createTable("People", "Username", "Name","Gender");
        testtable.insertRecord("ab123", "Jo","M");
        testtable.insertRecord("cd456", "Sam","M");
        testtable.insertRecord("ef789", "Amy","F");
        testtable.insertRecord("gh012", "Pete","M");
        printTable(testtable);
    }

    public static void main(String[] args) {
        Printing p = new Printing();
        p.run();
    }
}
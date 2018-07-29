/* class to deal with table
   treat a table as a list of a list of strings
   use row number to do common operations on a table at this stage
*/

import java.util.*;

public class Table{
    private String tableName;
    private List<String> attributes;
    private List<List<String>> table;
    private int rows = 0;

    // create a table with given attribute names
    void createTable(String name,String...attributenames){
        this.tableName = name;
        this.table = new ArrayList<List<String>>();
        attributes = new ArrayList<>(attributenames.length);
        for(int i=0;i<attributenames.length;i++){
            attributes.add(i,attributenames[i]);
        }
    }

    // get the table
    List<List<String>> getTable(){
        return table;
    }

    // get the name of the table
    String gettableName(){
        return tableName;
    }

    // how many records in the table
    int gettableSize(){
        return rows;
    }

    // how many attributes(columns) in the table
    int getAttributesNum(){
        return attributes.size();
    }

    // get the attribute name of a specific column
    String getAttributeName(int col){
        if(col<0 || col>=attributes.size()){
            throw new Error("column number is out of bound");
        }
        return attributes.get(col);
    }

    // returns an array of strings containing all attributes names
    List<String> getAllAttributesNames(){
        List<String> attrinamescopy = new ArrayList<String>(attributes.size());
        for(int i = 0; i < attributes.size(); i++)  {
            attrinamescopy.add(i,attributes.get(i));
        }
        return attrinamescopy;
    }

    // find the given attribute name is in which column
    // give the column number
    // if not found, return -1
    int getAttributePos(String s){
        for(int i = 0; i < attributes.size(); i++){
            if(attributes.get(i).equals(s))
                return i;
        }
        return -1;
    }

    // change attribute name
    boolean alterAttributeName(String oldName,String newName){
        int pos = getAttributePos(oldName);
        attributes.set(pos,newName);
        return true;
    }

    // tests 1-9
    private void testTableMethods(){
        Table testtable = new Table();
        testtable.createTable("People","Username","Name");
        claim(testtable.gettableName().equals("People"));
        claim(testtable.getAttributesNum() == 2);
        claim(testtable.getAttributeName(  0).equals("Username"));
        claim(testtable.getAllAttributesNames().get(0).equals("Username"));
        claim(testtable.getAllAttributesNames().get(1).equals("Name"));
        claim(testtable.getAttributePos("Username") == 0);
        claim(testtable.getAttributePos("user") == -1);
        testtable.alterAttributeName("Username","Userid");
        claim(testtable.getAttributeName(0) == "Userid");
        claim(testtable.gettableSize() == 0);
    }

    // insert a single record into the table
    boolean insertRecord(String...values){
        if(values.length!= attributes.size()){
            return false;
        }
        List<String> temprecord = new ArrayList<String>();
        for(int i=0;i<values.length;i++){
            temprecord.add(i,values[i]);
        }
        table.add(new ArrayList<String>());
        for(int j=0;j<attributes.size();j++) {
            table.get(rows).add(temprecord.get(j));
        }
        rows++;
        return true;
    }

    // select a single record by using its rownumber
    List<String> selectRecord(int rowNumber){
        if(rowNumber<0 || rowNumber>=gettableSize()){
            throw new Error("rowNumber is out of bound");
        }
        List<String> recordValues = new ArrayList<String>(attributes.size());
        for(int i = 0; i < attributes.size(); i++){
            recordValues.add(i,table.get(rowNumber).get(i));
        }
        return recordValues;
    }

    // select multiple records by the start and end rowNumbers
    List<List<String>> selectMultiRecords(int start, int end){
        if(start<0 || end>=gettableSize() || start>end){
            throw new Error("Invalid start and end rowNumbers");
        }
        List<List<String>> rs = new ArrayList<List<String>>(end-start+1);
        int i = 0;
        while(start <= end)  {
            rs.add(i++,selectRecord(start));
            start++;
        }
        return rs;
    }

    // return all records
    List<List<String>> returnAllRecords(){
        List<List<String>> allRecords = new ArrayList<List<String>>(rows+1);
        for(int i = 0; i <gettableSize(); i++)  {
            allRecords.add(i,selectRecord(i));
        }
        return allRecords;
    }

    // returns the whole table including both attributes and all records
    List<List<String>> returnTable(){
        List<String> columns = getAllAttributesNames();
        List<List<String>> rs = returnAllRecords();
        List<List<String>> temptable = new ArrayList<List<String>>(table.size()+1);
        temptable.add(0,columns);
        for(int i=1;i<table.size()+1;i++){
            temptable.add(i,rs.get(i-1));
        }
        return temptable;
    }

    // delete a single record of the given rowNumber
    boolean deleteRecord(int rowNumber){
        if(rowNumber<0 || rowNumber>rows){
            return false;
        }
        table.remove(rowNumber);
        rows--;
        return true;
    }

    // delete several records
    boolean deleteMultiRecords(int start, int end){
        if(start<0 || end>=gettableSize() || start>end){
            return false;
        }
        while(start <= end){
            deleteRecord(start);
            end--;
        }
        return true;
    }

    // change the value of a specific row of a specific attribute
    boolean updateRecord(String attribute,int rowNumber,String newValue){
        int pos = getAttributePos(attribute);
        if(pos == -1 || rowNumber<0 || rowNumber>rows){
            return false;
        }
        table.get(rowNumber).set(pos,newValue);
        return true;
    }

    // add a new attribute(column) to a specific position of the table
    boolean alterTable (String newattribute,int pos) {
        if(pos<0 || pos>attributes.size() || !checkAttributeName(newattribute)){
            return false;
        }
        List<String> tempattributes = new ArrayList<String>(attributes.size() + 1);
        for(int i = 0,j = 0; i < attributes.size() + 1; i++)  {
            if(i == pos){
                tempattributes.add(i,newattribute);
            }
            else  {
                tempattributes.add(i,attributes.get(j));
                j++;
            }
        }
        attributes = tempattributes;
        for(int j=0;j<table.size();j++){
            table.get(j).add(pos,"null");
        }
        return true;
    }

    // check whether new attributename already exists
    boolean checkAttributeName(String newattributename){
        for(int i=0;i<getAttributesNum();i++){
            if(newattributename.equals(getAttributeName(i))){
                return false;
            }
        }
        return true;
    }

    // tests 10-28
    private void testOperationOnTable(){
        Table testtable = new Table();
        testtable.createTable("People","Username","Name");

        // INSERT tests 10-11
        testtable.insertRecord("ab123","Jo");
        testtable.insertRecord("cd456","Sam");
        testtable.insertRecord("ef789","Amy");
        testtable.insertRecord("gh012","Pete");
        claim(testtable.insertRecord("123","234","Angel")== false);
        claim(testtable.gettableSize() == 4);

        // SELECT tests 12-15
        claim(testtable.selectRecord(1).get(0).equals("cd456"));
        claim(testtable.selectRecord(1).get(1).equals("Sam"));
        claim(testtable.selectMultiRecords(0,2).get(0).get(1).equals("Jo"));
        claim(testtable.selectMultiRecords(0,2).get(2).get(0).equals("ef789"));

        // DELETE tests 16-24
        testtable.deleteRecord(1);
        claim(testtable.gettableSize() == 3);
        claim(testtable.selectRecord(0).get(0).equals("ab123"));
        claim(testtable.deleteRecord(-3) == false);
        claim(testtable.deleteRecord(5) == false);
        claim(testtable.deleteMultiRecords(1,3) == false);
        claim(testtable.deleteMultiRecords(-1,1) == false);
        claim(testtable.deleteMultiRecords(2,1) == false);
        testtable.deleteMultiRecords(1,2);
        claim(testtable.gettableSize() == 1);
        claim(testtable.selectRecord(0).get(1).equals("Jo"));

        //UPDATE test 25
        testtable.updateRecord("Username",0,"newab123");
        claim(testtable.selectRecord(0).get(0).equals("newab123"));

        //ALTER tests 26-28
        testtable.alterTable("gender",2);
        claim(testtable.getAttributeName(2).equals("gender"));
        testtable.alterTable("id",0);
        claim(testtable.getAttributeName(0).equals("id"));
        claim(testtable.alterTable("nationality",5) == false);
    }

    void claim(boolean b){
        if(!b) throw new Error("Table Test"+testNumber+"fails");
        testNumber++;
    }
    private int testNumber = 1;

    // testing
    private void run(){
        testTableMethods();
        testOperationOnTable();
        System.out.println("All Table Tests Passed!");
    }

    public static void main(String[] args){
        Table t = new Table();
        t.run();
    }
}
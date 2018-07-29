/* class to deal with table
   add keys to tables
   use HashMap to realize referring to a record by a key
*/
import java.util.*;

public class Table{
    private String tableName;
    private List<String> attributes;
    private Map<String,List<String>> table;
    private int keycolumn;

    // create a table with given attribute names
    void createTable(String name,String...attributenames){
        this.tableName = name;
        this.table = new LinkedHashMap<String, List<String>>();
        attributes = new ArrayList<>(attributenames.length);
        for(int i=0;i<attributenames.length;i++){
            attributes.add(i,attributenames[i]);
        }
    }

    boolean setKeyColumn(int colNumber){
        if(colNumber<0 || colNumber>=attributes.size()){
            return false;
        }
        keycolumn = colNumber;
        return true;
    }

    int getKeycolumn(){
        return keycolumn;
    }

    // return all the values in the keycolumn
    List<String> getKeys()  {
        Set<String> keyValues = table.keySet();
        String[] tempkeys = keyValues.toArray(new String[keyValues.size()]);
        List<String> keys = new ArrayList<String>(tempkeys.length);
        for(int i=0;i<tempkeys.length;i++){
            keys.add(i,tempkeys[i]);
        }
        return keys;
    }

    String getKeyAttribute(){
        return attributes.get(keycolumn);
    }

    // check whether a string can be a key value
    boolean checkUnique(String s)  {
        List<String> keys = getKeys();
        for(int i=0;i<keys.size();i++){
            if(keys.get(i).equals(s)){
                return false;
            }
        }
        return true;
    }

    // get the table
    Map<String, List<String>> getTable(){
        return table;
    }

    // get the name of the table
    String gettableName(){
        return tableName;
    }

    // how many records in the table
    int gettableSize(){
        return table.size();
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

    // returns a list of strings containing all attributes names
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
        for(int i=0;i<values.length;i++) {
            temprecord.add(i, values[i]);
        }
        String keyValue = values[keycolumn];
        if(checkUnique(keyValue)){
            table.put(keyValue,temprecord);
            return true;
        }
        return false;
    }

    // select a single record by using its keyvalue
    List<String> selectRecord(String keyValue){
        if(!validKeyValue(keyValue)){
            throw new Error("Invalid key");
        }
        List<String> recordValues = table.get(keyValue);
        return recordValues;
    }

    // check whether a string is a key value
    boolean validKeyValue(String keyValue){
        if(table.containsKey(keyValue)) {
            return true;
        }
        return false;
    }

    // select multiple records by given key values
    List<List<String>> selectMultiRecords(String...keyValues){
        List<List<String>> rs = new ArrayList<List<String>>(keyValues.length);
        for(int i=0;i<keyValues.length;i++){
            if(!validKeyValue(keyValues[i])){
                throw new Error("Invalid key");
            }
            rs.add(i,table.get(keyValues[i]));
        }
        return rs;
    }

    // return all records
    List<List<String>> returnAllRecords(){
        List<String> keys = getKeys();
        List<List<String>> allRecords = new ArrayList<List<String>>(table.size());
        for(int i = 0; i <table.size(); i++)  {
            allRecords.add(i,selectRecord(keys.get(i)));
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

    // delete a single record of the given key value
    boolean deleteRecord(String keyValue){
        if(!validKeyValue(keyValue)){
            return false;
        }
        table.remove(keyValue);
        return true;
    }

    // delete several records by given key values
    boolean deleteMultiRecords(String...keyValues){
        int flag = 0;
        for(int i=0;i<keyValues.length;i++){
            if(!validKeyValue(keyValues[i])){
                flag++;
            }
        }
        if(flag>0){
            return false;
        }
        else{
            for(int j=0;j<keyValues.length;j++){
                deleteRecord(keyValues[j]);
            }
            return true;
        }
    }

    // change the value of a specific row of a specific attribute
    boolean updateRecord(String attribute,String keyValue,String newValue){
        int pos = getAttributePos(attribute);
        if(!validKeyValue(keyValue)){
            return false;
        }
        table.get(keyValue).set(pos,newValue);
        return true;
    }

    // add a new attribute(column) to a specific position of the table
    boolean alterTable (String newattribute,int pos) {
        if(pos<0 || pos>attributes.size() || !checkAttributeName(newattribute)){
            return false;
        }
        List<String> tempattributes = new ArrayList<String>(attributes.size()+1);
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
            table.get(getKeys().get(j)).add(pos,"null");
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
        testtable.setKeyColumn(0);

        // INSERT tests 10-12
        testtable.insertRecord("ab123","Jo");
        testtable.insertRecord("cd456","Sam");
        testtable.insertRecord("ef789","Amy");
        testtable.insertRecord("gh012","Pete");
        claim(testtable.insertRecord("123","234","Angel")== false);
        claim(testtable.insertRecord("ab123","Jo") == false);
        claim(testtable.gettableSize() == 4);

        // SELECT tests 13-16
        claim(testtable.selectRecord("cd456").get(0).equals("cd456"));
        claim(testtable.selectRecord("cd456").get(1).equals("Sam"));
        claim(testtable.selectMultiRecords("ab123","cd456","ef789").get(0).get(1).equals("Jo"));
        claim(testtable.selectMultiRecords("ab123","cd456","ef789").get(2).get(0).equals("ef789"));

        // DELETE tests 17-25
        testtable.deleteRecord("cd456");
        claim(testtable.gettableSize() == 3);
        claim(testtable.selectRecord("ab123").get(0).equals("ab123"));
        claim(testtable.deleteRecord("cd") == false);
        claim(testtable.deleteRecord("ef") == false);
        claim(testtable.deleteMultiRecords("ab123","cd") == false);
        claim(testtable.deleteMultiRecords("ab123","cd456") == false);
        claim(testtable.deleteMultiRecords("ab123"," NULL") == false);
        testtable.deleteMultiRecords("ef789","gh012");
        claim(testtable.gettableSize() == 1);
        claim(testtable.selectRecord("ab123").get(1).equals("Jo"));

        //UPDATE test 26
        testtable.updateRecord("Username","ab123","newab123");
        claim(testtable.selectRecord("ab123").get(0).equals("newab123"));

        //ALTER tests 27-30
        testtable.alterTable("gender",2);
        claim(testtable.getAttributeName(2).equals("gender"));
        testtable.alterTable("id",0);
        claim(testtable.getAttributeName(0).equals("id"));
        claim(testtable.alterTable("nationality",5) == false);
        claim(testtable.alterTable("gender",0) == false);
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
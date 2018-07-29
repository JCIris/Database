/* class to manage tables
   use HashMap to realize referring to a table by its table name
   wrap previous things in this class
*/

import java.util.*;

public class Database{
    private Map<String, Table> database;
    private String name;

    Database(String name){
        this.name = name;
        this.database = new LinkedHashMap<String,Table>();
    }

    // get the name of the database
    String getDatabaseName(){
        return name;
    }

    // get how many tables in the database
    int getTableNum()  {
        return database.size();
    }

    // get all the table names in the database
    List<String> getTableNames(){
        Set<String> tablenames = database.keySet();
        String[] tempnames = tablenames.toArray(new String[tablenames.size()]);
        List<String> names = new ArrayList<String>(tempnames.length);
        for(int i=0;i<tempnames.length;i++){
            names.add(i,tempnames[i]);
        }
        return names;
    }

    // check whether a table is in the database
    boolean isInDatabase(String tablename){
        if(database.containsKey(tablename)){
            return true;
        }
        return false;
    }

    // get a table in the database by using its name
    Table getTable(String tablename)  {
        if(isInDatabase(tablename) == false){
            return null;
        }
        else {
            return database.get(tablename);
        }
    }

    // insert a table to database if not exists
    boolean insertTable(Table table){
        if(isInDatabase(table.gettableName())){
            return false;
        }
        else{
            database.put(table.gettableName(),table);
            return true;
        }
    }

    // delete a table from the database if exists
    boolean dropTable(String tablename)  {
        if(!isInDatabase(tablename)){
            return false;
        }
        database.remove(tablename);
        return true;
    }

    // load file to the database for adding a table
    boolean FiletoTable(String filename){
        Files files = new Files();
        Table table = new Table();
        if(!files.readFromFile(filename,table)){
            return false;
        }
        if(table == null || !insertTable(table)){
            return false;
        }
        return true;
    }

    // write a table in the database to a file
    boolean TabletoFile(String tablename,String filename){
        if(!isInDatabase(tablename)){
            return false;
        }
        Table table = getTable(tablename);
        Files files = new Files();
        files.writeToFile(filename,table);
        return true;
    }

    boolean tablePrint(String tablename){
        if(!isInDatabase(tablename)){
            return false;
        }
        Printing p = new Printing();
        p.printTable(getTable(tablename));
        return true;
    }

    List<String> selectTableRecord(String tablename, String keyvalue){
        if(!isInDatabase(tablename)){
            throw new Error("table not in the database");
        }
        return getTable(tablename).selectRecord(keyvalue);
    }

    List<List<String>> selectMultiTableRecords(String tablename, String...keyValues){
        if(!isInDatabase(tablename)){
            throw new Error("table not in the database");
        }
        return getTable(tablename).selectMultiRecords(keyValues);
    }

    boolean deleteTableRecord(String tablename, String keyvalue){
        if(!isInDatabase(tablename) || !getTable(tablename).deleteRecord(keyvalue)){
            return false;
        }
        return true;
    }

    boolean deleteMultiTableRecords(String tablename, String...keyvalues){
        if(!isInDatabase(tablename) || !getTable(tablename).deleteMultiRecords(keyvalues)){
            return false;
        }
        return true;
    }

    boolean updateTableRecord(String tablename,String attribute,String keyvalue,String newvalue){
        if(!isInDatabase(tablename) || !getTable(tablename).updateRecord(attribute,keyvalue,newvalue)){
            return false;
        }
        return true;
    }

    boolean alterTableCol(String tablename, String newattribute,int pos){
        if(!isInDatabase(tablename) || !getTable(tablename).alterTable(newattribute,pos)){
            return false;
        }
        return true;
    }

    boolean insertTableRecord(String tablename, String...values){
        if(!isInDatabase(tablename) || !getTable(tablename).insertRecord(values)){
            return false;
        }
        return true;
    }

    // tests 1-46
    private void testdatabase(Database db){
        // tests 1-3
        // empty database
        claim(db.getDatabaseName().equals("PeopleandAnimals"));
        claim(db.getTableNum() == 0);
        claim(db.getTable("table1") == null);

        // tests 4-14
        // insert table to the datebase and insert record to specific table
        Table testtable1 = new Table();
        testtable1.createTable("Animal","Id","Name","Kind","Owner");
        claim(db.insertTable(testtable1)==true);
        claim(db.insertTableRecord("Animal","1","Fido","dog","ab123")==true);
        claim(db.insertTableRecord("Animal","2","Wanda","fish","ef789")==true);
        claim(db.insertTableRecord("Animal","3","Garfield","cat","ab123")==true);
        claim(db.insertTableRecord("Animal","4","Kate","cat","cd456")==true);
        // keyvalue must be unique
        claim(db.insertTableRecord("Animal","3","G","c","ab123")==false);
        claim(db.getTableNum() == 1);
        claim(db.getTableNames().get(0).equals("Animal"));
        claim(db.getTable("Animal")!=null);
        claim(db.isInDatabase("Animal") == true);
        // can't insert again if exists
        claim(db.insertTable(testtable1) == false);

        //tests 15-16
        // select record and multiple records
        claim(db.selectTableRecord("Animal","1").get(1).equals("Fido")==true);
        claim(db.selectMultiTableRecords("Animal","1","2").get(1).get(2).equals("fish")==true);

        // tests 17-19
        // delete record and multiple records
        claim(db.deleteTableRecord("Animal","1")==true);
        claim(db.deleteMultiTableRecords("Animal","2","3")==true);
        // can't delete if keyvalue doesn't exist
        claim(db.deleteTableRecord("Animal","1")==false);

        // tests 20-30
        // can't drop a table if not exists
        claim(db.dropTable("table2") == false);
        Table testtable2 = new Table();
        testtable2.createTable("table2","id","number");
        claim(db.insertTable(testtable2)==true);
        claim(db.insertTableRecord("table2","0","zero")==true);
        claim(db.insertTableRecord("table2","1","one")==true);
        claim(db.getTableNum() == 2);
        claim(db.getTableNames().get(1).equals("table2"));
        claim(db.getTable("table2")!=null);
        claim(db.isInDatabase("table2") == true);
        claim(db.dropTable("table2") == true);
        // can't delete again if not exists
        claim(db.dropTable("table2") == false);
        claim(db.getTableNum() == 1);

        // tests 31-40
        // can't load file if there's no such file
        claim(db.FiletoTable("Student.txt") == false);
        // can't load file if the table already exists
        claim(db.FiletoTable("Animal.txt") == false);
        claim(db.FiletoTable("People.txt") == true);
        claim(db.getTableNum() == 2);
        claim(db.getTableNames().get(1).equals("People"));
        claim(db.getTable("People")!=null);
        claim(db.dropTable("Animal") == true);
        claim(db.FiletoTable("Animal.txt") == true);
        // can't write table to file if the table not exists
        claim(db.TabletoFile("table2","table2.txt") == false);
        claim(db.TabletoFile("Animal","AnimaltoFile.txt") == true);

        // tests 41-46
        // update and alter table
        claim(db.updateTableRecord("People","Name","ab123","John")==true);
        claim(db.selectTableRecord("People","ab123").get(1).equals("John")==true);
        claim(db.alterTableCol("People","Gender",-1)==false);
        claim(db.alterTableCol("People","Gender",1)==true);
        claim(db.insertTableRecord("People","ij345","F","Anna")==true);
        claim(db.selectTableRecord("People","ij345").get(1).equals("F")==true);
    }

    void claim(boolean b){
        if(!b) throw new Error("Database Test"+testNumber+"fails");
        testNumber++;
    }

    private int testNumber = 1;

    // testing
    private void run(Database db){
        testdatabase(db);
        System.out.println("All Database Tests Passed!");
    }

    public static void main(String[] args){
        Database d = new Database("PeopleandAnimals");
        d.run(d);
    }
}
/* class to let users operate on database
   by entering text in command line.
   If the first word you entered equals to one of the options,
   you will get some hints about format you should enter.
   If the command you entered is not valid,
   program will tell you to enter again.
   If you want to get out of the database, use "stop".
*/

import java.util.*;

public class User{
    void showOptions(){
        System.out.println("You can use the commands below");
        System.out.println("-use");
        System.out.println("-show");
        System.out.println("-create");
        System.out.println("-drop");
        System.out.println("-import");
        System.out.println("-output");
        System.out.println("-print");
        System.out.println("-select");
        System.out.println("-delete");
        System.out.println("-insert");
        System.out.println("-update");
        System.out.println("-alter");
        System.out.println("-stop");
        System.out.println("Enter command");
    }

    String getLine(){
        Scanner userInput = new Scanner(System.in);
        String line = userInput.nextLine();
        while(notEmptyLine(line) == false) {
            System.out.println("You entered nothing. Enter again.");
            line = userInput.nextLine();
        }
        return line;
    }

    // if user inputs nothing, return false
    boolean notEmptyLine(String line){
        if(line.length() == 0){
            return false;
        }
        return true;
    }

    // check whether use command is valid
    // it should be "use"+databasename
    boolean checkUseDatabase(String usedb){
        String[] temp = usedb.split(" ");
        if(temp.length!=2){
            return false;
        }
        if(!temp[0].equals("use")){
            return false;
        }
        return true;
    }

    String useDatabase(){
        Scanner input = new Scanner(System.in);
        String usedb = input.nextLine();
        if(checkUseDatabase(usedb)){
            return usedb.split(" ")[1];
        }
        else{
            do{
                System.out.println("Invalid input. Use:  use databasename");
                System.out.println("Enter again.");
                usedb = input.nextLine();
            }while(!checkUseDatabase(usedb));
            return usedb.split(" ")[1];
        }
    }

    void showTables(Database db,List<String> input){
        if(input.size()!=2 || !(input.get(0).equals("show") && input.get(1).equals("tables"))){
            System.out.println("Invalid input. Use: show tables");
            System.out.println("Enter again.");
            return;
        }
        List<String> tablenames = db.getTableNames();
        String[] temp = new String[tablenames.size()];
        for(int k=0;k<tablenames.size();k++){
            temp[k] = tablenames.get(k);
        }
        if(tablenames.size() == 0) {
            System.out.println("Empty database");
            return;
        }
        for (int i = 0; i < tablenames.size(); i++) {
            System.out.print(temp[i] + " ");
        }
        System.out.println();
    }

    void createNewTable(Database db, List<String > input){
        if(!checkCreateCommand(input)){
            System.out.println("Invalid input. Use: create tablename attributenames");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        String[] attris = new String[input.size() - 2];
        for (int i = 0; i < input.size() - 2; i++) {
            attris[i] = input.get(i + 2);
        }
        Table table = new Table();
        table.createTable(tablename, attris);
        if (db.insertTable(table)){
            System.out.println("Table " + tablename + " created");
            return;
        }
        else {
            System.out.println("Can't create an existed table. Try others.");
            System.out.println("Enter again.");
        }
    }

    // check whether create command is valid
    // it should contain "create"+tablename+attributenames
    boolean checkCreateCommand(List<String> input){
        if(input.size()<3){
            return false;
        }
        return true;
    }

    void dropTable(Database db, List<String> input){
        if(!checkDropCommand(input)){
            System.out.println("Invalid input. Use: drop tablename");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        if(db.dropTable(tablename)){
            System.out.println("Table "+tablename+" dropped");
            return;
        }
        else{
            System.out.println("Can't drop a table unexists. Try others.");
        }
    }

    // check whether drop command is valid
    // it should contain "drop" + tablename
    boolean checkDropCommand(List<String> input){
        if(input.size()!=2){
            return false;
        }
        return true;
    }

    void importFile(Database db,List<String> input){
        if(!checkImportCommand(input)){
            System.out.println("Invalid input. Use: import filename");
            System.out.println("Enter again.");
            return;
        }
        if(db.FiletoTable(input.get(1))){
            System.out.println("File "+input.get(1)+" imported");
            return;
        }
        else{
            System.out.println("File doesn't exist or tablename already exists");
            System.out.println("Enter again.");
        }
    }

    // check whether import command is valid
    // it should contain "import" + filename + tablename
    boolean checkImportCommand(List<String> input){
        if(input.size()!=2){
            return false;
        }
        return true;
    }

    void Output(Database db,List<String> input){
        if(!checkOutputCommand(input)){
            System.out.println("Invalid input. Use: output tablename filename");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        String filename = input.get(2);
        if(db.TabletoFile(tablename,filename)){
            System.out.println("Table "+tablename+" has been written to a file");
            return;
        }
        else{
            System.out.println("Table doesn't exist");
            System.out.println("Enter again.");
        }
    }

    // check whether output command is valid
    // it should contain "output" + tablename + filename
    boolean checkOutputCommand(List<String> input){
        if(input.size()!=3){
            return false;
        }
        return true;
    }

    void printOutTable(Database db, List<String> input){
        if(!checkPrintCommand(input)){
            System.out.println("Invalid input. Use: print tablename");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        if(db.tablePrint(tablename)){
            return;
        }
        else{
            System.out.println("Table "+tablename+" doesn't exist");
            System.out.println("Enter again.");
        }
    }

    // check whether print command is valid
    // it should contain "print" + tablename
    boolean checkPrintCommand(List<String> input){
        if(input.size()!=2){
            return false;
        }
        return true;
    }

    void Select(Database db,List<String> input){
        if(!checkSelectCommand(input)){
            System.out.println("Invalid input. Use: select tablename keyvalue");
            System.out.println("Enter again.");
            return;
        }
        try {
            String tablename = input.get(1);
            String keyvalue = input.get(2);
            List<String> r = db.selectTableRecord(tablename, keyvalue);
            System.out.println("The record you select is as below:");
            for (int i = 0; i < r.size(); i++) {
                System.out.print(r.get(i) + " ");
            }
            System.out.println();
        }catch (Exception e){System.out.println("Table or key not exists.\n Enter again.");}
    }

    // check whether select command is valid
    // it should contain "select" + tablename + keyvalue
    boolean checkSelectCommand(List<String> input){
        if(input.size()!=3){
            return false;
        }
        return true;
    }

    void Delete(Database db,List<String> input){
        if(!checkDeleteCommand(input)){
            System.out.println("Invalid input. Use: delete tablename keyvalue");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        String keyvalue = input.get(2);
        if(db.deleteTableRecord(tablename,keyvalue)){
            System.out.println("Record deleted. You can check by printing table.");
            return;
        }
        else{
            System.out.println("Table or key value doesn't exist");
            System.out.println("Enter again.");
        }
    }

    // check whether delete command is valid
    // it should contain "delete" + tablename + keyvalue
    boolean checkDeleteCommand(List<String> input){
        if(input.size()!=3){
            return false;
        }
        return true;
    }

    void Insert(Database db,List<String> input){
        if(!checkInsertCommand(input)){
            System.out.println("Invalid input. Use: insert tablename values");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        String[] values = new String[input.size()-2];
        for(int i=0;i<values.length;i++){
            values[i] = input.get(i+2);
        }
        if(db.insertTableRecord(tablename,values)){
            System.out.println("Record inserted. You can check by printing table.");
            return;
        }
        else{
            System.out.println("Table doesn't exist or key value is not unique or wrong value numbers");
            System.out.println("Enter again.");
        }
    }

    // check whether insert command is valid
    // it should contain "insert" + tablename + values
    boolean checkInsertCommand(List<String> input){
        if(input.size()<3){
            return false;
        }
        return true;
    }

    void Update(Database db,List<String> input){
        if(!checkUpdateCommand(input)){
            System.out.println("Invalid input. Use: update tablename attributename keyvalue newvalue");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        String attributename = input.get(2);
        String keyvalue = input.get(3);
        String newvalue = input.get(4);
        if(db.updateTableRecord(tablename,attributename,keyvalue,newvalue)){
            System.out.println("Value updated. You can check by printing table.");
            return;
        }
        else{
            System.out.println("Table or key value doesn't exist.");
            System.out.println("Enter again.");
        }
    }

    // check whether update command is valid
    // it should contain "update" + tablename + attributename + keyvalue + newvalue
    boolean checkUpdateCommand(List<String> input){
        if(input.size()!=5){
            return false;
        }
        return true;
    }

    void Alter(Database db,List<String> input){
        if(!checkAlterCommand(input)){
            System.out.println("Invalid input. Use: alter tablename newattributename position");
            System.out.println("Enter again.");
            return;
        }
        String tablename = input.get(1);
        String newattributename = input.get(2);
        try {
            Integer pos = Integer.valueOf(input.get(3));
            if (db.alterTableCol(tablename, newattributename, pos)) {
                System.out.println("New column added. You can check by printing table.");
                return;
            } else {
                System.out.println("Table doesn't exist or pos out of bound or new column name already exists");
                System.out.println("Enter again.");
            }
        }catch (Exception e){System.out.println("Position should be a number.\nEnter again.");}
    }

    // check whether alter command is valid
    // it should contain "alter" + tablename + newattributename + position
    boolean checkAlterCommand(List<String> input){
        if(input.size()!=4){
            return false;
        }
        return true;
    }

    void responsetoCommand(Database db,List<String> input){
        switch (input.get(0)){
            case "create"    :createNewTable(db,input); break;
            case "drop"      :dropTable(db,input); break;
            case "show"      :showTables(db,input); break;
            case "import"    :importFile(db,input); break;
            case "output"    :Output(db,input); break;
            case "print"     :printOutTable(db,input); break;
            case "select"    :Select(db,input); break;
            case "delete"    :Delete(db,input); break;
            case "insert"    :Insert(db,input); break;
            case "update"    :Update(db,input); break;
            case "alter"     :Alter(db,input); break;
            default          :System.out.println("Invalid input. Use options above.");break;
        }
    }

    // main design
    void program(Database db){
        boolean running = true;
        while(running){
            System.out.println("Enter command.");
            List<String> input = new ArrayList<String>();
            String line = getLine();
            String[] templine = line.split(" ");
            for(int i=0;i<templine.length;i++){
                input.add(i,templine[i]);
            }
            if(stopConnection(input)){
                System.out.println("Connection stopped");
                running = false;
            }
            else {
                responsetoCommand(db,input);
            }
        }
    }

    // use "stop" to stop connection
    boolean stopConnection(List<String> input){
        if(input.size()==1 && input.get(0).equals("stop")){
            return true;
        }
        return false;
    }

    void run(){
        showOptions();
        String databasename = useDatabase();
        Database db = new Database(databasename);
        program(db);
    }

    public static void main (String[] args){
        User u = new User();
        u.run();
    }
}
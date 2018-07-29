/* class to deal with tuple, an individual record in a table
   ignore the field names now
   just store the string values
 */

public class Record{
    private String[] tuple;

    // initialize
    Record (int i)  {
        if(i > 0)  {
            tuple = new String[i];
        }
    }

    // how many strings in a tuple
    int tupleSize(){
        return tuple.length;
    }

    String[] getTuple(){
        return tuple;
    }

    // test 1
    private void testtupleSize(){
        Record testrecord = new Record(3);
        testrecord.setValue("a",0);
        testrecord.setValue("b",1);
        testrecord.setValue("c",2);
        claim(testrecord.tupleSize() == 3);
    }

    // get a specific value in a tuple
    String getValue(int i){
        if(i > tuple.length-1 || i < 0){
            return "Value index is out of bound";
        }
        return tuple[i];
    }

    // tests 2-5
    private void testgetValue(){
        Record testrecord = new Record(3);
        testrecord.setValue("a",0);
        testrecord.setValue("b",1);
        testrecord.setValue("c",2);
        claim(testrecord.getValue(0).equals("a"));
        claim(testrecord.getValue(2).equals("c"));
        claim(testrecord.getValue(-1).equals("Value index is out of bound"));
        claim(testrecord.getValue(3).equals("Value index is out of bound"));
    }

    // set the value of a specific field
    boolean setValue(String s, int i){
        if(i >= 0 && i<tuple.length){
            tuple[i] = s;
            return true;
        }
        return false;
    }

    // tests 6-8
    private void testsetValue(){
        Record testrecord = new Record(3);
        testrecord.setValue("a",0);
        testrecord.setValue("b",1);
        testrecord.setValue("c",2);
        testrecord.setValue("newc",2);
        claim(testrecord.getValue(2) == "newc");
        claim(testrecord.setValue("hello",-1) == false);
        claim(testrecord.setValue("world",3) == false);
    }

    void setTuple(String...values){
        for(int i=0;i<values.length;i++){
            tuple[i] = values[i];
        }
    }

    // tests 9-10
    private void testsetTuple(){
        Record testrecord = new Record(4);
        testrecord.setTuple("zero","one","two","three");
        claim(testrecord.tupleSize() == 4);
        claim(testrecord.getValue(1).equals("one"));
    }

    // tests 11-12
    private void testgetTuple(){
        Record testrecord = new Record(4);
        testrecord.setTuple("zero","one","two","three");
        String[] testtuple = testrecord.getTuple();
        claim(testtuple[0].equals("zero"));
        claim(testtuple[3].equals("three"));
    }

    // insert a new value
    boolean insertvalue(int i){
        if(i<0 || i>tupleSize()){
            return false;
        }
        String[] temptuple = new String[tuple.length+1];
        for(int k = 0,l = 0;k<tupleSize()+1;k++){
            if(k == i){
                temptuple[k] = "NULL";
            }
            else{
                temptuple[k] = tuple[l];
                l++;
            }
        }
        tuple = temptuple;
        return true;
    }

    // tests 13-18
    private void testinsertvalue(){
        Record testrecord = new Record(3);
        testrecord.setValue("a",0);
        testrecord.setValue("b",1);
        testrecord.setValue("c",2);
        claim(testrecord.getValue(3) == "Value index is out of bound");
        testrecord.insertvalue(3);
        claim(testrecord.getValue(3) == "NULL");
        claim(testrecord.tupleSize() == 4);
        testrecord.setValue("newvalue",3);
        claim(testrecord.getValue(3) == "newvalue");
        testrecord.insertvalue(1);
        claim(testrecord.tupleSize() == 5);
        claim(testrecord.getValue(1) == "NULL");
    }

    void claim(boolean b){
        if(!b) throw new Error("Record Test"+testNumber+"fails");
        testNumber++;
    }
    private int testNumber = 1;

    // testing
    private void run1(){
        testtupleSize();
        testgetValue();
        testsetValue();
        testsetTuple();
        testgetTuple();
        testinsertvalue();
        System.out.println("All Record Tests Passed!");
    }

    public static void main(String[] args){
        Record r1 = new Record(3);
        r1.run1();
    }
}

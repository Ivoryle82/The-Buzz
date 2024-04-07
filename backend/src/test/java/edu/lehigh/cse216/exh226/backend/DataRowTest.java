// package edu.lehigh.cse216.exh226.backend;

// import edu.lehigh.cse216.exh226.backend.DataRow;
// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;

// /**
//  * Unit test for simple App.
//  */
// public class DataRowTest extends TestCase {
//     /**
//      * Create the test case
//      * 
//      * @param testName : name of the test case
//      */
//     public DataRowTest(String testName) {
//         super(testName);
//     }

//     /**
//      * @return the suite of tests being tested
//      */
//     public static Test suite() {
//         return new TestSuite(DataRowTest.class);
//     }

//     /**
//      * Ensure that the constructor populates every field of the object it creates
//      */
//     public void testConstructor() {
//         String title = "Test Title";
//         String content = "Test Content";
//         int id = 17;
//         DataRow d = new DataRow(id, title, content); // i guess we are checking if the DataRow class works

//         assertTrue(d.mTitle.equals(title));
//         assertTrue(d.mContent.equals(content));
//         assertTrue(d.mId == id);
//         assertFalse(d.mCreated == null);
//     }

//     /**
//      * Ensure that the copy constructor works correctly
//      */
//     public void testCopyconstructor() {
//         String title = "Test Title For Copy";
//         String content = "Test Content For Copy";
//         int id = 177; // Will this create conflicts in our actual database data?
//         DataRow d = new DataRow(id, title, content);
//         DataRow d2 = new DataRow(d);

//         assertTrue(d2.mTitle.equals(d.mTitle));
//         assertTrue(d2.mContent.equals(d.mContent));
//         assertTrue(d2.mId == d.mId);
//         assertTrue(d2.mCreated.equals(d.mCreated));
//     }
// }


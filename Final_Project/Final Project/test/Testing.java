import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Project tests")
public class Testing {
    /* Test cases for Project*/

    @Test
    @Order(1)
    @DisplayName("Testing Government class for input validations and Singleton-Design approach")
    void Government1() throws Exception {
        assertThrows(RuntimeException.class, () -> Government.getInstance("    "));
        assertThrows(RuntimeException.class, () -> Government.getInstance(""));
        assertThrows(RuntimeException.class, () -> Government.getInstance(null));
        assertThrows(RuntimeException.class, () -> Government.getInstance("g2.properties"));
        Government.getInstance("g1.properties");
    }

    @Test
    @Order(2)
    @DisplayName("Testing instantiation of MobileDevice for input validations")
    void MobileDeviceInstantiation() throws Exception {

        assert Government.resetDatabase();

        Government gov = Government.getInstance("g1.properties");
        assertThrows(RuntimeException.class, () -> new MobileDevice("     ", gov));
        assertThrows(RuntimeException.class, () -> new MobileDevice("", gov));
        assertThrows(RuntimeException.class, () -> new MobileDevice(null, gov));
        assertThrows(RuntimeException.class, () -> new MobileDevice("mm1.properties", null));

    }

    @Test
    @Order(3)
    @DisplayName("Input validations for recordContact() method")
    void recordContact1() throws Exception {
        Government gov = Government.getInstance("g1.properties");
        MobileDevice m1 = new MobileDevice("m1.properties", gov);
        MobileDevice m2 = new MobileDevice("m2.properties", gov);
        assertThrows(RuntimeException.class, () -> m1.recordContact(" ", 23, 34), "Recorded contact invalid!");
        assertThrows(RuntimeException.class, () -> m1.recordContact("", 23, 34), "Recorded contact invalid!");
        assertThrows(RuntimeException.class, () -> m1.recordContact(null, 23, 34), "Recorded contact invalid!");
        assertThrows(RuntimeException.class, () -> m1.recordContact(m2.getHash(), -1, 24), "Recorded contact date is invalid!");
        assertThrows(RuntimeException.class, () -> m1.recordContact(m2.getHash(), 3, 0), "Recorded contact duration is invalid!");
    }


    @Test
    @Order(4)
    @DisplayName("Instantiating 10 MobileDevices & 1 Government, recording contacts, recording positive tests, synchronizing data multiple times")
    void synchronizeTest1() throws Exception {

        assert Government.resetDatabase();

        Government gov = Government.getInstance("g1.properties");

        MobileDevice m1 = new MobileDevice("m1.properties", gov);
        MobileDevice m2 = new MobileDevice("m2.properties", gov);
        MobileDevice m3 = new MobileDevice("m3.properties", gov);
        MobileDevice m4 = new MobileDevice("m4.properties", gov);
        MobileDevice m5 = new MobileDevice("m5.properties", gov);
        MobileDevice m6 = new MobileDevice("m6.properties", gov);
        MobileDevice m7 = new MobileDevice("m7.properties", gov);
        MobileDevice m8 = new MobileDevice("m8.properties", gov);
        MobileDevice m9 = new MobileDevice("m9.properties", gov);
        MobileDevice m10 = new MobileDevice("m10.properties", gov);

        m1.recordContact(m2.getHash(), 20, 12);
        m1.recordContact(m3.getHash(), 19, 45);
        m1.recordContact(m4.getHash(), 24, 70);
        m3.recordContact(m4.getHash(), 17, 32);
        m2.recordContact(m1.getHash(), 21, 2);
        m2.recordContact(m3.getHash(), 21, 4);
        m3.recordContact(m2.getHash(), 24, 5);
        m3.recordContact(m8.getHash(), 23, 20);
        m7.recordContact(m8.getHash(), 28, 17);
        m6.recordContact(m8.getHash(), 22, 23);
        m2.recordContact(m8.getHash(), 28, 12);
        m2.recordContact(m9.getHash(), 21, 10);
        m3.recordContact(m9.getHash(), 32, 34);
        m5.recordContact(m8.getHash(), 23, 40);
        m3.recordContact(m6.getHash(), 18, 30);
        m4.recordContact(m8.getHash(), 22, 50);
        m4.recordContact(m1.getHash(), 21, 31);
        m4.recordContact(m6.getHash(), 21, 21);
        m7.recordContact(m6.getHash(), 23, 25);
        m9.recordContact(m6.getHash(), 19, 16);
        m9.recordContact(m5.getHash(), 18, 27);
        m9.recordContact(m10.getHash(), 23, 3);
        m10.recordContact(m9.getHash(), 23, 40);
        m8.recordContact(m5.getHash(), 22, 20);
        m8.recordContact(m3.getHash(), 21, 11);
        m8.recordContact(m4.getHash(), 24, 14);
        m8.recordContact(m9.getHash(), 29, 37);
        m1.recordContact(m7.getHash(), 29, 29);
        m2.recordContact(m6.getHash(), 22, 14);

        gov.recordTestResult("covidtest1", 17, true);
        gov.recordTestResult("covidtest2", 21, true);
        gov.recordTestResult("covidtest3", 23, true);
        gov.recordTestResult("covidtest4", 25, true);
        gov.recordTestResult("covidtest5", 30, true);

        m1.positiveTest("covidtest1");
        m6.positiveTest("covidtest2");
        m2.positiveTest("covidtest3");
        m8.positiveTest("covidtest4");
        m10.positiveTest("covidtest5");

        assertFalse(m1.synchronizeData());
        assertTrue(m2.synchronizeData());
        assertTrue(m3.synchronizeData());
        assertTrue(m4.synchronizeData());
        assertFalse(m5.synchronizeData());
        assertFalse(m6.synchronizeData());
        assertTrue(m7.synchronizeData());
        assertFalse(m8.synchronizeData());
        assertTrue(m9.synchronizeData());
        assertFalse(m10.synchronizeData());
        assertTrue(m2.synchronizeData());
        assertTrue(m3.synchronizeData());
        assertTrue(m4.synchronizeData());
        assertTrue(m7.synchronizeData());
        assertTrue(m1.synchronizeData());
        assertFalse(m2.synchronizeData());
        assertFalse(m3.synchronizeData());
        assertFalse(m4.synchronizeData());
        assertFalse(m7.synchronizeData());

        m4.recordContact(m10.getHash(), 31, 30);
        assertTrue(m4.synchronizeData());

        assert Government.resetDatabase();
    }

    @Test
    @Order(5)
    @DisplayName("Instantiating 4 MobileDevices & 1 Government, recording positive & negative tests, recording contacts, synchronizing data multiple times")
    void synchronizeTest2() throws Exception {

        assert Government.resetDatabase();

        Government gov = Government.getInstance("g1.properties");
        MobileDevice m1 = new MobileDevice("m1.properties", gov);
        MobileDevice m2 = new MobileDevice("m2.properties", gov);
        MobileDevice m3 = new MobileDevice("m3.properties", gov);
        MobileDevice m4 = new MobileDevice("m4.properties", gov);

        gov.recordTestResult("covidtest1", 13, true);
        gov.recordTestResult("covidtest2", 14, false);
        gov.recordTestResult("covidtest3", 16, true);

        m1.positiveTest("covidtest1");
        m1.positiveTest("covidtest1");
        m2.positiveTest("covidtest3");

        m1.recordContact(m2.getHash(), 3, 5);
        m2.recordContact(m1.getHash(), 3, 45);
        m1.recordContact(m3.getHash(), 5, 6);
        m3.recordContact(m1.getHash(), 5, 2);
        m2.recordContact(m3.getHash(), 1, 16);
        m3.recordContact(m2.getHash(), 1, 46);
        m3.recordContact(m4.getHash(), 7, 50);
        m4.recordContact(m3.getHash(), 7, 31);

        assertFalse(m1.synchronizeData());
        assertTrue(m2.synchronizeData());
        assertFalse(m4.synchronizeData());
        assertTrue(m3.synchronizeData());
        assertFalse(m3.synchronizeData());

        gov.recordTestResult("covidtest4", 15, true);
        m4.positiveTest("covidtest4");

        assertFalse(m4.synchronizeData());
        assertTrue(m3.synchronizeData());

        assert Government.resetDatabase();
    }

    @Test
    @Order(6)
    @DisplayName("Instantiating 7 MobileDevices & 1 Government, recording positive & negative tests, recording contacts, synchronizing data multiple times")
    void synchronizeTest3() throws Exception {

        assert Government.resetDatabase();

        Government gov = Government.getInstance("g1.properties");

        MobileDevice m1 = new MobileDevice("m1.properties", gov);
        MobileDevice m2 = new MobileDevice("m2.properties", gov);
        MobileDevice m3 = new MobileDevice("m3.properties", gov);
        MobileDevice m4 = new MobileDevice("m4.properties", gov);
        MobileDevice m5 = new MobileDevice("m5.properties", gov);
        MobileDevice m6 = new MobileDevice("m6.properties", gov);
        MobileDevice m7 = new MobileDevice("m7.properties", gov);

        gov.recordTestResult("CovidTest1", 6, false);
        gov.recordTestResult("CovidTest2", 4, false);
        gov.recordTestResult("CovidTest3", 23, false);
        gov.recordTestResult("CovidTest4", 61, false);
        gov.recordTestResult("CovidTest5", 52, true);
        gov.recordTestResult("CovidTest6", 76, false);
        gov.recordTestResult("CovidTest7", 64, false);
        gov.recordTestResult("CovidTest8", 42, false);
        gov.recordTestResult("CovidTest9", 18, false);
        gov.recordTestResult("CovidTest10", 12, false);
        gov.recordTestResult("CovidTest11", 54, true);
        gov.recordTestResult("CovidTest12", 22, false);
        gov.recordTestResult("CovidTest13", 38, false);
        gov.recordTestResult("CovidTest14", 39, false);

        m1.recordContact(m2.getHash(), 50, 30);
        m1.recordContact(m3.getHash(), 50, 35);
        m1.recordContact(m4.getHash(), 50, 37);
        m2.recordContact(m1.getHash(), 50, 30);
        m2.recordContact(m3.getHash(), 50, 38);
        m2.recordContact(m5.getHash(), 50, 39);
        m2.recordContact(m7.getHash(), 50, 28);
        m3.recordContact(m1.getHash(), 50, 35);
        m1.recordContact(m3.getHash(), 50, 22);
        m3.recordContact(m1.getHash(), 50, 22);
        m3.recordContact(m2.getHash(), 50, 38);
        m3.recordContact(m4.getHash(), 50, 36);
        m3.recordContact(m5.getHash(), 50, 41);
        m3.recordContact(m7.getHash(), 50, 47);
        m4.recordContact(m1.getHash(), 50, 37);
        m4.recordContact(m3.getHash(), 50, 36);
        m4.recordContact(m6.getHash(), 50, 34);
        m5.recordContact(m2.getHash(), 50, 39);
        m5.recordContact(m6.getHash(), 50, 42);
        m5.recordContact(m3.getHash(), 50, 41);
        m6.recordContact(m5.getHash(), 50, 42);
        m6.recordContact(m4.getHash(), 50, 34);
        m7.recordContact(m2.getHash(), 50, 28);
        m7.recordContact(m3.getHash(), 50, 47);

        m1.positiveTest("CovidTest1");
        m1.positiveTest("CovidTest2");
        m1.positiveTest("CovidTest3");
        m1.positiveTest("CovidTest4");
        m1.positiveTest("CovidTest5");
        m2.positiveTest("CovidTest11");

        assertFalse(m1.synchronizeData());
        assertTrue(m2.synchronizeData());
        assertTrue(m3.synchronizeData());
        assertTrue(m4.synchronizeData());
        assertTrue(m5.synchronizeData());
        assertFalse(m6.synchronizeData());
        assertTrue(m7.synchronizeData());

        assertEquals(5, gov.findGatherings(50, 2, 32, 0.6f));
        assertEquals(2, gov.findGatherings(50, 3, 32, 0.6f));
        assertEquals(0, gov.findGatherings(50, 4, 32, 0.49f));
        assertEquals(0, gov.findGatherings(50, 5, 32, 0.49f));

        assert Government.resetDatabase();
    }

    @Test
    @Order(7)
    @DisplayName("Instantiating 10 MobileDevices & 1 Government, recording positive & negative tests, recording contacts, synchronizing data multiple times")
    void synchronizeTest4() throws Exception {

        assert Government.resetDatabase();

        Government government = Government.getInstance("g1.properties");
        MobileDevice m1 = new MobileDevice("m1.properties", government);
        MobileDevice m2 = new MobileDevice("m2.properties", government);
        MobileDevice m3 = new MobileDevice("m3.properties", government);
        MobileDevice m4 = new MobileDevice("m4.properties", government);
        MobileDevice m5 = new MobileDevice("m5.properties", government);
        MobileDevice m6 = new MobileDevice("m6.properties", government);
        MobileDevice m7 = new MobileDevice("m7.properties", government);
        MobileDevice m8 = new MobileDevice("m8.properties", government);
        MobileDevice m9 = new MobileDevice("m9.properties", government);
        MobileDevice m10 = new MobileDevice("m10.properties", government);

        m1.recordContact(m2.getHash(), 10, 5);
        m1.recordContact(m3.getHash(), 10, 7);
        m1.recordContact(m4.getHash(), 10, 8);
        m3.recordContact(m4.getHash(), 10, 10);
        m2.recordContact(m3.getHash(), 10, 10);
        m5.recordContact(m3.getHash(), 10, 4);
        m5.recordContact(m4.getHash(), 10, 12);
        m5.recordContact(m6.getHash(), 10, 6);
        m6.recordContact(m7.getHash(), 10, 9);
        m5.recordContact(m7.getHash(), 10, 13);
        m5.recordContact(m3.getHash(), 10, 1);

        assertFalse(m1.synchronizeData());
        assertFalse(m2.synchronizeData());
        assertFalse(m3.synchronizeData());
        assertFalse(m4.synchronizeData());
        assertFalse(m5.synchronizeData());
        assertFalse(m6.synchronizeData());
        assertFalse(m7.synchronizeData());
        assertFalse(m8.synchronizeData());
        assertFalse(m9.synchronizeData());
        assertFalse(m10.synchronizeData());

        assertEquals(2, government.findGatherings(10, 3, 4, 0.1f));
        assertEquals(1, government.findGatherings(10, 4, 4, 0.1f));
        assertEquals(2, government.findGatherings(10, 3, 5, 0.1f));
        assertEquals(1, government.findGatherings(10, 3, 4, 1f));
        assertEquals(2, government.findGatherings(10, 3, 5, 0.25f));
        assertEquals(1, government.findGatherings(10, 4, 5, 0.1f));
        assertEquals(1, government.findGatherings(10, 4, 4, 0.1f));
        assertEquals(1, government.findGatherings(10, 4, 4, 0.1f));
        assertEquals(1, government.findGatherings(10, 5, 5, 0.1f));

        assert Government.resetDatabase();
    }


}
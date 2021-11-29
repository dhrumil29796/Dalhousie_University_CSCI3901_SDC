// Public class Main, containing all the method calls
// Everything is written inside try block
// Catching all possible thrown exceptions & printing the stack trace
public class Main {

    // Driver method
    public static void main(String[] args) {

        // Try block
        try {
            System.out.println(Government.resetDatabase());

            // Test1 - Testing the normal flow without findGatherings
/*            Government gov = Government.getInstance("g1.properties");
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

            m1.recordContact(m2.getHash(), 3, 45);
            m2.recordContact(m1.getHash(), 3, 45);
            m1.recordContact(m3.getHash(), 5, 26);
            m3.recordContact(m1.getHash(), 5, 26);
            m2.recordContact(m3.getHash(), 1, 26);
            m3.recordContact(m2.getHash(), 1, 26);
            m3.recordContact(m4.getHash(), 7, 55);
            m4.recordContact(m3.getHash(), 7, 55);

            System.out.println("Synchronizing m1");
            System.out.println(m1.synchronizeData());
            System.out.println("Synchronizing m2");
            System.out.println(m2.synchronizeData());
            System.out.println("Synchronizing m4");
            System.out.println(m4.synchronizeData());
            System.out.println("Synchronizing m3");
            System.out.println(m3.synchronizeData());
            System.out.println("Synchronizing m3 again");
            System.out.println(m3.synchronizeData());

            gov.recordTestResult("covidtest4", 15, true);
            m4.positiveTest("covidtest4");

            System.out.println("Synchronizing m4 again");
            System.out.println(m4.synchronizeData());
            System.out.println("Synchronizing m3 3rd time");
            System.out.println(m3.synchronizeData());*/


            // Test2 - Testing the normal flow with findGatherings
/*            Government gov = Government.getInstance("g1.properties");
            MobileDevice m1 = new MobileDevice("m1.properties", gov);
            MobileDevice m2 = new MobileDevice("m2.properties", gov);
            MobileDevice m3 = new MobileDevice("m3.properties", gov);
            MobileDevice m4 = new MobileDevice("m4.properties", gov);
            MobileDevice m5 = new MobileDevice("m5.properties", gov);
            MobileDevice m6 = new MobileDevice("m6.properties", gov);
            MobileDevice m7 = new MobileDevice("m7.properties", gov);

            gov.recordTestResult("covidtest1", 90, true);
            gov.recordTestResult("covidtest2", 90, true);
            gov.recordTestResult("covidtest3", 91, false);
            gov.recordTestResult("covidtest4", 91, true);

            m1.positiveTest("covidtest1");
            m2.positiveTest("covidtest2");
            m3.positiveTest("covidtest4");

            m1.recordContact(m2.getHash(), 93, 30);
            m1.recordContact(m3.getHash(), 93, 20);
            m1.recordContact(m4.getHash(), 93, 20);
            m2.recordContact(m1.getHash(), 93, 20);
            m2.recordContact(m3.getHash(), 93, 30);
            m3.recordContact(m4.getHash(), 93, 20);
            m4.recordContact(m3.getHash(), 93, 20);
            m5.recordContact(m6.getHash(), 93, 30);
            m5.recordContact(m7.getHash(), 93, 20);
            m5.recordContact(m1.getHash(), 93, 20);
            m5.recordContact(m2.getHash(), 93, 20);
            m7.recordContact(m6.getHash(), 93, 20);
            m7.recordContact(m5.getHash(), 93, 30);
            m1.recordContact(m5.getHash(), 93, 20);
            m2.recordContact(m5.getHash(), 93, 20);

            System.out.println("Mobile 1: " + m1.synchronizeData());
            System.out.println("Mobile 2: " + m2.synchronizeData());
            System.out.println("Mobile 3: " + m3.synchronizeData());
            System.out.println("Mobile 4: " + m4.synchronizeData());
            System.out.println("Mobile 5: " + m5.synchronizeData());
            System.out.println("Mobile 6: " + m6.synchronizeData());
            System.out.println("Mobile 7: " + m7.synchronizeData());
            System.out.println("Mobile 1: " + m1.synchronizeData());

            System.out.println("The no.of gatherings: " + gov.findGatherings(93, 2, 10, 0.6f));*/


            // Test 3
/*            Government gov1 = Government.getInstance("g1.properties");
            MobileDevice m1 = new MobileDevice("m1.properties", gov1);
            MobileDevice m2 = new MobileDevice("m2.properties", gov1);
            MobileDevice m3 = new MobileDevice("m3.properties", gov1);
            MobileDevice m4 = new MobileDevice("m4.properties", gov1);
            MobileDevice m5 = new MobileDevice("m5.properties", gov1);

            gov1.recordTestResult("covidtest1", 90, true);
            gov1.recordTestResult("covidtest2", 90, true);
            gov1.recordTestResult("covidtest3", 91, false);
            gov1.recordTestResult("covidtest4", 91, true);

            m1.positiveTest("covidtest1");
            m2.positiveTest("covidtest2");
            m3.positiveTest("covidtest4");

            m1.recordContact(m2.getHash(), 91, 10);
            m1.recordContact(m4.getHash(), 91, 20);
            m2.recordContact(m1.getHash(), 91, 10);
            m3.recordContact(m4.getHash(), 92, 20);
            m3.recordContact(m5.getHash(), 93, 30);
            m4.recordContact(m1.getHash(), 91, 20);
            m4.recordContact(m3.getHash(), 92, 20);
            m5.recordContact(m3.getHash(), 93, 30);

            System.out.println("Mobile 1: " + m1.synchronizeData());
            System.out.println("Mobile 2: " + m2.synchronizeData());
            System.out.println("Mobile 3: " + m3.synchronizeData());
            System.out.println("Mobile 4: " + m4.synchronizeData());
            System.out.println("Mobile 1: " + m1.synchronizeData());*/

/*
            // Test 4
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

            gov.recordTestResult("covidtest1", 2, true);
            gov.recordTestResult("covidtest2", 6, true);
            gov.recordTestResult("covidtest3", 8, true);
            gov.recordTestResult("covidtest4", 10, true);
            gov.recordTestResult("covidtest5", 15, true);*/

/*            gov.recordTestResult("covidtest6", 17, false);
            gov.recordTestResult("covidtest7", 13, false);
            gov.recordTestResult("covidtest8", 46, false);
            gov.recordTestResult("covidtest9", 3, false);*/

/*            m1.positiveTest("covidtest1");
            m6.positiveTest("covidtest2");
            m2.positiveTest("covidtest3");
            m8.positiveTest("covidtest4");
            m10.positiveTest("covidtest5");*/

/*            m1.recordContact(m2.getHash(), 5, 10);
            m1.recordContact(m3.getHash(), 7, 7);
            m1.recordContact(m4.getHash(), 9, 76);
            m3.recordContact(m4.getHash(), 2, 20);
            m2.recordContact(m3.getHash(), 3, 20);
            m3.recordContact(m2.getHash(), 7, 20);
            m3.recordContact(m4.getHash(), 2, 20);
            m3.recordContact(m8.getHash(), 8, 20);
            m7.recordContact(m8.getHash(), 3, 20);
            m3.recordContact(m8.getHash(), 7, 20);
            m2.recordContact(m8.getHash(), 3, 20);
            m2.recordContact(m9.getHash(), 6, 20);
            m3.recordContact(m9.getHash(), 7, 20);
            m5.recordContact(m8.getHash(), 8, 10);
            m3.recordContact(m6.getHash(), 3, 10);
            m4.recordContact(m8.getHash(), 7, 10);
            m4.recordContact(m6.getHash(), 6, 10);
            m7.recordContact(m6.getHash(), 8, 10);
            m9.recordContact(m6.getHash(), 4, 10);
            m9.recordContact(m5.getHash(), 3, 10);
            m9.recordContact(m3.getHash(), 8, 30);
            m1.recordContact(m9.getHash(), 2, 30);
            m8.recordContact(m5.getHash(), 7, 30);
            m8.recordContact(m3.getHash(), 1, 10);
            m8.recordContact(m4.getHash(), 9, 10);
            m8.recordContact(m9.getHash(), 4, 30);
            m1.recordContact(m7.getHash(), 4, 20);
            m2.recordContact(m6.getHash(), 7, 10);
            m2.recordContact(m9.getHash(), 3, 30);
            m7.recordContact(m3.getHash(), 5, 20);
            m6.recordContact(m5.getHash(), 9, 20);
            m8.recordContact(m6.getHash(), 3, 30);
            m3.recordContact(m2.getHash(), 8, 10);
            m4.recordContact(m3.getHash(), 3, 30);
            m7.recordContact(m5.getHash(), 1, 20);
            m8.recordContact(m4.getHash(), 7, 10);
            m4.recordContact(m3.getHash(), 8, 30);
            m3.recordContact(m2.getHash(), 5, 20);
            m2.recordContact(m4.getHash(), 7, 10);
            m8.recordContact(m3.getHash(), 2, 30);
            m9.recordContact(m3.getHash(), 8, 20);
            m10.recordContact(m1.getHash(), 8, 30);
            m5.recordContact(m6.getHash(), 4, 10);
            m2.recordContact(m10.getHash(), 8, 30);
            m7.recordContact(m10.getHash(), 3, 20);
            m2.recordContact(m7.getHash(), 7, 10);
            m7.recordContact(m3.getHash(), 3, 30);
            m4.recordContact(m8.getHash(), 5, 10);*/

/*            m1.recordContact(m2.getHash(), 10, 5);
            m1.recordContact(m3.getHash(), 10, 7);
            m1.recordContact(m4.getHash(), 10, 8);
            m3.recordContact(m4.getHash(), 10, 10);
            m2.recordContact(m3.getHash(), 10, 10);
            m5.recordContact(m3.getHash(), 10, 4);
            m5.recordContact(m3.getHash(), 10, 1);
            m3.recordContact(m4.getHash(), 10, 12);
            m5.recordContact(m6.getHash(), 10, 6);
            m6.recordContact(m7.getHash(), 10, 9);
            m5.recordContact(m7.getHash(), 10, 13);*/

/*            m1.recordContact(m2.getHash(), 10, 5);
            m1.recordContact(m3.getHash(), 10, 7);
            m1.recordContact(m4.getHash(), 10, 8);
            m3.recordContact(m4.getHash(), 10, 10);
            m2.recordContact(m3.getHash(), 10, 10);
            m5.recordContact(m3.getHash(), 10, 4);
            m5.recordContact(m3.getHash(), 10, 1);
            m5.recordContact(m4.getHash(), 10, 12);
            m5.recordContact(m6.getHash(), 10, 6);
            m6.recordContact(m7.getHash(), 10, 9);
            m5.recordContact(m7.getHash(), 10, 13);*/

/*            System.out.println("Mobile 1: " + m1.synchronizeData());
            System.out.println("Mobile 2: " + m2.synchronizeData());
            System.out.println("Mobile 3: " + m3.synchronizeData());
            System.out.println("Mobile 4: " + m4.synchronizeData());
            System.out.println("Mobile 5: " + m5.synchronizeData());
            System.out.println("Mobile 6: " + m6.synchronizeData());
            System.out.println("Mobile 7: " + m7.synchronizeData());
            System.out.println("Mobile 8: " + m8.synchronizeData());
            System.out.println("Mobile 9: " + m9.synchronizeData());
            System.out.println("Mobile 10: " + m10.synchronizeData());*/

/*            Government gov = Government.getInstance("g1.properties");

            MobileDevice m1 = new MobileDevice("m1.properties", gov);
            MobileDevice m2 = new MobileDevice("m2.properties", gov);
            MobileDevice m3 = new MobileDevice("m3.properties", gov);
            MobileDevice m4 = new MobileDevice("m4.properties", gov);
            MobileDevice m5 = new MobileDevice("m5.properties", gov);

            m1.recordContact(m2.getHash(), 10, 30);
            m1.recordContact(m3.getHash(), 10, 35);
            m1.recordContact(m4.getHash(), 10, 37);
            m2.recordContact(m1.getHash(), 10, 30);
            m2.recordContact(m3.getHash(), 10, 38);
            m2.recordContact(m5.getHash(), 10, 39);
            m3.recordContact(m1.getHash(), 10, 35);
            m3.recordContact(m2.getHash(), 10, 38);
            m3.recordContact(m4.getHash(), 10, 36);
            m4.recordContact(m1.getHash(), 10, 37);
            m4.recordContact(m3.getHash(), 10, 36);
            m5.recordContact(m2.getHash(), 10, 39);

            System.out.println("Mobile 1: " + m1.synchronizeData());
            System.out.println("Mobile 2: " + m2.synchronizeData());
            System.out.println("Mobile 3: " + m3.synchronizeData());
            System.out.println("Mobile 4: " + m4.synchronizeData());
            System.out.println("Mobile 5: " + m5.synchronizeData());*/


            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 2, 1, 0.01f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(20, 2, 1, 0.01f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(3, 2, 1, 0.01f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(4, 2, 1, 0.01f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(4, 2, 1, 0.5f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(4, 2, 1, 1f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(4, 2, 1, 0.9f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(3, 2, 1, 0.01f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(4, 3, 1, 0.01f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(93, 1, 10, 0.5f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 3, 4, 0.1f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 4, 4, 0.1f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 3, 5, 0.1f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 3, 4, 1f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 3, 5,  0.25f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 3, 36, 0.6f));
            //System.out.println("The no.of gatherings: " + gov.findGatherings(10, 3, 36, 0.6f));


            // Test 5 - Testing the program flow with findGatherings
/*            Government gov = Government.getInstance("g1.properties");

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

            MobileDevice A = new MobileDevice("m1.properties", gov);
            MobileDevice B = new MobileDevice("m2.properties", gov);
            MobileDevice C = new MobileDevice("m3.properties", gov);
            MobileDevice D = new MobileDevice("m4.properties", gov);
            MobileDevice E = new MobileDevice("m5.properties", gov);
            MobileDevice F = new MobileDevice("m6.properties", gov);
            MobileDevice G = new MobileDevice("m7.properties", gov);

            A.recordContact(B.getHash(), 50, 30);
            A.recordContact(C.getHash(), 50, 35);
            A.recordContact(D.getHash(), 50, 37);
            B.recordContact(A.getHash(), 50, 30);
            B.recordContact(C.getHash(), 50, 38);
            B.recordContact(E.getHash(), 50, 39);
            B.recordContact(G.getHash(), 50, 28);
            C.recordContact(A.getHash(), 50, 35);
            A.recordContact(C.getHash(), 50, 22);
            C.recordContact(A.getHash(), 50, 22);
            C.recordContact(B.getHash(), 50, 38);
            C.recordContact(D.getHash(), 50, 36);
            C.recordContact(E.getHash(), 50, 41);
            C.recordContact(G.getHash(), 50, 47);
            D.recordContact(A.getHash(), 50, 37);
            D.recordContact(C.getHash(), 50, 36);
            D.recordContact(F.getHash(), 50, 34);
            E.recordContact(B.getHash(), 50, 39);
            E.recordContact(F.getHash(), 50, 42);
            E.recordContact(C.getHash(), 50, 41);
            F.recordContact(E.getHash(), 50, 42);
            F.recordContact(D.getHash(), 50, 34);
            G.recordContact(B.getHash(), 50, 28);
            G.recordContact(C.getHash(), 50, 47);

            A.positiveTest("CovidTest1");
            A.positiveTest("CovidTest2");
            A.positiveTest("CovidTest3");
            A.positiveTest("CovidTest4");
            A.positiveTest("CovidTest5");
            B.positiveTest("CovidTest11");

            System.out.println(A.synchronizeData());
            System.out.println(B.synchronizeData());
            System.out.println(C.synchronizeData());
            System.out.println(D.synchronizeData());
            System.out.println(E.synchronizeData());
            System.out.println(F.synchronizeData());
            System.out.println(G.synchronizeData());

            System.out.println(gov.findGatherings(50, 2, 32, 0.6f));
            System.out.println(gov.findGatherings(50, 3, 32, 0.6f));
            System.out.println(gov.findGatherings(50, 4, 32, 0.49f));
            System.out.println(gov.findGatherings(50, 5, 32, 0.49f));*/


            // Test 4 - Testing the programFLow with findGatherings

            Government government = Government.getInstance("g1.properties");
            MobileDevice md1 = new MobileDevice("m1.properties", government);
            MobileDevice md2 = new MobileDevice("m2.properties", government);
            MobileDevice md3 = new MobileDevice("m3.properties", government);
            MobileDevice md4 = new MobileDevice("m4.properties", government);
            MobileDevice md5 = new MobileDevice("m5.properties", government);
            MobileDevice md6 = new MobileDevice("m6.properties", government);
            MobileDevice md7 = new MobileDevice("m7.properties", government);
            MobileDevice md8 = new MobileDevice("m8.properties", government);
            MobileDevice md9 = new MobileDevice("m9.properties", government);
            MobileDevice md10 = new MobileDevice("m10.properties", government);

            md1.recordContact(md2.getHash(), 10, 5);
            md1.recordContact(md3.getHash(), 10, 7);
            md1.recordContact(md4.getHash(), 10, 8);
            md3.recordContact(md4.getHash(), 10, 10);
            md2.recordContact(md3.getHash(), 10, 10);
            md5.recordContact(md3.getHash(), 10, 4);
            md5.recordContact(md4.getHash(), 10, 12);
            md5.recordContact(md6.getHash(), 10, 6);
            md6.recordContact(md7.getHash(), 10, 9);
            md5.recordContact(md7.getHash(), 10, 13);
            md5.recordContact(md3.getHash(), 10, 1);

            System.out.println(md1.synchronizeData());
            System.out.println(md2.synchronizeData());
            System.out.println(md3.synchronizeData());
            System.out.println(md4.synchronizeData());
            System.out.println(md5.synchronizeData());
            System.out.println(md6.synchronizeData());
            System.out.println(md7.synchronizeData());
            System.out.println(md8.synchronizeData());
            System.out.println(md9.synchronizeData());
            System.out.println(md10.synchronizeData());

            System.out.println(government.findGatherings(10, 3, 4, 0.1f));
            System.out.println(government.findGatherings(10, 4, 4, 0.1f));
            System.out.println(government.findGatherings(10, 3, 5, 0.1f));
            System.out.println(government.findGatherings(10, 3, 4, 1f));
            System.out.println(government.findGatherings(10, 3, 5, 0.25f));
            System.out.println(government.findGatherings(10, 4, 5, 0.1f));
            System.out.println(government.findGatherings(10, 4, 4, 0.1f));
            System.out.println(government.findGatherings(10, 4, 4, 0.1f));
            System.out.println(government.findGatherings(10, 5, 5, 0.1f));


            System.out.println(Government.resetDatabase());
        }
        // Catch block to catch all possible thrown exceptions
        catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println(e.getMessage());
        }
    }
}

package Lab0xFF;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Main {


    private static int startAtAlgorithm = 0;
    private static int numberOfAlgorithms = 3;
    private static int StartN = 4;
    private static int maxN = 1000000;
    private static int maxTrials = 1;

    public static void main(String[] args) {


        //length of list to be sorted
        int N;
        int useAlgorithm;
        int trials;

        //for time and doubling ratios
        long timeStampBefore = 0;
        long timeStampAfter = 0;
        long[] AverageTime = new long[numberOfAlgorithms + 1];
        float[] TimeRatio = new float[numberOfAlgorithms + 1];
        long[][] HoldTimes = new long[numberOfAlgorithms + 1][maxN + 1];
        int AverageSlot = 0;

        String[] FinalReturnString = new String[4];
        double[] FinalReturnCost = new double [4];

        double bestCost;

        N = StartN;

        int FirstIteration = 0;






        //This loop will double the overall size of the list until a max value is reached
        while (N <= maxN) {


            useAlgorithm = startAtAlgorithm;


            Graph testGraph = new Graph(N);
            testGraph.GenerateRandomCircularGraphCostMatrix();

            //used to test and print out the graph stuff on each iteration of N
         //   testGraph.PrintCircularListOrder();
            //  testGraph.PrintXYList();
            //testGraph.PrintCostMatrix();

            //This loop will determine which algorithm is being used, then run the number of trials on it
            while (useAlgorithm <= numberOfAlgorithms) {

                trials = 0;


           //     if(useAlgorithm == 0 && N > 11)
             //   break;


                while (trials < maxTrials) {



                    //obtain a single timed trial, UseTheSort function determines which sort will be used
                    timeStampBefore = getCpuTime();
                    FinalReturnString[useAlgorithm] = UseAlgorithm(useAlgorithm, testGraph);
                    timeStampAfter = getCpuTime();

                    FinalReturnCost[useAlgorithm] = testGraph.FinalCost;

                    //Add the time for each trial
                    AverageTime[AverageSlot] = AverageTime[AverageSlot] + (timeStampAfter - timeStampBefore);

                    //used to skip first two algorithms and still pass a result
                  //  AverageTime[0] = 10000000;
                    //AverageTime[1] = 10000000;

                    trials++;
                }

                AverageTime[AverageSlot] = AverageTime[AverageSlot] / trials;


                //go to the next algorithm
                useAlgorithm++;
                AverageSlot++;
            }


            bestCost = FinalReturnCost[0];

            if (N == StartN || N % 10 == 0) {


                //Print a header for the chart
                System.out.println(String.format("\n%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s",
                        "N",
                        "Brute Time",
                        "Doubling Ratios",
                        "Expected Doubling",
                        "Brute Final Path",
                        "Final Cost",
                        "Real Final",
                        "N",
                        "Dynamic Time",
                        "Doubling Ratios",
                        "Expected Doubling",
                        "Dynamic Final Path",
                        "Final Cost",
                        "Real Final",
                        "N",
                        "Greedy",
                        "Doubling Ratios",
                        "Expected Doubling",
                        "Greedy Final Path",
                        "Final Cost",
                        "Best Cost",
                        "Real Final",
                        "N",
                        "Any Colony",
                        "Doubling Ratios",
                        "Expected Doubling",
                        "Ant Colony Final Path",
                        "Final Cost",
                        "Best Cost",
                        "Real Final"));
            }


            //If first iteration
            if (FirstIteration == 0)
            {
                //make it so this if does not get hit again
                FirstIteration++;

                //Print a header for the chart
                System.out.println(String.format("%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s",
                        N,
                        AverageTime[0],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[0]),
                        FinalReturnCost[0],
                        testGraph.GetCircularListOrder(),
                        N,
                        AverageTime[1],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[1]),
                        FinalReturnCost[1],
                        testGraph.GetCircularListOrder(),
                        N,
                        AverageTime[2],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[2]),
                        FinalReturnCost[2],
                        bestCost,
                        testGraph.GetCircularListOrder(),
                        N,
                        AverageTime[3],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[2]),
                        FinalReturnCost[3],
                        bestCost,
                        testGraph.GetCircularListOrder()));

            }
            //else it is even
            else if(N % 2 == 0)
            {
                //if a value has been placed in the needed spot for the doubling ratio
                if(HoldTimes[0][N / 2] != 0) {
                    //Print a header for the chart
                    System.out.println(String.format("%35s%35s%35.2f%35s%35s%35s%50s%35s%35s%35.02f%35s%35s%35s%50s%35s%35s%35.02f%35s%35s%35s%35s%50s%35s%35s%35.02f%35s%35s%35s%35s%50s",
                            N,
                            AverageTime[0],
                            (float) AverageTime[0] / HoldTimes[0][N / 2],
                            Factorial(N-1) / Factorial((N-1)/2),
                            FormatGraphString(FinalReturnString[0]),
                            FinalReturnCost[0],
                            testGraph.GetCircularListOrder(),
                            N,
                            AverageTime[1],
                            (float) AverageTime[1] / HoldTimes[1][N / 2],
                            Math.pow(N, N/2) / Math.pow(N/2, N/4),
                            FormatGraphString(FinalReturnString[1]),
                            FinalReturnCost[1],
                            testGraph.GetCircularListOrder(),
                            N,
                            AverageTime[2],
                            (float) AverageTime[2] / HoldTimes[2][N / 2],
                            N / (N / 2),
                            FormatGraphString(FinalReturnString[2]),
                            FinalReturnCost[2],
                            bestCost,
                            testGraph.GetCircularListOrder(),
                            N,
                            AverageTime[3],
                            (float) AverageTime[3] / HoldTimes[3][N / 2],
                            Math.pow(N, N/2) / Math.pow(N/2, N/4),
                            FormatGraphString(FinalReturnString[3]),
                            FinalReturnCost[3],
                            bestCost,
                            testGraph.GetCircularListOrder()));
                }
                else{
                    //Print a header for the chart
                    System.out.println(String.format("%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s",
                            N,
                            AverageTime[0],
                            "N/A",
                            "N/A",
                            FormatGraphString(FinalReturnString[0]),
                            FinalReturnCost[0],
                            testGraph.GetCircularListOrder(),
                            N,
                            AverageTime[1],
                            "N/A",
                            "N/A",
                            FormatGraphString(FinalReturnString[1]),
                            FinalReturnCost[1],
                            testGraph.GetCircularListOrder(),
                            N,
                            AverageTime[2],
                            "N/A",
                            "N/A",
                            FormatGraphString(FinalReturnString[2]),
                            FinalReturnCost[2],
                            bestCost,
                            testGraph.GetCircularListOrder(),
                            N,
                            AverageTime[3],
                            "N/A",
                            "N/A",
                            FormatGraphString(FinalReturnString[3]),
                            FinalReturnCost[3],
                            bestCost,
                            testGraph.GetCircularListOrder()));
                }

            }
            else
            {
                //Print a header for the chart
                System.out.println(String.format("%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s%35s%35s%35s%35s%35s%35s%35s%50s",
                        N,
                        AverageTime[0],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[0]),
                        FinalReturnCost[0],
                        testGraph.GetCircularListOrder(),
                        N,
                        AverageTime[1],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[1]),
                        FinalReturnCost[1],
                        testGraph.GetCircularListOrder(),
                        N,
                        AverageTime[2],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[2]),
                        FinalReturnCost[2],
                        bestCost,
                        testGraph.GetCircularListOrder(),
                        N,
                        AverageTime[3],
                        "N/A",
                        "N/A",
                        FormatGraphString(FinalReturnString[3]),
                        FinalReturnCost[3],
                        bestCost,
                        testGraph.GetCircularListOrder()));
            }


            //for doubling
            for (int i = 0; i < 4; i++)
                HoldTimes[i][N] = AverageTime[i];


            AverageSlot = 0;
            N++;

        }
    }

    /**
     UseAlgorithm
     FormatGraphString
     Factorial
     getCPUTime
     */


    static String UseAlgorithm(int useAlgorithm, Graph testGraph)
    {

        String returnString = "";

        //make sure starting values are correct
        testGraph.ChosenPath = "";
        testGraph.ChosenCost = testGraph.MAX;
        testGraph.FinalPath = "";
        testGraph.FinalCost = testGraph.MAX;
        testGraph.tmpPath = "";
        testGraph.tmpCost = 0;


        switch (useAlgorithm)
        {
            case 0:
                //used to bypass first alg
                //returnString = "Test";
                returnString = testGraph.RunTSPBruteForce();
                break;
            case 1:
                //used to bypass second alg
                //returnString = "Test";
                returnString = testGraph.RunTSPDynamicProg();
                break;
            case 2:
                returnString = testGraph.RunTSPGreedy();
                break;
            case 3:
                returnString = testGraph.RunAntColony();
                break;
        }

        return returnString;
    }

//format for printing tables
    public static String FormatGraphString(String Input)
    {
        String newString = "";

        // System.out.println("String is " + Input);

        char[] InputArray = Input.toCharArray();

        //if larger then 10, format into a smaller string
        if(Input.length() > 10) {
            for (int i = 0; i < Input.length(); i++) {
                //only put it into the new string if the first or last five digits
                if (i < 5 || i > Input.length() - 6) {
                    //seperate first and last five digits
                    if (Input.length() - 5 == i)
                        newString = newString + " ... ";

                    newString = newString + InputArray[i];
                }
            }
            return newString;
        }
        //else return the orig String
        else
            return Input;
    }

    //calculate the factorial
    public static double Factorial(int number)
    {
        double newValue = 1;

        for(int i = number; i > 0; i--)
            newValue = newValue * i;

        return newValue;

    }

    /** Get CPU time in nanoseconds since the program(thread) started. */
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime()
    {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }


}

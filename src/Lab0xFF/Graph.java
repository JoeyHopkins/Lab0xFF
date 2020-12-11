package Lab0xFF;

import java.text.Format;
import java.util.Random;

public class Graph {


    double[][] CostMatrix;
    double[][] XYList;
    int[] CircularOrder;
    int Size;
    int maxDistance = 100;

    int [] NeedToVisit;

    double[][] DynamicSolutionTable;

    double MAX = 2000000000;
    int initial = 0;

    String ChosenPath;
    double ChosenCost = MAX;
    String FinalPath;
    double FinalCost;
    String tmpPath;
    double tmpCost;

    int GreedyPathObtained = 0;
    double[] LargestSide;
    int MAXGreedyStringSize = 0;

    //used to fill with random values
    private static Random random = new Random();

    //init a new graph constructor
    public Graph(int size)
    {
        CostMatrix = new double[size][size];
        XYList = new double [size][2];
        CircularOrder = new int[size];
        NeedToVisit = new int [size];
        Size = size;
        InitGraph();
        InitXYList();
        CalcGreedyMaxStringSize();
    }

    /**
     * InitGraph
     * InitXYList
     * CalcGreedyMaxStringSize
     * GenerateRandomCostMatrix
     * GenerateRandomEuclideanCostMatrix
     * VerifyFinalCosts
     * GenerateRandomCircularXYCords
     * ResetCircularOrderArray
     * VerifyCostMatrixLegs
     * MakeLegsRandom
     * VerifyCircularLegs
     * CalculateLegDistance
     * GetCircularListOrder
     * RunTSPBruteForce
     * BruteForce
     * RunTSPDynamicProg
     * DynamicProgramming
     * ConvertTourNodesToBinaryNumber
     * RunTSPGreedy
     * greedy
     * getMaxSideSize
     *GetNeedToVisitList
     * CopyFrom
     * NormalCopyFrom
     * ClearNeedToVisitList
     * CheckIfAllVisited
     * RunAntColony
     * AntColony
     * resetDoubleMatrixToZeros
     * resetMatrixToZeros
     * attraction
     * PrintCostMatrix
     * PrintXYList
     * PrintSolutionTable
     * PrintCircularListOrder
     **/

    //initialize the graph to all -1
    public void InitGraph()
    {

        for (int j = 0; j < Size; j++)
            for (int k = 0; k < Size; k++)
                CostMatrix[j][k] = -1;
    }

    //create a random XY for each vertex
    public void InitXYList()
    {
        for(int i = 0; i < Size; i++)
        {
            XYList[i][0] = random.nextInt(maxDistance);
            XYList[i][1] = random.nextInt(maxDistance);
        }
    }

    public void CalcGreedyMaxStringSize()
    {
        for(int i = 0; i < Size; i++)
            MAXGreedyStringSize = MAXGreedyStringSize + String.valueOf(i).length();
    }

    public void GenerateRandomCostMatrix()
    {
        GenerateRandomCostMatrixLegs();
        VerifyCostMatrixLegs();
        MakeLegsRandom();
    }

    public void GenerateRandomEuclideanCostMatrix()
    {
        GenerateRandomCostMatrixLegs();
        VerifyCostMatrixLegs();
        CalculateLegDistance();
    }

    public void GenerateRandomCircularGraphCostMatrix()
    {
        //set up the legs to have a random connections
        GenerateRandomCostMatrixLegs();
        VerifyCostMatrixLegs();

        //Create Circular XY Cords and put them in the list
        ResetCircularOrderArray();
        GenerateRandomCircularXYCords();

        //Make sure that the legs of the circle are connected
        VerifyCircularLegs();


        //calculate the distance between the vertex
        CalculateLegDistance();

    }

    public void VerifyFinalCosts()
    {
        for(int i = 0; i < Size; i++)
            for(int j = 0; j < Size; j++)
                if(CostMatrix[i][j] == 0 || CostMatrix[i][j] == 1)
                    CostMatrix[i][j] = CostMatrix[i][j] + 2;

    }

    //Generate a new set of XY cords to put all the nodes randomly into a circle
    public void GenerateRandomCircularXYCords()
    {
        double stepAngle = (2 * Math.PI) / Size;
        int stepCount = 0;

        int radius = 50;

        int randomChoice = 0;

        for(int i = 0; i < Size; i++)
        {
            randomChoice = random.nextInt(Size);

            //go to the next slot or the beginning if at the end of the list
            while(CircularOrder[randomChoice] != -1)
            {
                randomChoice++;

                if(randomChoice == Size)
                    randomChoice = 0;
            }


            XYList[randomChoice][0] =(int) (radius * Math.sin(i * stepAngle));
            XYList[randomChoice][1] =(int) (radius * Math.cos(i * stepAngle));

            //set the number in the list to i
            CircularOrder[randomChoice] = i;

        }
    }

    //set all the values in the CircularOrder array to -1
    public void ResetCircularOrderArray()
    {
        for(int i = 0; i < Size; i++)
            CircularOrder[i] = -1;
    }

    //determine which legs are connected based on a random chance
    public void GenerateRandomCostMatrixLegs()
    {

        int isLeg;
        int i, j;

        for (i = 0; i < Size; i++)
        {
            for (j = 0; j < Size; j++)
            {

                //will either be a 0 or a 1
                isLeg = random.nextInt(100) % 10;

                //leave main diagonal as is and go to the next row
                if (i == j)
                    break;
                else if (isLeg < 8)
                {
                    CostMatrix[i][j] = 1;
                    CostMatrix[j][i] = CostMatrix[i][j];
                }
            }
        }
    }

    //if every vertex does not have at least 2 legs, add more to it
    public void VerifyCostMatrixLegs()
    {
        int i = 0;
        int count;
        int randomIndex;

        while (i < Size)
        {
            count = 0;

            //count the number of edges each vertex has
            for(int j = 0; j < Size; j++)
                if(CostMatrix[i][j] != -1)
                    count++;

            //if vertex has less than 2 edges
            while(count < 2)
            {
                //get a random index
                randomIndex = random.nextInt(Size);

                //while random index is not set on a changeable value, go to the next index
                while(CostMatrix[i][randomIndex] != -1 || i == randomIndex)
                {

                    //if at the end if the Matrix, go to the beginning
                    if(randomIndex + 1 == Size)
                        randomIndex = 0;
                        //else go to the next index
                    else
                        randomIndex++;

                }


                //set value to a 1 to represent a leg is now connecting the current nodes
                CostMatrix[i][randomIndex] = 1;
                CostMatrix[randomIndex][i] = 1;


                count++;
            }

            i++;
        }
    }

    //make the legs in the graph have random distances
    public void MakeLegsRandom()
    {
        for (int i = 0; i < Size; i++)
            for (int j = 0; j < Size; j++)
            {
                if (i == j)
                    break;
                if (CostMatrix[i][j] == 1)
                {
                    CostMatrix[i][j] = random.nextInt(maxDistance) + 1;
                    CostMatrix[j][i] = CostMatrix[i][j];
                }
            }
    }

    //make sure the legs that connect around the circle are connected
    public void VerifyCircularLegs()
    {
        int counter = 0;
        int listCounter;
        int holdCounter;
        int numberConnected = 0;

        while (numberConnected <= Size)
        {
            listCounter = CircularOrder[counter];

            //increment the listCounter by one or go to the beginning if it contains the last index on the list
            if(listCounter + 1 != Size)
                listCounter++;
            else
                listCounter = 0;


            holdCounter = 0;

            //find the location of the next number and put the index in holdCounter
            while(CircularOrder[holdCounter] != listCounter)
                holdCounter++;


            //connect both of the obtained vertex
            CostMatrix[counter][holdCounter] = 1;
            CostMatrix[holdCounter][counter] = 1;


            //set to do the next node in next iteration
            counter = holdCounter;

            numberConnected++;
        }
    }

    //calculates the distance between the nodes based on the XY cords
    public void CalculateLegDistance()
    {
        for (int i = 0; i < Size; i++)
            for (int j = 0; j < Size; j++)
            {
                if (i == j)
                    break;
                if (CostMatrix[i][j] == 1)
                {
                    CostMatrix[i][j] = Math.pow((Math.abs(XYList[i][0] - XYList[j][0])), 2) + Math.pow((Math.abs(XYList[i][1] - XYList[j][1])), 2);
                    CostMatrix[i][j] = Math.sqrt(CostMatrix[i][j]);
                    CostMatrix[j][i] = CostMatrix[i][j];
                }
            }
    }

    //return circular list in bracket form. ie {0, 1, 2, 3, 0}
    public String GetCircularListOrder()
    {
        String result = "{0";
        int i = 0;
        int holdIndex = 0;
        int holdValue;

        //System.out.println(Size);

        //if size is less than 10
        if(Size > 1) {
            while (i < Size) {
                holdValue = CircularOrder[holdIndex];

                //increment hold value, or revert it to the beginning of the list if it contains the last index
                if (holdValue + 1 != Size)
                    holdValue++;
                else
                    holdValue = 0;


                //find the index of the needed value
                holdIndex = 0;
                while (holdValue != CircularOrder[holdIndex])
                    holdIndex++;


                if(i < 5 || i > Size - 5) {
                    //add obtained value to the string
                    result = result + ", " + holdIndex;
                }

                if (i == Size - 6 && Size > 9)
                    result = result + ", ...";


                i++;
            }
        }

        //add ending brackets to string
        result = result + "}";

        return result;
    }

    //init values for TSP and call the recursive Brute Force call
    public String RunTSPBruteForce()
    {
        int StartNode = 0;
        int EndNode = 0;


        //set up list of tour nodes
        int [] TourNodes = new int [Size - 1];
        for(int i = 1; i < Size; i++)
            TourNodes[i-1] = i;



        BruteForce(StartNode, EndNode, TourNodes);

        FinalPath = ChosenPath;
        FinalCost = ChosenCost;


        return FinalPath;
    }

    //Run the BruteForce Recursive Algorithm
    public double BruteForce(int StartNode, int EndNode, int [] TourNodes)
    {

        int newTourNodesIndex = 0;
        int[] newTourNodes = new int[TourNodes.length - 1];
        double returnCost;

        double currentCost = 0;
        String currentPath = "";


        //If only one node is in the list, build cost and path off single node
        if (TourNodes.length ==  1)
        {

            tmpPath = String.valueOf(StartNode);
            tmpPath = tmpPath + TourNodes[0];
            tmpPath = tmpPath + EndNode;

            //if a path exists
            if(CostMatrix[StartNode][TourNodes[0]] != -1 && CostMatrix[TourNodes[0]][EndNode] != -1)
                tmpCost = CostMatrix[StartNode][TourNodes[0]] + CostMatrix[TourNodes[0]][EndNode];
                //else mark as path does not exist
            else
                tmpCost = -1;


            return tmpCost;
        }
        //else
        else
        {

            double currentLowCost = MAX;

            //for each node in tour nodes
            for(int k = 0; k < TourNodes.length; k++)
            {

                //make a list without the current node
                newTourNodesIndex = 0;
                for(int i = 0; i < TourNodes.length; i++)
                {
                    if(TourNodes[k] != TourNodes[i])
                    {
                        newTourNodes[newTourNodesIndex] = TourNodes[i];
                        newTourNodesIndex++;
                    }
                }


                if(CostMatrix[StartNode][TourNodes[k]] > 0) {


                    //call recursive function and carry the cost
                    returnCost = BruteForce(TourNodes[k], EndNode, newTourNodes);


                    //If beginning of the list iterations
                    if (newTourNodesIndex == 1 && tmpCost != -1 && CostMatrix[StartNode][TourNodes[k]] != -1) {
                        //get the cost
                        currentCost = tmpCost + CostMatrix[StartNode][TourNodes[k]];


                        //if smallest cost, set as current
                        if (currentCost < currentLowCost)
                        {
                            currentLowCost = currentCost;
                            currentPath = StartNode + tmpPath;
                        }
                    }
                    //for when the size of TourNodes > 1
                    else if (newTourNodesIndex > 1 && CostMatrix[StartNode][TourNodes[k]] != -1)
                    {
                        //get the cost
                        currentCost = returnCost + CostMatrix[StartNode][TourNodes[k]];


                        //if smallest cost, set as current
                        if (currentCost < currentLowCost)
                        {
                            currentLowCost = currentCost;
                            currentPath = StartNode + ChosenPath;
                        }
                    }


                    //if the current path is the smallest path, set it as the chosenPath
                    if (currentLowCost != MAX) {
                        if (currentPath.length() > ChosenPath.length() || currentLowCost < ChosenCost) {
                            ChosenCost = currentLowCost;
                            ChosenPath = currentPath;
                        }
                    }
                }

            }

            //return the cost
            return ChosenCost;
        }
    }

    //set up and run the recursive Dynamic Algorithm
    public String RunTSPDynamicProg()
    {

        int StartNode = 0;
        int EndNode = 0;

        //set up list of tour nodes
        int [] TourNodes = new int [Size - 1];
        for(int i = 1; i < Size; i++)
            TourNodes[i-1] = i;


        //set up the solution table
        DynamicSolutionTable = new double[(int)Math.pow(2, Size - 1)][Size];


        DynamicProgramming(StartNode, EndNode, TourNodes);

        FinalPath = ChosenPath;
        FinalCost = ChosenCost;

        // PrintSolutionTable();

        return FinalPath;
    }

    //this is the recursive one
    public double DynamicProgramming(int StartNode, int EndNode, int [] TourNodes)
    {
        int newTourNodesIndex = 0;
        int[] newTourNodes = new int[TourNodes.length - 1];
        double returnCost;

        double currentCost = 0;
        String currentPath = "";

        int falseReturn = 0;

        int TourNodesConvertedNumber = ConvertTourNodesToBinaryNumber(TourNodes);


        //If the solution table contains a solution, return the solution in the table
        if(DynamicSolutionTable[TourNodesConvertedNumber][StartNode] != 0) {

            tmpPath = String.valueOf(StartNode);

            for(int i = 0; i < TourNodes.length; i++)
                tmpPath = tmpPath + TourNodes[i];

            tmpPath = tmpPath + EndNode;

            ChosenPath = tmpPath;

            return DynamicSolutionTable[TourNodesConvertedNumber][StartNode];
        }
        else {
            //If only one node is in the list, build cost and path off single node
            if (TourNodes.length ==  1)
            {

                tmpPath = String.valueOf(StartNode);
                tmpPath = tmpPath + TourNodes[0];
                tmpPath = tmpPath + EndNode;


                //if a path exists, store it in the table
                if(CostMatrix[StartNode][TourNodes[0]] != -1 && CostMatrix[TourNodes[0]][EndNode] != -1)
                    DynamicSolutionTable[TourNodesConvertedNumber][StartNode] = CostMatrix[StartNode][TourNodes[0]] + CostMatrix[TourNodes[0]][EndNode];
                    //else mark as path does not exist
                else
                    DynamicSolutionTable[TourNodesConvertedNumber][StartNode] = 0;


                ChosenPath = tmpPath;

                return DynamicSolutionTable[TourNodesConvertedNumber][StartNode];
            }
            //else the list has multiple nodes
            else
            {

                double currentLowCost = MAX;

                //for each node in tour nodes
                for(int k = 0; k < TourNodes.length; k++) {

                    //make a list without the current node
                    newTourNodesIndex = 0;
                    for (int i = 0; i < TourNodes.length; i++) {
                        if (TourNodes[k] != TourNodes[i]) {
                            newTourNodes[newTourNodesIndex] = TourNodes[i];
                            newTourNodesIndex++;
                        }
                    }



                    if(CostMatrix[StartNode][TourNodes[k]] > 0) {


                        //call recursive function and carry the cost
                        returnCost = DynamicProgramming(TourNodes[k], EndNode, newTourNodes);

                        //if the path exists and a path exists from StartNode to TourNodes
                        if (returnCost != 0 && CostMatrix[StartNode][TourNodes[k]] != -1) {

                            currentCost = returnCost + CostMatrix[StartNode][TourNodes[k]];

                            //if smallest cost, set as current
                            if (currentCost < currentLowCost) {
                                currentLowCost = currentCost;
                                currentPath = StartNode + ChosenPath;
                            }

                        }


                        //if the current path is the smallest path, set it as the chosenPath
                        if (currentLowCost != MAX) {
                            if (currentLowCost < ChosenCost || currentPath.length() > ChosenPath.length()) {
                                ChosenCost = currentLowCost;
                                ChosenPath = currentPath;
                            }
                        }

                    }

                }


                return  ChosenCost;

            }

        }

    }

    //Get the decimal representation of a given list of TourNodes
    public int ConvertTourNodesToBinaryNumber(int [] TourNodes)
    {
        int[] Binary = new int[Size];
        int returnValue = 0;


        //make an array of binary numbers
        for(int i = 0; i < TourNodes.length; i++)
            Binary[TourNodes[i]] = 1;


        //convert this to decimal
        for(int i = 0; i < Binary.length; i++)
            if(Binary[i] == 1)
                returnValue = (int) (returnValue + Math.pow(2, i - 1));

/*
        System.out.println("TESTED ARRAYS: Tour Nodes");

        for(int i = 0; i < TourNodes.length; i++)
            System.out.println(i + ": " + TourNodes[i] + " ");


        System.out.println("TESTED ARRAYS: Binary");
        for(int i = 0; i < Binary.length; i++)
            System.out.println(i + ": " + Binary[i] + " ");

        System.out.println("Return Value " + returnValue);
 */


        return returnValue;
    }

    //Set up the Greedy Algorithm
    public String RunTSPGreedy()
    {

        int StartNode = 0;
        int currentNode = 0;
        int EndNode = 0;
        double myCost = 0;
        String  newPath = "";

        GreedyPathObtained = 0;

        int [] visited = new int [Size];


        getMaxSideSize();


        greedy(StartNode, currentNode, EndNode, visited, newPath, myCost);

        FinalPath = ChosenPath;
        FinalCost = ChosenCost;

        return FinalPath;
    }

    //run the greedy algorithm
    public int[] greedy(int StartNode, int CurrentNode, int EndNode, int[] VisitedNodes, String CurrentPath, double CurrentCost)
    {

        int [] LocalNeedToVisit;

        double NeedLargerThan = 0;
        int Index = 0;

        double MyCost;
        double holdValue = 0;

        String MyPath = CurrentPath + CurrentNode;


        //set the current node to visited
        VisitedNodes[CurrentNode] = 1;


        //check if all nodes have been visited yet
        int haveAllBeenVisited = MyPath.length();

        //If all Nodes have been visited
        if(haveAllBeenVisited == MAXGreedyStringSize)
        {
            //if a path exists from the current node to the EndNode
            if(CostMatrix[CurrentNode][EndNode] != -1)
            {
                GreedyPathObtained = 1;
                tmpCost = 0;
                ChosenCost = CostMatrix[CurrentNode][EndNode] + CurrentCost;
                ChosenPath = MyPath + EndNode;
            }
            //else a path does not exist from the current node to the final node
            else
            {
                //Mark as not visited and return the list
                tmpCost = -1;
                VisitedNodes[CurrentNode] = 0;
                return VisitedNodes;
            }


        }
        //else not all have been visited
        else
        {

            //Clear then get the list of nodes with the lowest distance from the current Node
            ClearNeedToVisitList();
            NeedLargerThan = GetNeedToVisitList(CurrentNode, NeedLargerThan, VisitedNodes);
            LocalNeedToVisit = CopyFrom(NeedToVisit);


            //if there is a node that can be reached
            if(LocalNeedToVisit.length > 0) {

                while (Index < LocalNeedToVisit.length) {

                    //check if the next one that needs to be visited has already been visited or not
                    //if it has, increment the index
                    if(VisitedNodes[LocalNeedToVisit[Index]] == 1)
                        Index++;
                    //if nothing is left in the list, break out of the loop
                    if(Index >= LocalNeedToVisit.length)
                        break;

                    //get cost of current + next before sending it to the next node
                    MyCost = CurrentCost + CostMatrix[CurrentNode][LocalNeedToVisit[Index]];

                    if(LocalNeedToVisit[Index] != holdValue)
                    {
                        holdValue = LocalNeedToVisit[Index];
                        greedy(StartNode, LocalNeedToVisit[Index], EndNode, VisitedNodes, MyPath, MyCost);
                    }
                    else
                        LocalNeedToVisit[Index] = 0;


                    Index++;

                    if(tmpCost == -1)
                    {

                        //while NeedLargerThen is smaller then the largest value for currentNode,
                        while(NeedLargerThan < LargestSide[CurrentNode])
                        {
                            //get a new list
                            ClearNeedToVisitList();
                            NeedLargerThan = GetNeedToVisitList(CurrentNode, NeedLargerThan, VisitedNodes);
                            LocalNeedToVisit = CopyFrom(NeedToVisit);

                            //if list size is larger than 0, get out of the current loop
                            if(LocalNeedToVisit.length != 0) {
                                tmpCost = 0;
                                Index = 0;
                                break;
                            }
                        }


                        //if the list cannot be remade, return -1
                        if(NeedLargerThan >= LargestSide[CurrentNode] && Index != 0)
                        {

                            //If no path exists return No Path Exists
                            if(CurrentNode == StartNode)
                            {
                                ChosenPath = " < No Path Exists!!! >";
                                ChosenCost = -1;
                            }

                            //Mark as not visited and return the list
                            tmpCost = -1;
                            VisitedNodes[CurrentNode] = 0;
                            return VisitedNodes;
                        }

                    }
                }
            }
            //else there are no more nodes to keep going
            else
            {
                VisitedNodes[CurrentNode] = 0;
                tmpCost = -1;
                return VisitedNodes;
            }

        }

        return  VisitedNodes;

    }

    //get the maximum reachable side length for each node
    public void getMaxSideSize()
    {
        LargestSide = new double[Size];

        for(int i = 0; i < Size; i++)
            for(int j = 0; j < Size; j++)
                if(CostMatrix[i][j] > LargestSide[i])
                    LargestSide[i] = CostMatrix[i][j];

    }

    //get and return a list of numbers from the current node that are larger then the givin input NeedLargerThan, and pass NeedLargerThanBack
    public double GetNeedToVisitList(int CurrentNode, double NeedLargerThan, int[] VisitedNodes)
    {
        double lowest = MAX;
        NeedToVisit = new int [Size];
        int NeedToVisitListIndex = 0;

        //get lowest cost of a leg that exits the current node && is the lowest current node still available
        for(int i = 0; i < VisitedNodes.length; i++)
            if(VisitedNodes[i] == 0 && CostMatrix[CurrentNode][i] < lowest && i != CurrentNode)
                if(CostMatrix[CurrentNode][i] != -1 && CostMatrix[CurrentNode][i] > NeedLargerThan)
                    lowest = CostMatrix[CurrentNode][i];

        //fill the need to visit list
        for(int i = 0; i < CostMatrix.length; i++)
            if(CostMatrix[CurrentNode][i] == lowest && VisitedNodes[i] == 0)
            {
                NeedToVisit[NeedToVisitListIndex] = i;
                NeedToVisitListIndex++;
            }


        return lowest;
    }

    //return a copy of the input array into another array
    public int [] CopyFrom(int [] Orig)
    {
        int sizeCounter = 0;

        for(int i = 0; i < Orig.length; i++)
            if(Orig[i] != 0)
                sizeCounter++;


        int [] copy = new int [sizeCounter];



        for(int i = 0; i < copy.length; i++)
            copy[i] = Orig[i];

        return copy;

    }

    //return a copy of the input array into another array
    public int [] NormalCopyFrom(int [] Orig)
    {
        int [] copy = new int [Size];

        for(int i = 0; i < copy.length; i++)
            copy[i] = Orig[i];

        return copy;
    }

    //Set all values on the list to empty
    public void ClearNeedToVisitList()
    {
        for(int i = 0; i < NeedToVisit.length; i++)
            NeedToVisit[i] = 0;
    }

    //return 1 if all have been visited or 0 if there are some still not visited
    public int CheckIfAllVisited(int [] VisitedNodes)
    {
        int visited = 1;


        for(int i = 0; i < VisitedNodes.length; i++)
            if (VisitedNodes[i] == 0)
                visited = 0;

        return visited;
    }

    //prepare the ant colony algorithm
    public String RunAntColony()
    {
        int numberOfAnts = 3;
        int PheromoneFactor = 5;
        double DecayFactor = .9;
        int maxUnchangedTimeSteps = 1000;
        int homeNode = 0;

        AnyColony(Size, CostMatrix, numberOfAnts, PheromoneFactor, DecayFactor, maxUnchangedTimeSteps,homeNode);

        return FinalPath;
    }

    //run the  ant colony algorithm
    public void AnyColony(int N, double[][] Dist, int M, int PheromoneFactor, double DecayFactor, int maxUnchangedTimeSteps, int homeNode)
    {

        double [][] Phero = new double [Size][Size];
        double [][] NewPhero = new double [Size][Size];
        int [] VisitedNodes = new int [Size];
        char[] FinalChars = new char[Size];

        //keep track of current ant path
        int [] Path = new int [Size];

        //step number
        int step = 0;

        //current node
        int k = 0;

        //potential next node
        int h = 0;

        //cost of path
        double pathCost = 0;

        //random number used to select each edge
        double Q;

        //total attraction of all remaining available edges for next step
        double totalA = 0;

        //cumulative selection probability of edges considered for next step
        double cumProb;

        double edgeSelectionProbablity;

        double minPathCostSoFar = MAX;

        //store the best array taken to date by any ant
        int [] minPath = new int [Size];

        String MinString = "";

        //keep track of the time steps
        int currentTimeStep = 0;

        int numberOfConsecutiveUnchanges = 0;

        int BreakoutCounter = 0;
        int BreakoutCounter2 = 0;




        while(numberOfConsecutiveUnchanges < maxUnchangedTimeSteps)
        {
            //reset NewPhero matrix to 0's
            resetDoubleMatrixToZeros(NewPhero);

            //for each Ant
            for(int CurrentAnt = 0; CurrentAnt < M; CurrentAnt++)
            {

                //reset Path cost
                pathCost = 0;

                //reset all nodes to Not visited
                resetMatrixToZeros(VisitedNodes);


                //Start back at the home node and set it as visited
                Path[0] = homeNode;
                VisitedNodes[homeNode] = 1;



                //for each step
                for(step = 1; step < Size; step++)
                {
                    //set k to our current node
                    k = Path[step - 1];

                    //reset the total Attraction
                    totalA = 0;


                    //for each potential step,
                    // if the node has not been visited, the path exists
                    // calculate the total attraction
                    for(h = 0; h < Size; h++)
                        if(VisitedNodes[h] == 0 && Dist[k][h] != -1 && Dist [k][h] != 0) {
                            totalA = totalA + attraction(k, h, Phero, Dist);
                            BreakoutCounter = 0;
                        }
                        else
                            BreakoutCounter++;


                    //break out if path does not exist
                    if(BreakoutCounter >= Size)
                    {
                        BreakoutCounter2 = 1;
                        BreakoutCounter = 0;
                        break;
                    }


                    //set Q equal to a random number between 0 and 1
                    Q = random.nextInt(10000);
                    Q = Q / 10000;


                    //reset the cumulative Probability
                    cumProb = 0;


                    //for each possible choice, determine the chosen Node
                    for(h = 0; h < Size; h++)
                    {
                        //if the potential node has not yet ben visited and there exists a path
                        if(VisitedNodes[h] != 1 && Dist[k][h] != -1)
                        {
                            //  iyoiuyiuiu
                            //determine the current potential probability
                            edgeSelectionProbablity = attraction(h, k, Phero, Dist) / totalA;


                            //add it to the current cumulative probability
                            cumProb = cumProb + edgeSelectionProbablity;

                            //if Q lies in the current cumProbability, break out and h will be the chosen potential node
                            if(Q < cumProb)
                                break;
                        }
                    }


                    //set the next node to visit equal to the chosen node, set it to visited, and calculate the path cost
                    Path[step] = h;
                    VisitedNodes[h] = 1;
                    pathCost = pathCost + Dist[k][h];

                }


                if(BreakoutCounter2 == 1) {
                    BreakoutCounter2 = 0;
                    break;
                }


                //if a path exists from the last node to the home node, add the path cost back to the home node
                if(Dist[h][homeNode] != -1)
                    pathCost =  pathCost + Dist[h][homeNode];
                    //else set the pathCost to max since path does not exist
                else
                    pathCost = MAX;


                //if path has the best cost so far, or if its the very first TimeStep and the first ant
                if((pathCost < minPathCostSoFar || (CurrentAnt == 0 && currentTimeStep == 0)) && pathCost != MAX)
                {
                    minPathCostSoFar = pathCost;
                    minPath = NormalCopyFrom(Path);

                    numberOfConsecutiveUnchanges = 0;

                }
                //else increment the number of times that the minimum path has not been changed
                else
                    numberOfConsecutiveUnchanges++;



                //if a path exists
                if(pathCost != MAX) {
                    //The current ant lays down pheromones
                    for (step = 0; step < Size; step++) {
                        //set the current node
                        k = Path[step];

                        //set the next node, mod makes it return back to 0 at the end
                        h = Path[(step + 1) % Size];

                        //lay down new pheromone
                        NewPhero[k][h] = NewPhero[k][h] + PheromoneFactor / pathCost;
                    }
                }

            }//close CurrentAnt working loop

            //update the pheromones for the next step
            for(k = 0; k < Size; k++)
            {
                for (h = 0; h < Size; h++)
                {
                    //decay some of the old pheromones
                    Phero[k][h] = Phero[k][h] * DecayFactor;

                    //add new pheromones to the old pheromones
                    Phero[k][h] = Phero[k][h] + NewPhero[k][h];

                    //make both side of the array the same in terms of pheromone
                    Phero[h][k] = Phero[k][h];
                }
            }

            currentTimeStep++;
        }

        //build up FinalPath and Set Final Cost
        for(int j = 0; j < minPath.length; j++)
            FinalPath = FinalPath + minPath[j];
        FinalPath = FinalPath + homeNode;

        FinalCost = minPathCostSoFar;
    }

    public void resetDoubleMatrixToZeros(double Matrix[][])
    {
        for(int i = 0; i < Matrix.length; i++)
            for(int j = 0; j < Matrix.length; j++)
                Matrix[i][j] = 0;
    }

    public void resetMatrixToZeros(int Matrix[])
    {
        for(int i = 0; i < Matrix.length; i++)
            Matrix[i] = 0;
    }

    //calculate the attraction of the paths for the ants
    public double attraction(int h, int k, double [][] Phero, double [][] Dist)
    {
        return (1 + Phero[k][h]) / Dist[k][h];
    }

    public void PrintCostMatrix()
    {

        System.out.print(String.format("\n%10s", ""));

        for(int k = 0; k < Size; k++)
            System.out.print(String.format("%9s", k + ":"));

        System.out.println();


        for (int i = 0; i < Size; i++)
        {
            System.out.print(String.format("%9s", i + ":"));

            for (int j = 0; j < Size; j++) {
                if (CostMatrix[i][j] != -1)
                    System.out.print(String.format("%9.2f", CostMatrix[i][j]));

                else
                    System.out.print(String.format("%9s", ""));
            }
            System.out.println();
        }
    }

    public void PrintXYList()
    {
        for(int i = 0; i < Size; i++)
        {
            System.out.print(i + ": " + XYList[i][0] + " ");
            System.out.print(XYList[i][1]);
            System.out.println();
        }
    }

    public void PrintSolutionTable()
    {

        for(int i = 0; i < DynamicSolutionTable.length; i++) {

            System.out.print(i + ": ");

            for (int j = 0; j < DynamicSolutionTable[0].length; j++)
            {
                System.out.print(DynamicSolutionTable[i][j] + " ");
            }
            System.out.println();

        }
    }

    public void PrintCircularListOrder()
    {
        System.out.println();

        for(int i = 0; i < CircularOrder.length; i++)
        {
            System.out.print(CircularOrder[i] + ", ");
        }

        System.out.println();

    }

}

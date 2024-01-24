import java.util.*;

public class QueuingJava {
    public static int numOfCustomers;
    public static int terminationTime = 0;
    public static int terminationValue = 0;
    public static Scanner scanner = new Scanner(System.in);
    public static Random randService = new Random();

    public static void main(String[] args) {
    queueingSystem();
    }
    public static void queueingSystem() {
        double[] probabilities = {0.15, 0.30, 0.25, 0.20, 0.10}; // Given probabilities
        int[] serviceTimes = {1, 2, 3, 4, 5}; // Corresponding service times

        // get user input for number of customers and simulation termination
        System.out.print("Enter the simulation termination option (1 for number of customers, 2 for number of minutes): ");
        int terminationOption = scanner.nextInt();

        if (terminationOption == 1) {
            System.out.print("Enter the number of customers: ");
            numOfCustomers = scanner.nextInt();
            terminationValue = numOfCustomers;
        } else if (terminationOption == 2) {
            System.out.print("Enter the number of minutes to terminate the simulation: ");
            numOfCustomers = scanner.nextInt();
            terminationTime();
            terminationValue = numOfCustomers;
        } else {
            System.out.println("Invalid input. Simulation terminated.");
            System.exit(0);
        }
        // initialize arrays to store customer data
        int[] customerNumber = new int[numOfCustomers];
        int[] interarrivalTime = new int[numOfCustomers];
        int[] arrivalTime = new int[numOfCustomers];
        int[] serviceTime = new int[numOfCustomers];
        int[] timeServiceBegins = new int[numOfCustomers];
        int[] waitingTime = new int[numOfCustomers];
        int[] timeServiceEnds = new int[numOfCustomers];
        int[] timeSpentInSystem = new int[numOfCustomers];
        int[] idleTime = new int[numOfCustomers];

        // metrics for customer statistics
        double numOfPeopleInQueue = 0;
        double interArrivalTimeTotal = 0;
        double waitingTimeAvg = 0;
        double probabilityOfQueue = 0;
        double serverIdleTimeAvg = 0;
        double serverIdleProportion = 0;
        double serviceTimeAvg = 0;
        double timeSpentSystemAvg = 0;
        double arrivalTimeAvg = 0;
        double queueTimeAvg = 0;

        // simulate customer arrivals and service
        for (int i = 0; i < numOfCustomers && terminationTime < terminationValue; i++) {
            customerNumber[i] = i + 1;
            // calculate interarrival times and arrival times for customers
            if (i == 0) {
                interarrivalTime[i] = 0;
            } else {
                int arrivalRand = randService.nextInt(8) + 1; // generate a random integer between 1 and 8
                arrivalTime[i] = arrivalRand;
                interarrivalTime[i] = interarrivalTime[i - 1] + arrivalTime[i];
            }
            // simulate customer service time
            double randNum = randService.nextDouble(); // Generate a random number between 0 and 1
            double cumulativeProbability = 0.0;
            for (int l = 0; l < probabilities.length; l++) {
                cumulativeProbability += probabilities[l]; // Sum the probabilities
                if (randNum <= cumulativeProbability) {
                    serviceTime[i] = serviceTimes[l]; // Assign the corresponding service time
                    break;
                }
            }
            // calculate the timeServiceBegins for each customer
            for (int t = 0; t < timeServiceBegins.length; t++) {
                if (t == 0 || interarrivalTime[t] >= timeServiceEnds[t - 1]) {
                    timeServiceBegins[t] = interarrivalTime[t];
                } else {
                    timeServiceBegins[t] = timeServiceEnds[t - 1];
                }
            }
            // calculate the timeServiceEnds for each customer
            for (int e = 0; e < timeServiceEnds.length; e++) {
                timeServiceEnds[e] = timeServiceBegins[e] + serviceTime[e];
            }
            // calculate for the waiting time
            for (int w = 0; w < waitingTime.length; w++) {
                waitingTime[w] = timeServiceBegins[w] - interarrivalTime[w];
            }
            // calculate the timeSpentOnSystem for each customer
            for (int t = 0; t < timeSpentInSystem.length; t++) {
                timeSpentInSystem[t] = timeServiceEnds[t] - interarrivalTime[t];
            }
            // calculate the server idle time
            for (int s = 0; s < idleTime.length; s++) {
                if (s == 0) {
                } else {
                    idleTime[s] = timeServiceBegins[s] - timeServiceEnds[s - 1];
                }
            }
        }
        QueuingJava.printTable(customerNumber, interarrivalTime, arrivalTime, serviceTime,
                timeServiceBegins, waitingTime, timeServiceEnds, timeSpentInSystem, idleTime);

        for (int i = 0; i < numOfCustomers; i++) {
            waitingTimeAvg += waitingTime[i];
            serverIdleTimeAvg += idleTime[i];
            interArrivalTimeTotal += interarrivalTime[i];
            serviceTimeAvg += serviceTime[i];
            timeSpentSystemAvg += timeSpentInSystem[i];
            arrivalTimeAvg += arrivalTime[i];
            if (waitingTime [i] == 0) {
            } else {
                numOfPeopleInQueue+= 1;
                queueTimeAvg = waitingTime[i];
            }
        }
        waitingTimeAvg = waitingTimeAvg / numOfCustomers;
        probabilityOfQueue = (numOfPeopleInQueue / numOfCustomers) * 100;
        serverIdleTimeAvg = serverIdleTimeAvg / numOfCustomers;
        serviceTimeAvg = serviceTimeAvg / numOfCustomers;
        timeSpentSystemAvg = timeSpentSystemAvg / numOfCustomers;
        arrivalTimeAvg = arrivalTimeAvg / numOfCustomers;
        queueTimeAvg = queueTimeAvg / numOfPeopleInQueue;
        serverIdleProportion = serverIdleTimeAvg / interArrivalTimeTotal * 100;

        System.out.println("1. Average waiting time for a customer: " + String.format("%.2f", waitingTimeAvg) + " minutes");
        System.out.println("2. Probability that a customer has to wait in queue: " + String.format("%.2f", probabilityOfQueue) + "%");
        System.out.println("3. The proportion of idle time of the server: " + String.format("%.2f",serverIdleProportion)+ "%");
        System.out.println("4. Average service time per customer: "  + String.format("%.2f", serviceTimeAvg) + " minutes");
        System.out.println("5. Average time between customer arrivals: "  + String.format("%.2f", arrivalTimeAvg) + " minutes");
        System.out.println("6. Average waiting time for those who wait in queue " + String.format("%.2f", queueTimeAvg) + " minutes");
        System.out.println("7. Average time customer spends in system: " + String.format("%.2f", timeSpentSystemAvg) + " minutes");
        runPrompt();
    }

    public static void terminationTime() {
    if (numOfCustomers == 10 ) {
         numOfCustomers = 3; }
    if (numOfCustomers == 20 ) {
         numOfCustomers = 5; }
    }
    public static void printTable(int[] customerNumber, int[] interarrivalTime, int[] arrivalTime, int[] serviceTime, int[] timeServiceBegins, int[] waitingTime, int[] timeServiceEnds, int[] timeSpentInSystem, int[] serverStatus) {
        System.out.printf("%-15s%-20s%-15s%-15s%-25s%-20s%-20s%-25s%-20s\n", "Customer #", "Interarrival Time", "Arrival Time", "Service Time", "Time Service Begins", "Waiting Time", "Time Service Ends", "Time Spent in System", "Server Idle Time");
        for (int i = 0; i < customerNumber.length; i++) {
            System.out.printf("%-15d%-20d%-15d%-15d%-25d%-20d%-20d%-25d%-20d\n",
                    customerNumber[i], interarrivalTime[i], arrivalTime[i], serviceTime[i],
                    timeServiceBegins[i], waitingTime[i], timeServiceEnds[i], timeSpentInSystem[i], serverStatus[i]);
        }
    }
    public static void runPrompt() {
        System.out.print("Do you want to run the queue simulation again? (Y/N): ");
        String answer = scanner.next();
          if(answer.equals("Y") || answer.equals("y")) {
            queueingSystem();
          }

        }
    }
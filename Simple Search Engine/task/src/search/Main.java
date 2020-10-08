package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static Map<String, List<Integer>> map = new TreeMap<>();

    public static void main(String[] args) {
        if (args.length == 2) {
            if ("--data".equals(args[0])) {
                String pathFile = args[1];
                ArrayList<String> people = readPeopleFromFile(pathFile);
                expandTheSearch(people);
            }
        }
    }

    public static void expandTheSearch(ArrayList<String> people) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Find a person");
            System.out.println("2. Print all people");
            System.out.println("0. Exit");
            switch (scanner.nextLine()) {
                case "1": {
                    search(people);
                    break;
                }
                case "2": {
                    printAllPeople(people);
                    break;
                }
                case "0": {
                    System.out.println("\nBye!");
                    return;
                }
                default: {
                    System.out.println("Incorrect option! Try again.");
                    break;
                }
            }
        }


    }

    public static ArrayList<String> readPeopleFromFile(String pathToFile) {
        File file = new File(pathToFile);
        ArrayList<String> people = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                people.add(input);
                grantAccess(input, people);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToFile);
        }
        return people;
    }

    public static ArrayList<String> searchInformationMapped(ArrayList<String> people, String dataToSearch) {
        ArrayList<String> peopleFound = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> m : map.entrySet()) {
            if (m.getKey().toLowerCase().equals(dataToSearch.toLowerCase())) {
                for (Integer i : m.getValue()) {
                    peopleFound.add(people.get(i));
                }
                break;
            }
        }
        return peopleFound;
    }

    public static void search(ArrayList<String> people) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String choice = scanner.nextLine();
        System.out.println("Enter a name or email to search all suitable people.");
        String[] wordsToLookFor = scanner.nextLine().split("\\s");
        switch (choice) {
            case "ALL": {
                searchAll(people, wordsToLookFor);
                break;
            }
            case "ANY": {
                searchAny(people, wordsToLookFor);
                break;
            }
            case "NONE": {
                searchNone(people, wordsToLookFor);
                break;
            }
        }

    }

    public static void searchAny(ArrayList<String> people, String[] wordsToLookFor) {
        ArrayList<String> peopleFound = new ArrayList<>();
        for (String s : wordsToLookFor) {
            peopleFound.addAll(searchInformationMapped(people, s));
        }
        for (String s : peopleFound) {
            System.out.println(s);
        }
    }

    public static void searchAll(ArrayList<String> people, String[] wordsToLookFor) {
        ArrayList<String> peopleFound = new ArrayList<>();
        ArrayList<String> peopleConfirmed = new ArrayList<>();
        boolean isFirstIteration = true;
        for (String s : wordsToLookFor) {
            peopleFound.clear();
            peopleFound.addAll(searchInformationMapped(people, s));
            if (isFirstIteration) {
                peopleConfirmed.addAll(peopleFound);
                isFirstIteration = false;
            }
            peopleConfirmed.removeIf(str -> !peopleFound.contains(str));
        }
        for (String s : peopleConfirmed) {
            System.out.println(s);
        }
    }

    public static void searchNone(ArrayList<String> people, String[] wordsToLookFor) {
        ArrayList<Integer> index = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            index.add(i);
        }
        for (Map.Entry<String, List<Integer>> m : map.entrySet()) {
            for (String s : wordsToLookFor) {
                if (s.toLowerCase().equals(m.getKey().toLowerCase())) {
                    for (Integer i : m.getValue())
                        index.remove(i);
                }
            }
        }
        for (Integer i : index) {
            System.out.println(people.get(i));
        }
    }

    public static void printAllPeople(ArrayList<String> people) {
        System.out.println("\n=== List of people ===");
        for (String person : people) {
            System.out.println(person);
        }
    }

    public static void grantAccess(String input, ArrayList<String> people) {
        String[] inputArray = input.split("\\s");
        for (String s : inputArray) {
            boolean flag = true;
            for (Map.Entry<String, List<Integer>> m : map.entrySet()) {
                if (m.getKey().equals(s)) {
                    m.getValue().add(people.size() - 1);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                List<Integer> listForMap = new ArrayList<>();
                listForMap.add(people.size() - 1);
                map.put(s, listForMap);
            }
        }
    }

}

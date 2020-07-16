package flashcards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int ReturnMax(List<Integer> list) {
        int max = list.get(0);
        for(int x : list) {
            if(x > max) max = x;
        }
        return max;
    }
    public static void main(String[] args){
        List<String> cards = new ArrayList<>();
        List<String> definitions = new ArrayList<>();
        List<String> hardestCards = new ArrayList<>();
        List<String> Log = new ArrayList<>();
        List<Integer> Errors = new ArrayList<>();
        if(args.length > 0) {
            for(int i = 0; i < args.length; i++) {
                if(args[i].equals("-import")) {
                    String filename = args[i+1];
                    try {
                        Scanner in = new Scanner(new File(String.valueOf(Paths.get(filename))));
                        String cardsAndDefinitions = in.nextLine();
                        String[] words = cardsAndDefinitions.split("=");
                        for (int j = 0; j < words.length-1; j+=3) {
                            cards.add(words[j]);
                            definitions.add(words[j+1]);
                        }
                        in.close();
                        System.out.println(words.length / 3 + " cards have been loaded.");
                        Log.add(words.length / 3 + " cards have been loaded.");
                    } catch (IOException e) {
                        System.out.println("File not found.");
                        Log.add("File not found.");
                    }
                }
            }
        }
        String FileNameForLogging = "";
        final char dm = (char) 34;
        boolean tf = true;

        while (tf) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            Log.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String input = scanner.nextLine();
            Log.add(input);
            switch (input) {
                case "add":
                    System.out.println("The card:");
                    Log.add("The card:");
                    String card = scanner.nextLine();
                    Log.add(card);
                    if (cards.contains(card)) {
                        System.out.println("The card " + dm + card + dm + " already exists.");
                        Log.add("The card " + dm + card + dm + " already exists.");
                        break;
                    } else {
                        cards.add(card);
                    }
                    System.out.println("The definition of the card:");
                    Log.add("The definition of the card:");
                    String definition = scanner.nextLine();
                    Log.add(definition);
                    if (definitions.contains(definition)) {
                        System.out.println("The definition " + dm + definition + dm + " already exists.");
                        Log.add("The definition " + dm + definition + dm + " already exists.");
                        cards.remove(card);
                        break;
                    } else {
                        definitions.add(definition);
                    }
                    String x = "The pair " + "(" + dm + card + dm + ":" + dm + definition + dm + ")"
                            + " has been added.";
                    System.out.println(x);
                    Log.add(x);
                    break;
                case "remove":
                    System.out.println("The card:");
                    Log.add("The card:");
                    String removeCard = scanner.nextLine();
                    Log.add(removeCard);
                    if(cards.contains(removeCard)) {
                        for (int i = 0; i < cards.size(); i++) {
                            if (cards.get(i).equals(removeCard)) {
                                cards.remove(i);
                                definitions.remove(i);
                                System.out.println("The card has been removed.");
                                Log.add("The card has been removed.");
                                break;
                            }
                        }
                    } else {
                        System.out.println("Can't remove " + dm + removeCard + dm + ":" + " there is no such card.");
                        Log.add("Can't remove " + dm + removeCard + dm + ":" + " there is no such card.");
                    }
                    if(hardestCards.contains(removeCard)) {
                        Errors.remove(hardestCards.indexOf(removeCard));
                        hardestCards.remove(removeCard);
                    }
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    Log.add("Bye bye!");
                    if(args.length > 0) {
                        for (int i = 0; i < args.length; i++) {
                            if(args[i].equals("-export")) {
                                String fileName = args[i+1];
                                try {
                                    File file = new File(String.valueOf(Paths.get(fileName)));
                                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                                    for (int j = 0; j < cards.size(); j++) {
                                        fileOutputStream.write(cards.get(j).getBytes());
                                        fileOutputStream.write("=".getBytes());
                                        fileOutputStream.write(definitions.get(j).getBytes());
                                        fileOutputStream.write("=".getBytes());
                                        if(hardestCards.contains(cards.get(j))) {
                                            int tempError = Errors.get(hardestCards.indexOf(cards.get(j)));
                                            fileOutputStream.write(String.valueOf(tempError).getBytes());
                                        } else {
                                            fileOutputStream.write(String.valueOf(0).getBytes());
                                        }
                                        if(j + 1 != cards.size()) fileOutputStream.write("=".getBytes());
                                    }
                                    fileOutputStream.close();
                                    System.out.println(cards.size() + " cards have been saved.");
                                    Log.add(cards.size() + " cards have been saved.");
                                } catch (IOException e) {
                                    System.out.println("File not found.");
                                    Log.add("File not found.");
                                }
                                break;
                            }
                        }
                    }
                    try {
                        File file = new File(String.valueOf(Paths.get(FileNameForLogging)));
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        for (String s : Log) {
                            fileOutputStream.write(s.getBytes());
                            fileOutputStream.write("\n".getBytes());
                        }
                        fileOutputStream.close();
                    } catch (IOException e) {
                        Log.add("File not found.");
                    }
                    tf = false;
                    break;
                case "ask":
                    Scanner anotherScanner = new Scanner(System.in);
                    System.out.println("How many times to ask?");
                    Log.add("How many times to ask?");
                    int askTimes = scanner.nextInt();
                    Log.add(String.valueOf(askTimes));
                    int i = 0;
                    while(askTimes > 0) {
                        askTimes--;
                        if(i >= cards.size()) i = 0;
                        System.out.println("Print the definition of " + dm + cards.get(i) + dm + ":");
                        Log.add("Print the definition of " + dm + cards.get(i) + dm + ":");
                        String def = anotherScanner.nextLine();
                        Log.add(def);
                        if (def.equals(definitions.get(i))) {
                            System.out.println("Correct answer");
                            Log.add("Correct answer");
                        } else {
                            if (definitions.contains(def)) {
                                int index = 0;
                                for (int k = 0; k < definitions.size(); k++) {
                                    if(def.equals(definitions.get(k))) {
                                        index = k;
                                        break;
                                    }
                                }
                                System.out.println("Wrong answer. (The correct one is " + dm + definitions.get(i) + dm
                                        + "," + " you've just written the definition of " + dm + cards.get(index) +
                                        dm + " card.)");
                                Log.add("Wrong answer. (The correct one is " + dm + definitions.get(i) + dm
                                        + "," + " you've just written the definition of " + dm + cards.get(index) +
                                        dm + " card.)");
                            } else {
                                System.out.println("Wrong answer. The correct one is " + dm + definitions.get(i) +
                                        dm + ".");
                                Log.add("Wrong answer. The correct one is " + dm + definitions.get(i) + dm + ".");
                            }
                            if(hardestCards.contains(cards.get(i))) {
                                int tempError = Errors.get(hardestCards.indexOf(cards.get(i))) + 1;
                                Errors.set(hardestCards.indexOf(cards.get(i)), tempError);
                            } else {
                                hardestCards.add(cards.get(i));
                                Errors.add(1);
                            }
                        }
                        i++;
                    }
                    break;
                case "import":
                    System.out.println("File name:");
                    Log.add("File name:");
                    String filename = scanner.nextLine();
                    Log.add(filename);
                    try {
                        Scanner in = new Scanner(new File(String.valueOf(Paths.get(filename))));
                        String cardsAndDefinitions = in.nextLine();
                        String[] words = cardsAndDefinitions.split("=");
                        for (int j = 0; j < words.length-2; j+=3) {
                            if(cards.contains(words[j])) {
                                definitions.set(cards.indexOf(words[j]), words[j+1]);
                            } else {
                                cards.add(words[j]);
                                definitions.add(words[j+1]);
                            }
                            if(hardestCards.contains(words[j])) {
                                Errors.set(hardestCards.indexOf(words[j]), Integer.parseInt(words[j+2]));
                            }else {
                                hardestCards.add(words[j]);
                                Errors.add(Integer.parseInt(words[j+2]));
                            }
                        }
                        in.close();
                        System.out.println(words.length / 3 + " cards have been loaded.");
                        Log.add(words.length / 3 + " cards have been loaded.");
                    } catch (IOException e) {
                        System.out.println("File not found.");
                        Log.add("File not found.");
                    }
                    break;
                case "export":
                    System.out.println("File name:");
                    Log.add("File name:");
                    String fileName = scanner.nextLine();
                    Log.add(fileName);
                    try {
                        File file = new File(String.valueOf(Paths.get(fileName)));
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        for (int j = 0; j < cards.size(); j++) {
                            fileOutputStream.write(cards.get(j).getBytes());
                            fileOutputStream.write("=".getBytes());
                            fileOutputStream.write(definitions.get(j).getBytes());
                            fileOutputStream.write("=".getBytes());
                            if(hardestCards.contains(cards.get(j))) {
                                int tempError = Errors.get(hardestCards.indexOf(cards.get(j)));
                                fileOutputStream.write(String.valueOf(tempError).getBytes());
                            } else {
                                fileOutputStream.write(String.valueOf(0).getBytes());
                            }
                            if(j + 1 != cards.size()) fileOutputStream.write("=".getBytes());
                        }
                        fileOutputStream.close();
                        System.out.println(cards.size() + " cards have been saved.");
                        Log.add(cards.size() + " cards have been saved.");
                    } catch (IOException e) {
                        System.out.println("File not found.");
                        Log.add("File not found.");
                    }
                    break;
                case "hardest card":
                    if(hardestCards.size() == 0) {
                        System.out.println("There are no cards with errors.");
                        Log.add("There are no cards with errors.");
                    } else if (hardestCards.size() == 1) {
                        System.out.println("The hardest card is " + dm + hardestCards.get(0) + dm + ". You have "
                                + Errors.get(0) + " errors answering it.");
                        Log.add("The hardest card is " + hardestCards.get(0) + ". You have "  + Errors.get(0) +
                                " errors answering it.");
                    } else {
                        int max = ReturnMax(Errors);
                        StringBuilder allHardestCards = new StringBuilder("The hardest cards are ");
                        for (int j = 0; j < hardestCards.size(); j++) {
                            if(j + 1 == hardestCards.size())  {
                                allHardestCards.append(dm).append(hardestCards.get(j)).append(dm).append(". ");
                            } else {
                                allHardestCards.append(dm).append(hardestCards.get(j)).append(dm).append(", ");
                            }
                        }
                        allHardestCards.append("You have ").append(max).append(" errors answering them.");
                        System.out.println(allHardestCards);
                        Log.add(allHardestCards.toString());
                    }
                    break;
                case "reset stats":
                    hardestCards.clear();
                    Errors.clear();
                    System.out.println("Card statistics has been reset.");
                    Log.add("Card statistics has been reset.");
                    break;
                case "log":
                    System.out.println("File name:");
                    Log.add("File name:");
                    String FileName = scanner.nextLine();
                    FileNameForLogging = FileName;
                    Log.add(FileName);
                    System.out.println("The log has been saved.");
                    Log.add("The log has been saved.");
                    break;
                default:
                    break;
            }
        }
    }
}

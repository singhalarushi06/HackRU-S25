import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class IngredientReader {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> ingredients = new ArrayList<>();

        //give an option to import a link to a recipe and parse the ingredients
        System.out.println("Would you like to enter a link to a recipe or type out the ingredients? (link/type):");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("link")) {
            System.out.println("Enter the link to your recipe:");
            String link = scanner.nextLine();
            if (link.startsWith("http://") || link.startsWith("https://")) {
                ingredients.addAll(parseIngredientsFromLink(link));
            }
        } else if (choice.equalsIgnoreCase("type")) {
            System.out.println("Enter your recipe (type 'done' to finish):");
            while (true) {
                String line = scanner.nextLine();
                if (line.equalsIgnoreCase("done")) {
                    break;
                }
                if (isIngredient(line)) {
                    ingredients.add(line);
                }
            }
        } else {
            System.out.println("Invalid choice. Please restart the program and choose either 'link' or 'type'.");
            scanner.close();
            return;
        }

        scanner.close();
    }

    private static boolean isIngredient(String line) {
        // Simple heuristic to determine if a line is an ingredient
        return line.matches(".*\\d+.*");
    }

    private static ArrayList<String> parseIngredientsFromLink(String link) {
        // Code to get ingredients from a recipe link and store it in an array list
        ArrayList<String> list = new ArrayList<>();
        //find the ingredients in the link and add the ingredients to the list
        try {
            java.net.URL url = new java.net.URL(link);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Failed to fetch the recipe. HTTP response code: " + responseCode);
            }

            Scanner urlScanner = new Scanner(url.openStream());
            while (urlScanner.hasNext()) {
                String line = urlScanner.nextLine();
                if (isIngredient(line)) {
                    list.add(line);
                }
            }
            urlScanner.close();
        } catch (IOException e) {
            System.out.println("An error occurred while fetching the recipe: " + e.getMessage());
        }
        
        return list;
        
        
    }
}
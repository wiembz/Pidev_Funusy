package pidev.esprit.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BadWordsLoader {
    public static List<String> loadBadWords(String filePath) {
        List<String> badWordsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                badWordsList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return badWordsList;
    }
}

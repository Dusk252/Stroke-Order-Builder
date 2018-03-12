import java.io.*;
import java.util.*;

import static java.lang.Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;

/**
 * Created by Dusk on 13-Oct-16.
 */
public class ReadFile {

    private String fs;

    //read file into memory
    public ReadFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        br.close();
        fs = sb.toString();
    }

    //
    public List<String> read_kanji(int kanji_col) throws IOException, IndexOutOfBoundsException {
        List<String> kanji = new ArrayList<>();
        if (kanji_col < 0) {
            for (char k : fs.toCharArray()) {
                if (Character.UnicodeBlock.of(k) == CJK_UNIFIED_IDEOGRAPHS) {
                    if (!kanji.contains(k+"")) kanji.add(k+"");
                }
            }
        }
        else {
            BufferedReader bufReader = new BufferedReader(new StringReader(fs));
            String line;
            while ((line = bufReader.readLine()) != null) {
                String[] parts = line.split("\t");
                for (char k : parts[kanji_col].toCharArray()) {
                    if (Character.UnicodeBlock.of(k) == CJK_UNIFIED_IDEOGRAPHS) {
                        if (!kanji.contains(k+"")) kanji.add(k+"");
                    }
                }
            }
        }
        return kanji;
    }

    public List<Vocab> read_vocab(int word_col, int reading_col) throws IOException, IndexOutOfBoundsException {
        List<Vocab> vocab = new ArrayList<>();
        BufferedReader bufReader = new BufferedReader(new StringReader(fs));
        String line;
        while ((line = bufReader.readLine()) != null) {
            String[] parts = line.split("\t");
            Vocab v = new Vocab(parts[word_col], parts[reading_col]);

            int flag = 0;
            for (char c : parts[word_col].toCharArray()) {
                if (Character.UnicodeBlock.of(c) != CJK_UNIFIED_IDEOGRAPHS) flag = 1;
            }
            v.setKanaflag(flag);

            if (!vocab.contains(v)) vocab.add(v);
        }
        return vocab;
    }
}
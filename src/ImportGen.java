import java.io.*;
import java.util.*;

/**
 * Created by Dusk on 14-Oct-16.
 * [kanji, main example, main example hidden, main example reading, meaning, on, kun, nanori, misc, words]
 */
public class ImportGen {

    public static void write(List<KanjiInfo> dic, List<String> kanji, List<Vocab> vocab, String filename) {
        try {
            File fout = new File(filename);
            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (String k : kanji) {
                String tmp = k + "\t";

                int flag = 0;
                List<Vocab> kv = new ArrayList<>();
                List<Vocab> kv0 = new ArrayList<>();
                for (Vocab v : vocab) {
                    for (char c : v.getVocab().toCharArray()) {
                        if(k.equals(c+"")) flag = 1;
                    }
                    if(flag == 1) {
                        kv.add(v);
                        if(v.getKanaflag() == 0) kv0.add(v);
                        flag = 0;
                    }
                }
                int index = (int) (Math.random() * kv0.size());
                Vocab ex;
                try {
                    ex = kv0.get(index);
                } catch (IndexOutOfBoundsException e) {
                    index = (int) (Math.random() * kv.size());
                    ex = kv.get(index);
                }
                tmp = tmp + ex.getVocab() + "\t" + ex.getVocab().replaceAll(k, "\uFF3F") + "\t" + ex.getReading() + "\t";

                //add kaji from dictionary to import file only if
                for (KanjiInfo ki : dic) {
                    if (ki.getKanji().equals(k)) {
                        tmp = tmp + ki.getUnicode() + "\t" + ki.getMeaning() + "\t" + ki.getOn() + "\t" + ki.getKun() + "\t" + ki.getNanori() + "\t" +
                                "Grade: " + ki.getGrade() + ", " + "Stroke count: " + ki.getStroke_count() + ", " + "Freq: " + ki.getFreq() + ", " + "JLPT: " + ki.getJlpt() + "\t";
                        break;
                    }
                }

                for (Vocab v : kv) {
                    tmp = tmp + v.getVocab() + " (" + v.getReading() + ")<br />";
                }


                bw.write(tmp.replaceAll("null", "\u2014"));
                bw.newLine();
            }

            bw.close();
        } catch(IOException e) {
            System.out.println("Failed to write file");
        }
    }

}

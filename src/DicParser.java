import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * Created by Dusk on 13-Oct-16.
 * [kanji, grade, stroke_count, freq, jlpt, meaning, on, kun, nanori]
 */
public class DicParser {

    private final List<KanjiInfo> dictionary = new ArrayList<>();

    public DicParser(InputStream is) {
        try {
            //File f = new File(filename);
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("character");
            for (int i = 0; i < nList.getLength(); i++) {
                KanjiInfo current = new KanjiInfo();
                Node k = nList.item(i);
                Element e = (Element) k;

                current.setKanji(e.getElementsByTagName("literal").item(0).getTextContent());

                NodeList t = e.getElementsByTagName("codepoint");
                Element x = (Element) t.item(0);
                t = x.getElementsByTagName("cp_value");
                for (int j = 0; j < t.getLength(); j++) {
                    x = (Element) t.item(j);
                    if (x.getAttribute("cp_type").equals("ucs")) {
                        current.setUnicode(0 + x.getTextContent());
                        break;
                    }
                }

                t = e.getElementsByTagName("misc");
                x = (Element) t.item(0);
                if (x.getElementsByTagName("grade").getLength() > 0) {
                    current.setGrade(x.getElementsByTagName("grade").item(0).getTextContent());
                }
                if (x.getElementsByTagName("stroke_count").getLength() > 0) {
                    current.setStroke_count(x.getElementsByTagName("stroke_count").item(0).getTextContent());
                }
                if (x.getElementsByTagName("freq").getLength() > 0) {
                    current.setFreq(x.getElementsByTagName("freq").item(0).getTextContent());
                }
                if (x.getElementsByTagName("jlpt").getLength() > 0) {
                    current.setJlpt(x.getElementsByTagName("jlpt").item(0).getTextContent());
                }

                if (e.getElementsByTagName("reading_meaning").getLength() > 0) {
                    t = e.getElementsByTagName("reading_meaning");
                    x = (Element) t.item(0);
                    t = x.getElementsByTagName("rmgroup");
                    Element y = (Element) t.item(0);
                    t = y.getElementsByTagName("meaning");
                    int j = 0;
                    String meaning = "";
                    if (x.getElementsByTagName("meaning").getLength() > 0) {
                        while (j < t.getLength() && !(t.item(j).hasAttributes())) {
                            Element z = (Element) t.item(j);
                            meaning = meaning + z.getTextContent() + ", ";
                            j++;
                        }
                        current.setMeaning(meaning.substring(0, meaning.length() - 2));
                    }
                    t = y.getElementsByTagName("reading");
                    String on = "";
                    String kun = "";
                    for (j = 0; j < t.getLength(); j++) {
                        Element z = (Element) t.item(j);
                        if (z.getAttribute("r_type").equals("ja_on")) {
                            on = on + z.getTextContent() + "\u3001 ";
                        }
                        if (z.getAttribute("r_type").equals("ja_kun")) {
                            kun = kun + z.getTextContent() + "\u3001 ";
                        }
                    }
                    if (!on.equals("")) current.setOn(on.substring(0, on.length() - 2));
                    if (!kun.equals("")) current.setKun(kun.substring(0, kun.length() - 2));
                    t = x.getElementsByTagName("nanori");
                    String nanori = "";
                    for (j = 0; j < t.getLength(); j++) {
                        Element z = (Element) t.item(j);
                        nanori = nanori + z.getTextContent() + "\u3001 ";
                    }
                    if (!nanori.equals("")) current.setNanori(nanori.substring(0, nanori.length() - 2));
                }
                dictionary.add(current);
            }
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public List<KanjiInfo> getDic() {
        return dictionary;
    }
}

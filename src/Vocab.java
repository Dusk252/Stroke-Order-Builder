/**
 * Created by Dusk on 14-Oct-16.
 */
public class Vocab {

    private String vocab;
    private String reading;
    private int kanaflag = 0;

    public Vocab(String vocab, String reading) {
        this.vocab = vocab;
        this.reading = reading;
    }

    public String getVocab() {
        return vocab;
    }

    public String getReading() {
        return reading;
    }

    public int getKanaflag() {
        return kanaflag;
    }

    public void setKanaflag(int f) {
        kanaflag = f;
    }
}

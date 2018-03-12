import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Dusk on 13-Oct-16.
 */

public class main {
    public static void main(String [ ] args) {

        //final long startTime = System.currentTimeMillis();
        //parse arguments with Apache Commons CLI

        //define options
        Options options = new Options();

        Option kanji = Option.builder("k")
                .argName("filename> <column")
                .desc("use to provde the path to a kanji text file and specify the column in case it's a tsv")
                .hasArg()
                .optionalArg(true)
                .numberOfArgs(2)
                .valueSeparator(' ')
                .build();

        Option vocab = Option.builder("v")
                .argName("filename> <word column> <reading column")
                .desc("required option. use to provide the path to vocab file and specify the word and reading columns")
                .required()
                .hasArg()
                .optionalArg(true)
                .numberOfArgs(3)
                .valueSeparator(' ')
                .build();

        Option output = Option.builder("o")
                .argName("filename")
                .desc("required option. use to provide the output file path")
                .required()
                .hasArg()
                .numberOfArgs(1)
                .build();

        Option help = Option.builder("help")
                .hasArg(false)
                .desc("prints this message")
                .build();

        options.addOption(kanji);
        options.addOption(vocab);
        options.addOption(output);
        options.addOption(help);

        //check for help if required arguments aren't all there
        for (String s : args) {
            if (s.equals("-help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("stroke-order-builder", options);
                return;
            }
        }

        //create parser
        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            line = parser.parse(options, args, true);
        }
        catch (ParseException e) {
            System.err.println(e.getMessage());
            return;
        }


        //query CommandLine
        int kanji_col = -1;
        int word_col = 0, reading_col = 1;
        String k_filename;

        String v_filename = line.getOptionValues("v")[0];
        if (line.getOptionValues("v").length > 1) {
            try {
                word_col = Integer.parseInt(line.getOptionValues("v")[1]);
                reading_col = Integer.parseInt(line.getOptionValues("v")[2]);
            }
            catch (NumberFormatException e) {
                System.err.println("The values for <word column> and <reading column> must be integers.");
                return;
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("The -v option requires either exactly 1 parameter <filename> or 3 parameters <filename> <word column> <reading column>.");
                return;
            }
        }

        if (line.hasOption ("k")) {
            k_filename = line.getOptionValues("k")[0];
            if (line.getOptionValues("k").length > 1) {
                try {
                    kanji_col = Integer.parseInt(line.getOptionValues("k")[1]);
                }
                catch (NumberFormatException e) {
                    System.err.println("The values for <word column> and <reading column> must be integers.");
                    return;
                }
            }
        }
        else {
            //if user doesn't input a filename for kanji, use vocab file by default
            k_filename = v_filename;
            kanji_col = word_col;
        }

        String out = line.getOptionValue("o");

        //execute
        //String dictionary = "kanjidic2-normalized.xml";

        DicParser d;
        try {
            InputStream dictionary = Thread.currentThread().getContextClassLoader().getResourceAsStream("kanjidic2-normalized.xml");
            d = new DicParser(dictionary);
        }
        catch (IllegalArgumentException e) {
            System.err.println("Could not find the dictionary file.");
            return;
        }
        try {
            ReadFile rk = new ReadFile(k_filename);
            ReadFile rv = new ReadFile(v_filename);
            try {
                ImportGen.write(d.getDic(), rk.read_kanji(kanji_col), rv.read_vocab(word_col, reading_col), out);
            }
            catch (IOException e) {
                System.err.println("There was a problem with reading the input files.");
            }
            catch (IndexOutOfBoundsException e) {
                System.err.println("The column values input don't match with the file structure.");
            }
        }
        catch (IOException e) {
            System.err.println("The file doesn't exist.");
        }

        //final long endTime = System.currentTimeMillis();

        System.out.println("Import file created successfully at: " + out );
        //System.out.println("Total execution time: " + (endTime - startTime));
    }
}
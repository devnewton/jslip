package im.bci.jslip;

import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author devnewton
 */
public class Main {

    private static final Whitelist WHITELIST = Whitelist.none().addTags("b", "i", "s", "u", "tt", "code", "spoiler");

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        for (;;) {
            String line = scanner.nextLine();
            String cleaned = cleanMessage(line);
            System.out.println(cleaned);
        }
    }

    public static String cleanMessage(String message) {
        Document doc = Jsoup.parseBodyFragment(message);
        doc.body().children().select("span[style='text-decoration: line-through']").tagName("s");
        doc.body().children().select("span[style='text-decoration: underline']").tagName("u");
        for (Element element : doc.body().children().select(":not(a,b,i,s,u,tt,code,spoiler)")) {
            element.replaceWith(TextNode.createFromEncoded(element.toString(), null));
        }
        for (Element element : doc.body().children().select("a")) {
            element.replaceWith(TextNode.createFromEncoded(element.attr("href"), null));
        }
        Cleaner cleaner = new Cleaner(WHITELIST);
        doc = cleaner.clean(doc);
        doc.outputSettings().indentAmount(0).prettyPrint(false);
        message = doc.body().html();
        return message;
    }
}

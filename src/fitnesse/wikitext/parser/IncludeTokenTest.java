package fitnesse.wikitext.parser;

import fitnesse.wiki.*;
import fitnesse.wikitext.translator.Translator;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class IncludeTokenTest {
    @Test public void scansIncludes() {
        ParserTest.assertScansTokenType("!include name", SymbolType.Include, true);
    }

    @Test public void translatesIncludedSibling() throws Exception {
        String result = translate("!include PageTwo\n");
        assertContains(result, "class=\"collapsable\"");
        assertContains(result, "PageTwo");
        assertContains(result, "page <i>two</i>");
    }

    @Test public void translatesSetup() throws Exception {
        String result = translate("!include -setup PageTwo\n");
        assertContains(result, "class=\"hidden\"");
    }

    private String translate(String input) throws Exception {
        WikiPage root = InMemoryPage.makeRoot("RooT");
        PageCrawler crawler = root.getPageCrawler();
        crawler.setDeadEndStrategy(new VirtualEnabledPageCrawler());
        WikiPage currentPage = crawler.addPage(root, PathParser.parse("PageOne"), "page one");
        crawler.addPage(root, PathParser.parse("PageTwo"), "page ''two''");
        String result = new Translator(currentPage).translate(input);
        return result;
    }

    private void assertContains(String result, String substring) {
        assertTrue(result.indexOf(substring) >= 0);
    }

}

package fitnesse.wikitext.parser;

import fitnesse.wiki.*;
import util.Maybe;

public class IncludeToken extends Token {
    public Maybe<String> render(Scanner scanner) {
        scanner.moveNext();
        if (!scanner.isType(SymbolType.Whitespace)) return Maybe.noString;
        scanner.moveNext();
        String option = "";
        if (scanner.isType(SymbolType.Text) && scanner.getCurrentContent().startsWith("-")) {
            option = scanner.getCurrentContent();
            scanner.moveNext();
            if (!scanner.isType(SymbolType.Whitespace)) return Maybe.noString;
            scanner.moveNext();
        }
        if (!scanner.isType(SymbolType.Text)) return Maybe.noString;
        String pageName = scanner.getCurrentContent();
        scanner.moveNext();
        if (!scanner.isType(SymbolType.Newline)) return Maybe.noString;

        PageCrawler crawler = getPage().getPageCrawler();
        crawler.setDeadEndStrategy(new VirtualEnabledPageCrawler());
        WikiPagePath pagePath = PathParser.parse(pageName);
        WikiPage includedPage;
        try {
            includedPage = crawler.getSiblingPage(getPage(), pagePath);
            String collapseType = option.equals("-setup") ? "hidden" : "collapsable";
            return new Maybe<String>(new CollapsibleRule().generateHtml(pageName, includedPage.getData().getHtml(), collapseType));
        } catch (Exception e) {
            return new Maybe<String>(e.toString());
        }
    }
}

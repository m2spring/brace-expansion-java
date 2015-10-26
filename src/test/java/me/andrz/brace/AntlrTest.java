package me.andrz.brace;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import me.andrz.brace.antlr.BraceExpansionLexer;
import me.andrz.brace.antlr.BraceExpansionParser;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anders on 7/31/15.
 */
public class AntlrTest {

    String[] strings = new String[] {
            "",
            "A",
            "AB",
            "b,B",
            "{A}",
            "{A}{B}",
            "{{}}",
            "{{A}}",
            "{b,B}",
            "a{b,B}",
            "{b,B}c",
            "a{b,B}c",
            "a{b,B}c,d",
            "x,ya{b,{1\\,2,3}B}c,d",
            "x,ya{bb,AA{1,2,33}{4,5}BB}cc,dd",
    };

    @Ignore
    @Test
    public void testLexer() {
        for (String s : strings) {
            System.out.println("STRING: \"" + s + "\"");

            BraceExpansionLexer lexer = new BraceExpansionLexer(new ANTLRInputStream(s));

            // Get a list of matched tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Pass the tokens to the parser
            BraceExpansionParser parser = new BraceExpansionParser(tokens);

            // Specify our entry point
            ParserRuleContext context = parser.root();

            System.out.println("TEXT: " + context.getText());

            System.out.println("TREE: " + context.toStringTree(parser));
        }
    }

//        String str = "x,ya{bb,AA{1\\,2,33}{4,5}BB}cc,dd";
//        String str = "x,ya{bb,AA{1,2,33}{4,5}BB}cc,dd";

    @Test
    public void testEmpty() {
        assertExpand(
                "",
                Arrays.asList(
                        ""
                )
        );
    }

    @Test
    public void testA() {
        assertExpand(
                "a",
                Arrays.asList(
                        "a"
                )
        );
    }

    @Test
    public void test2() {
        assertExpand(
                "a{b,c}d",
                Arrays.asList(
                        "abd",
                        "acd"
                )
        );
    }

    @Test
    public void test2s() {
        assertExpand(
                "a{b,{c,d},e}f",
                Arrays.asList(
                        "abf",
                        "acf",
                        "adf",
                        "aef"
                )
        );
    }

    @Test
    public void test3() {
        assertExpand(
                "a{b,c,d}e",
                Arrays.asList(
                        "abe",
                        "ace",
                        "ade"
                )
        );
    }

    @Test
    public void test22() {
        assertExpand(
                "a{b,c}d{e,f}g",
                    Arrays.asList(
                    "abdeg",
                    "acdeg",
                    "abdfg",
                    "acdfg"
            )
        );
    }

    public void assertExpand(String s, List<String> exs) {
        System.out.println(s);
        List<String> strs = BraceExpansion.expand(s);
        System.out.println(strs);
        MatcherAssert.assertThat(strs, Matchers.equalTo(exs));
    }
}
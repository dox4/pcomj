import com.studyvm.pcomj.adaptor.Accumulator;
import com.studyvm.pcomj.base.Pair;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;
import com.studyvm.pcomj.combinator.*;
import com.studyvm.pcomj.parser.CharParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.studyvm.pcomj.utils.ParserBuilder.*;
import static com.studyvm.pcomj.utils.UtilParser.digit1To9;
import static org.junit.jupiter.api.Assertions.*;

public class CombinatorTest {
    @Test
    public void testAnd() {
        AndParser<Character, Character> ab = symbol('a').and(symbol('b'));
        Optional<ParseResult<Pair<Character, Character>>> r1 = ab.parse(new ParserInput("ab"));
        assertTrue(r1.isPresent());
        assertTrue(r1.get().input().empty());
        Optional<ParseResult<Pair<Character, Character>>> r2 = ab.parse(new ParserInput("cd"));
        assertFalse(r2.isPresent());
        Optional<ParseResult<Pair<Character, Character>>> r3 = ab.parse(new ParserInput("ac"));
        assertFalse(r3.isPresent());
    }

    @Test
    public void testOr() {
        OrParser<Character> or = symbol('a').or(symbol('b'));
        Optional<ParseResult<Character>> r1 = or.parse(new ParserInput("ab"));
        assertTrue(r1.isPresent());
        assertEquals(1, r1.get().input().restLength());
        Optional<ParseResult<Character>> r2 = or.parse(new ParserInput("cd"));
        assertFalse(r2.isPresent());
    }

    @Test
    public void testMany() {
        TestInputs.NUMBERS.clear();
        ManyParser<Character> manyA = symbol('a').many();
        Optional<ParseResult<List<Character>>> ra = manyA.parse(new ParserInput("aaa"));
        assertTrue(ra.isPresent());
        assertEquals(0, ra.get().input().restLength());
        ManyParser<Character> natural = digit1To9.many();
        Optional<ParseResult<List<Character>>> rn = natural.parse(TestInputs.NUMBERS);
        assertTrue(rn.isPresent());
        ParseResult<List<Character>> rl = rn.get();
        int s1 = rl.value().size();
        assertEquals(4, s1);
        assertEquals(5, rl.input().restLength());
        int intValue = rl.value()
                .stream()
                .map(c -> c - '0')
                .reduce(0, (a, b) -> a * 10 + b);
        assertEquals(1234, intValue);
    }


    @Test
    public void testPack() {
        TestInputs.NUMBERS.clear();
        Accumulator<Character, Integer> natural = accumulate(digit1To9, c -> c - '0', (a, b) -> a * 10 + b, 0);
        CharParser leftParen = symbol('(');
        CharParser rightParen = symbol(')');
        TakeLeftParser<Integer, Character> naturalInParens = pack(leftParen, natural, rightParen);
        Optional<ParseResult<Integer>> r = naturalInParens.parse(new ParserInput("(1234)"));
        assertTrue(r.isPresent());
        assertEquals(1234, r.get().value());
    }

    @Test
    public void testAtLeast() {
        AtLeastParser<Character> a = symbol('a').atLeast(2);
        Optional<ParseResult<List<Character>>> r1 = a.parse(new ParserInput("aaa"));
        assertTrue(r1.isPresent());
        assertEquals(r1.get().value().size(), 3);
        Optional<ParseResult<List<Character>>> r2 = a.parse(new ParserInput("a"));
        assertFalse(r2.isPresent());
    }
}

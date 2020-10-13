import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.Parser;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.studyvm.pcomj.utils.ParserBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleParserTest {


    @Test
    public void testCharParser() {
        TestInputs.FUNCTION.clear();
        Parser<Character> a = symbol('a');
        Optional<ParseResult<Character>> r1 = a.parse(TestInputs.FUNCTION);
        assertFalse(r1.isPresent());
        Parser<Character> f = symbol('f');
        Optional<ParseResult<Character>> r2 = f.parse(TestInputs.FUNCTION);
        assertTrue(r2.isPresent());
        assertTrue(r2.get().input().equals("unction"));
    }

    @Test
    public void testStringParser() {
        TestInputs.FUNCTION.clear();
        Parser<String> kw = keyword("func");
        Optional<ParseResult<String>> r1 = kw.parse(TestInputs.FUNCTION);
        assertTrue(r1.isPresent());
        ParseResult<String> result = r1.get();
        assertEquals(result.value(), "func");
        assertTrue(result.input().equals("tion"));
    }

    @Test
    public void testOneOfParser() {
        TestInputs.NUMBERS.clear();
        Parser<Character> digit = oneOf("0123456789");
        Optional<ParseResult<Character>> r = digit.parse(TestInputs.NUMBERS);
        assertTrue(r.isPresent());
        assertEquals('1', r.get().value());
        assertEquals('2', r.get().input().current());
    }
}

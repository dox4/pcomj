import com.studyvm.pcomj.adaptor.Accumulator;
import com.studyvm.pcomj.adaptor.Mapper;
import com.studyvm.pcomj.base.Pair;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;
import com.studyvm.pcomj.combinator.OptionParser;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.studyvm.pcomj.utils.ParserBuilder.accumulate;
import static com.studyvm.pcomj.utils.ParserBuilder.symbol;
import static com.studyvm.pcomj.utils.UtilParser.digit0To9;
import static com.studyvm.pcomj.utils.UtilParser.digit1To9;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdaptorTest {
    @Test
    public void testAccumulator() {
        TestInputs.NUMBERS.clear();
        Accumulator<Character, Integer> natural = accumulate(digit1To9, c -> c - '0', (a, b) -> a * 10 + b, 0);
        Optional<ParseResult<Integer>> r1 = natural.parse(TestInputs.NUMBERS);
        assertTrue(r1.isPresent());
        assertEquals(1234, r1.get().value());
        assertEquals(5, r1.get().input().restLength());
    }

    @Test
    public void testConvertor() {
        TestInputs.NUMBERS.clear();
        Mapper<String, Double> decimal = digit1To9.accumulate()
                .and(symbol('.'))
                .convert(pair -> pair.first() + pair.second())
                .and(digit0To9.accumulate())
                .convert(pair -> pair.first() + pair.second())
                .convert(Double::parseDouble);
        Optional<ParseResult<Double>> result = decimal.parse(TestInputs.NUMBERS);
        assertTrue(result.isPresent());
        assertEquals(1234.5678, result.get().value());

        TestInputs.NUMBERS.clear();
        OptionParser<Character> sign = symbol('+').or(symbol('-')).option('+');
        Mapper<Pair<Character, Double>, Double> number = sign.and(decimal).convert(pair -> pair.first() == '-' ? -pair.second() : pair.second());
        Optional<ParseResult<Double>> r2 = number.parse(new ParserInput("-3.1415926"));
        assertTrue(r2.isPresent());
        assertEquals(-3.1415926, r2.get().value());
    }
}

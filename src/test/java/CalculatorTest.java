import com.studyvm.pcomj.adaptor.Mapper;
import com.studyvm.pcomj.base.AbstractParserCombinator;
import com.studyvm.pcomj.base.Pair;
import com.studyvm.pcomj.base.ParseResult;
import com.studyvm.pcomj.base.ParserInput;
import com.studyvm.pcomj.combinator.*;
import com.studyvm.pcomj.parser.CharParser;
import com.studyvm.pcomj.parser.OneOfParser;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Scanner;
import java.util.function.BinaryOperator;

import static com.studyvm.pcomj.utils.ParserBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculatorTest {
    public static void main(String[] args) {
        ChainLeftParser<Expr, Pair<Character, Expr>> expr = new CalculatorTest().expr();
        System.out.println("simple calculator.");
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input.equals(":q")) {
                break;
            }
            Optional<ParseResult<Expr>> r1 = expr.parse(new ParserInput(input));
            if (r1.isPresent()) {
                System.out.println("=> " + r1.get().value().eval());
            } else {
                System.out.println("error.");
            }
        } while (true);
    }

    private OptionParser<Void> optionalSpace() {
        return space().ignore().option(null);
    }

    private AbstractParserCombinator<Expr> primary() {
        OrParser<Character> sign = symbol('+').or(symbol('-'));
        OneOfParser digit1To9 = oneOf("123456789");
        OneOfParser digit0To9 = oneOf("0123456789");
        StringAccumulator digitAccumulate = digit0To9.accumulate();
        Mapper<Pair<Character, String>, String> integer = digit1To9
                .and(digitAccumulate.option("")).concat();
        CharParser point = symbol('.');
        Mapper<Pair<Character, String>, String> fractional = point.and(digitAccumulate).concat();

        Mapper<Pair<String, String>, String> decimalGT1 = integer.and(fractional.option("")).convert(Pair::concat);
        Mapper<Pair<Character, String>, String> decimalLT1 = symbol('0').and(fractional).concat();

        Mapper<String, Double> decimal = decimalGT1.or(decimalLT1)
                .convert(Double::parseDouble);

//        Convertor<String, Double> decimal =
//                integer.or(symbol('0').convert(String::valueOf))
//                        .and(point).convert(pair -> pair.first() + pair.second())
//                        .and(frac).convert(pair -> pair.first() + pair.second())
//                        .convert(Double::parseDouble);
        Mapper<Pair<Character, Double>, Double> signedNumber =
                sign.option('+')
                        .and(decimal)
                        .convert(pair -> pair.first() == '-' ? -pair.second() : pair.second());

        return signedNumber.convert(PrimaryExpr::new);
    }

    private AbstractParserCombinator<BiExpr> op(char ch) {
        AbstractParserCombinator<Expr> operand = primary();
        CharParser op = symbol(ch);
        OptionParser<Void> sp = optionalSpace();
        return operand
                .takeLeft(sp)
                .takeLeft(op)
                .takeLeft(sp)
                .and(operand)
                .convert(pair -> BiExpr.with(ch, pair.first(), pair.second()));

    }

    private AbstractParserCombinator<BiExpr> mul() {
        return op('*');
    }

    private AbstractParserCombinator<BiExpr> div() {
        return op('/');
    }

    @Test
    public void testPrimary() {
        AbstractParserCombinator<Expr> primary = primary();
        Optional<ParseResult<Expr>> r1 = primary.parse(new ParserInput("0.123456"));
        assertTrue(r1.isPresent());
        assertEquals(0.123456, r1.get().value().eval());
        Optional<ParseResult<Expr>> r2 = primary.parse(new ParserInput("-0.123456"));
        assertTrue(r2.isPresent());
        assertEquals(-0.123456, r2.get().value().eval());
        Optional<ParseResult<Expr>> r3 = primary.parse(new ParserInput("12340"));
        assertTrue(r3.isPresent());
        assertEquals(12340, r3.get().value().eval());
    }

    @Test
    public void testMul() {
        Optional<ParseResult<BiExpr>> r1 = mul().parse(new ParserInput("2  *2"));
        assertTrue(r1.isPresent());
        assertEquals(4, r1.get().value().eval());
    }

    @Test
    public void testDiv() {
        Optional<ParseResult<BiExpr>> r1 = div().parse(new ParserInput("3.0 /\t8.0"));
        assertTrue(r1.isPresent());
        assertEquals(0.375, r1.get().value().eval());
    }

    private ChainLeftParser<Expr, Pair<Character, Expr>> term() {
        AbstractParserCombinator<Expr> primary = primary();
        OptionParser<Void> skipSpace = optionalSpace();
        OrParser<Character> operator = symbol('*').or(symbol('/'));
        ManyParser<Pair<Character, Expr>> rhs =
                pack(skipSpace, operator, skipSpace)
                        .and(primary)
                        .many();
        return primary
                .chainLeft(rhs, (lhs, pair) -> BiExpr.op(pair.first()).apply(lhs, pair.second()));
    }

    @Test
    public void testTerm() {
        ChainLeftParser<Expr, Pair<Character, Expr>> term = term();
        Optional<ParseResult<Expr>> r1 = term.parse(new ParserInput("12.34 * 56.78 / 90 "));
        assertTrue(r1.isPresent());
        assertEquals(12.34 * 56.78 / 90, r1.get().value().eval());
        Optional<ParseResult<Expr>> r2 = term.parse(new ParserInput("-1.234"));
        assertTrue(r2.isPresent());
        assertEquals(-1.234, r2.get().value().eval());
    }

    private ChainLeftParser<Expr, Pair<Character, Expr>> expr() {
        ChainLeftParser<Expr, Pair<Character, Expr>> term = term();
        OptionParser<Void> skip = optionalSpace();
        OrParser<Character> op = symbol('+').or(symbol('-'));
        TakeLeftParser<Character, Void> sep = pack(skip, op, skip);
        ManyParser<Pair<Character, Expr>> rhs = sep.and(term).many();
        return term.chainLeft(rhs, (lhs, pair) -> BiExpr.op(pair.first()).apply(lhs, pair.second()));
    }

    @Test
    public void testExpr() {
        ChainLeftParser<Expr, Pair<Character, Expr>> term = term();
        OptionParser<Void> skip = optionalSpace();
        OrParser<Character> op = symbol('+').or(symbol('-'));
        TakeLeftParser<Character, Void> sep = pack(skip, op, skip);
        ManyParser<Pair<Character, Expr>> rhs = sep.and(term).many();
        ChainLeftParser<Expr, Pair<Character, Expr>> expr = term.chainLeft(rhs, (lhs, pair) -> BiExpr.op(pair.first()).apply(lhs, pair.second()));

        String input = "1.23 + 4.5 * 6 - 7.8 / -9.01";

        Optional<ParseResult<Expr>> r1 = expr.parse(new ParserInput(input));
        assertTrue(r1.isPresent());
        assertEquals(1.23 + 4.5 * 6 - 7.8 / -9.01, r1.get().value().eval());
    }

    interface Expr {
        double eval();
    }

    static class UnsupportedOperatorException extends RuntimeException {
        public UnsupportedOperatorException(String s) {
            super(s);
        }
    }

    static abstract class BiExpr implements Expr {
        protected final Expr a, b;

        protected BiExpr(Expr a, Expr b) {
            this.a = a;
            this.b = b;
        }

        static BiExpr with(char op, Expr l, Expr r) {
            switch (op) {
                case '*':
                    return new MultiExpr(l, r);
                case '/':
                    return new DivExpr(l, r);
                case '+':
                    return new AddExpr(l, r);
                case '-':
                    return new SubExpr(l, r);
                default:
                    throw new UnsupportedOperatorException("unsupported operator: " + op);
            }
        }

        static BinaryOperator<Expr> op(char op) {
            switch (op) {
                case '*':
                    return MultiExpr::new;
                case '/':
                    return DivExpr::new;
                case '+':
                    return AddExpr::new;
                case '-':
                    return SubExpr::new;
                default:
                    throw new IllegalStateException("Unexpected value: " + op);
            }
        }
    }

    static class MultiExpr extends BiExpr {

        MultiExpr(Expr a, Expr b) {
            super(a, b);
        }

        @Override
        public double eval() {
            return a.eval() * b.eval();
        }
    }

    static class DivideByZeroException extends RuntimeException {
    }

    static class DivExpr extends BiExpr {

        protected DivExpr(Expr a, Expr b) {
            super(a, b);
        }

        @Override
        public double eval() {
            double b = this.b.eval();
            if (b == 0.0) {
                throw new DivideByZeroException();
            }
            return a.eval() / b;
        }
    }

    static class AddExpr extends BiExpr {
        protected AddExpr(Expr a, Expr b) {
            super(a, b);
        }

        @Override
        public double eval() {
            return a.eval() + b.eval();
        }
    }

    static class SubExpr extends BiExpr {

        protected SubExpr(Expr a, Expr b) {
            super(a, b);
        }

        @Override
        public double eval() {
            return a.eval() - b.eval();
        }
    }

    static class PrimaryExpr implements Expr {
        final double value;

        PrimaryExpr(double value) {
            this.value = value;
        }

        @Override
        public double eval() {
            return value;
        }
    }
}

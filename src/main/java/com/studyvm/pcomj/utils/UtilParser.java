package com.studyvm.pcomj.utils;

import com.studyvm.pcomj.parser.OneOfParser;

public interface UtilParser {
    OneOfParser digit0To9 = ParserBuilder.oneOf("0123456789");
    OneOfParser digit1To9 = ParserBuilder.oneOf("123456789");
}

package base.asserts
import base.utils.LogUtils
import org.apache.log4j.Logger
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert

import static org.hamcrest.Matchers.*
/**
 * 断言类，junit断言的简单封装
 */

class Assert {
    protected final static Logger logger = LogUtils.getLogger(Assert)

    static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        MatcherAssert.assertThat(actual, matcher)
    }

    static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        MatcherAssert.assertThat(reason, actual, matcher)
    }

    static <T> boolean verifyThat(T actual, Matcher<? super T> matcher) {
        try {
            assertThat(actual, matcher)
            return true
        } catch (AssertionError e) {
            logger.error(e.getStackTrace())
            return false
        }
    }

    static <T> boolean verifyThat(String reason, T actual, Matcher<? super T> matcher) {
        try {
            assertThat(reason, actual, matcher)
            return true
        } catch (AssertionError e) {
            logger.error(e.getStackTrace())
            return false
        }
    }


    static void assertGreater(int actual, int expect ) {
        assertGreater(null, actual, expect)
    }

    static void assertGreater(String reason = null, int actual, int expect = 0) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is larger than $expect"
        assertThat(message, actual, greaterThan(expect))
    }

    static void assertEquals(String reason = null, Object actual, Object expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is $expect"
        assertThat(message, actual, equalTo(expect))
    }

    static void assertNotEquals(String reason = null, Object actual, Object expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is not $expect"
        assertThat(message, actual, not(equalTo(expect)))
    }

    static void assertStringIsEmpty(String reason = null, String actual) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is empty"
        assertThat(message, actual.trim(), isEmptyString())
    }

    static void assertListEquals(String reason = null, List actual, List expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is $expect"
        assertThat(message, actual.sort(), equalTo(expect.sort()))
    }

    static void assertListContainsObject(String reason=null, List actual, Object expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual contains $expect"
        if(!actual.contains(expect)){
            throw new AssertionError(message)
        }
    }

    static void assertListNotContainsObject(String reason=null, List actual, Object expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual not contains $expect"
        if(actual.contains(expect)){
            throw new AssertionError(message)
        }
    }


    static void assertListContainsList(String reason=null, List actual, List expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual contains $expect"
        if(!actual.containsAll(expect)){
            throw new AssertionError(message)
        }
    }

    static void assertListNotContainsList(String reason=null, List actual, List expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual not contains $expect"
        if(isListNotContainsListAnyItem(actual,expect)){
            throw new AssertionError(message)
        }
    }

    static void assertObjectInList(String reason=null, Object actual, List expect){
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual in $expect"
        assertThat(message, actual, isIn(expect))

    }



    private static  boolean isListNotContainsListAnyItem(List list,List containsList){
        boolean hasContains=false
        containsList.each{
            if(list.contains(it))hasContains=true
        }
        return hasContains
    }

    static void assertListInList(String reason=null, List actual, List expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual in $expect"
        if(!expect.containsAll(actual)){
            throw new AssertionError(message)
        }
    }



    static void assertStringContains(String reason = null, String actual, String expect) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual contains $expect"
        assertThat(message, actual, containsString(expect))
    }

    static void assertTrue(String reason = null, boolean actual) {
        assertEquals(reason, actual, true)
    }

    static void assertFalse(String reason = null, boolean actual) {
        assertEquals(reason, actual, false)
    }

    static void assertNull(String reason = null, Object actual) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is null"
        assertThat(message, actual, nullValue())
    }

    static void assertNotNull(String reason = null, Object actual) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is not null"
        assertThat(message, actual, notNullValue())
    }

    static void assertListNotEmpty(String reason = null, List actual) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is not null"
        assertThat(message, actual, allOf(notNullValue(),not(hasSize(0))))
    }

    static void assertListIsEmpty(String reason = null, List actual) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is empty"
        assertThat(message, actual, allOf(notNullValue(),hasSize(0)))
    }

    static void assertStringIsNumber(String reason = null, Object actual) {
        String message
        if (reason)
            message = reason
        else
            message = "expcet $actual is a Number"
        def m=actual==~/\d+(\.\d+)?/
        assertTrue(message, m)
    }



}
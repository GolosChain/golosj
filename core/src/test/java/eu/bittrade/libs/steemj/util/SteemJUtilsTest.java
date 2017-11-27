package eu.bittrade.libs.steemj.util;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test some specific methods of the {@link SteemJUtils} class.
 *
 * @author <a href=\"http://steemit.com/@dez1337\">dez1337</a>
 */
public class SteemJUtilsTest {
    /**
     * Test if the {@link SteemJUtils#verifyJsonString(String)} method is
     * working correctly.
     */
    @Test
    public void testExtractLinksFromContent() {
        assertTrue(SteemJUtils.verifyJsonString("{\"menu\": {\"id\": \"file\", \"value\": \"File\", \"popup\": "
                + "{ \"menuitem\": [ {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"}, "
                + "{\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},"
                + "{\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}]}}}"));
        assertFalse(SteemJUtils.verifyJsonString("{\"menu\": {\"id\": \"file\", \"value\": \"File\", \"popup\": "
                + "{ \"menuitem\": [ {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"}, "
                + "{\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},"
                + "{\"value\": [\"Close\",] \"onclick\": \"CloseDoc()\"}]}}"));
    }

    @Test
    public void testFastDateFormatParse() throws ParseException {
        String date = "2017-11-26T20:51:21";
        long before = System.currentTimeMillis();
        long result = 0;
        for (int i = 0; i < 10_000_00; i++) {
            result = SteemJUtils.transformStringToTimestamp(date);
        }
        long after = System.currentTimeMillis();
        System.out.println(after - before);
        before = System.currentTimeMillis();
        long result1 = 0;
        FastDateFormat fdp = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss", TimeZone.getDefault(), Locale.getDefault());
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 10_000_00; i++) {
            c.setTime(fdp.parse(date + "GMT"));
            assertEquals(result, c.getTimeInMillis());
        }
        after = System.currentTimeMillis();
        System.out.println(after - before);

    }
}

package webGrude;

import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import webGrude.elements.WrongTypeForField;
import webGrude.http.BrowserClient;
import webGrude.http.GetException;
import webGrude.mappables.Foo;
import webGrude.mappables.TooManyResultsError;
import webGrude.mappables.WrongTypeError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrowserTest {

    private static final String RESOURCE_URL = Foo.class.getResource("Foo.html").toString();

    @Test
    public void testMappingFromResource() {
        final Foo foo = Browser.get(RESOURCE_URL, Foo.class);

        assertEquals("Title", foo.someContent.title);
        assertEquals("Lorem ipsum", foo.someContent.text);

        assertEquals("Nested content Title", foo.someNestedContent.getHeader());
        assertEquals("Nested content", foo.someNestedContent.content);

        assertEquals(2, foo.section.someRepeatingContent.size());
        assertEquals("bar baz", foo.section.someRepeatingContent.get(0));
        assertEquals("bar2 baz2", foo.section.someRepeatingContent.get(1));

        assertEquals("<p> Get content as <br> element </p>", foo.htmlContent.html());

        assertEquals("<a href=\"linkToBeExtracted1\">Some useless text</a> \n<a href=\"linkToBeExtracted2\">Some useless text</a>", foo.linksInnerHtml);
        assertEquals("<a href=\"./page2\">link to next page</a>", foo.linksOuterHtml);

        assertEquals("linkToBeExtracted1", foo.linksWithHref.get(0));
        assertEquals("linkToBeExtracted2", foo.linksWithHref.get(1));

        assertEquals(RESOURCE_URL + "/./page2", foo.nextPage.getLinkUrl());

        assertEquals("www.example.com", foo.linkList.get(0).getLinkUrl());
        assertEquals(RESOURCE_URL + "/./page3", foo.linkList.get(1).getLinkUrl());

        assertEquals("HEAD1", foo.repeatingContentsNoSurroundingTag.get(0).head);
        assertEquals("TAIL1", foo.repeatingContentsNoSurroundingTag.get(0).tail);
        assertEquals("HEAD2", foo.repeatingContentsNoSurroundingTag.get(1).head);
        assertEquals("TAIL2", foo.repeatingContentsNoSurroundingTag.get(1).tail);

        assertEquals(0, foo.doesNotExist.size());

        assertEquals(42, foo.getIntValue());
        assertEquals(42.24, foo.getFloatValue(), 0.001);
        assertEquals(3.1415, foo.fHref, 0.00001);
        assertTrue(foo.getBoolValue());

        assertEquals(41, foo.afterLoadValue);
    }

    @Test
    public void testUrlSubstitution() {
        Browser.setWebClient(new BrowserClient() {
            public String get(String url) {
                return "DUMMY";
            }

            public String post(String post, BasicNameValuePair... params) {
                return "DUMMY";
            }
        });
        Browser.get(PageWithParameterizedURL.class, "x", "y");
        assertEquals("http://www.foo.com/x/bar/y/baz", Browser.getCurentUrl());
    }

    @Test(expected = GetException.class)
    public void testUriInvalidFormat() {
        Browser.get("jnnkljbnkjb", Foo.class);
    }

    @Test(expected = GetException.class)
    public void testUriNotAccessible() {
        Browser.get("www.thisurldoesnotexis.bla.blabla", Foo.class);
    }

    @Test(expected = TooManyResultsException.class)
    public void tooManyResults() {
        Browser.get(RESOURCE_URL, TooManyResultsError.class);
    }

    @Test(expected = WrongTypeForField.class)
    public void testWrongType() {
        Browser.get(RESOURCE_URL, WrongTypeError.class);
    }

}
package webGrude;

import java.util.List;

import org.jsoup.nodes.Element;

import webGrude.annotations.Page;
import webGrude.annotations.Selector;
import webGrude.elements.Visitable;

@Page
public class Foo {
	@Selector("#some-content") static public class SomeContent {
		@Selector("h1") public String title;
		@Selector("div") public String text;
	}
	
	@Selector("#some-nested-content") static public class SomeNestedContent {
		@Selector("h1") public String header;
		@Selector("span") public String content;
	}

	@Selector("#section") static public class Section {
		@Selector(".some-repeating-content") public List<String> someRepeatingContent;
		public SomeNestedContent someNestedContent;
	}
	
	@Selector("#html-content") public Element htmlContent;
	@Selector("p a") public Visitable<Foo> nextPage;
	
	public SomeContent someContent;
	public SomeNestedContent someNestedContent;
	public Section section;

}

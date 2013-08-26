package org.wsp.parser;

import org.htmlparser.beans.StringBean;

public class StringExtractor {
	private String resource;

	/**
	 * Construct a StringExtractor to read from the given resource.
	 * 
	 * @param resource
	 *            Either a URL or a file name.
	 */
	public StringExtractor(String resource) {
		this.resource = resource;

	}

	/**
	 * Extract the text from a page.
	 * 
	 * @return The textual contents of the page.
	 * @param links
	 *            if <code>true</code> include hyperlinks in output.
	 * @exception ParserException
	 *                If a parse error occurs.
	 */
	public String extractStrings(boolean links) {
		StringBean sb;
		sb = new StringBean();
		sb.setLinks(links);
		sb.setURL(resource);

		return (sb.getStrings());
	}
}

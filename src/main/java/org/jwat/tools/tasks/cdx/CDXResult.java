package org.jwat.tools.tasks.cdx;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class CDXResult {

	protected File srcFile;

	protected String filename;

	protected long consumed = 0;

	protected List<CDXEntry> entries = new LinkedList<CDXEntry>();

	/**
	 * Get a list of cddx entries for this result.
	 * @return
     */
	public List<CDXEntry> getEntries() {
		return entries;
	}
}

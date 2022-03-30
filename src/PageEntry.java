/**
 * This lab was produced by Gayeon Park on April 2020
 */

public class PageEntry {
	private int processNumber;
	private int pageNumber;
	private int timeLoaded;
	
	PageEntry (int processNum, int pageNum) {
		processNumber = processNum;
		pageNumber = pageNum;
	}
	
	public int getProcessNumber() {
		return processNumber;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}
	
	public int getTimeLoaded() {
		return timeLoaded;
	}
	
	public void setTimeLoaded(int tLoaded) {
		timeLoaded = tLoaded;
	}	

	@Override
	public boolean equals(Object obj) {
		PageEntry other = (PageEntry) obj;
		if (pageNumber == other.getPageNumber() && processNumber == other.getProcessNumber())
			return true;
		return false;
	}

}

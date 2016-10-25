import java.util.Iterator;


/**
 * Simple but somewhat efficient implementation of IDnaStrand. \ This
 * implementation uses StringBuilders to represent genomic/DNA data.
 * 
 * @author ola
 * @date January 2008, modified and commented September 2008
 * @date October 2011, made myInfo a StringBuilder rather than a String
 * @date October 2011, modified to add new methods and remove old ones
 */

public class StringBuilderStrand implements IDnaStrand {
	
	private StringBuilder myInfo;
	private int myAppends;

	/**
	 * Create a strand representing an empty DNA strand, length of zero.
	 */
	public StringBuilderStrand() {
		// Syntactic trick: calls the other constructor (the one that
		// takes a String) with an empty String.
		this("");
	}

	/**
	 * Create a strand representing s. No error checking is done to see if s
	 * represents valid genomic/DNA data.
	 * 
	 * @param s
	 *            is the source of cgat data for this strand
	 */
	public StringBuilderStrand(String s) {
		initialize(s);
	}

	public IDnaStrand cutAndSplice(String enzyme, String splicee) {
		int pos = 0;
		int start = 0;
		StringBuilder search = myInfo;
		boolean first = true;
		IDnaStrand ret = null;

		/*
		 * The next line is very syntax-dense. .indexOf looks for the first
		 * index at which enzyme occurs, starting at pos. Saying pos = ...
		 * assigns the result of that operation to the pos variable; the value
		 * of pos is then compared against zero.
		 * 
		 * .indexOf returns -1 if enzyme can't be found. Therefore, this line
		 * is:
		 * 
		 * "While I can find enzyme, assign the location where it occurs to pos,
		 * and then execute the body of the loop."
		 */
		while ((pos = search.indexOf(enzyme, pos)) >= 0) {
			if (first) {
				ret = getInstance(search.substring(start, pos));
				first = false;
			} else {
				ret.append(search.substring(start, pos));

			}
			start = pos + enzyme.length();
			ret.append(splicee);
			pos++;
		}

		if (start < search.length()) {
			// NOTE: This is an important special case! If the enzyme
			// is never found, return an empty String.
			if (ret == null) {
				ret = getInstance("");
			} else {
				ret.append(search.substring(start));
			}
		}
		return ret;
	}

	/**
	 * Initialize this strand so that it represents the value of source. No
	 * error checking is performed.
	 * 
	 * @param source
	 *            is the source of this enzyme
	 */
	public void initialize(String source) {
		myInfo = new StringBuilder(source);
		myAppends = 0;
	}

	/**
	 * Returns the number of nucleotides/base-pairs in this strand.
	 */
	public long size() {
		return myInfo.length();
	}

	@Override
	public String toString() {
		return myInfo.toString();
	}

	public String strandInfo() {
		return this.getClass().getName();
	}

	/**
	 * Simply append a strand of dna data to this strand. No error checking is
	 * done. This method isn't efficient; it doesn't use a StringBuilder or a
	 * StringBuffer.
	 * 
	 * @param dna
	 *            is the String appended to this strand
	 */
	public IDnaStrand append(String dna) {
		myInfo.append(dna);
		myAppends++;
		return this;
	}

	public IDnaStrand reverse() {
		StringBuilder copy = new StringBuilder(myInfo);
		StringBuilderStrand ss = new StringBuilderStrand();
		copy.reverse();
		ss.myInfo = copy;
		return ss;
	}

	public String getStats() {
		return String.format("# append calls = %d", myAppends);
	}

	@Override
	public Iterator<Character> iterator() {
		return  new CharDnaIterator(this);
	}
	public char charAt(int index) {
		return myInfo.charAt(index);
	}
	
	@Override
	public IDnaStrand getInstance(String source) {
		return new StringBuilderStrand(source);
	}
}

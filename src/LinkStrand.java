import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.transform.Source;

public class LinkStrand implements IDnaStrand {

	private class Node {
		String info;
		Node next;

		public Node(String s) {
			info = s;
			next = null;
		}
	}

	private Node myFirst, myLast;
	private long mySize;
	private int myAppends;
	Node myList;
	int count;

	public LinkStrand() {
		this("");
	}

	public LinkStrand(String s) {
		initialize(s);
	}

/*	@Override
	public Iterator<Character> iterator() {
		// TODO Auto-generated method stub
		return null;
	}*/

	@Override
	public IDnaStrand cutAndSplice(String enzyme, String splicee) { //add an exception for it append > 0 ot myFirst.next != null
		if (myAppends > 0) {
			throw new UnsupportedOperationException();
		}
		int pos = 0;
		int start = 0;
		StringBuilder search = new StringBuilder(myFirst.info);
		boolean first = true;
		IDnaStrand ret = null;

		// code identical to StringStrand, both String and StringBuilder
		// support .substring and .indexOf

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

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return mySize;
	}

	@Override
	public void initialize(String source) {
		myFirst = new Node(source);
		myLast = myFirst;
		myAppends = 0;
		mySize = source.length();
		count =0;
		myList = myFirst;
		// TODO Auto-generated method stub

	}

	@Override
	public IDnaStrand getInstance(String source) {
		// TODO Auto-generated method stub
		return new LinkStrand(source);
	}

	@Override
	public IDnaStrand append(String dna) {
		// TODO Auto-generated method stub
		Node nextnode = new Node(dna);
		myLast.next = nextnode;
		myLast = myLast.next;
		mySize += dna.length();
		myAppends++;
		return this;
	}

	@Override
	public IDnaStrand reverse() {
		// TODO Auto-generated method stub
		LinkStrand revStrand = new LinkStrand("");
		if (myFirst == null) return this;
		Node rev = null;
		Node list = myFirst;
		while (list != null) {
			Node temp = list.next;
			list.next = rev;
			rev = list;
			list = temp;
		}
		
		revStrand.myFirst = rev;
		Node postrev = revStrand.myFirst;
		while (postrev != null) {
			StringBuilder reverse = new StringBuilder(postrev.info);
			reverse.reverse();
			postrev.info = 	reverse.toString();

			postrev = postrev.next;
			
		}
		return revStrand;
	}

	@Override
	public String getStats() {
		return String.format("# appends = %d", myAppends);
	}

	@Override
	public char charAt(int index) {
		// TODO Auto-generated method stub
		if (count > index){
			count = 0;
			myList = myFirst;
		}
		while (count + myList.info.length() <= index){
			count += myList.info.length();
			myList = myList.next;
		}
		return myList.info.charAt(index - count);
	}

	public String toString() {
		Node current = myFirst;
		StringBuilder source = new StringBuilder();
		while (current != null) {
			source.append(current.info);
			current = current.next;
		}
		return source.toString();
	}

}

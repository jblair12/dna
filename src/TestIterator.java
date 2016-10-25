import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Testing iterator
 * @author ola
 *
 */
public class TestIterator {
	private IDnaStrand myStrand;
	
	public IDnaStrand getNewStrand(String s) {
		return new StringStrand(s);
		// return new LinkStrand(s);
		//return new StringBuilderStrand(s);
	}

	
	@Before
	public void setUp() throws Exception {
		myStrand = getNewStrand("cgat");
		for(int k=0; k < 10000; k++){
			myStrand.append("cgat");
		}
	}

	@Test
	public void testRandomIndexes() {
		Random rand = new Random(12356);
		char[] arr = {'c','g','a','t'};
		
		for(int k=0; k < 30; k++) {
			int index = rand.nextInt((int)myStrand.size());
			char ch = myStrand.charAt(index);
			assertTrue(k+"-th index is "+index,ch == arr[index % 4]);
		}
	}
	
	@Test(timeout=10000)
	public void testPerformanceForward(){
		char[] arr = {'c','g','a','t'};
		ArrayList<Double> ftimes = new ArrayList<Double>();
		int max = (int) myStrand.size()-1;
		int[] sizes = {max/4, max/2, 3*max/4, max};
		
		for(int ss=0; ss < sizes.length; ss++) {
			int size = sizes[ss];
			double start = System.nanoTime();
			for(int k=0; k < size; k++) {
				char ch = myStrand.charAt(k);
				assertTrue(k+"-th char error",ch == arr[k % 4]);
			}
			double end = System.nanoTime();
			double time = (end-start)/1e9;
			ftimes.add(time);
		}
		double diff = ftimes.get(1) - ftimes.get(0);
		for(int k=1; k < ftimes.size(); k++) {
			double f1 = ftimes.get(k-1);
			double f2 = ftimes.get(k);
			double idiff = f2 - f1;
			double abd = Math.abs(idiff-diff);
			String label = String.format("%d and %d with %2.3g and %2.3g", k-1,k,f1,f2);
			assertTrue(label,abd < 0.5);

		}
	}
	
	@Test
	public void testPerformanceBackwards(){
		char[] arr = {'c','g','a','t'};
		ArrayList<Double> rtimes = new ArrayList<Double>();
		
		if (! myStrand.strandInfo().contains("Link")) return;

		int max = (int) myStrand.size()-1;
		int[] sizes = {max/4, max/2, 3*max/4, max};
		for(int ss=sizes.length-1; ss >= 0; ss--) {
			int size = sizes[ss];
			double start = System.nanoTime();
			for(int k= size; k >= 0; k--){
				char ch = myStrand.charAt(k);
				assertTrue(k+"-th char error",ch == arr[k % 4]);
			}
			double end = System.nanoTime();
			double rtime = (end-start)/1e9;
			rtimes.add(rtime);
		}
		
		
		double zero = rtimes.get(rtimes.size()-1);
		double half = rtimes.get(2);
		for(int k=1; k < sizes.length; k++) {
			double f1 = rtimes.get(rtimes.size()-k-1);
			double ratio = sizes[k]*1.0/sizes[0];
			double low = (ratio -0.5)*(ratio - 0.5);
			double high = (ratio +0.5)*(ratio + 0.5);
			double runratio = f1/zero;
			String label = String.format("%d to %d", sizes[k],sizes[0]);
			assertTrue(label+ " low",runratio > low);
			assertTrue(label+ " high", runratio < high);
		}
		
	}
}

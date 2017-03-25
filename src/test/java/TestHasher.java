/*
JUnit tests for Hasher class
*/

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestForHasher {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test_classHasher1() {
		//to find the Hash password for password = abcd;
		String c="$2a$10$4rKRs4BEoCHTD.V1OqG5G.K0Ih5rjGZF8Lap1WSXRYiUIvkFSP4Qq";
		Hasher.hashPass("abcd");
		System.out.println(	Hasher.hashPass("abcd"));
		//assertEquals(c, Hasher.hashPass("abcd"));
	}
    @Test
	public void test_classHarsher2() {
		//to check if the previous Hash password is equal to newly generated Hass Password;
		String c="$2a$10$4rKRs4BEoCHTD.V1OqG5G.K0Ih5rjGZF8Lap1WSXRYiUIvkFSP4Qq";
		Hasher.hashPass("abcd");
		System.out.println(	Hasher.hashPass("abcd"));
		assertEquals(c, Hasher.hashPass("abcd"));
	}
    @Test
	public void test_classcheckPass1() {
		//to check boolean class if inputted Hash Password is equal to the Hash Password of abcd;
		String c="$2a$10$4rKRs4BEoCHTD.V1OqG5G.K0Ih5rjGZF8Lap1WSXRYiUIvkFSP4Qq";
		//	System.out.println(	Hasher.hashPass("abcd"));
		assertEquals(true,Hasher.checkPass("abcd",c));
	}

}

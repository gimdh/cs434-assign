package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
   test("adding ints") {
     assert(1 + 2 === 3)
   }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }


  test("singletonSet(1) contains 1") {
    new TestSets {
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  trait LargeTestSets extends TestSets {
    val s = union(s1, s2)
    val t = union(s2, s3)
    val a = union(s, s3)
  }


  test("intersect only contains duplicate element") {
    new LargeTestSets {
      val r = intersect(s, t)

      assert(!contains(r, 1), "Intersect 1")
      assert(contains(r, 2), "Intersect 2")
      assert(!contains(r, 3), "Intersect 3")
    }
  }

  test("diff only contains non-duplicate element") {
    new LargeTestSets {
      val r = diff(s, t)

      assert(contains(r, 1), "Diff 1")
      assert(!contains(r, 2), "Diff 2")
      assert(!contains(r, 3), "Diff 3")
    }
  }

  test("filter only contains elements satisfy filter") {
    new LargeTestSets {
      val r = filter(a, (x: Int) => x % 2 != 0)

      assert(contains(r, 1), "Filter 1")
      assert(!contains(r, 2), "Filter 2")
      assert(contains(r, 3), "Filter 3")
    }
  }

  test("forall is only true when all elements satisfy predicate") {
    new LargeTestSets {
      assert(forall(a, (x: Int) => x > 0), "Filter x > 0")
      assert(!forall(a, (x: Int) => x < 2), "Filter x < 2")
    }
  }

  test("exists is true when element satisfies given predicate exists") {
    new LargeTestSets {
      assert(exists(a, (x: Int) => x > 2), "Exists x > 2")
      assert(!exists(a, (x: Int) => x < 0), "Exists x < 0")
    }
  }

  test("map transforms set according to given function") {
    new LargeTestSets {
      val r = map(a, (x: Int) => x + 1)

      assert(!contains(r, 1), "Map 1")
      assert(contains(r, 2), "Map 2")
      assert(contains(r, 3), "Map 3")
      assert(contains(r, 4), "Map 4")
    }
  }
}

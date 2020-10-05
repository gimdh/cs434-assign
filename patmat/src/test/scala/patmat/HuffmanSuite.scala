package patmat

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {

  trait TestTrees {
    val t1: CodeTree = Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5)

    /* t2: a-00, b-01, d-1 */
    val t2: CodeTree = Fork(Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5), Leaf('d', 4), List('a', 'b', 'd'), 9)

  }

  trait TestChars {
    val c1: List[Char] = List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd')
    val c2: List[Char] = List('a', 'd', 'b', 'b', 'a', 'd', 'd')
    val b2: List[Bit] = List(0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1)
  }


  //weight
  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("weight of a leaf") {
    assert(weight(Leaf('c', 3)) === 3)
  }

  //chars
  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a', 'b', 'd'))
    }
  }

  test("chars of a leaf") {
    assert(chars(Leaf('c', 3)) === List('c'))
  }

  //times
  test("times for some character list") {
    new TestChars {
      assert(times(c1).toSet === List(('h', 1), ('e', 1), ('o', 2), ('l', 3), ('d', 1), (' ', 1), ('w', 1), ('r', 1), (',', 1)).toSet)
    }
  }

  //string2chars
  test("string2chars(\"hello, world\")") {
    new TestChars {
      assert(string2Chars("hello, world") === c1)
    }
  }

  //makeOrderedLeafList
  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 3)))
  }

  //singleton
  test("singleton for some CodeTree lists") {
    new TestTrees {
      assert(singleton(List(t1, t2)) === false)
      assert(singleton(List(t1)))
    }
  }

  //combine
  test("combine of small or empty leaf list") {
    val smallList = List(Leaf('a', 1))
    val emptyList = List()
    assert(combine(smallList) === smallList)
    assert(combine(emptyList) === emptyList)
  }

  test("combine of some leaf list") {
    val leafList = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leafList) === List(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Leaf('x', 4)))
  }

  //createCodeTree
  test("createCodeTree for some text") {
    new TestTrees {
      assert(createCodeTree(List('a', 'b', 'a', 'b', 'b')) === t1)
    }
  }

  //decode
  test("decode some code") {
    new TestTrees with TestChars {
      assert(decode(t2, b2) === c2)
    }
  }

  //encode
  test("encode some code") {
    new TestTrees with TestChars {
      assert(encode(t2)(c2) === b2)
    }
  }

  trait TestIpsum {
    val loremIpsum: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    val ipsumChars = string2Chars(loremIpsum)
    val ipsumTree = createCodeTree(ipsumChars)
  }

  //decode / encode
  test("encoded then decoded text should be identical") {
    new TestIpsum {
      assert(decode(ipsumTree, encode(ipsumTree)(ipsumChars)) === ipsumChars)
    }
  }

  //quickEncode
  test("quickEncode some code") {
    new TestTrees with TestChars {
      assert(quickEncode(t2)(c2) === b2)
    }
  }

  //decode/ quickEncode
  test("quickEncoded then decoded text should be identical") {
    new TestIpsum {
      assert(decode(ipsumTree, quickEncode(ipsumTree)(ipsumChars)) === ipsumChars)
    }
  }
}

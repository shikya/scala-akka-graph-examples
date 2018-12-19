import com.shrikant.example.CubeCalculator
import org.scalatest.FunSuite

class CubeCalculatorTest extends FunSuite {
  test("CubeCalculatorTest.cube") {
    assert(CubeCalculator.cube(2) == 8)
  }
}

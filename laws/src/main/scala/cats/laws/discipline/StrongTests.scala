package cats
package laws
package discipline

import cats.functor.Strong
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._

trait StrongTests[F[_, _]] extends ProfunctorTests[F] {
  def laws: StrongLaws[F]

  def strong[A: Arbitrary, B: Arbitrary, C: Arbitrary, D: Arbitrary, E: Arbitrary, G: Arbitrary](implicit
    ArbFAB: Arbitrary[F[A, B]],
    ArbFBC: Arbitrary[F[B, C]],
    ArbFCD: Arbitrary[F[C, D]],
    EqFAB: Eq[F[A, B]],
    EqFAG: Eq[F[A, G]],
    EqFAEDE: Eq[F[(A, E), (D, E)]],
    EqFEAED: Eq[F[(E, A), (E, D)]]
  ): RuleSet =
    new RuleSet {
      def name = "strong"
      def bases = Nil
      def parents = Seq(profunctor[A, B, C, D, E, G])
      def props = Seq(
        "strong first distributivity" -> forAll(laws.strongFirstDistributivity[A, B, C, D, E] _),
        "strong second distributivity" -> forAll(laws.strongSecondDistributivity[A, B, C, D, E] _)
      )
    }
}

object StrongTests {
  def apply[F[_, _]: Strong]: StrongTests[F] =
    new StrongTests[F] { def laws = StrongLaws[F] }
}
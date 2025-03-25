/**
* Term := Variable | Abstraction | Application
*/
public sealed interface Term permits Variable, Abstraction, Application {
    // de Bruijn index
    record Variable(int index) implements Term {}

    // \lambda m
    record Abstraction(Term m) implements Term {}

    // m n, i.e. m is applied to n
    record Apply(Term m, Term n) implements Term {}

    static Term shift(Term t, int dPlace, int cutoff) {
        return switch (t) {
            case Variable variable -> variable.index() < cutoff ? variable 
                : new Variable(variable.index() + dPlace);
            case Abstraction abs -> new Abstraction(shift(abs.m(),
                dPlace, cutoff + 1));
            case Apply apply -> new Apply(shift(apply.m(), dPlace, cutoff),
                shift(apply.n(), dPlace, cutoff));
        }
    }

    /**
    * [j -> s]t
    */
    static Term substitution(int j, Term s, Term t) {
        return switch (t) {
            case Variable variable -> variable.index()  == j ? s 
                : t;
            case Abstraction abs -> new Abstraction(
                substitution(j+1, shift(s, 1, 0), abs.m()));
            case Apply apply -> new Apply(substitution(j, s, apply.m()), substitution(j, s, apply.n()));
        }
    }

    static Term betaReduce(Abstraction abs, Variable v) {
        return shift(substitution(0, v, abs), -1, 0);
    }
}
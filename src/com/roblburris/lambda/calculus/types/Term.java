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

    static void substitution(Term t, Term s, int j) {
        return switch (t) {
            case Variable variable -> variable.index() < cutoff ? variable 
                : new Variable(variable.index() + dPlace);
            case Abstraction abs -> new Abstraction(shift(abs.m(),
                dPlace, cutoff + 1));
            case Apply apply -> new Apply(shift(apply.m(), dPlace, cutoff),
                shift(apply.n(), dPlace, cutoff));
        }
    }
}
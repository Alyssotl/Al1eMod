package codes.alyssotl.al1e.utils;

public class Pair<U, V> {

    /**
     * The first element of this pair
     */
    private final U first;

    /**
     * The second element of this pair
     */
    private final V second;

    /**
     * Constructs a new pair with the given values
     *
     * @param first  the first element
     * @param second the second element
     */
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    /*
     * Returns the first element of this pair
     */
    public U first() {
        return first;
    }

    /*
     * Returns the second element of this pair
     */
    public V second() {
        return second;
    }
}

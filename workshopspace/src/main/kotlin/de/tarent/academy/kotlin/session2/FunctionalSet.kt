package de.tarent.academy.kotlin.session2

typealias Set = (Int) -> Boolean
typealias Filter = (Int) -> Boolean

object FunctionSet {
    val emptySet: () -> Set = {
        { i: Int -> false }
    }

    val singleSet: (Int) -> Set = { element: Int ->
        { i: Int -> element == i }
    }

    val contains: (Set, Int) -> Boolean = { set, j -> set(j) }

    val union: (Set, Set) -> Set = { left , right ->
        { i: Int ->
            contains(left, i) || contains(right, i)
        }
    }

    val intersect: (Set, Set) -> Set = { left , right ->
        { i: Int ->
            contains(left, i) && contains(right, i)
        }
    }

    val diff: (Set, Set) -> Set = { left , right ->
        { i: Int ->
            (contains(left, i) && !contains(right, i)) || (!contains(left, i) && contains(right, i))
        }
    }

    val filterBound = 1000
    val filter: (Set, Filter) -> Set = { set , accept ->
        { i: Int ->
            set(i) && accept(i)
        }
    }

    // Implement equals to filter
    val exists: (Set, Filter) -> Boolean = { set, filter ->
        val filtered = filter(set, filter)
        (0..filterBound).any(filtered)
    }

    // Implement equals to filter
    val forall: (Set, Filter) -> Boolean = { set, filter ->
        val filtered = filter(set, filter)
        (0..filterBound).filter(set).all(filtered)
    }

}


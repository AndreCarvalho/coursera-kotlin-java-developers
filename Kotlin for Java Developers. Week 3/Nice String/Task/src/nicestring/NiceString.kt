package nicestring

fun String.isNice(): Boolean {
    fun isVowel(c: Char): Boolean = c in arrayOf('a', 'e', 'i', 'o', 'u')

    var badSubstrings = arrayOf("bu", "ba", "be")

    val pairs = this.zipWithNext()

    val doesNotContainBadSubstrings = badSubstrings.none { this.contains(it) }
    val hasAtLeast3Vowels = this.filter(::isVowel).length >= 3
    val containsDoubleLetter = pairs.any { (f, s) -> f == s }

    return arrayOf(doesNotContainBadSubstrings, hasAtLeast3Vowels, containsDoubleLetter)
            .filter {x -> x}
            .size >= 2
}
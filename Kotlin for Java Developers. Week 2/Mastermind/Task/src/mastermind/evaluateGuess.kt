package mastermind

import kotlin.math.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val secretArray = secret.toCharArray()
    val guessArray = guess.toCharArray()
    val matches = Array(4) {false}

    for (i in 0 until secretArray.size) {
        if (secretArray[i] == guessArray[i])
            matches[i] = true
    }
    val rightPosition = matches.filter { x -> x }.count()

    val remainingSecretArray = Array(4 - rightPosition) { Char.MIN_VALUE }
    val remainingGuessArray = Array(4 - rightPosition) { Char.MIN_VALUE }

    if (rightPosition < 4) { // not all are correct. let's evaluate remaining
        var pos = 0
        for (i in 0 until matches.size) {
            if (matches[i]) continue
            else {
                remainingGuessArray[pos] = guessArray[i]
                remainingSecretArray[pos] = secretArray[i]
                pos++
            }
        }
    }

    val remainSecretCharMap = toHashMapCount(remainingSecretArray)
    val remainGuessCharMap =  toHashMapCount(remainingGuessArray)

    var wrongPosition = 0
    remainGuessCharMap.forEach { char, count ->
        val countInGuess = remainSecretCharMap[char]
        wrongPosition += min(count, countInGuess ?: 0)
    }

    return Evaluation(rightPosition, wrongPosition)
}

private fun toHashMapCount(remainingGuessArray: Array<Char>): HashMap<Char, Int> {
    return remainingGuessArray.groupBy { it }.map { x -> Pair(x.key, x.value.count()) }
            .fold(HashMap()) { acc, pair ->
                acc[pair.first] = pair.second
                acc
            }
}

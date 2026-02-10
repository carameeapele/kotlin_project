package com.example.domain.service

class WordService {

    private val wordList = listOf(
        "Apple", "House", "Cat", "Sun", "Tree",
        "Car", "Fish", "Star", "Book", "Flower",
        "Dog", "Mountain", "Guitar", "Pizza", "Robot",
        "Butterfly", "Castle", "Rocket", "Umbrella", "Snowman"
    )

    private val usedWords = mutableListOf<String>()

    fun pickRandomWord(): String {
        val available = wordList.filter { it !in usedWords }
        val pool = available.ifEmpty {
            usedWords.clear()
            wordList
        }
        val word = pool.random()
        usedWords.add(word)
        return word
    }

    fun reset() {
        usedWords.clear()
    }

    fun getWordCount(): Int = wordList.size

    fun getUsedWordCount(): Int = usedWords.size

    fun getRemainingWordCount(): Int = wordList.size - usedWords.size
}
package com.example.domain.service

import com.example.common.model.ChatMessage

class GuessService {

    fun isCorrectGuess(guess: String, currentWord: String): Boolean {
        if (currentWord.isEmpty()) return false
        return guess.trim().equals(currentWord, ignoreCase = true)
    }

    fun createChatMessage(
        senderId: String,
        senderName: String,
        text: String
    ): ChatMessage {
        return ChatMessage(
            senderId = senderId,
            senderName = senderName,
            text = text.trim()
        )
    }

    fun createCorrectGuessMessage(
        senderId: String,
        senderName: String
    ): ChatMessage {
        return ChatMessage(
            senderId = senderId,
            senderName = senderName,
            text = "",
            isCorrectGuess = true,
            isSystemMessage = true
        )
    }

    fun createSystemMessage(text: String): ChatMessage {
        return ChatMessage(
            senderId = "system",
            senderName = "System",
            text = text,
            isSystemMessage = true
        )
    }
}
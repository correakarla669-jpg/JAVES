package com.javes.app.ui

/** Estados visuais do assistente. */
enum class JarvisState(val label: String) {
    IDLE("AGUARDANDO"),
    LISTENING("OUVINDO"),
    PROCESSING("PROCESSANDO"),
    SPEAKING("FALANDO")
}

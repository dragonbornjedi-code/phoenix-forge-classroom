package com.phoenixforge.classroom.teacher.domain.sage

/**
 * OpenAI-compatible providers and models with documented free tiers.
 * User only types an API key; URL and model come from these presets.
 */
data class AiProviderPreset(
    val id: String,
    val displayName: String,
    val baseUrl: String,
    val freeTierModels: List<String>,
    val signupHint: String,
)

object AiProviderCatalog {
    val providers: List<AiProviderPreset> = listOf(
        AiProviderPreset(
            id = "openrouter",
            displayName = "OpenRouter",
            baseUrl = "https://openrouter.ai/api/v1/chat/completions",
            freeTierModels = listOf(
                "google/gemma-2-9b-it:free",
                "meta-llama/llama-3.2-3b-instruct:free",
                "qwen/qwen-2.5-7b-instruct:free",
                "mistralai/mistral-7b-instruct:free",
            ),
            signupHint = "openrouter.ai — many :free models",
        ),
        AiProviderPreset(
            id = "gemini",
            displayName = "Google Gemini",
            baseUrl = "https://generativelanguage.googleapis.com/v1beta/openai/chat/completions",
            freeTierModels = listOf(
                "gemini-2.0-flash",
                "gemini-1.5-flash",
                "gemini-1.5-flash-8b",
            ),
            signupHint = "aistudio.google.com/apikey",
        ),
        AiProviderPreset(
            id = "groq",
            displayName = "Groq",
            baseUrl = "https://api.groq.com/openai/v1/chat/completions",
            freeTierModels = listOf(
                "llama-3.3-70b-versatile",
                "llama-3.1-8b-instant",
                "gemma2-9b-it",
            ),
            signupHint = "console.groq.com — fast free tier",
        ),
        AiProviderPreset(
            id = "cursor",
            displayName = "Cursor (OpenAI-compatible)",
            baseUrl = "https://api.cursor.com/v1/chat/completions",
            freeTierModels = listOf(
                "auto",
            ),
            signupHint = "Cursor dashboard — if your plan exposes an API key",
        ),
        AiProviderPreset(
            id = "codex",
            displayName = "OpenAI Codex / ChatGPT",
            baseUrl = "https://api.openai.com/v1/chat/completions",
            freeTierModels = listOf(
                "gpt-4o-mini",
            ),
            signupHint = "platform.openai.com — trial credits may apply",
        ),
        AiProviderPreset(
            id = "cline",
            displayName = "Cline / OpenRouter proxy",
            baseUrl = "https://openrouter.ai/api/v1/chat/completions",
            freeTierModels = listOf(
                "anthropic/claude-3.5-haiku",
                "google/gemma-2-9b-it:free",
            ),
            signupHint = "Use your OpenRouter key in Cline; same URL here",
        ),
        AiProviderPreset(
            id = "windsurf",
            displayName = "Windsurf (Codeium)",
            baseUrl = "https://api.codeium.com/v1/chat/completions",
            freeTierModels = listOf(
                "mistral-large",
            ),
            signupHint = "codeium.com — check Windsurf plan for API access",
        ),
        AiProviderPreset(
            id = "kiro",
            displayName = "Kiro / AWS Bedrock proxy",
            baseUrl = "https://bedrock-runtime.us-east-1.amazonaws.com/openai/v1/chat/completions",
            freeTierModels = listOf(
                "amazon.nova-lite-v1:0",
            ),
            signupHint = "AWS free tier — Bedrock OpenAI-compatible endpoint",
        ),
        AiProviderPreset(
            id = "together",
            displayName = "Together AI",
            baseUrl = "https://api.together.xyz/v1/chat/completions",
            freeTierModels = listOf(
                "meta-llama/Llama-3.2-3B-Instruct-Turbo",
            ),
            signupHint = "together.ai — signup credits",
        ),
        AiProviderPreset(
            id = "mistral",
            displayName = "Mistral",
            baseUrl = "https://api.mistral.ai/v1/chat/completions",
            freeTierModels = listOf(
                "mistral-small-latest",
            ),
            signupHint = "console.mistral.ai — free dev tier",
        ),
    )

    fun findByUrl(url: String): AiProviderPreset? =
        providers.firstOrNull { it.baseUrl == url.trim() }

    fun defaultProvider(): AiProviderPreset = providers.first()
}

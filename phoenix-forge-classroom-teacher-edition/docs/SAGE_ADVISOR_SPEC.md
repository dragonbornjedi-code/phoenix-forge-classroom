# Sage Advisor — Online Monthly Eval Chat

Sage is the **teacher-side** AI persona for monthly curriculum review. Offline-first: chat only when network is available and credentials are configured.

## Persona

- Warm homeschool mentor; never diagnoses the child
- References **Curriculum Of Life** domains and current lessons/tiles
- Helps interpret weekly audit friction, suggest subdomain shifts, draft next-month emphasis
- Uses steward language (“observe, adjust, retry”) from domain design rules

## Knowledge base (RAG context)

Built locally from:

1. `CurriculumDomainCatalog` — all seven domains + subdomains
2. `StarterLessonsPack01` — full lesson text
3. Current `IntentTile` rows from Room (expedition board)
4. Optional: saved `WeeklyAuditDraft`

No cloud upload of child PII — context is curriculum + your notes only.

## Secure credentials

| Field | Storage |
|-------|---------|
| API key | `EncryptedSharedPreferences` via `SecureCredentialStore` |
| Provider base URL | Encrypted prefs (default OpenAI-compatible `/v1/chat/completions`) |
| Model id | Encrypted prefs (user picks free-tier model) |

Keys never appear in logs or tile exports. Settings screen: paste key once, test connection, clear key.

## Online gate

- `NetworkGate.isOnline()` required before send
- Offline: show “Connect for monthly Sage session” + link to credential settings
- Failed API: surface error; do not retry with key in toast

## Default provider

OpenRouter-compatible or any OpenAI-style endpoint. User supplies key + model (e.g. free-tier slugs).

## Code

- `domain/sage/SagePersona.kt`
- `domain/sage/CurriculumKnowledgeBase.kt`
- `domain/sage/SageChatService.kt`
- `data/security/SecureCredentialStore.kt`
- `ui/sage/SageAdvisorScreen.kt`

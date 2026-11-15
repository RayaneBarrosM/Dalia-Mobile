import os
import re
from typing import Literal

from fastapi import FastAPI, HTTPException
from google import genai
from google.genai import types
from pydantic import BaseModel, Field

from prompts.system_prompt import SYSTEM_PROMPT

# Modelos disponíveis na sua conta, em ordem de prioridade
PRIMARY_MODEL = os.environ.get("GEMINI_MODEL", "gemini-3.5-flash")
FALLBACK_MODELS = [
    PRIMARY_MODEL,
    "gemini-3.5-flash",
    "gemini-3.5-flash-lite",
    "gemini-3.7-flash",
    "gemini-3.6-flash",
    "gemini-3.8-flash",
]
MODELS_TO_TRY = list(dict.fromkeys(FALLBACK_MODELS))

DEBUG = os.environ.get("DALIA_DEBUG", "false").lower() == "true"

MAX_TURNS = 20  # limita o histórico enviado (controle de custo/contexto)

TRIGGERS = re.compile(
    r"(me bate|me bateu|apanh|agred|espanc|ameaç|me machuc|viol[eê]ncia|abus|estupr|"
    r"medo (dele|do meu)|me controla|me proíbe|me tranc|vai me matar|maria da penha)",
    re.IGNORECASE,
)
HEALTH_URGENT = re.compile(
    r"(dor (muito )?(forte|intensa)|sangramento (muito )?(forte|intenso)|desmai|"
    r"febre alta|tontura forte|dor na barriga e (estou|acho que estou) gr[aá]vida)",
    re.IGNORECASE,
)
HEALTH_FOOTER = (
    "\n\n---\n**Esses sintomas merecem atenção médica.** Procure um pronto-atendimento "
    "ou UBS agora; em emergência, ligue **SAMU 192**."
)
SAFETY_FOOTER = (
    "\n\n---\n**Se você está em perigo:** ligue **190** (polícia) ou **180** "
    "(Central de Atendimento à Mulher, gratuito, 24h, sigiloso). "
    "Você também pode procurar uma Delegacia da Mulher."
)

app = FastAPI(title="Dalia API")
client = genai.Client(
    api_key=os.environ["GEMINI_API_KEY"],
    http_options=types.HttpOptions(timeout=15_000),  # 15s: se demorar, pula pro próximo modelo
)


class Message(BaseModel):
    role: Literal["user", "assistant", "model"]
    content: str = Field(min_length=1, max_length=4000)


class ChatRequest(BaseModel):
    messages: list[Message] = Field(min_length=1)


class ChatResponse(BaseModel):
    reply: str


@app.get("/health")
def health() -> dict:
    return {"status": "ok"}


@app.post("/chat", response_model=ChatResponse)
def chat(req: ChatRequest) -> ChatResponse:
    msgs = req.messages[-MAX_TURNS:]
    while msgs and msgs[0].role != "user":
        msgs = msgs[1:]
    if not msgs or msgs[-1].role != "user":
        raise HTTPException(400, "A última mensagem deve ser do usuário.")

    contents = [
        types.Content(
            role="user" if m.role == "user" else "model",
            parts=[types.Part(text=m.content)],
        )
        for m in msgs
    ]

    resp = None
    last_error = None

    config = types.GenerateContentConfig(
        system_instruction=SYSTEM_PROMPT,
        temperature=0.2,
        max_output_tokens=4096,
        safety_settings=[
            types.SafetySetting(category=c, threshold="BLOCK_ONLY_HIGH")
            for c in (
                "HARM_CATEGORY_HARASSMENT",
                "HARM_CATEGORY_DANGEROUS_CONTENT",
                "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                "HARM_CATEGORY_HATE_SPEECH",
            )
        ],
    )

    for model_name in MODELS_TO_TRY:
        try:
            if DEBUG:
                print(f"[DALIA] Tentando modelo: {model_name}", flush=True)

            resp = client.models.generate_content(
                model=model_name,
                contents=contents,
                config=config,
            )

            if resp and resp.text:
                if DEBUG:
                    print(f"[DALIA] Resposta gerada via {model_name}!", flush=True)
                break

            resp = None

        except Exception as e:
            last_error = e

            if DEBUG:
                print(
                    f"[DALIA] Falha em {model_name}: {str(e)[:200]}",
                    flush=True,
                )

            continue

    if not resp:
        raise HTTPException(502, f"Não foi possível responder no momento. Tente novamente.")

    reply = (resp.text or "").strip() or (
        "Desculpe, tive um probleminha aqui e não consegui te responder agora. Se estiver em perigo, ligue 190 ou 180."
    )

    user_text = " ".join(m.content for m in msgs if m.role == "user")
    if TRIGGERS.search(user_text) and "180" not in reply:
        reply += SAFETY_FOOTER
    if HEALTH_URGENT.search(msgs[-1].content) and "192" not in reply:
        reply += HEALTH_FOOTER

    return ChatResponse(reply=reply)

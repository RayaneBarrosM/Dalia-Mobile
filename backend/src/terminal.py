
"""Chat da Dália no terminal.

Uso: python -m src.terminal
Comandos: /limpar e /sair
"""

from fastapi import HTTPException
from src.main import ChatRequest, Message, chat

historico: list[Message] = []

print("Dália 💜")
print("Digite /limpar para apagar a conversa ou /sair para encerrar.\n")

while True:
    try:
        texto = input("Você: ").strip()
    except (EOFError, KeyboardInterrupt):
        print("\nAté logo! 💜")
        break

    if not texto:
        continue

    if texto == "/sair":
        print("Até logo! 💜")
        break

    if texto == "/limpar":
        historico.clear()
        print("Conversa apagada. 💜\n")
        continue

    historico.append(Message(role="user", content=texto))

    try:
        resposta = chat(ChatRequest(messages=historico)).reply

    except HTTPException as e:
        historico.pop()
        print(f"\nDália: Tive um probleminha para responder. Tente novamente daqui a pouco. 💜")

        if e.status_code >= 500:
            print("(Ocorreu um erro técnico. Consulte o terminal do servidor se necessário.)\n")
        else:
            print()

        continue

    historico.append(Message(role="assistant", content=resposta))
    print(f"\nDália: {resposta}\n")

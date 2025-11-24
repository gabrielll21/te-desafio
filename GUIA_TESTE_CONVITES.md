# 🎯 Guia de Teste - Convites de Desafio

## 📋 Pré-requisitos

Antes de testar os convites, você precisa:

1. ✅ **Ter pelo menos 2 usuários cadastrados**
2. ✅ **Os usuários devem ser amigos** (pedido de amizade aceito)
3. ✅ **Ter pelo menos 1 desafio criado** (pelo usuário que vai enviar o convite)

---

## 🚀 Passo a Passo para Testar

### **CENÁRIO 1: Enviar Convite pela Página de Amigos** ⭐ (NOVO)

Esta é a forma mais direta de desafiar um amigo:

#### 1. Preparação
1. Faça login como **Usuário A** (ex: joao@email.com)
2. Certifique-se de que você tem:
   - ✅ Pelo menos 1 amigo adicionado
   - ✅ Pelo menos 1 desafio criado

#### 2. Enviar Convite
1. Acesse a página de **Amigos**: `/friends` ou clique em "Amigos" no menu
2. Na lista de amigos, você verá um botão **"🎯 Desafiar"** ao lado de cada amigo
3. Clique no botão **"Desafiar"** do amigo que você quer desafiar
4. Um **modal** será aberto com:
   - **Campo "Selecione um desafio"**: Escolha um dos seus desafios criados
   - **Campo "Mensagem (opcional)"**: Adicione uma mensagem personalizada
5. Clique em **"📨 Enviar Convite"**

#### 3. Verificar Convite Enviado
1. Acesse `/convites/enviados` ou clique em "Convites" no menu → "Enviados"
2. Você deve ver o convite listado com status **"PENDENTE"**
3. O convite mostra:
   - Título do desafio
   - Nome do destinatário
   - Mensagem (se você adicionou)
   - Data de envio

---

### **CENÁRIO 2: Enviar Convite pela Página "Meus Desafios"**

#### 1. Preparação
1. Faça login como **Usuário A**
2. Acesse `/desafios/meus` ou clique em "Desafios" → "Meus Desafios"

#### 2. Enviar Convite
1. Na lista dos seus desafios, clique no botão **"Convidar Amigo"** de um desafio
2. Um **modal** será aberto
3. Selecione o amigo que você quer convidar
4. (Opcional) Adicione uma mensagem
5. Clique em **"📨 Enviar Convite"**

---

### **CENÁRIO 3: Receber e Responder Convite**

#### 1. Visualizar Convites Recebidos
1. Faça login como **Usuário B** (o amigo que recebeu o convite)
2. Acesse `/convites/recebidos` ou clique em "Convites" no menu → "Recebidos"
3. Você deve ver o convite recebido mostrando:
   - 📨 **Nome do remetente** (quem enviou)
   - 📧 **Email do remetente**
   - 🎯 **Título do desafio**
   - 📝 **Descrição do desafio**
   - 💬 **Mensagem personalizada** (se houver)
   - 📅 **Datas do desafio** (início e fim)
   - ⭐ **Dificuldade e pontos**
   - 📬 **Data de envio**

#### 2. Aceitar Convite
1. Na lista de convites recebidos
2. Clique no botão **"✓ Aceitar"** do convite
3. Você será redirecionado para a página de convites recebidos
4. O convite aceito **não aparecerá mais** na lista de pendentes

#### 3. Verificar Status (Usuário A)
1. Faça login novamente como **Usuário A**
2. Acesse `/convites/enviados`
3. O status do convite deve estar como **"ACEITO"**

---

### **CENÁRIO 4: Recusar Convite**

#### 1. Recusar
1. Faça login como **Usuário B**
2. Acesse `/convites/recebidos`
3. Clique no botão **"✗ Recusar"** do convite
4. Você será redirecionado para a página de convites recebidos

#### 2. Verificar Status (Usuário A)
1. Faça login como **Usuário A**
2. Acesse `/convites/enviados`
3. O status do convite deve estar como **"RECUSADO"**

---

### **CENÁRIO 5: Cancelar Convite Enviado**

#### 1. Cancelar
1. Faça login como **Usuário A**
2. Acesse `/convites/enviados`
3. Encontre um convite com status **"PENDENTE"**
4. Clique no botão **"Cancelar Convite"**
5. O convite será cancelado

#### 2. Verificar
1. O convite não aparecerá mais na lista de recebidos do destinatário
2. O status mudará para **"CANCELADO"**

---

## 🔍 Verificações Importantes

### ✅ Checklist de Validações

- [ ] **Não pode enviar convite para si mesmo**
  - Tente enviar um convite para você mesmo → deve dar erro

- [ ] **Não pode enviar convite para não-amigo**
  - Tente enviar convite para alguém que não é seu amigo → deve dar erro

- [ ] **Não pode enviar convite duplicado**
  - Envie um convite para um amigo
  - Tente enviar o mesmo convite novamente → deve dar erro "Já existe um convite pendente"

- [ ] **Contador de convites no menu**
  - O número de convites pendentes deve aparecer no menu/navbar
  - Deve atualizar automaticamente quando você recebe/aceita/recusa convites

---

## 🎨 Onde Encontrar as Funcionalidades

### **Enviar Convite:**
1. **Página de Amigos** (`/friends`)
   - Botão "🎯 Desafiar" ao lado de cada amigo
   - ⭐ **RECOMENDADO** - Forma mais direta

2. **Página "Meus Desafios"** (`/desafios/meus`)
   - Botão "Convidar Amigo" em cada desafio
   - Útil quando você quer convidar alguém para um desafio específico

### **Ver Convites:**
1. **Convites Recebidos** (`/convites/recebidos`)
   - Ver convites que você recebeu
   - Aceitar ou recusar

2. **Convites Enviados** (`/convites/enviados`)
   - Ver convites que você enviou
   - Cancelar convites pendentes

---

## 🐛 Problemas Comuns

### ❌ "Erro ao enviar pedido: O remetente não é amigo do destinatário"
**Solução:** Certifique-se de que os dois usuários são amigos. Verifique em `/friends` se o amigo aparece na lista.

### ❌ "Já existe um convite pendente para este desafio"
**Solução:** O destinatário já tem um convite pendente para este desafio. Aguarde ele aceitar/recusar ou cancele o convite anterior.

### ❌ "Você ainda não criou desafios"
**Solução:** Crie pelo menos um desafio antes de tentar enviar convites. Acesse `/desafios/novo`.

### ❌ "Você ainda não tem amigos"
**Solução:** Adicione amigos primeiro. Acesse `/friends/buscar` para procurar usuários e enviar pedidos de amizade.

---

## 📊 Verificação no Banco de Dados

Para verificar os convites diretamente no banco:

1. Acesse `http://localhost:8080/h2-console`
2. Execute:
```sql
-- Ver todos os convites
SELECT * FROM CONVITE_DESAFIO;

-- Ver convites pendentes
SELECT * FROM CONVITE_DESAFIO WHERE STATUS = 'PENDENTE';

-- Ver convites aceitos
SELECT * FROM CONVITE_DESAFIO WHERE STATUS = 'ACEITO';

-- Ver detalhes completos (com nomes)
SELECT 
    cd.id,
    cd.status,
    r.nome as remetente,
    d.nome as destinatario,
    des.titulo as desafio,
    cd.mensagem,
    cd.criado_em
FROM CONVITE_DESAFIO cd
JOIN USUARIO r ON cd.remetente_id = r.id
JOIN USUARIO d ON cd.destinatario_id = d.id
JOIN DESAFIO des ON cd.desafio_id = des.id;
```

---

## ✅ Resumo do Fluxo Completo

```
1. Usuário A cria um desafio
   ↓
2. Usuário A vai em "Amigos" → Clica "Desafiar" no amigo
   ↓
3. Usuário A seleciona o desafio e envia convite
   ↓
4. Usuário B recebe notificação (contador no menu)
   ↓
5. Usuário B acessa "Convites Recebidos"
   ↓
6. Usuário B vê o convite e clica "Aceitar"
   ↓
7. Usuário A vê o status "ACEITO" em "Convites Enviados"
   ↓
8. Ambos podem participar do desafio!
```

---

## 🎯 Dica Final

**Teste com 2 navegadores diferentes** (ou modo anônimo):
- Navegador 1: Login como Usuário A
- Navegador 2: Login como Usuário B

Isso facilita muito o teste do fluxo completo de envio e recebimento de convites!


# 📋 Guia de Testes - Sistema de Desafios

## 🚀 Como Iniciar o Projeto

### Pré-requisitos
- Java 21 instalado
- Maven instalado (ou use o Maven Wrapper incluído)

### Passos para iniciar:

1. **Navegue até a pasta do projeto:**
   ```bash
   cd desafios/desafios
   ```

2. **Inicie a aplicação Spring Boot:**
   ```bash
   # Windows
   mvnw.cmd spring-boot:run
   
   # Linux/Mac
   ./mvnw spring-boot:run
   
   # Ou se tiver Maven instalado
   mvn spring-boot:run
   ```

3. **Aguarde a aplicação iniciar** (você verá mensagens no console)

4. **Acesse no navegador:**
   - URL: `http://localhost:8080`
   - Console H2 Database: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:file:./data/desafiosdb`
     - Username: `sa`
     - Password: (deixe em branco)

---

## ✅ Checklist de Testes

### 1️⃣ TESTE: Cadastro de Usuário

**Objetivo:** Criar uma nova conta no sistema

**Passos:**
1. Acesse `http://localhost:8080`
2. Você será redirecionado para `/login`
3. Clique em "Cadastrar" ou acesse `/cadastro`
4. Preencha o formulário:
   - **Nome:** João Silva
   - **Email:** joao@email.com
   - **Senha:** senha123 (mínimo 6 caracteres)
5. Clique em "Cadastrar"
6. Você deve ser redirecionado para `/login?cadastro=sucesso`

**Resultado esperado:**
- ✅ Mensagem de sucesso no login
- ✅ Usuário criado no banco de dados

**Teste adicional:**
- Tente cadastrar com o mesmo email novamente → deve mostrar erro "Este email já está cadastrado!"

---

### 2️⃣ TESTE: Login

**Objetivo:** Fazer login no sistema

**Passos:**
1. Na página de login (`/login`)
2. Preencha:
   - **Email:** joao@email.com
   - **Senha:** senha123
3. Clique em "Entrar"

**Resultado esperado:**
- ✅ Redirecionamento para `/` (home)
- ✅ Você está autenticado

**Teste adicional:**
- Tente login com senha errada → deve mostrar erro
- Tente login com email inexistente → deve mostrar erro

---

### 3️⃣ TESTE: Gerenciar Conta (Perfil)

**Objetivo:** Visualizar e editar informações do perfil

#### 3.1 Visualizar Perfil
1. Faça login
2. Acesse `/perfil` ou clique em "Meu Perfil"
3. Verifique se suas informações aparecem

#### 3.2 Editar Perfil
1. Acesse `/perfil/editar`
2. Altere o nome (ex: "João Silva Santos")
3. Clique em "Salvar"
4. Você deve ser redirecionado para `/perfil` com mensagem de sucesso

**Resultado esperado:**
- ✅ Nome atualizado com sucesso
- ✅ Mudanças refletidas na visualização do perfil

---

### 4️⃣ TESTE: Adicionar Amigo

**Objetivo:** Enviar e aceitar pedidos de amizade

#### 4.1 Criar Segundo Usuário
1. Faça logout (`/logout`)
2. Cadastre um novo usuário:
   - **Nome:** Maria Santos
   - **Email:** maria@email.com
   - **Senha:** senha123
3. Faça login com este novo usuário

#### 4.2 Enviar Pedido de Amizade
1. Faça login como **João** (joao@email.com)
2. Acesse `/friends` ou "Amigos"
3. Procure por "Enviar pedido de amizade" ou similar
4. Envie pedido para **Maria** (maria@email.com)

**Nota:** Dependendo da implementação da interface, você pode precisar:
- Buscar usuário por email
- Ou ter uma lista de usuários disponíveis

#### 4.3 Aceitar Pedido de Amizade
1. Faça logout
2. Faça login como **Maria** (maria@email.com)
3. Acesse `/friends/requests/incoming` ou "Pedidos Recebidos"
4. Você deve ver o pedido de amizade de João
5. Clique em "Aceitar"
6. Acesse `/friends` → João deve aparecer na lista de amigos

**Resultado esperado:**
- ✅ Pedido de amizade enviado
- ✅ Pedido aparece na lista de recebidos
- ✅ Após aceitar, amizade é criada
- ✅ Amigo aparece na lista de amigos

**Testes adicionais:**
- Tente enviar pedido para si mesmo → deve dar erro
- Tente enviar pedido duplicado → deve dar erro
- Teste recusar pedido
- Teste cancelar pedido enviado

---

### 5️⃣ TESTE: CRUD de Desafio

**Objetivo:** Criar, visualizar, editar e excluir desafios

#### 5.1 Criar Desafio
1. Faça login
2. Acesse `/desafios/novo` ou "Criar Desafio"
3. Preencha o formulário:
   - **Título:** Correr 5km por dia
   - **Descrição:** Desafio de corrida diária por 30 dias
   - **Data Início:** (escolha uma data futura)
   - **Data Final:** (escolha uma data 30 dias depois)
   - **Status:** ATIVO
   - **Pontuação Máxima:** 100
   - **Dificuldade:** 5 (1-10)
   - **Categoria:** (selecione uma categoria existente)
   - **Subcategoria:** (opcional)
4. Clique em "Salvar"

**Resultado esperado:**
- ✅ Desafio criado com sucesso
- ✅ Redirecionamento para lista de desafios
- ✅ Desafio aparece na lista

**Nota:** Se não houver categorias no banco, você pode precisar criar algumas manualmente via H2 Console ou criar um script de inicialização.

#### 5.2 Listar Desafios
1. Acesse `/desafios`
2. Verifique se seu desafio aparece na lista
3. Teste filtros (se implementados):
   - Busca por título
   - Filtro por categoria

#### 5.3 Visualizar Detalhes do Desafio
1. Na lista de desafios, clique em um desafio
2. Ou acesse `/desafios/{id}` (substitua {id} pelo ID do desafio)
3. Verifique se todas as informações aparecem corretamente

#### 5.4 Editar Desafio
1. Acesse `/desafios/{id}/editar` (substitua {id} pelo ID do seu desafio)
2. Altere alguns campos (ex: título, descrição)
3. Clique em "Salvar"
4. Verifique se as alterações foram aplicadas

**Resultado esperado:**
- ✅ Formulário pré-preenchido com dados atuais
- ✅ Alterações salvas com sucesso
- ✅ Redirecionamento para detalhes do desafio

**Teste de segurança:**
- Tente editar um desafio criado por outro usuário → deve dar erro de permissão

#### 5.5 Excluir Desafio
1. Acesse `/desafios/{id}/editar`
2. Clique em "Excluir" ou acesse `/desafios/{id}/excluir` via POST
3. Confirme a exclusão (se houver confirmação)
4. Verifique se o desafio foi removido da lista

**Resultado esperado:**
- ✅ Desafio excluído com sucesso
- ✅ Redirecionamento para lista de desafios
- ✅ Desafio não aparece mais na lista

---

### 6️⃣ TESTE: Enviar Desafio para Amigo

**Objetivo:** Enviar um convite de desafio para um amigo

**Pré-requisito:** Você e o amigo devem ser amigos (teste 4 concluído)

**Passos:**
1. Faça login como **João**
2. Crie um desafio (se ainda não tiver)
3. Acesse os detalhes do desafio (`/desafios/{id}`)
4. Procure por "Enviar para amigo" ou "Convidar amigo"
5. Ou acesse diretamente a funcionalidade de envio de convite
6. Selecione **Maria** como destinatário
7. (Opcional) Adicione uma mensagem personalizada
8. Clique em "Enviar Convite"

**Resultado esperado:**
- ✅ Convite enviado com sucesso
- ✅ Convite aparece em `/convites/enviados` (João)
- ✅ Convite aparece em `/convites/recebidos` (Maria)

**Testes adicionais:**
- Tente enviar convite para si mesmo → deve dar erro
- Tente enviar convite para não-amigo → deve dar erro
- Tente enviar convite duplicado → deve dar erro

---

### 7️⃣ TESTE: Aceitar/Recusar Convite de Desafio

**Objetivo:** Responder a convites de desafio recebidos

#### 7.1 Visualizar Convites Recebidos
1. Faça login como **Maria**
2. Acesse `/convites/recebidos`
3. Você deve ver o convite enviado por João

#### 7.2 Aceitar Convite
1. Na lista de convites recebidos
2. Clique em "Aceitar" no convite
3. Verifique se o status mudou para "ACEITO"

**Resultado esperado:**
- ✅ Convite aceito com sucesso
- ✅ Status atualizado
- ✅ Convite pode aparecer em "Convites Aceitos" (se houver essa visualização)

#### 7.3 Recusar Convite (teste alternativo)
1. João envia outro convite para Maria
2. Maria acessa `/convites/recebidos`
3. Clique em "Recusar"
4. Verifique se o status mudou para "RECUSADO"

#### 7.4 Cancelar Convite Enviado
1. Faça login como **João**
2. Acesse `/convites/enviados`
3. Clique em "Cancelar" em um convite pendente
4. Verifique se o status mudou para "CANCELADO"

---

### 8️⃣ TESTE: Registrar Progresso do Desafio

**Objetivo:** Registrar etapas e progresso em um desafio aceito

**Pré-requisito:** 
- Você deve ter aceitado um convite de desafio
- Ou ter criado um desafio

**Passos:**
1. Faça login como **Maria** (que aceitou o desafio)
2. Acesse `/desafios/{id}/progresso` (substitua {id} pelo ID do desafio)
3. Você deve ver:
   - Lista de progresso geral do desafio
   - Seu progresso pessoal
4. Para registrar novo progresso:
   - Selecione uma ação:
     - **INICIO:** Marca o início do desafio
     - **ETAPA:** Registra uma etapa intermediária
     - **CONCLUIDO:** Marca o desafio como concluído
   - (Opcional) Adicione uma nota/descrição
5. Clique em "Registrar Progresso"

**Resultado esperado:**
- ✅ Progresso registrado com sucesso
- ✅ Aparece na lista de progresso
- ✅ Histórico criado automaticamente
- ✅ Redirecionamento para a página de progresso

**Testes adicionais:**
- Registre múltiplos progressos
- Verifique se aparecem em ordem cronológica
- Verifique se o histórico está sendo criado

---

## 🔍 Verificação no Banco de Dados (H2 Console)

### Acessar H2 Console:
1. Acesse `http://localhost:8080/h2-console`
2. Preencha:
   - **JDBC URL:** `jdbc:h2:file:./data/desafiosdb`
   - **User Name:** `sa`
   - **Password:** (deixe em branco)
3. Clique em "Connect"

### Tabelas para verificar:

```sql
-- Ver usuários cadastrados
SELECT * FROM USUARIO;

-- Ver desafios criados
SELECT * FROM DESAFIO;

-- Ver amizades
SELECT * FROM AMIZADE;

-- Ver pedidos de amizade
SELECT * FROM PEDIDO_AMIZADE;

-- Ver convites de desafio
SELECT * FROM CONVITE_DESAFIO;

-- Ver progressos registrados
SELECT * FROM PROGRESSO;

-- Ver histórico
SELECT * FROM HISTORICO;
```

---

## 📝 Checklist Completo de Testes

Marque cada item conforme testar:

### Autenticação
- [ ] Cadastro de novo usuário
- [ ] Login com credenciais válidas
- [ ] Login com credenciais inválidas
- [ ] Logout

### Perfil
- [ ] Visualizar perfil
- [ ] Editar nome do perfil
- [ ] Validação de campos obrigatórios

### Amizades
- [ ] Enviar pedido de amizade
- [ ] Visualizar pedidos recebidos
- [ ] Visualizar pedidos enviados
- [ ] Aceitar pedido de amizade
- [ ] Recusar pedido de amizade
- [ ] Cancelar pedido de amizade
- [ ] Listar amigos

### Desafios (CRUD)
- [ ] Criar novo desafio
- [ ] Listar desafios
- [ ] Visualizar detalhes do desafio
- [ ] Editar desafio próprio
- [ ] Tentar editar desafio de outro usuário (deve falhar)
- [ ] Excluir desafio próprio
- [ ] Filtrar desafios por categoria
- [ ] Buscar desafios por título

### Convites de Desafio
- [ ] Enviar convite para amigo
- [ ] Visualizar convites recebidos
- [ ] Visualizar convites enviados
- [ ] Aceitar convite
- [ ] Recusar convite
- [ ] Cancelar convite enviado
- [ ] Tentar enviar convite para não-amigo (deve falhar)
- [ ] Tentar enviar convite duplicado (deve falhar)

### Progresso
- [ ] Visualizar progresso do desafio
- [ ] Registrar início do desafio
- [ ] Registrar etapa intermediária
- [ ] Registrar conclusão do desafio
- [ ] Adicionar notas ao progresso
- [ ] Ver histórico de progresso

---

## ⚠️ Problemas Comuns e Soluções

### Erro: "Categoria não encontrada"
**Solução:** Crie categorias no banco de dados via H2 Console:
```sql
INSERT INTO CATEGORIA (nome, descricao) VALUES 
('Fitness', 'Desafios relacionados a exercícios físicos'),
('Estudos', 'Desafios relacionados a aprendizado'),
('Criatividade', 'Desafios artísticos e criativos');
```

### Erro: "Usuário não encontrado"
**Solução:** Verifique se você está logado corretamente e se o email está correto

### Erro: "O remetente não é amigo do destinatário"
**Solução:** Certifique-se de que os usuários são amigos antes de enviar convites

### Página não encontrada (404)
**Solução:** Verifique se a aplicação está rodando e se a URL está correta

### Erro de compilação
**Solução:** 
1. Limpe o projeto: `mvn clean`
2. Recompile: `mvn compile`
3. Verifique se Java 21 está instalado: `java -version`

---

## 📊 Dados de Teste Sugeridos

### Usuários para teste:
1. **João Silva** - joao@email.com - senha123
2. **Maria Santos** - maria@email.com - senha123
3. **Pedro Costa** - pedro@email.com - senha123

### Categorias sugeridas:
- Fitness
- Estudos
- Criatividade
- Social
- Saúde

### Desafios de exemplo:
- "Correr 5km por dia por 30 dias"
- "Ler 1 livro por semana"
- "Fazer 100 flexões por dia"
- "Meditar 10 minutos diários"

---

## 🎯 Próximos Passos Após Testes

1. Verifique se todas as funcionalidades estão funcionando
2. Teste casos de erro e validações
3. Verifique a segurança (usuários não podem editar dados de outros)
4. Teste a performance com múltiplos usuários
5. Verifique se os dados estão sendo persistidos corretamente

---

**Boa sorte com os testes! 🚀**


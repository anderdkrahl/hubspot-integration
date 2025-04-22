# HubSpot OAuth Integration

## Objetivo do Projeto
Este projeto tem como objetivo integrar uma API local em Spring Boot com a API do HubSpot, utilizando autenticação OAuth2 para obter tokens de acesso. O sistema realiza a troca do código de autorização por tokens e utiliza o refresh token para renovar o acesso. Através dessa integração, é possível realizar operações, como a criação de contatos e o processamento de eventos de webhook, diretamente com a API do HubSpot.

## Requisitos Técnicos
- **Java 17**: Certifique-se de que a versão correta do Java esteja instalada;
- **Spring Boot**: O projeto utiliza o framework Spring Boot para gerenciar a estrutura e as dependências;
- **NGROK**: Usado para fornecer URLs públicas para testes de webhooks. Necessário para testar a integração com a API do HubSpot em ambientes de desenvolvimento.

## **Video demonstrando as configurações necessárias e o projeto executando**
[Integração HubSpot e Spring Boot](https://www.youtube.com/watch?v=z8NKEf-FCbE)  


## Requisitos do HubSpot
1. **Conta de desenvolvedor HubSpot**: 
   - Criar uma conta de desenvolvedor HubSpot para gerar as credenciais da API;
   - Configurar as URLs de redirecionamento e o escopo necessário para o aplicativo;
   - Configurar os webhooks no portal de desenvolvedor para eventos específicos (como criação de contatos);
  
2. **Conta normal do HubSpot**: 
   - Uma conta HubSpot regular será necessária para instalar o aplicativo e testar a integração.


## Como Executar o Projeto
1. **Clonar o projeto e configurar o NGROK**:
  ````
  git clone https://github.com/anderdkrahl/hubspot-integration.git
  ````
  - Criar uma conta para utilizar o NGROK e baixar o mesmo, seguir as instruções de instalação de acordo com o sistema operacional;
  ````
  https://ngrok.com/downloads/windows
  ````
  - Após se autenticar no NGROK, acesse o o dashboard e copie o authtoken;
  ````
  https://dashboard.ngrok.com/get-started/your-authtoken
  ````
  - No terminal, configurar o NGROK com o seu token;
  ````  
  ngrok authtoken <seu_auth_token>
  ngrok http 8080
  ````
  - O NGROK irá fornecer uma URL que será utilizada nos Webhooks, guarde essa URL para o próximo passo. Exemplo;
  ````
  https://fa60-b8e0-ae10.ngrok-free.app
  ````

2. **Configuração da conta desenvolvedor do Hubspot**:
  - Criar uma conta de desenvolvedor no Hubspot;
  - Criar um App;
  - Acessar a tab "Auth" dentro do app e adicionar a URL **localhost:8080/oauth-callback** em Redirect URLs;
  - Em Scope, adicionar os escopos **crm.objects.contacts.read, crm.objects.contacts.write, oauth**;
  - Em webhooks, criar um novo que escuta os processos do tipo **"contact.creation"**, apontar para a URL criada pelo NGROK e concatenar o endpoint do webhook, por exemplo:
  ````
  https://fa60-b8e0-ae10.ngrok-free.app/contacts/webhook
  ````

3. **Conta cliente do Hubspot**
  - Utilizar uma conta secundária que não é desenvolvedora, ela será utilizada apenas para fazer o nosso APP;
  - Pode ser utilizada uma conta nova ou uma qualquer.
  
4. **Configurar o application.properties do projeto**
  - Para que o fluxo de autenticação OAuth 2 e a criação de contatos funcione corretamente com o Hubspot, precisamos armazenar algumas informações que estão listadas no app da conta desenvolvedora: client-id e client-secret. Esses dois códigos vão ser armazenados dentro do arquivo src/main/resources/application.properties, por exemplo:
  ````
  hubspot.client-id= <client-id>
  hubspot.client-secret= <client-secret>
  ````
  
5. **Executar o projeto**
 - Após executar todos os passos acima, ter a conta desenvolvedora do hubspot autenticada e o arquivo application.properties preenchido corretamente, já é possível executar o projeto. Recomendo que a conta desenvolvedora fique autenticada em uma aba normal, para que seja possível acompanhar o histórico de requests realizados;
 - Utilize uma aba anônima para ignorar a sessão da conta desenvolvedor, nessa aba será possível autenticar em uma conta diferente(não desenvolvedora);
 - Pronto, agora será possível acessar os endpoints do projeto, recomendo começar por **localhost:8080/authorize** 


## Explicações sobre o que foi feito no projeto
O projeto foi construído utilizando basicamente Spring Boot, Spring Thymeleaf e Java. O spring Thymeleaf foi utilizado apenas para exibir páginas específicas com facilidade em alguns endpoints. O fluxo inteiro se baseia em estar autenticado ou não, todas as requests passam por um Interceptor que verifica se existe um token de acesso ou não, caso exista e esteja expirado, é feita a troca de tokens utilizando o refresh token antes de completar a requisição.
Alguns logs vão ser exibidos no terminal a fim didático, para que seja possível acompanhar quando o token está sendo criado, atualizando e também quando webhooks são utilizados.

## Endpoints Principais
### 1. **/authorize**
   - **Método HTTP**: `GET`
   - **Exemplo de uso**: Monta a URL necessária para autenticar o app no Hubspot e redireciona para esse link, no sucesso faz uma callback para o endpoint **/oauth-callback** enviando de volta um código.

### 2. **/oauth-callback**
   - **Método HTTP**: `GET`
   - **Exemplo de uso**: Recebe o código de autorização e realiza a troca por um token, através do método **exchangeCodeForToken** da classe **TokenService**.

### 3. **/contacts/create**
   - **Método HTTP**: `POST`
   - **Exemplo de uso**: Faz uma requisição POST para criar um contato no Hubspot, respeitando o cabeçalho Bearer e o Rate Limit. Recomendo fazer esse processo usando o Insomnia ou Postman, enviando o seguinte JSON para a url l**ocalhost:8080/contacts/create**:
   ````
   {
      "email": "randomemail@gmail.com",
      "firstname": "Mari",
      "lastname": "Ana"
   }
  ````

### 4. **/contacts/webhooks**
   - **Método HTTP**: `POST`
   - **Exemplo de uso**: O Hubspot fica verificando a criação de contatos, assim que ocorre uma, envia uma notificação para o endpoint. Isso é feito automaticamente pela API do Hubspot e não precisamos fazer nada localmente, apenas recebemos essa resposta e imprimimos no terminal quando recebido. Irá aparecer algo do tipo:
  ````
   Valid webhook: [{"eventId":566534713,"subscriptionId":3482289 ...}]
  ````

## Endpoints Extra
### 1. **/**
   - **Método HTTP**: `GET`
   - **Exemplo de uso**: Página inicial que informa se você está logado, só é acessível depois de autenticar.

### 2. **/unauthorized**
   - **Método HTTP**: `GET`
   - **Exemplo de uso**: Página responsável por informar ao usuário que ele não está logado.
     
### 3. **/contacts/list**
   - **Método HTTP**: `GET`
   - **Exemplo de uso**: Lista todos os contatos cadastrados no Hubspot em formato de JSON, respeitando autenticação e rate limit.

## Testes unitários
Foi criado um teste unitário utilizando JUnit 5 para o mecanismo de controle do rate limit, feito pela classe **RateLimitHandler**. O teste verifica se o sistema irá aguardar caso a API do Hubspot tenha atingido o seu Rate Limit.

## Considerações finais
O desenvolvimento do projeto foi baseado nas informações encontratadas nos docs do Hubspot, buscando implementar o fluxo authorization code flow, conforme solicitado no desafio. Não foram utilizadas libs extras além do Spring Thymeleaf. Um ponto de melhoria para o futuro seria utilizar o Spring Security, ele faz todo esse fluxo de autenticação quase automaticamente e também garante segurança reforçada no projeto. Até tentei utilizar porém não sei se encaixa com o desafio, além disso acrescenta outra camada de complexidade. Portanto resolvi seguir sem o Spring Security.

Descrição
Este serviço é responsável pela geração, armazenamento e gerenciamento dos relatórios do sistema de eventos. Ele se integra com outros módulos, como o módulo de eventos e inscrições, consumindo dados via API para gerar relatórios conforme necessidade.

Funcionalidades
Geração de relatórios personalizados.

Armazenamento e consulta de relatórios já gerados.

Autenticação via token JWT (caso esteja implementado).

Integração com módulos externos via REST API.

Tecnologias utilizadas
Java 17+

Spring Boot

Spring Data JPA / Hibernate

Banco de dados relacional (ex: MySQL, PostgreSQL)

Maven (para gerenciamento de dependências)

JWT para autenticação (se aplicável)

Como usar
Pré-requisitos
Java JDK 17 ou superior

Maven 3.6+

Banco de dados configurado (MySQL/PostgreSQL/etc)

Docker (opcional, caso queira rodar containerizado)

Rodando localmente
Clone o repositório:
```
git clone https://github.com/AndrielsonLTeza/relatorios.git
cd relatorios
```

Configure as propriedades do banco de dados e outras variáveis no arquivo src/main/resources/application.properties ou application.yml:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/relatoriosdb
spring.datasource.username=usuario
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
```
Build e execute a aplicação:

```
mvn clean install
mvn spring-boot:run
```
Acesse o serviço em http://localhost:8080

Configuração de ambiente
As principais configurações ficam no arquivo application.properties, onde você pode ajustar:

Porta do servidor

Configurações do banco de dados

URLs dos serviços externos para integração

Propriedades de logging

Endpoints principais
Os endpoints podem variar conforme o projeto, abaixo um exemplo típico:

Método	URL	Descrição
GET	/api/relatorios	Listar relatórios
GET	/api/relatorios/{id}	Obter relatório por ID
POST	/api/relatorios	Criar novo relatório
DELETE	/api/relatorios/{id}	Remover relatório


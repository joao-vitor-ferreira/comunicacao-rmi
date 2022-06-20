# Como utilizar o programa #

Primeiro se deve compilar, seguindo a seção Como compilar, e em seguida seguir os passos na seção Como executar.


## 1 - Como compilar ##

Execute na pasta raiz:

```sh

sh compile.sh

```


## 2 - Como executar ##

    Esta seção é composta por 3 passos: RMI, Server e Client.
    De preferência, execute em 3 terminais diferentes para que você veja claramente o que está ocorrendo.
    Não feche o terminal de nenhum deles enquanto não rodar o Client, pois dependendo do sistema estará parando o processo RMI ou o Server.

### 2.1 - RMI ###

O ```rmiregistry``` deve rodar na pasta onde está o binário do servidor, na porta 1099. É essencial para que o projeto com RMI funcione. Para isso, execute estes comandos:

```sh
cd server
rmiregistry 1099 & 
```

Faca isso para 3 servidores, em 3 portas diferentes.

### 2.2 - Server ###

Ainda na pasta ```server```, execute:

```sh
java WaveletServer
```

Você deverá ver a mensagem "Server ready".

### 2.3 - Client ###

Entre na pasta ```client```
```sh
cd ../client
```

Então, execute:

```sh
java WaveletClient [servidor1] [porta1] [servidor2] [porta2] [servidor3] [porta3]
```

# Como utilizar o programa #

Primeiro se deve compilar, seguindo a seção Como compilar, e em seguida seguir os passos na seção Como executar.


# 1 - Como compilar

Execute na pasta raiz:

sh compile.sh


# 2 - Como executar

    Esta seção é composta por 3 passos: RMI, Server e Client.
    De preferência, execute em 3 terminais diferentes para que você veja claramente o que está ocorrendo.
    Não feche o terminal de nenhum deles enquanto não rodar o Client, pois dependendo do sistema estará parando o processo RMI ou o Server.

# 2.1 - RMI

O rmiregistry deve rodar na pasta onde está o binário do servidor, na porta 1099.
É essencial para que o projeto com RMI funcione. Para isso, execute estes comandos:

cd server
rmiregistry 1099 & 


# 2.2 - Server

Ainda na pasta server, execute:

java BinServer

Você deverá ver a mensagem "Server ready".


# 2.3 - Client

Entre na pasta client

cd ../client

Então, execute:

java BinClient
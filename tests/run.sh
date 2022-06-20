#!/bin/bash

rmistart() {
    retorno=""
    exec rmiregistry 1099 &
}

bash -c 'retorno=$( rmistart )'
exit

echo $retorno


echo "Escolha s para servidor"
echo "Ou c para cliente"

read line

if [ $line == "c" ]
then
    exec cd client && java BinClient
else
    if [ $line == "s" ]
    then
        exec cd server && java BinServer
    else
        echo "Nenhuma das opções foram selecionadas"
    fi
fi
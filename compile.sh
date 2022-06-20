#!/bin/sh

# Compilar o servidor
javac $(find ./server/* | grep .java) -Xlint:deprecation
# Compilar o cliente
javac $(find ./client/* | grep .java) -Xlint:deprecation
cd server
rmic bin.Wavelet
cp bin/Wavelet_Stub.class ../client/bin

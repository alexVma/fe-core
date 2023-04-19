#!/bin/bash
ruta_original=`pwd`
pkill -9 -f fe-core-final
cd /home/siim/SIIM_FE/
rm /home/siim/recursos_siim/FE/log/*
echo "Inicia proceso de Factura electronica."
/usr/lib/jvm/java-8-oracle/bin/java -jar fe-core-final.jar 

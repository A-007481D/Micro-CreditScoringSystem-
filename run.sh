#!/bin/bash

# Microfinance Credit Scoring System - Run Script (Java 8)

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║   Système de Scoring Crédit - Microfinance Maroc              ║"
echo "║   Version 1.0 (Java 8)                                         ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Check if compiled
if [ ! -d "out" ]; then
    echo "Le projet n'est pas compilé. Exécution de compile.sh..."
    ./compile.sh
    if [ $? -ne 0 ]; then
        echo "✗ Erreur de compilation"
        exit 1
    fi
fi

# Check if PostgreSQL driver exists
if [ ! -f "lib/postgresql.jar" ]; then
    echo "✗ Driver PostgreSQL introuvable dans lib/"
    echo "Exécutez d'abord: ./compile.sh"
    exit 1
fi

# Run the application
echo "Démarrage de l'application..."
echo ""
java -cp "out:lib/postgresql.jar" com.microfin.Main

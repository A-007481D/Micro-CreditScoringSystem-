#!/bin/bash

# Microfinance Credit Scoring System - Compile Script (Java 8)
# This script compiles the project without Maven

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║   Compilation du Système de Scoring Crédit - Java 8           ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Create output directory
mkdir -p out

# Download PostgreSQL JDBC driver if not present
if [ ! -f "lib/postgresql-42.7.7.jar" ]; then
    echo "Téléchargement du driver PostgreSQL JDBC..."
    mkdir -p lib
    curl -L https://jdbc.postgresql.org/download/postgresql-42.7.7.jar -o lib/postgresql-42.7.7.jar
    echo "✓ Driver PostgreSQL téléchargé"
fi

# Compile all Java files
echo "Compilation des fichiers Java..."
find src/main/java -name "*.java" > sources.txt

javac -source 8 -target 8 \
    -d out \
    -cp "lib/postgresql-42.7.7.jar" \
    @sources.txt

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie!"
    rm sources.txt
    
    # Create manifest file
    echo "Main-Class: com.microfin.Main" > manifest.txt
    echo "Class-Path: lib/postgresql-42.7.7.jar" >> manifest.txt
    
    # Create JAR file
    echo "Création du fichier JAR..."
    jar cfm microfin.jar manifest.txt -C out .
    
    if [ $? -eq 0 ]; then
        echo "✓ Fichier JAR créé: microfin.jar"
        rm manifest.txt
    fi
    
    echo ""
    echo "Pour exécuter l'application:"
    echo "  ./run.sh"
    echo "ou"
    echo "  java -cp out:lib/postgresql-42.7.7.jar com.microfin.Main"
else
    echo "✗ Erreur de compilation"
    rm sources.txt
    exit 1
fi

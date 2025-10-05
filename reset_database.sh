#!/bin/bash

# Microfinance Credit Scoring System - Database Reset Script
# This script completely drops and recreates the database with all tables

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║   Réinitialisation de la Base de Données - Microfin           ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Database configuration
DB_NAME="microfin"
DB_USER="postgres"

echo "⚠️  ATTENTION: Cette opération va supprimer TOUTES les données!"
echo ""
read -p "Êtes-vous sûr de vouloir continuer? (oui/non): " confirmation

if [ "$confirmation" != "oui" ]; then
    echo "Opération annulée."
    exit 0
fi

echo ""
echo "1. Suppression de la base de données existante..."
dropdb -U $DB_USER $DB_NAME 2>/dev/null
echo "✓ Base de données supprimée (ou n'existait pas)"

echo ""
echo "2. Création d'une nouvelle base de données..."
createdb -U $DB_USER $DB_NAME
if [ $? -eq 0 ]; then
    echo "✓ Base de données créée avec succès"
else
    echo "✗ Erreur lors de la création de la base de données"
    exit 1
fi

echo ""
echo "3. Démarrage de l'application..."
echo "   (Les tables seront créées automatiquement)"
echo ""
echo "════════════════════════════════════════════════════════════════"
echo ""

# Run the application
./run.sh

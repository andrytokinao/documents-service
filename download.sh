#!/bin/bash

# --- Variables à modifier directement ---
GITHUB_API_URL="https://maven.pkg.github.com"
GITHUB_OWNER="andrytokinao"
GITHUB_REPO="documents-service"
GITHUB_PACKAGE="documents-service"
GITHUB_TOKEN=""
ENVIRONMENT="prod"

# --- Récupérer la version passée en paramètre ---
GITHUB_VERSION="$1"

if [ -z "$GITHUB_VERSION" ]; then
    echo "Usage: $0 <version>"
    echo "Exemple : $0 1.0.2"
    exit 1
fi

# --- Construction de l'URL de téléchargement ---
PACKAGE_URL="$GITHUB_API_URL/$GITHUB_OWNER/$GITHUB_REPO/com/kinga/documents-service/$GITHUB_PACKAGE/$GITHUB_VERSION/$GITHUB_PACKAGE-$GITHUB_VERSION.jar"

# Nom du fichier local
OUTPUT_FILE="$GITHUB_PACKAGE-$GITHUB_VERSION.jar"

echo "Téléchargement du package GitHub version $GITHUB_VERSION depuis : $PACKAGE_URL"

# --- Télécharger le package ---
curl -L -u "$GITHUB_OWNER:$GITHUB_TOKEN" -o "$OUTPUT_FILE" "$PACKAGE_URL"

if [ $? -eq 0 ]; then
    echo "Téléchargement terminé : $OUTPUT_FILE"
else
    echo "Erreur lors du téléchargement"
    exit 1
fi

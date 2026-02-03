# Guide Base de Données - Projet Maintenance

## 🐳 Configuration Docker

Ce projet utilise Docker pour gérer la base de données MySQL et phpMyAdmin. Tous les membres de l'équipe doivent utiliser la même configuration.

---

## 📋 Prérequis

- Docker Desktop installé et lancé
- Port 3306 (MySQL) et 8080 (phpMyAdmin) disponibles

---

## 🚀 Démarrage

### 1. Cloner le projet
```bash
git clone [URL_DU_REPO]
cd maintenance-app
```

### 2. Démarrer les containers Docker
```bash
docker-compose up -d
```

### 3. Vérifier que les containers tournent
```bash
docker ps
```

Vous devriez voir :
- `maintenance_mysql` (port 3306)
- `maintenance_phpmyadmin` (port 8080)

---

## 🌐 Accès à phpMyAdmin

**URL** : http://localhost:8080

**Identifiants** :
- **Serveur** : `mysql`
- **Utilisateur** : `root`
- **Mot de passe** : `root123`

Ou utilisez l'utilisateur applicatif :
- **Utilisateur** : `maintenance_user`
- **Mot de passe** : `maintenance_pass`

---

## 💾 Structure de la base

La base de données `maintenance_db` contient 3 tables :

### **techniciens**
- `id` : INT (clé primaire)
- `nom` : VARCHAR(100)
- `qualification` : VARCHAR(100)
- `disponibilite` : BOOLEAN

### **batiments**
- `id` : INT (clé primaire)
- `nom` : VARCHAR(100)
- `localisation` : VARCHAR(200)

### **interventions**
- `id` : INT (clé primaire)
- `technicien_id` : INT (clé étrangère)
- `batiment_id` : INT (clé étrangère)
- `date_intervention` : DATE
- `type_intervention` : VARCHAR(100)
- `statut` : ENUM('Planifiee', 'En cours', 'Terminee')
- `description` : TEXT

---

## 🔄 Réinitialiser la base de données

Si vous voulez repartir de zéro avec les données initiales :

```bash
# Arrêter et supprimer les containers
docker-compose down -v

# Redémarrer (le script init.sql sera réexécuté)
docker-compose up -d
```

---

## ⚙️ Configuration Java

L'application se connecte automatiquement à la base avec :
- **URL** : `jdbc:mysql://localhost:3306/maintenance_db`
- **Utilisateur** : `maintenance_user`
- **Mot de passe** : `maintenance_pass`

---

## 📝 Données de test

Le fichier `database/init.sql` contient :
- 4 techniciens (Jean Dupont, Kevin Leroy, Dani, Marie Curie)
- 3 bâtiments (A, B, C à différents emplacements)
- 3 interventions (différents statuts pour tester)

---

## 🛑 Arrêt des containers

```bash
# Arrêter sans supprimer les données
docker-compose stop

# Arrêter et supprimer (garde les données dans le volume)
docker-compose down

# Arrêter et TOUT supprimer (données incluses)
docker-compose down -v
```

---

## 🐛 Résolution de problèmes

### Erreur "port déjà utilisé"
```bash
# Voir ce qui utilise le port
lsof -i :3306
lsof -i :8080

# Arrêter le processus ou changer le port dans docker-compose.yml
```

### La base ne contient pas de données
```bash
# Vérifier les logs
docker logs maintenance_mysql

# Réinitialiser
docker-compose down -v
docker-compose up -d
```

### L'application Java ne se connecte pas
1. Vérifier que Docker tourne : `docker ps`
2. Tester la connexion : accéder à phpMyAdmin
3. Vérifier les identifiants dans `DatabaseConnection.java`

---

## 👥 Travail en équipe

**IMPORTANT** : 
- Ne modifiez PAS le fichier `docker-compose.yml` sans coordination
- Pour ajouter des données de test, modifiez `database/init.sql` et commitez
- Après un pull avec des changements SQL, faites : `docker-compose down -v && docker-compose up -d`

---

## ✅ Checklist avant le rendu

- [ ] Docker fonctionne sur toutes les machines de l'équipe
- [ ] phpMyAdmin accessible par tous
- [ ] Application Java se connecte sans erreur
- [ ] Toutes les fonctionnalités CRUD testées
- [ ] Script SQL à jour dans Git

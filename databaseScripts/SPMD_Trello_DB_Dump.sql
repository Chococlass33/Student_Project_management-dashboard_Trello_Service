CREATE DATABASE  IF NOT EXISTS `spmd-trello` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spmd-trello`;
-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: spmd-trello
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `action`
--

DROP TABLE IF EXISTS `action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `action` (
  `id` varchar(32) NOT NULL COMMENT 'This is the id of the action.',
  `idMember` varchar(32) NOT NULL COMMENT 'This is the id of the member that performed the action.',
  `type` varchar(36) NOT NULL COMMENT 'This is the type of action performed.',
  `data` json NOT NULL COMMENT 'This represents data related to the action.',
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'This is when this entry was created.',
  `dateLastModified` datetime DEFAULT NULL COMMENT 'This is when this entry was last modified.',
  PRIMARY KEY (`id`),
  KEY `idMember_idx` (`idMember`) /*!80000 INVISIBLE */,
  KEY `idType_idx` (`type`),
  CONSTRAINT `FK__ACTION__MEMBER` FOREIGN KEY (`idMember`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table holds information on actions performed by a particular member.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `action`
--

LOCK TABLES `action` WRITE;
/*!40000 ALTER TABLE `action` DISABLE KEYS */;
/*!40000 ALTER TABLE `action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `id` varchar(32) NOT NULL,
  `idOrganization` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `descData` json DEFAULT NULL,
  `shortLink` varchar(15) NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__BOARD__ORGANIZATION` (`idOrganization`),
  CONSTRAINT `FK__BOARD__ORGANIZATION` FOREIGN KEY (`idOrganization`) REFERENCES `organization` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This represents the board relation.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_membership`
--

DROP TABLE IF EXISTS `board_membership`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_membership` (
  `id` varchar(32) NOT NULL,
  `idBoard` varchar(32) NOT NULL,
  `idMember` varchar(32) NOT NULL,
  `memberType` varchar(10) NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__BOARD_MEMBERSHIP__MEMBER_idx` (`idMember`),
  KEY `FK__BOARD_MEMBERSHIP__BOARD` (`idBoard`),
  CONSTRAINT `FK__BOARD_MEMBERSHIP__BOARD` FOREIGN KEY (`idBoard`) REFERENCES `board` (`id`),
  CONSTRAINT `FK__BOARD_MEMBERSHIP__MEMBER` FOREIGN KEY (`idMember`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_membership`
--

LOCK TABLES `board_membership` WRITE;
/*!40000 ALTER TABLE `board_membership` DISABLE KEYS */;
/*!40000 ALTER TABLE `board_membership` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card` (
  `id` varchar(32) NOT NULL,
  `idList` varchar(32) NOT NULL,
  `idBoard` varchar(32) NOT NULL,
  `checkItemStates` json DEFAULT NULL,
  `closed` tinyint DEFAULT NULL,
  `dateLastActivity` datetime NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `descData` json DEFAULT NULL,
  `due` datetime DEFAULT NULL,
  `dueCompleted` tinyint DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `pos` float NOT NULL,
  `shortLink` varchar(15) NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__CARD__LIST_idx` (`idList`),
  KEY `FK__CARD__BOARD_idx` (`idBoard`),
  CONSTRAINT `FK__CARD__BOARD` FOREIGN KEY (`idBoard`) REFERENCES `board` (`id`),
  CONSTRAINT `FK__CARD__LIST` FOREIGN KEY (`idList`) REFERENCES `list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_label`
--

DROP TABLE IF EXISTS `card_label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_label` (
  `id` varchar(32) NOT NULL,
  `idCard` varchar(32) NOT NULL,
  `idLabel` varchar(32) NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__CARD_LABEL__LABEL_idx` (`idLabel`),
  KEY `FK__CARD_LABEL__CARD` (`idCard`),
  CONSTRAINT `FK__CARD_LABEL__CARD` FOREIGN KEY (`idCard`) REFERENCES `card` (`id`),
  CONSTRAINT `FK__CARD_LABEL__LABEL` FOREIGN KEY (`idLabel`) REFERENCES `label` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_label`
--

LOCK TABLES `card_label` WRITE;
/*!40000 ALTER TABLE `card_label` DISABLE KEYS */;
/*!40000 ALTER TABLE `card_label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_member`
--

DROP TABLE IF EXISTS `card_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_member` (
  `id` varchar(32) NOT NULL,
  `idCard` varchar(32) NOT NULL,
  `idMember` varchar(32) NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__CARD_MEMBER_MEMBER_idx` (`idMember`),
  KEY `FK__CARD_MEMBER_CARD` (`idCard`),
  CONSTRAINT `FK__CARD_MEMBER_CARD` FOREIGN KEY (`idCard`) REFERENCES `card` (`id`),
  CONSTRAINT `FK__CARD_MEMBER_MEMBER` FOREIGN KEY (`idMember`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_member`
--

LOCK TABLES `card_member` WRITE;
/*!40000 ALTER TABLE `card_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `card_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checkitem`
--

DROP TABLE IF EXISTS `checkitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checkitem` (
  `id` varchar(32) NOT NULL,
  `idChecklist` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL,
  `nameData` json DEFAULT NULL,
  `pos` float NOT NULL,
  `state` varchar(15) NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__CHECKITEM__CHECKLIST_idx` (`idChecklist`),
  CONSTRAINT `FK__CHECKITEM__CHECKLIST` FOREIGN KEY (`idChecklist`) REFERENCES `checklist` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkitem`
--

LOCK TABLES `checkitem` WRITE;
/*!40000 ALTER TABLE `checkitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `checkitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checklist`
--

DROP TABLE IF EXISTS `checklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `checklist` (
  `id` varchar(32) NOT NULL,
  `idCard` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL,
  `pos` float NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__CHECKLIST__CARD` (`idCard`),
  CONSTRAINT `FK__CHECKLIST__CARD` FOREIGN KEY (`idCard`) REFERENCES `card` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checklist`
--

LOCK TABLES `checklist` WRITE;
/*!40000 ALTER TABLE `checklist` DISABLE KEYS */;
/*!40000 ALTER TABLE `checklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `label` (
  `id` varchar(32) NOT NULL,
  `idBoard` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL,
  `colour` varchar(20) DEFAULT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__LABEL__BOARD` (`idBoard`),
  CONSTRAINT `FK__LABEL__BOARD` FOREIGN KEY (`idBoard`) REFERENCES `board` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This represents the labels of a board.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `list`
--

DROP TABLE IF EXISTS `list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `list` (
  `id` varchar(32) NOT NULL,
  `idBoard` varchar(32) NOT NULL,
  `name` varchar(100) NOT NULL,
  `pos` float NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__LIST__BOARD` (`idBoard`),
  CONSTRAINT `FK__LIST__BOARD` FOREIGN KEY (`idBoard`) REFERENCES `board` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `list`
--

LOCK TABLES `list` WRITE;
/*!40000 ALTER TABLE `list` DISABLE KEYS */;
/*!40000 ALTER TABLE `list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `id` varchar(32) NOT NULL COMMENT 'This is the id of the member.',
  `memberType` varchar(45) NOT NULL COMMENT 'This is the type of member. For example: "normal".',
  `fullName` varchar(100) NOT NULL COMMENT 'This is the full name of the member.',
  `email` varchar(100) DEFAULT NULL COMMENT 'This is the email of the member.',
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'This is when this entry was created.',
  `dateLastModified` datetime DEFAULT NULL COMMENT 'This is when this entry was last modified.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This table represents a member.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `id` varchar(32) NOT NULL COMMENT 'This is the id of the organization.',
  `name` varchar(100) NOT NULL COMMENT 'This is the name of the organization.',
  `displayName` varchar(100) NOT NULL COMMENT 'This is the name displayed.',
  `description` varchar(100) DEFAULT NULL COMMENT 'This is the description of the organization.',
  `descData` json DEFAULT NULL COMMENT 'This includes additional data to display the description.',
  `teamType` varchar(20) NOT NULL COMMENT 'This is the type of the team.',
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'This is when this entry was created.',
  `dateLastModified` datetime DEFAULT NULL COMMENT 'This is when this entry was last modified.',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_member`
--

DROP TABLE IF EXISTS `organization_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization_member` (
  `id` varchar(32) NOT NULL COMMENT 'This is the ID of the composite entity.',
  `idOrganization` varchar(32) NOT NULL,
  `idMember` varchar(32) NOT NULL,
  `dateCreated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateLastModified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idMember_idx` (`idMember`),
  KEY `FK__ORGANIZATION_MEMBER__ORGANIZATION` (`idOrganization`),
  CONSTRAINT `FK__ORGANIZATION_MEMBER__MEMBER` FOREIGN KEY (`idMember`) REFERENCES `member` (`id`) ON UPDATE RESTRICT,
  CONSTRAINT `FK__ORGANIZATION_MEMBER__ORGANIZATION` FOREIGN KEY (`idOrganization`) REFERENCES `organization` (`id`) ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='This is the composite table for organization and member.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_member`
--

LOCK TABLES `organization_member` WRITE;
/*!40000 ALTER TABLE `organization_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-13 20:38:12

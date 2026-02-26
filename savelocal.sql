-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: recycle_db
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `board_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `status` varchar(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `update_at` datetime(6) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`board_id`),
  KEY `FKfyf1fchnby6hndhlfaidier1r` (`user_id`),
  CONSTRAINT `FKfyf1fchnby6hndhlfaidier1r` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (1,'수원 장안구에 사는데 페트병 라벨 제거가 어렵습니다. 물에 불려도 안 떨어지는 강한 접착 라벨은 그냥 달린 채로 배출해도 되나요? 아니면 일반쓰레기로 버려야 하나요?','2026-02-20 23:24:50.000000','WAITING','페트병 라벨이 잘 안 떨어질 때는 어떻게 하나요?','2026-02-20 23:24:50.000000',2),(2,'권선구에 이사 왔는데 음식물 쓰레기 봉투 색이 원래 노란색(황색)이 맞나요? 동네마다 다르다고 들었는데 수원시는 어떤 봉투를 써야 하나요?','2026-02-20 23:24:50.000000','ANSWERED','음식물 배출 봉투 색이 맞는지 확인하고 싶어요','2026-02-20 23:24:50.000000',3),(3,'팔달구 거주자입니다. 택배 스티로폼에 테이프가 깊이 파고들어 완전히 제거가 어렵습니다. 테이프가 조금 남은 상태로 스티로폼 배출해도 재활용이 되나요?','2026-02-20 23:24:50.000000','WAITING','스티로폼 테이프 일부 남아있어도 되나요?','2026-02-20 23:24:50.000000',4),(4,'영통구 매탄동에 사는데 옆 동네 친구는 저랑 수거 요일이 다르다고 하더라고요. 동마다 수거 요일이 다른지, 그리고 정확한 제 동네 요일은 어디서 확인할 수 있나요?','2026-02-20 23:24:50.000000','ANSWERED','영통구 재활용 수거 요일이 동마다 다른가요?','2026-02-20 23:24:50.000000',5),(5,'캠핑 다녀오고 나서 부탄가스 캔이 여러 개 남았는데, 안에 가스가 조금 남은 것 같아요. 그냥 캔으로 배출하면 안 된다고 들었는데 정확한 처리 방법을 알려주세요.','2026-02-20 23:24:50.000000','ANSWERED','부탄가스 캔 배출 방법이 궁금합니다','2026-02-20 23:24:50.000000',2);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_reply`
--

DROP TABLE IF EXISTS `board_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_reply` (
  `board_reply_id` bigint NOT NULL AUTO_INCREMENT,
  `author_name` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `reply_content` varchar(255) DEFAULT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `board_id` bigint DEFAULT NULL,
  PRIMARY KEY (`board_reply_id`),
  KEY `FKr1lvvxwqtol2ng5hydob4x108` (`board_id`),
  CONSTRAINT `FKr1lvvxwqtol2ng5hydob4x108` FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_reply`
--

LOCK TABLES `board_reply` WRITE;
/*!40000 ALTER TABLE `board_reply` DISABLE KEYS */;
INSERT INTO `board_reply` VALUES (1,'관리자','2026-02-20 23:24:50.000000','안녕하세요, 관리자입니다! 음식물 전용봉투는 수원시 공통으로 황색(노란색) 봉투를 사용합니다. 가까운 슈퍼마켓·편의점·동 행정복지센터에서 구입 가능하며, 칩 방식 수거함이 있는 곳은 전용 용기를 이용하셔도 됩니다.','2026-02-20 23:24:50.000000',2),(2,'관리자','2026-02-20 23:24:50.000000','안녕하세요! 수원시는 기본적으로 수거 요일이 동(洞)별로 다르게 운영됩니다. 정확한 수거 요일은 영통구청 환경위생과(031-5191-8904)나 거주하시는 동 행정복지센터에 문의하시거나, 수원시청 대표전화(1899-3300)로 확인하실 수 있습니다.','2026-02-20 23:24:50.000000',4),(3,'관리자','2026-02-20 23:24:50.000000','부탄가스 캔은 잔여가스가 있으면 매우 위험합니다! 반드시 야외 환기되는 장소에서 노즐을 끝까지 눌러 가스를 완전히 빼거나, 송곳으로 여러 곳에 구멍을 뚫어 잔여가스를 완전히 제거한 후 투명봉투에 담아 캔(STEEL) 수거일에 배출해 주세요!','2026-02-20 23:24:50.000000',5);
/*!40000 ALTER TABLE `board_reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disposal_schedule`
--

DROP TABLE IF EXISTS `disposal_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disposal_schedule` (
  `schedule_id` bigint NOT NULL AUTO_INCREMENT,
  `category` enum('ETC','FOOD','GENERAL','GLASS','PAPER','PLASTIC','STEEL','STYROFOAM','WRAP') DEFAULT NULL,
  `disposal_day` varchar(255) NOT NULL,
  `disposal_time` varchar(255) DEFAULT NULL,
  `region_id` bigint DEFAULT NULL,
  PRIMARY KEY (`schedule_id`),
  UNIQUE KEY `uk_region_category` (`region_id`,`category`),
  CONSTRAINT `FKb30jxrstmqf14q6phhl9aa7nh` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disposal_schedule`
--

LOCK TABLES `disposal_schedule` WRITE;
/*!40000 ALTER TABLE `disposal_schedule` DISABLE KEYS */;
INSERT INTO `disposal_schedule` VALUES (1,'PLASTIC','화요일, 금요일','전날 20:00 이후 ~ 당일 05:00',1),(2,'PAPER','월요일, 목요일','전날 20:00 이후 ~ 당일 05:00',1),(3,'STEEL','수요일','전날 20:00 이후 ~ 당일 05:00',1),(4,'GLASS','수요일','전날 20:00 이후 ~ 당일 05:00',1),(5,'FOOD','월요일~토요일 (매일)','전날 20:00 이후 ~ 당일 05:00',1),(6,'GENERAL','화요일, 목요일, 토요일','전날 20:00 이후 ~ 당일 05:00',1),(7,'WRAP','화요일, 금요일','전날 20:00 이후 ~ 당일 05:00',1),(8,'STYROFOAM','목요일','전날 20:00 이후 ~ 당일 05:00',1),(9,'PLASTIC','월요일, 목요일','전날 20:00 이후 ~ 당일 05:00',2),(10,'PAPER','화요일, 금요일','전날 20:00 이후 ~ 당일 05:00',2),(11,'STEEL','화요일','전날 20:00 이후 ~ 당일 05:00',2),(12,'GLASS','목요일','전날 20:00 이후 ~ 당일 05:00',2),(13,'FOOD','월요일~토요일 (매일)','전날 20:00 이후 ~ 당일 05:00',2),(14,'GENERAL','월요일, 수요일, 금요일','전날 20:00 이후 ~ 당일 05:00',2),(15,'WRAP','월요일, 목요일','전날 20:00 이후 ~ 당일 05:00',2),(16,'STYROFOAM','화요일','전날 20:00 이후 ~ 당일 05:00',2),(17,'PLASTIC','화요일, 목요일','전날 20:00 이후 ~ 당일 05:00',3),(18,'PAPER','월요일, 금요일','전날 20:00 이후 ~ 당일 05:00',3),(19,'STEEL','화요일','전날 20:00 이후 ~ 당일 05:00',3),(20,'GLASS','금요일','전날 20:00 이후 ~ 당일 05:00',3),(21,'FOOD','월요일~토요일 (매일)','전날 20:00 이후 ~ 당일 05:00',3),(22,'GENERAL','화요일, 목요일, 토요일','전날 20:00 이후 ~ 당일 05:00',3),(23,'WRAP','화요일, 목요일','전날 20:00 이후 ~ 당일 05:00',3),(24,'STYROFOAM','수요일','전날 20:00 이후 ~ 당일 05:00',3),(25,'PLASTIC','월요일, 수요일','전날 20:00 이후 ~ 당일 05:00',4),(26,'PAPER','화요일, 목요일','전날 20:00 이후 ~ 당일 05:00',4),(27,'STEEL','목요일','전날 20:00 이후 ~ 당일 05:00',4),(28,'GLASS','금요일','전날 20:00 이후 ~ 당일 05:00',4),(29,'FOOD','월요일~토요일 (매일)','전날 20:00 이후 ~ 당일 05:00',4),(30,'GENERAL','월요일, 수요일, 금요일','전날 20:00 이후 ~ 당일 05:00',4),(31,'WRAP','월요일, 수요일','전날 20:00 이후 ~ 당일 05:00',4),(32,'STYROFOAM','금요일','전날 20:00 이후 ~ 당일 05:00',4);
/*!40000 ALTER TABLE `disposal_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback`
--

DROP TABLE IF EXISTS `feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback` (
  `feedback_id` bigint NOT NULL,
  `content` varchar(1000) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `feedback_type` enum('CLASSIFICATION_ERROR','CONTENT_ERROR','ETC','MISSING_INFO','SCHEDULE_ERROR') DEFAULT NULL,
  `detail_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `feedback_status` enum('ANSWERED','WAITING') DEFAULT NULL,
  PRIMARY KEY (`feedback_id`),
  KEY `FK5qokp4523jnovg9v7banwd9bo` (`detail_id`),
  KEY `FK7k33yw505d347mw3avr93akao` (`user_id`),
  CONSTRAINT `FK5qokp4523jnovg9v7banwd9bo` FOREIGN KEY (`detail_id`) REFERENCES `trash_detail` (`detail_id`),
  CONSTRAINT `FK7k33yw505d347mw3avr93akao` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback`
--

LOCK TABLES `feedback` WRITE;
/*!40000 ALTER TABLE `feedback` DISABLE KEYS */;
INSERT INTO `feedback` VALUES (1,'페트병 라벨 제거가 어려운 경우에 대한 안내가 부족합니다. 접착력이 강한 라벨(물에 불려도 안 떨어지는 경우)에 대한 구체적인 처리 방법 안내를 추가해 주세요.','2026-02-20 23:24:50.000000','CONTENT_ERROR',1,2,'WAITING'),(2,'수원 권선구 재활용 수거 요일 정보가 실제와 다를 수 있습니다. 동마다 요일이 다른데 대표 요일만 표시되어 있어 혼란스럽습니다. 동별 상세 안내 또는 안내 문구 개선을 요청드립니다.','2026-02-20 23:24:50.000000','SCHEDULE_ERROR',9,3,'ANSWERED'),(3,'스티로폼 중 색상이 있는 것(분홍색·녹색 등)은 재활용이 안 된다고 하는데, 해당 내용이 배출 정보에 명시되어 있지 않습니다. 추가 안내 부탁드립니다.','2026-02-20 23:24:50.000000','MISSING_INFO',23,4,'ANSWERED'),(4,'영통구 음식물 쓰레기 RFID 방식 수거함 위치 안내가 없습니다. 우리 동네에 전용 수거함이 어디 있는지 앱 내에서 확인할 수 있으면 좋겠습니다.','2026-02-20 23:24:50.000000','CONTENT_ERROR',29,5,'WAITING');
/*!40000 ALTER TABLE `feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_reply`
--

DROP TABLE IF EXISTS `feedback_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_reply` (
  `feedbackreply_id` bigint NOT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `reply_content` varchar(255) DEFAULT NULL,
  `update_at` datetime(6) DEFAULT NULL,
  `feedback_id` bigint DEFAULT NULL,
  PRIMARY KEY (`feedbackreply_id`),
  KEY `FKbpnhh37vewd38yycdhyde7j66` (`feedback_id`),
  CONSTRAINT `FKbpnhh37vewd38yycdhyde7j66` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`feedback_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_reply`
--

LOCK TABLES `feedback_reply` WRITE;
/*!40000 ALTER TABLE `feedback_reply` DISABLE KEYS */;
INSERT INTO `feedback_reply` VALUES (1,'관리자','2026-02-20 23:24:50.000000','소중한 의견 감사합니다. 수거 요일 안내 페이지에 \"동별로 요일이 다를 수 있으며, 정확한 수거 요일은 동 행정복지센터 또는 수원시청(1899-3300)에 문의 바랍니다\" 문구를 추가하였습니다.','2026-02-20 23:24:50.000000',2),(2,'관리자','2026-02-20 23:24:50.000000','말씀해 주신 색상 스티로폼 관련 주의사항을 배출 정보에 추가하였습니다. 분홍·녹색 등 색상이 있는 스티로폼은 재활용이 어려워 일반쓰레기로 배출하셔야 합니다. 앞으로도 이런 오류를 발견하시면 적극적으로 알려주세요!','2026-02-20 23:24:50.000000',3);
/*!40000 ALTER TABLE `feedback_reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_reply_seq`
--

DROP TABLE IF EXISTS `feedback_reply_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_reply_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_reply_seq`
--

LOCK TABLES `feedback_reply_seq` WRITE;
/*!40000 ALTER TABLE `feedback_reply_seq` DISABLE KEYS */;
INSERT INTO `feedback_reply_seq` VALUES (101);
/*!40000 ALTER TABLE `feedback_reply_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_seq`
--

DROP TABLE IF EXISTS `feedback_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedback_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_seq`
--

LOCK TABLES `feedback_seq` WRITE;
/*!40000 ALTER TABLE `feedback_seq` DISABLE KEYS */;
INSERT INTO `feedback_seq` VALUES (51);
/*!40000 ALTER TABLE `feedback_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region` (
  `region_id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(50) NOT NULL,
  `district` varchar(50) DEFAULT NULL,
  `dong` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`region_id`),
  UNIQUE KEY `uk_region` (`city`,`district`,`dong`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
INSERT INTO `region` VALUES (2,'수원시','권선구','권선동'),(4,'수원시','영통구','매탄동'),(1,'수원시','장안구','정자동'),(5,'수원시','팔달구',NULL),(3,'수원시','팔달구','매향동');
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trash_detail`
--

DROP TABLE IF EXISTS `trash_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trash_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT,
  `category` enum('ETC','FOOD','GENERAL','GLASS','PAPER','PLASTIC','STEEL','STYROFOAM','WRAP') DEFAULT NULL,
  `caution` varchar(255) DEFAULT NULL,
  `disposal_method` varchar(255) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  `pre_treatment` varchar(255) DEFAULT NULL,
  `region_id` bigint DEFAULT NULL,
  PRIMARY KEY (`detail_id`),
  UNIQUE KEY `uk_region_category` (`region_id`,`category`),
  CONSTRAINT `FKbdmh6j4648ilk142kxbbv7329` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trash_detail`
--

LOCK TABLES `trash_detail` WRITE;
/*!40000 ALTER TABLE `trash_detail` DISABLE KEYS */;
INSERT INTO `trash_detail` VALUES (1,'PLASTIC','라벨 제거 안 된 것, 이물질 있는 것은 일반쓰레기 처리. 기름때 심한 용기도 일반쓰레기','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 가볍게 헹구기\n3단계. 라벨 완전히 제거\n4단계. 압착하여 부피 줄이기\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','페트병 / 플라스틱 용기','라벨 완전 제거 · 내용물 세척 · 압착',1),(2,'PAPER','음식물 묻은 종이·코팅지·사진·영수증(감열지)·금은박지는 일반쓰레기','1단계. 신문지·잡지는 가지런히 쌓기\n2단계. 박스는 테이프·스테이플러 제거 후 납작하게 접기\n3단계. 물기 및 이물질 제거\n4단계. 끈으로 묶거나 박스에 담기\n5단계. 지정 요일 집 앞 배출','종이류 (신문지 / 책 / 박스)','물기 · 이물질 제거, 박스는 테이프·스테이플러 제거',1),(3,'STEEL','부탄가스·살충제 캔은 야외에서 노즐 눌러 잔여가스 완전 제거 후 배출. 뚜껑은 따로 분리','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 헹구기\n3단계. 압착하여 부피 줄이기\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','캔류 (음료캔 / 통조림캔)','내용물 비우기 · 압착',1),(4,'GLASS','깨진 유리는 신문지에 싸서 \"깨진유리\" 표시 후 일반쓰레기. 도자기·사기·내열유리는 일반쓰레기','1단계. 내용물을 완전히 비우기\n2단계. 뚜껑(금속·플라스틱) 분리\n3단계. 라벨 제거\n4단계. 흐르는 물로 세척\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','유리병류 (소주병 / 맥주병 / 음료수병)','라벨 제거 · 내용물 세척 · 뚜껑 분리',1),(5,'FOOD','이쑤시개·젓가락·호일 등 이물질 절대 금지. 뼈·조개껍데기·계란껍데기·양파껍질·고추씨·복어내장은 일반쓰레기','1단계. 이물질(이쑤시개·젓가락 등) 완전히 제거\n2단계. 수분(물기) 최대한 짜내기\n3단계. 음식물 전용봉투(황색) 또는 칩방식 전용용기에 담기\n4단계. 지정 시간 집 앞 배출 (매일)','음식물 쓰레기','수분(물기) 최대한 제거 필수',1),(6,'GENERAL','재활용 가능 품목을 일반쓰레기에 섞어 배출 시 과태료 부과','1단계. 재활용품·음식물과 완전히 분리\n2단계. 수원시 종량제봉투(소각용)에 담기\n3단계. 지정 요일 집 앞 배출','일반쓰레기','재활용·음식물과 반드시 분리',1),(7,'WRAP','음식물이 많이 묻은 비닐은 닦아서 배출. 제거 불가 시 일반쓰레기. 검은 봉투는 불가','1단계. 내용물·이물질 완전히 제거\n2단계. 흐르는 물로 세척\n3단계. 완전히 건조\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','비닐류 (봉투 / 랩 / 뽁뽁이)','이물질 제거 및 세척 후 건조',1),(8,'STYROFOAM','이물질(테이프 등) 제거 안 된 것은 일반쓰레기. 색이 들어간 스티로폼은 일반쓰레기','1단계. 테이프·스티커·이물질 완전히 제거\n2단계. 납작하게 압착하여 부피 줄이기\n3단계. 끈으로 묶거나 별도 봉투에 담기\n4단계. 지정 요일 집 앞 배출','스티로폼 (포장재 / 컵)','테이프 · 이물질 완전 제거 후 압착',1),(9,'PLASTIC','라벨 미제거, 기름때 심한 용기는 일반쓰레기. PVC 재질은 재활용 불가','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 가볍게 헹구기\n3단계. 라벨 완전히 제거\n4단계. 압착하여 부피 줄이기\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','페트병 / 플라스틱 용기','내용물 비우기 · 세척 · 라벨 제거 · 압착',2),(10,'PAPER','코팅된 종이·영수증(감열지)·사진은 일반쓰레기. 음식물 오염 심한 것도 일반쓰레기','1단계. 박스는 테이프·스테이플러 제거 후 납작하게 접기\n2단계. 신문지·잡지는 가지런히 쌓기\n3단계. 물기 및 이물질 제거\n4단계. 끈으로 묶거나 박스에 담기\n5단계. 지정 요일 집 앞 배출','종이류 (신문지 / 잡지 / 박스)','테이프·스테이플러 제거, 물기 제거',2),(11,'STEEL','가스 잔량 있는 캔은 잔여가스 완전 제거 후 배출. 알루미늄 호일은 캔과 함께 배출 가능','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 헹구기\n3단계. 압착하여 부피 줄이기\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','캔류 (음료캔 / 식품캔)','내용물 비우기 · 헹구기 · 압착',2),(12,'GLASS','깨진 유리는 일반쓰레기. 거울·유리컵·내열유리·형광등은 일반쓰레기(형광등은 전용수거함)','1단계. 내용물을 완전히 비우기\n2단계. 뚜껑 분리\n3단계. 라벨 제거\n4단계. 흐르는 물로 세척\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','유리병류','뚜껑 분리 · 라벨 제거 · 내용물 세척',2),(13,'FOOD','뼈·패각류(조개·굴껍데기)·계란껍데기·고추씨·복숭아씨 등 딱딱한 것은 일반쓰레기. 이쑤시개 혼입 금지','1단계. 이물질(이쑤시개·젓가락 등) 완전히 제거\n2단계. 수분(물기) 충분히 짜내기\n3단계. 음식물 전용봉투(황색) 또는 칩방식 용기에 담기\n4단계. 지정 시간 집 앞 배출 (매일)','음식물 쓰레기','물기 충분히 제거 필수',2),(14,'GENERAL','혼합 배출 적발 시 과태료. 종량제봉투 미사용 배출 금지','1단계. 재활용품·음식물과 완전히 분리\n2단계. 수원시 종량제봉투에 담기\n3단계. 지정 요일 집 앞 배출','일반쓰레기','재활용·음식물과 분리',2),(15,'WRAP','오염이 심해 세척 불가 시 일반쓰레기. 뽁뽁이(에어캡)는 비닐로 배출 가능','1단계. 내용물·이물질 완전히 제거\n2단계. 흐르는 물로 세척\n3단계. 완전히 건조\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','비닐류','세척 후 건조하여 배출',2),(16,'STYROFOAM','이물질 잔존 시 일반쓰레기 처리. 스티로폼 컵(라면 용기)은 재활용 가능','1단계. 테이프·스티커·이물질 완전히 제거\n2단계. 납작하게 압착하여 부피 줄이기\n3단계. 끈으로 묶거나 별도 봉투에 담기\n4단계. 지정 요일 집 앞 배출','스티로폼','테이프·이물질 완전 제거 후 압착',2),(17,'PLASTIC','세제·샴푸 용기도 세척 후 배출 가능. 장난감·칫솔 등 혼합재질 플라스틱은 일반쓰레기','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 가볍게 헹구기\n3단계. 라벨 완전히 제거\n4단계. 압착하여 부피 줄이기\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','페트병 / 플라스틱 용기','내용물 비우기 · 세척 · 라벨 제거 · 압착',3),(18,'PAPER','코팅지·감열지·금은박지·부직포는 일반쓰레기','1단계. 박스는 테이프·스테이플러 제거 후 납작하게 접기\n2단계. 신문지·잡지는 가지런히 쌓기\n3단계. 물기 및 이물질 제거\n4단계. 끈으로 묶거나 박스에 담기\n5단계. 지정 요일 집 앞 배출','종이류','박스 테이프·스테이플러 제거, 물기 제거',3),(19,'STEEL','부탄가스캔은 반드시 잔여가스 제거 후 배출. 알루미늄 트레이·호일은 세척 후 함께 배출','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 헹구기\n3단계. 압착하여 부피 줄이기\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','캔류','내용물 비우기 · 헹구기 · 압착',3),(20,'GLASS','깨진 유리·도자기류는 신문지에 싸서 일반쓰레기. 판유리·거울·조명기구 유리도 일반쓰레기','1단계. 내용물을 완전히 비우기\n2단계. 뚜껑 제거\n3단계. 라벨 제거\n4단계. 흐르는 물로 세척\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','유리병류','뚜껑 제거 · 라벨 제거 · 세척',3),(21,'FOOD','뼈·조개껍데기·계란껍데기·견과류 껍질·고추씨는 일반쓰레기. 이물질 혼입 엄금','1단계. 이물질(이쑤시개·젓가락 등) 완전히 제거\n2단계. 수분(물기) 반드시 짜내기\n3단계. 음식물 전용봉투(황색) 또는 칩 방식 용기에 담기\n4단계. 지정 시간 집 앞 배출 (매일)','음식물 쓰레기','반드시 물기 제거 후 배출',3),(22,'GENERAL','재활용 가능 품목 혼합 배출 시 과태료(최대 100만원)','1단계. 재활용품·음식물과 완전히 분리\n2단계. 수원시 소각용 종량제봉투에 담기\n3단계. 지정 요일 집 앞 배출','일반쓰레기 (소각용)','재활용·음식물과 완전 분리',3),(23,'WRAP','오염 비닐은 닦아서 배출. 불가 시 일반쓰레기. 비닐 코팅된 종이봉투는 비닐로 배출','1단계. 내용물·이물질 완전히 제거\n2단계. 흐르는 물로 세척\n3단계. 완전히 건조\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','비닐류','내용물 제거 · 세척 · 건조',3),(24,'STYROFOAM','색상 있는 스티로폼·이물질 제거 불가 시 일반쓰레기','1단계. 테이프·스티커·이물질 완전히 제거\n2단계. 납작하게 압착하여 부피 줄이기\n3단계. 끈으로 묶거나 별도 봉투에 담기\n4단계. 지정 요일 집 앞 배출','스티로폼','테이프·스티커·이물질 완전 제거',3),(25,'PLASTIC','라벨 미제거·이물질 있으면 일반쓰레기. 단, 페트병 뚜껑은 뚜껑끼리 모아 투명봉투에 함께 배출 가능','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 가볍게 헹구기\n3단계. 라벨 완전히 제거\n4단계. 압착하여 부피 줄이기\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','페트병 / 플라스틱 용기','내용물 비우기 · 세척 · 라벨 완전 제거 · 압착',4),(26,'PAPER','코팅지·영수증(감열지)·사진·택배 송장(비닐 포함)은 일반쓰레기','1단계. 박스는 테이프·스테이플러 완전 제거 후 납작하게 접기\n2단계. 신문지·잡지는 가지런히 쌓기\n3단계. 물기 및 이물질 제거\n4단계. 끈으로 묶거나 박스에 담기\n5단계. 지정 요일 집 앞 배출','종이류 (신문지 / 잡지 / 박스 / 책)','테이프·스테이플러 완전 제거, 물기·이물질 제거',4),(27,'STEEL','가스 잔량 캔은 야외에서 잔여가스 완전 제거 필수. 알루미늄 트레이도 세척 후 함께 배출 가능','1단계. 내용물을 완전히 비우기\n2단계. 흐르는 물로 헹구기\n3단계. 압착하여 부피 줄이기\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','캔류 (알루미늄·철)','내용물 완전히 비우기 · 헹구기 · 압착',4),(28,'GLASS','깨진 유리는 반드시 신문지 등에 싸서 \"깨진유리\" 표시 후 일반쓰레기. 형광등은 전용수거함 이용','1단계. 내용물을 완전히 비우기\n2단계. 뚜껑(금속·플라스틱) 분리\n3단계. 라벨 제거\n4단계. 흐르는 물로 세척\n5단계. 투명봉투에 담아 지정 요일 집 앞 배출','유리병류','뚜껑(금속·플라스틱) 분리 · 라벨 제거 · 세척',4),(29,'FOOD','뼈·조개껍데기·계란껍데기·복어내장·고추씨·견과류껍질은 일반쓰레기. 이쑤시개·일회용품 혼입 금지','1단계. 이물질(이쑤시개·일회용품 등) 완전히 제거\n2단계. 수분(물기) 최대한 짜내기\n3단계. 음식물 전용봉투(황색) 또는 RFID·칩 방식 용기에 담기\n4단계. 지정 시간 집 앞 배출 (매일)','음식물 쓰레기','물기 최대한 제거 (수분 제거 시 무게 감소로 봉투 절약)',4),(30,'GENERAL','종량제봉투 미사용 배출·혼합 배출은 무단투기로 과태료 부과','1단계. 재활용품·음식물과 완전히 분리\n2단계. 수원시 종량제봉투(소각용)에 담기\n3단계. 지정 요일 집 앞 배출','일반쓰레기','재활용품·음식물과 분리 배출',4),(31,'WRAP','오염된 비닐은 닦아서 배출, 불가 시 일반쓰레기. 검은색·유색 봉투는 비닐류 배출 불가','1단계. 내용물·이물질 완전히 제거\n2단계. 흐르는 물로 세척\n3단계. 완전히 건조\n4단계. 투명봉투에 담아 지정 요일 집 앞 배출','비닐류','내용물 제거 · 세척 · 완전 건조 후 배출',4),(32,'STYROFOAM','이물질 제거 불가·색상 스티로폼은 일반쓰레기. 스티로폼 컵라면 용기는 재활용 가능','1단계. 테이프·스티커·이물질 완전히 제거\n2단계. 납작하게 압착하여 부피 줄이기\n3단계. 끈으로 묶거나 별도 봉투에 담기\n4단계. 지정 요일 집 앞 배출','스티로폼 (포장재 / 컵라면 용기)','테이프·스티커·이물질 완전 제거 후 압착',4);
/*!40000 ALTER TABLE `trash_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `email` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `region_id` bigint DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_users_email` (`email`),
  KEY `FKt4enlxyfjstamura4y1p3wtor` (`region_id`),
  CONSTRAINT `FKt4enlxyfjstamura4y1p3wtor` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'2026-02-20 23:24:50.000000','admin@recycle.com','관리자','$2a$10$Nk/a2Kfms5XKibDFuqQi3.EkxDhILTibfRH2dqbDa5SY/EpNz2SKa','ADMIN','2026-02-20 23:24:50.000000',1),(2,'2026-02-20 23:24:50.000000','user1@test.com','김환경','$2a$10$Nk/a2Kfms5XKibDFuqQi3.EkxDhILTibfRH2dqbDa5SY/EpNz2SKa','USER','2026-02-20 23:24:50.000000',1),(3,'2026-02-20 23:24:50.000000','user2@test.com','이재활','$2a$10$Nk/a2Kfms5XKibDFuqQi3.EkxDhILTibfRH2dqbDa5SY/EpNz2SKa','USER','2026-02-20 23:24:50.000000',2),(4,'2026-02-20 23:24:50.000000','user3@test.com','박초록','$2a$10$Nk/a2Kfms5XKibDFuqQi3.EkxDhILTibfRH2dqbDa5SY/EpNz2SKa','USER','2026-02-20 23:24:50.000000',3),(5,'2026-02-20 23:24:50.000000','user4@test.com','최분리','$2a$10$Nk/a2Kfms5XKibDFuqQi3.EkxDhILTibfRH2dqbDa5SY/EpNz2SKa','USER','2026-02-20 23:24:50.000000',4),(6,'2026-02-20 23:25:17.451463','jaewon0009@naver.com','최재원','$2a$10$2ysVzmuYTLCuM5YfDXnjcu.WFEWX3xkwMkJ2oUp1v8TD0pWBC0tbK','USER','2026-02-20 23:25:21.605641',5);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-22 18:45:19

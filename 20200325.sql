-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: davislam
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `tb_admin`
--

DROP TABLE IF EXISTS `tb_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_admin` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_class`
--

DROP TABLE IF EXISTS `tb_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_class` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `grade_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `class_name` (`name`),
  KEY `class_grade` (`grade_id`),
  CONSTRAINT `class_grade` FOREIGN KEY (`grade_id`) REFERENCES `tb_grade` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_course`
--

DROP TABLE IF EXISTS `tb_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_access`
--

DROP TABLE IF EXISTS `tb_cx_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_access` (
  `tcourse_id` int unsigned DEFAULT NULL,
  `access_date` date DEFAULT NULL,
  `t00t04` int unsigned DEFAULT NULL,
  `t04t08` int DEFAULT NULL,
  `t08t12` int DEFAULT NULL,
  `t12t16` int DEFAULT NULL,
  `t16t20` int DEFAULT NULL,
  `t20t24` int DEFAULT NULL,
  UNIQUE KEY `cx_a_tanda` (`tcourse_id`,`access_date`) USING BTREE,
  CONSTRAINT `cx_access_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_chapter_quiz`
--

DROP TABLE IF EXISTS `tb_cx_chapter_quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_chapter_quiz` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tcourse_id` int unsigned DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cx_cq_tandn` (`tcourse_id`,`name`) USING BTREE,
  CONSTRAINT `cx_cq_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_discuss`
--

DROP TABLE IF EXISTS `tb_cx_discuss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_discuss` (
  `student_id` int unsigned DEFAULT NULL,
  `tcourse_id` int unsigned DEFAULT NULL,
  `comments` int unsigned DEFAULT NULL,
  `replies` int unsigned DEFAULT NULL,
  `suggest_score` int DEFAULT NULL,
  UNIQUE KEY `cx_d_sandt` (`student_id`,`tcourse_id`) USING BTREE,
  KEY `cx_discuss_course` (`tcourse_id`),
  CONSTRAINT `cx_discuss_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cx_discuss_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_exam`
--

DROP TABLE IF EXISTS `tb_cx_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_exam` (
  `student_id` int unsigned DEFAULT NULL,
  `tcourse_id` int unsigned DEFAULT NULL,
  `score` int unsigned DEFAULT NULL,
  UNIQUE KEY `cx_e_sandt` (`student_id`,`tcourse_id`) USING BTREE,
  KEY `cx_se_course` (`tcourse_id`),
  CONSTRAINT `cx_se_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cx_se_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_score_info`
--

DROP TABLE IF EXISTS `tb_cx_score_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_score_info` (
  `student_id` int unsigned NOT NULL,
  `tcourse_id` int unsigned DEFAULT NULL,
  `video_score` decimal(5,2) unsigned DEFAULT NULL,
  `video_progress` int DEFAULT NULL,
  `quiz_score` decimal(5,2) unsigned DEFAULT NULL,
  `discuss_score` decimal(5,2) unsigned DEFAULT NULL,
  `work_score` decimal(5,2) unsigned DEFAULT NULL,
  `exam_score` decimal(5,2) unsigned DEFAULT NULL,
  `task_percentage` decimal(6,2) DEFAULT NULL,
  `score` decimal(5,2) unsigned DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `cx_si_sandt` (`student_id`,`tcourse_id`) USING BTREE,
  KEY `cx_si_course` (`tcourse_id`),
  CONSTRAINT `cx_si_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cx_si_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_student_chapter`
--

DROP TABLE IF EXISTS `tb_cx_student_chapter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_student_chapter` (
  `student_id` int unsigned DEFAULT NULL,
  `chapter_id` int unsigned DEFAULT NULL,
  `score` int DEFAULT NULL,
  UNIQUE KEY `cx_sc_sandc` (`student_id`,`chapter_id`) USING BTREE,
  KEY `cx_sc_chapter` (`chapter_id`),
  CONSTRAINT `cx_sc_chapter` FOREIGN KEY (`chapter_id`) REFERENCES `tb_cx_chapter_quiz` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cx_sc_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_video`
--

DROP TABLE IF EXISTS `tb_cx_video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_video` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tcourse_id` int unsigned DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cx_v_tandn` (`tcourse_id`,`name`) USING BTREE,
  CONSTRAINT `cx_video_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=817 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_video_watching`
--

DROP TABLE IF EXISTS `tb_cx_video_watching`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_video_watching` (
  `student_id` int unsigned DEFAULT NULL,
  `video_id` int unsigned DEFAULT NULL,
  `percentage` decimal(6,2) unsigned DEFAULT NULL,
  UNIQUE KEY `cx_vw_sandv` (`student_id`,`video_id`) USING BTREE,
  KEY `cx_vw_video` (`video_id`),
  CONSTRAINT `cx_vw_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cx_vw_video` FOREIGN KEY (`video_id`) REFERENCES `tb_cx_video` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_work`
--

DROP TABLE IF EXISTS `tb_cx_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_work` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tcourse_id` int unsigned DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cx_w_tandn` (`tcourse_id`,`name`) USING BTREE,
  CONSTRAINT `cx_work_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_cx_work_finishing`
--

DROP TABLE IF EXISTS `tb_cx_work_finishing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cx_work_finishing` (
  `student_id` int unsigned DEFAULT NULL,
  `work_id` int unsigned DEFAULT NULL,
  `score` int DEFAULT NULL,
  UNIQUE KEY `cx_wf_sandw` (`student_id`,`work_id`) USING BTREE,
  KEY `cx_wf_work` (`work_id`),
  CONSTRAINT `cx_wf_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cx_wf_work` FOREIGN KEY (`work_id`) REFERENCES `tb_cx_work` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_final_exam`
--

DROP TABLE IF EXISTS `tb_final_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_final_exam` (
  `student_id` int unsigned NOT NULL,
  `course_id` int unsigned NOT NULL,
  `score` int DEFAULT NULL,
  PRIMARY KEY (`student_id`,`course_id`),
  KEY `final_course` (`course_id`),
  CONSTRAINT `final_course` FOREIGN KEY (`course_id`) REFERENCES `tb_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `final_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_grade`
--

DROP TABLE IF EXISTS `tb_grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_grade` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `grade_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_pta`
--

DROP TABLE IF EXISTS `tb_pta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_pta` (
  `student_id` int unsigned NOT NULL,
  `course_id` int unsigned NOT NULL,
  `question_type` varchar(255) DEFAULT NULL,
  `question_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `score` int DEFAULT NULL,
  PRIMARY KEY (`student_id`,`course_id`,`question_num`),
  KEY `pta_course` (`course_id`),
  CONSTRAINT `pta_course` FOREIGN KEY (`course_id`) REFERENCES `tb_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pta_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_student`
--

DROP TABLE IF EXISTS `tb_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_student` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `student_num` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `class_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `student_num` (`student_num`) USING BTREE,
  KEY `student_class` (`class_id`),
  CONSTRAINT `student_class` FOREIGN KEY (`class_id`) REFERENCES `tb_class` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=353 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_teach_course`
--

DROP TABLE IF EXISTS `tb_teach_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_teach_course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `course_id` int unsigned DEFAULT NULL,
  `grade_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tc_course` (`course_id`),
  KEY `tc_grade` (`grade_id`),
  CONSTRAINT `tc_course` FOREIGN KEY (`course_id`) REFERENCES `tb_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tc_grade` FOREIGN KEY (`grade_id`) REFERENCES `tb_grade` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_announcement`
--

DROP TABLE IF EXISTS `tb_ykt_announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_announcement` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tcourse_id` int unsigned DEFAULT NULL,
  `announcement_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ykttc_and_ann` (`tcourse_id`,`announcement_name`) USING BTREE,
  CONSTRAINT `yktannounce_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_push`
--

DROP TABLE IF EXISTS `tb_ykt_push`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_push` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tcourse_id` int unsigned DEFAULT NULL,
  `push_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `yktpush_tandp` (`tcourse_id`,`push_name`) USING BTREE,
  CONSTRAINT `ykt_push_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_push_answer`
--

DROP TABLE IF EXISTS `tb_ykt_push_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_push_answer` (
  `student_id` int unsigned DEFAULT NULL,
  `push_question_id` int unsigned DEFAULT NULL,
  `answer_condition` bit(1) DEFAULT NULL,
  UNIQUE KEY `ykt_pa_sandp` (`student_id`,`push_question_id`) USING BTREE,
  KEY `ykt_pa_push_question` (`push_question_id`),
  CONSTRAINT `ykt_pa_push_question` FOREIGN KEY (`push_question_id`) REFERENCES `tb_ykt_push_question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ykt_pa_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_push_question`
--

DROP TABLE IF EXISTS `tb_ykt_push_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_push_question` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `push_id` int unsigned DEFAULT NULL,
  `question_num` varchar(255) DEFAULT NULL,
  `score` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ykt_pd_pandq` (`push_id`,`question_num`) USING BTREE,
  CONSTRAINT `ykt_pq_push` FOREIGN KEY (`push_id`) REFERENCES `tb_ykt_push` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=256 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_student_annoucement`
--

DROP TABLE IF EXISTS `tb_ykt_student_annoucement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_student_annoucement` (
  `student_id` int unsigned DEFAULT NULL,
  `announcement_id` int unsigned DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  UNIQUE KEY `ykt_sa_sanda` (`student_id`,`announcement_id`) USING BTREE,
  KEY `ykt_sa_accnouncement` (`announcement_id`),
  CONSTRAINT `ykt_sa_accnouncement` FOREIGN KEY (`announcement_id`) REFERENCES `tb_ykt_announcement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ykt_sa_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_student_push`
--

DROP TABLE IF EXISTS `tb_ykt_student_push`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_student_push` (
  `student_id` int unsigned DEFAULT NULL,
  `push_id` int unsigned DEFAULT NULL,
  `read_pages` int unsigned DEFAULT NULL,
  `total_duration_sec` int DEFAULT NULL,
  UNIQUE KEY `ykt_sp_sandp` (`student_id`,`push_id`) USING BTREE,
  KEY `ykt_sp_push` (`push_id`),
  CONSTRAINT `ykt_sp_push` FOREIGN KEY (`push_id`) REFERENCES `tb_ykt_push` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ykt_sp_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_student_ykt_class`
--

DROP TABLE IF EXISTS `tb_ykt_student_ykt_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_student_ykt_class` (
  `student_id` int unsigned DEFAULT NULL,
  `ykt_class_id` int unsigned DEFAULT NULL,
  `is_present` bit(1) DEFAULT NULL,
  UNIQUE KEY `ykt_syc_sandy` (`student_id`,`ykt_class_id`) USING BTREE,
  KEY `ykt_syc_condition` (`ykt_class_id`),
  CONSTRAINT `ykt_syc_condition` FOREIGN KEY (`ykt_class_id`) REFERENCES `tb_ykt_ykt_class_condition` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ykt_syc_student` FOREIGN KEY (`student_id`) REFERENCES `tb_student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_ykt_ykt_class_condition`
--

DROP TABLE IF EXISTS `tb_ykt_ykt_class_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_ykt_ykt_class_condition` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `tcourse_id` int unsigned DEFAULT NULL,
  `class_condition` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ykt_cc_tandc` (`tcourse_id`,`class_condition`) USING BTREE,
  CONSTRAINT `ykt_class_condition_course` FOREIGN KEY (`tcourse_id`) REFERENCES `tb_teach_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-25 19:57:45
insert into tb_admin(username, password) values("admin", "123456");
-- selfmember 테이블 생성
CREATE TABLE `kjw`.`selfmember` (
    `memberId` VARCHAR(20) NOT NULL,
    `memberPwd` VARCHAR(200) NOT NULL,
    `memberName` VARCHAR(100) NOT NULL,
    `memberMobile` VARCHAR(13) NOT NULL,
    `memberEmail` VARCHAR(100) NOT NULL,
    `memberRegDate` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `memberImg` VARCHAR(200) NOT NULL DEFAULT 'avatar.png',
    `memberPoint` INT NOT NULL DEFAULT 1000,
    `memberGender` VARCHAR(1) NOT NULL DEFAULT 'U',
    PRIMARY KEY (`memberId`),
    UNIQUE INDEX `memberMobile_UNIQUE` (`memberMobile` ASC) VISIBLE,
    UNIQUE INDEX `memberEmail_UNIQUE` (`memberEmail` ASC) VISIBLE);

-- selfhboard 테이블 생성
CREATE TABLE `selfhboard` (
    `boardNo` int NOT NULL AUTO_INCREMENT,
    `title` varchar(50) NOT NULL,
    `content` varchar(500) DEFAULT '내용없음',
    `writer` varchar(20) NOT NULL,
    `postDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `readCount` int NOT NULL DEFAULT '0',
    `ref` int NOT NULL DEFAULT '0',
    `step` int NOT NULL DEFAULT '0',
    `refOrder` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`boardNo`),
    KEY `fk_writer_idx` (`writer`),
    CONSTRAINT `fk_writer` FOREIGN KEY (`writer`) REFERENCES `selfmember` (`memberId`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci


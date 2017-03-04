DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_comment_with_article_id`(IN _id INT)
BEGIN
	SELECT id, username, content, profile_picture_url, created_by_admin, liker, created, updated, parent_id, state FROM comment WHERE article_id = _id;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_comment_with_id_article`(IN _id INT)
BEGIN
	SELECT id, username, content, profile_picture_url, created_by_admin, liker, created, updated, parent_id, state FROM comment WHERE article_id = _id;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_en_articles_by_id`(IN _id INT)
BEGIN
	SELECT id, title_en, description_en, large_image, root_cate,sub_cate, viewer, comment, username, created FROM article WHERE id = _id;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_en_articles_by_root_cate`(IN _root VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_en, small_image, root_cate, viewer, comment, created FROM article WHERE root_cate = _root ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_en_articles_by_sub_cate`(IN _root VARCHAR(25), IN _sub VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_en, small_image, root_cate, viewer, comment, created FROM article WHERE root_cate = _root AND JSON_CONTAINS(sub_cate, _sub) ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_vi_articles_by_id`(IN _id INT)
BEGIN
	SELECT id, title_vi, description_vi, large_image, root_cate, sub_cate, viewer, comment, username, created FROM article WHERE id = _id;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_vi_articles_by_root_cate`(IN _root VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_vi, small_image, root_cate, viewer, comment, created FROM article WHERE root_cate = _root ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `find_vi_articles_by_sub_cate`(IN _root VARCHAR(25), IN _sub VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_en, small_image, root_cate, viewer, comment, created FROM article WHERE root_cate = _root AND JSON_CONTAINS(sub_cate, _sub) ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_en_related_articles`(IN _id INT,IN _root VARCHAR(25), IN _sub VARCHAR(250), IN _offset INT)
BEGIN
	SELECT id, title_en, small_image, root_cate, viewer, comment, created FROM article WHERE root_cate = _root AND JSON_CONTAINS(sub_cate, _sub) AND id != _id ORDER BY created DESC LIMIT _offset;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_top_en_articles`(IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_en, small_image, root_cate, viewer, comment, created FROM article ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_top_vi_articles`(IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_vi, small_image, root_cate, viewer, comment, created FROM article ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_vi_related_articles`(IN _id INT,IN _root VARCHAR(25), IN _sub VARCHAR(250), IN _offset INT)
BEGIN
	SELECT id, title_vi, small_image, root_cate, viewer, comment, created FROM article WHERE root_cate = _root AND JSON_CONTAINS(sub_cate, _sub) AND id != _id ORDER BY created DESC LIMIT _offset;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_en_articles`(IN _keyword VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_en, small_image, root_cate, viewer, comment, created FROM article WHERE MATCH(title_en,title_vi) AGAINST(_keyword) ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_en_articles_with_root_cate`(IN _keyword VARCHAR(25), IN _root VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_en, small_image, root_cate, viewer, comment, created FROM article WHERE MATCH(title_en,title_vi) AGAINST(_keyword) AND root_cate = _root ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_vi_articles`(IN _keyword VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_vi, small_image, root_cate, viewer, comment, created FROM article WHERE MATCH(title_en,title_vi) AGAINST(_keyword) ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_vi_articles_with_root_cate`(IN _keyword VARCHAR(25), IN _root VARCHAR(25), IN _offset INT, IN _size INT, OUT _total INT)
BEGIN
	SELECT SQL_CALC_FOUND_ROWS id, title_vi, small_image, root_cate, viewer, comment, created FROM article WHERE MATCH(title_en,title_vi) AGAINST(_keyword) AND root_cate = _root ORDER BY id DESC LIMIT _offset, _size;
    SET _total = FOUND_ROWS();
END$$
DELIMITER ;

package red.don.api.android.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import red.don.api.android.entity.CheckEntity;

public interface CheckMapper extends Mapper<CheckEntity> {

  @Override
  @Select("SELECT COUNT(1) FROM `check` WHERE ${where}")
  public int countWhere(@Param("where") String where);

  @Override
  @Select("SELECT COUNT(1) FROM `check` WHERE `${field}` = #{value}")
  public int count(@Param("field") String field, @Param("value") Object value);

  @Override
  @Select("SELECT COUNT(1) FROM `check`")
  public int countAll();

  @Override
  @Delete("DELETE FROM `check` WHERE ${where}")
  public boolean deleteWhere(@Param("where") String where);

  @Override
  @Delete("DELETE FROM `check` WHERE `${field}` = #{value}")
  public boolean delete(@Param("field") String field, @Param("value") Object value);

  @Override
  @Delete("TRUNCATE `check`")
  public void deleteAll();

  @Override
  @Insert("INSERT INTO `check`(`user`, `date`) VALUES (#{user}, #{date})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  public boolean insert(CheckEntity entity);

  @Override
  @Select("SELECT * FROM `check` WHERE ${where}")
  public List<CheckEntity> selectWhere(@Param("where") String where);

  @Override
  @Select("SELECT * FROM `check` WHERE `${field}` = #{value}")
  public List<CheckEntity> select(@Param("field") String field, @Param("value") Object value);

  @Override
  @Select("SELECT * FROM `check`")
  public List<CheckEntity> selectAll();

  @Override
  @Select("SELECT * FROM `check` WHERE ${where} LIMIT 1")
  public CheckEntity selectOneWhere(@Param("where") String where);

  @Override
  @Select("SELECT * FROM `check` WHERE `${field}` = #{value} LIMIT 1")
  public CheckEntity selectOne(@Param("field") String field, @Param("value") Object value);

  @Override
  @Update("UPDATE `check` SET `user` = #{entity.user}, `date` = #{entity.date} WHERE `${field}` = #{value}")
  public boolean update(@Param("field") String field, @Param("value") Object value, CheckEntity entity);

  @Override
  @Update("UPDATE `check` SET `user` = #{entity.user}, `date` = #{entity.date} WHERE ${where}")
  public boolean updateWhere(@Param("where") String where, CheckEntity entity);

}

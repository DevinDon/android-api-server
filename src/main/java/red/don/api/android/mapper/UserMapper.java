package red.don.api.android.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import red.don.api.android.entity.UserEntity;

public interface UserMapper extends Mapper<UserEntity> {

  @Override
  @Select("SELECT COUNT(1) FROM `user` WHERE ${where}")
  public int countWhere(@Param("where") String where);

  @Override
  @Select("SELECT COUNT(1) FROM `user` WHERE `${field}` = #{value}")
  public int count(@Param("field") String field, @Param("value") Object value);

  @Override
  @Select("SELECT COUNT(1) FROM `user`")
  public int countAll();

  @Override
  @Delete("DELETE FROM `user` WHERE ${where}")
  public boolean deleteWhere(@Param("where") String where);

  @Override
  @Delete("DELETE FROM `user` WHERE `${field}` = #{value}")
  public boolean delete(@Param("field") String field, @Param("value") Object value);

  @Override
  @Delete("TRUNCATE `user`")
  public void deleteAll();

  @Override
  @Insert("INSERT INTO `user`(`email`, `name`, `password`, `token`) VALUES (#{email}, #{name}, #{password}, ${token})")
  public boolean insert(UserEntity entity);

  @Override
  @Select("SELECT * FROM `user` WHERE ${where}")
  public List<UserEntity> selectWhere(@Param("where") String where);

  @Override
  @Select("SELECT * FROM `user` WHERE `${field}` = #{value}")
  public List<UserEntity> select(@Param("field") String field, @Param("value") Object value);

  @Override
  @Select("SELECT * FROM `user`")
  public List<UserEntity> selectAll();

  @Override
  @Select("SELECT * FROM `user` WHERE ${where} LIMIT 1")
  public UserEntity selectOneWhere(@Param("where") String where);

  @Override
  @Select("SELECT * FROM `user` WHERE `${field}` = #{value} LIMIT 1")
  public UserEntity selectOne(@Param("field") String field, @Param("value") Object value);

  @Override
  @Update("UPDATE `user` SET `email` = #{entity.email}, `name` = #{entity.name}, `password` = #{entity.password}, `token` = ${entity.token} WHERE `${field}` = #{value}")
  public boolean update(@Param("field") String field, @Param("value") Object value, UserEntity entity);

  @Override
  @Update("UPDATE `user` SET `email` = #{entity.email}, `name` = #{entity.name}, `password` = #{entity.password}, `token` = ${entity.token} WHERE ${where}")
  public boolean updateWhere(@Param("where") String where, UserEntity entity);

}

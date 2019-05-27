package red.don.api.android.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import red.don.api.android.entity.LogEntity;

public interface LogMapper extends Mapper<LogEntity> {

  @Override
  public int count(String field, Object value);

  @Override
  @Select("SELECT COUNT(1) FROM `log`")
  public int countAll();

  @Override
  @Select("SELECT COUNT(1) FROM `log` WHERE ${where}")
  public int countWhere(@Param("where") String where);

  @Override
  public boolean delete(String field, Object value);

  @Override
  @Delete("TRUNCATE `log`")
  public void deleteAll();

  @Override
  public boolean deleteWhere(String where);

  @Override
  @Insert("INSERT INTO `log`(`method`, `uri`, `user`, `ip`, `time`) VALUES (#{method}, #{uri}, #{user}, #{ip}, #{time})")
  @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
  public boolean insert(LogEntity entity);

  @Override
  public List<LogEntity> select(String field, Object value);

  @Override
  public List<LogEntity> selectAll();

  @Override
  public LogEntity selectOne(String field, Object value);

  @Override
  @Select("SELECT * FROM `log` WHERE ${where} LIMIT 1")
  public LogEntity selectOneWhere(@Param("where") String where);

  @Override
  public List<LogEntity> selectWhere(String where);

  @Override
  public boolean update(String field, Object value, LogEntity entity);

  @Override
  public boolean updateWhere(String where, LogEntity entity);

}

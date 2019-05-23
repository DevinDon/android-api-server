package red.don.api.android.mapper;

import java.util.List;

public interface Mapper<T> {

  /**
   * Insert a record.
   * <p>
   * <code>@Insert("INSERT INTO `table`(`field1`, `field2`) VALUES (#{field1}, #{field2})")</code>
   * <p>
   * <code>@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")</code>
   *
   * @param entity T.
   * @return Success or not.
   */
  public boolean insert(T entity);

  /**
   * Delete records that match the `field` = 'value' condition.
   * <p>
   * <code>@Delete("DELETE FROM `table` WHERE `${field}` = #{value}")</code>
   * <p>
   * Use <code>@Param("field")</code> to annotate param field and
   * <code>@Param("value")</code> to annotate param value.
   *
   * @param field Table field.
   * @param value Field value.
   * @return Success or not.
   */
  public boolean delete(String field, Object value);

  /**
   * Delete records that match the where condition.
   * <p>
   * <code>@Delete("DELETE FROM `table` WHERE ${where}")</code>
   * <p>
   * Use <code>@Param("where")</code> to annotate param where.
   *
   * @param where Where condition.
   * @return Success or not.
   */
  public boolean deleteWhere(String where);

  /**
   * Delete all records. If foreign key exist, it will not able to truncate table.
   * <p>
   * <code>@Delete("TRUNCATE `table`")</code>
   */
  public void deleteAll();

  /**
   * Update records that match the `field` = 'value' condition.
   * <p>
   * <code>@Update("UPDATE `table` SET `field1` = #{field1}, `field2` = #{field2} WHERE `field` = #{value}")</code>
   * <p>
   * Use <code>@Param("field")</code> to annotate param field and
   * <code>@Param("value")</code> to annotate param value.
   *
   * @param field  Table field.
   * @param value  Field value.
   * @param entity T.
   * @return Success or not.
   */
  public boolean update(String field, Object value, T entity);

  /**
   * Update records that match the where condition.
   * <p>
   * <code>@Update("UPDATE `table` SET `field1` = #{field1}, `field2` = #{field2} WHERE ${where}")</code>
   *
   * @param where  Where condition.
   * @param entity Entity.
   * @return Success or not.
   */
  public boolean updateWhere(String where, T entity);

  /**
   * Select records that match the `field` = 'value' condition.
   * <p>
   * <code>@Select("SELECT * FROM `table` WHERE `${field}` = #{value}")</code>
   * <p>
   * Use <code>@Param("field")</code> to annotate param field and
   * <code>@Param("value")</code> to annotate param value.
   *
   * @param field Table field.
   * @param value Field value.
   * @return A list of records.
   */
  public List<T> select(String field, Object value);

  /**
   * Select records that match the where condition.
   * <p>
   * <code>@Select("SELECT * FROM `table` WHERE ${where}")</code>
   * <p>
   * Use <code>@Param("where")</code> to annotate param where.
   *
   * @param where Where condition.
   * @return A list of records.
   */
  public List<T> selectWhere(String where);

  /**
   * Select one record that match the `field` = 'value' condition.
   * <p>
   * <code>@Select("SELECT * FROM `table` WHERE `${field}` = #{value} LIMIT 1")</code>
   * <p>
   * Use <code>@Param("field")</code> to annotate param field and
   * <code>@Param("value")</code> to annotate param value.
   *
   * @param field Table field.
   * @param value Field value.
   * @return A record.
   */
  public T selectOne(String field, Object value);

  /**
   * Select a record that match the where condition.
   * <p>
   * <code>@Select("SELECT * FROM `table` WHERE ${where} LIMIT 1")</code>
   * <p>
   * Use <code>@Param("where")</code> to annotate param where.
   *
   * @param where Where condition.
   * @return A record.
   */
  public T selectOneWhere(String where);

  /**
   * Select all records.
   * <p>
   * <code>@Select("SELECT * FROM `table`")</code>
   *
   * @return A list of records.
   */
  public List<T> selectAll();

  /**
   * Count the number of records that match the `field` = 'value' condition.
   * <p>
   * <code>@Select("SELECT COUNT(1) FROM `table` WHERE `${field}` = #{value}")</code>
   * <p>
   * Use <code>@Param("field")</code> to annotate param field and
   * <code>@Param("value")</code> to annotate param value.
   *
   * @param field Table field.
   * @param value Field value.
   * @return Count number.
   */
  public int count(String field, Object value);

  /**
   * Count the number of records that match the where condition.
   * <p>
   * <code>@Select("SELECT COUNT(1) FROM `table` WHERE ${where}")</code>
   * <p>
   * Use <code>@Param("where")</code> to annotate param where.
   *
   * @param where Where condition.
   * @return Count number.
   */
  public int countWhere(String where);

  /**
   * Count the number of all records.
   * <p>
   * <code>@Select("SELECT COUNT(1) FROM `table`")</code>
   *
   * @return Count number.
   */
  public int countAll();

}

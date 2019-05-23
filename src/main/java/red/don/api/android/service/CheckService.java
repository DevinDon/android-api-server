package red.don.api.android.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import red.don.api.android.util.CalendarUtil;
import red.don.api.android.entity.CheckEntity;
import red.don.api.android.entity.UserEntity;
import red.don.api.android.mapper.CheckMapper;
import red.don.api.android.mapper.UserMapper;

@Service
public class CheckService {

  @Autowired
  private CheckMapper mapper;

  @Autowired
  private UserMapper userMapper;

  /**
   * Check in now.
   *
   * @return Is checked or not after this operation.
   */
  public boolean check(UserEntity user) {
    if (userMapper.count("email", user.getEmail()) == 0) {
      return false;
    }
    long today = CalendarUtil.today().getTimeInMillis();
    long tomorrow = CalendarUtil.tomorrow().getTimeInMillis();
    CheckEntity entity = mapper
        .selectOneWhere("`user` = '" + user.getEmail() + "' AND `date` >= " + today + " AND `date` < " + tomorrow);
    if (entity == null) {
      return mapper.insert(new CheckEntity(user.getEmail(), System.currentTimeMillis()));
    } else {
      return true;
    }
  }

  /**
   * View if the specified day is checked in.
   *
   * @param user User entity.
   * @param date Calendar date.
   * @return True or not.
   */
  public boolean view(UserEntity user, Calendar date) {
    var time = CalendarUtil.setToDayOfStart(date);
    long today = time.getTimeInMillis();
    time.add(Calendar.DAY_OF_MONTH, 1);
    long tomorrow = time.getTimeInMillis();
    if (mapper
        .countWhere("`user` = '" + user.getEmail() + "' AND `date` >= " + today + " AND `date` < " + tomorrow) == 0) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * View if the specified day is checked in.
   *
   * @param user User entity.
   * @param date Milliseconds.
   * @return True or not.
   */
  public boolean view(UserEntity user, long date) {
    var calendar = Calendar.getInstance();
    calendar.setTimeInMillis(date);
    return view(user, calendar);
  }

  /**
   * View if the specified day is checked in.
   *
   * @param user  User entity.
   * @param year  Year.
   * @param month Month, 1 is January.
   * @param day   Day of month.
   * @return True or not.
   */
  public boolean view(UserEntity user, int year, int month, int day) {
    return view(user, CalendarUtil.whichDay(year, month, day));
  }

}
